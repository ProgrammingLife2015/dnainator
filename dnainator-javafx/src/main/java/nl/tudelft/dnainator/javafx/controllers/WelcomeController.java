package nl.tudelft.dnainator.javafx.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.neo4j.io.fs.FileUtils;

import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.services.DBLoadService;
import nl.tudelft.dnainator.javafx.widgets.dialogs.ExceptionDialog;
import nl.tudelft.dnainator.javafx.widgets.dialogs.ProgressDialog;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;

/**
 * Controller for the welcome screen.
 */
public class WelcomeController {
	private static final String SELECT_OPTION = "Select new database...";
	private ObservableList<String> items;
	private ObjectProperty<Graph> dbProperty;
	private BooleanProperty done = new SimpleBooleanProperty(false, "done");;
	private DBLoadService dbload;
	private DirectoryChooser dirChooser;
	private ProgressDialog progressDialog;
	private String dbpath;
	private static final String DEFAULT_DB_PATH = "target/db";
	@SuppressWarnings("unused") @FXML private Button startButton;
	@SuppressWarnings("unused") @FXML private Button deleteButton;
	@SuppressWarnings("unused") @FXML private Button loadButton;
	@SuppressWarnings("unused") @FXML private ListView<String> list;
	
	@SuppressWarnings("unused") @FXML 
	private void startButtonAction(ActionEvent a) {
		done.setValue(true);
	}
	
	@SuppressWarnings("unused") @FXML 
	private void deleteButtonAction(ActionEvent a) {
		try {
			FileUtils.deleteRecursively(new File(getDBPath()));
			items.remove(getDBPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused") @FXML
	private void loadButtonAction(ActionEvent e) {
		progressDialog = new ProgressDialog(list.getParent());
		if (dbpath == SELECT_OPTION) {
			File dir = selectDirectory();
			if (dir == null) {
				return;
			}
		}
		dbload.restart();
	}

	@SuppressWarnings("unused") @FXML
	private void onMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && dbpath == SELECT_OPTION) {
			File dir = selectDirectory();
			if (dir == null) {
				return;
			}
			dbload.setDatabase(dir.getAbsolutePath());
			items.add(dir.getAbsolutePath());
			list.getSelectionModel().select(getDBPath());
		}
	}
	
	@SuppressWarnings("unused") @FXML
	private void initialize() throws IOException {
		dirChooser = new DirectoryChooser();
		dbload = new DBLoadService();
		dbload.setOnFailed(e -> {
			new ExceptionDialog(list.getParent(), dbload.getException(),
					"Database is already in use, please choose another.");
		});
		
		dbload.setOnSucceeded(e -> {
			dbProperty.setValue(dbload.getValue());
			startButton.setDisable(false);
			progressDialog.close();
		});
		dbload.setOnRunning(e -> progressDialog.show());
		dbProperty = new SimpleObjectProperty<>(this, "graph");
		items = FXCollections.observableArrayList(SELECT_OPTION);
		scanDirectory();
		list.setItems(items);
		list.getSelectionModel().select(getDBPath());
		list.getSelectionModel().selectedItemProperty().addListener((obj, oldV, newV) -> {
			deleteButton.setDisable(true);
			if (newV != SELECT_OPTION) {
				deleteButton.setDisable(false);
			}
			dbload.setDatabase(newV);
			setDBPath();
			System.out.println(this.dbpath);
		});
	}
	
	private void setDBPath(){
		this.dbpath = getDBPath();
	}
	
	private String getDBPath() {
		return dbload.getDatabase();
	}
	
	/**
	 * @return the list of database paths of the welcome screen.
	 */
	public ObservableList<String> getListedPaths() {
		return items;
	}
	
	/**
	 * Scan the directory containing the default location of databases.
	 * Adds all the directories found to the welcomescreen's list of selectables.
	 * @throws IOException
	 */
	private void scanDirectory() throws IOException {
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(Paths.get(DEFAULT_DB_PATH))) {
	        for (Path path : ds) {
	            if (Files.isDirectory(path)) {
	                items.add(path.toString());
	            }
	        }
	    }
	}
	
	/**
	 * @return The {@link ObjectProperty} used to indicate if the welcome screen is done.
	 */
	public ObjectProperty<Graph> dbProperty() {
		return dbProperty;
	}
	
	/**
	 * @return The BooleanProperty used to indicate if the welcome screen is done.
	 */
	public BooleanProperty doneProperty() {
		return done;
	}
	
	/**
	 * Sets up the {@link DirectoryChooser}.
	 * @return The selected directory, or null if none is chosen.
	 */
	private File selectDirectory() {
		dirChooser.setTitle(SELECT_OPTION);
		return dirChooser.showDialog(list.getScene().getWindow());
	}
}
