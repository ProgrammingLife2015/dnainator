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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;

/**
 * Controller for the welcome screen.
 */
public class WelcomeController {
	private ObjectProperty<Graph> dbProperty;
	private DBLoadService dbload;
	private DirectoryChooser dirChooser;
	private ProgressDialog progressDialog;
	private ObservableList<String> items;
	private static final String DEFAULT_DB_PATH = "target" + File.separator + "db";
	private static final String CORE = "neostore";
	@SuppressWarnings("unused") @FXML private Button deleteButton;
	@SuppressWarnings("unused") @FXML private Button loadButton;
	@SuppressWarnings("unused") @FXML private ListView<String> list;
	@SuppressWarnings("unused") @FXML private String selectDB;
	
	@SuppressWarnings("unused") @FXML 
	private void deleteButtonAction(ActionEvent a) {
		try {
			FileUtils.deleteRecursively(new File(getDBPath()));
			items.remove(getDBPath());
		} catch (Exception e) {
			new ExceptionDialog(list.getParent(), e, "Failed to delete database.");
		}
	}
	
	@SuppressWarnings("unused") @FXML
	private void loadButtonAction(ActionEvent e) {
		loadDB();
	}

	@SuppressWarnings("unused") @FXML
	private void onMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			loadDB();
		}
	}
	
	@SuppressWarnings("unused") @FXML
	private void onKeyPressed(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			loadDB();
		}
	}
	
	@SuppressWarnings("unused") @FXML
	private void initialize() {
		dirChooser = new DirectoryChooser();
		dbload = new DBLoadService();
		items = list.getItems();

		dbload.setOnFailed(e -> {
			progressDialog.close();
			new ExceptionDialog(list.getParent(), dbload.getException(),
					"Database is already in use, please choose another.");
		});
		dbload.setOnRunning(e -> progressDialog.show());
		dbload.setOnSucceeded(e -> {
			progressDialog.close();
			dbProperty.setValue(dbload.getValue());
		});
		dbProperty = new SimpleObjectProperty<>(this, "graph");
		scanDirectory(DEFAULT_DB_PATH);
		list.setItems(items);
		list.getSelectionModel().select(getDBPath());
		list.getSelectionModel().selectedItemProperty().addListener((obj, oldV, newV) -> {
			deleteButton.setDisable(true);
			if (newV != selectDB && newV != null) {
				deleteButton.setDisable(false);
			}
			dbload.setDatabase(newV);
		});
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
	 * If the default directory does not exist, create it.
	 * Adds all the directories found to the welcomescreen's list of selectables.
	 */
	private void scanDirectory(String dbpath) {
		if (!Files.exists(Paths.get(dbpath)) && new File(dbpath).mkdirs()) {
			return;
		} else {
			try (DirectoryStream<Path> ds = Files.newDirectoryStream(Paths.get(dbpath))) {
				for (Path path : ds) {
					if (Files.isDirectory(path) 
							&& Files.exists(Paths.get(path.toString() + File.separator + CORE))) {
						items.add(path.toString());
					}
				}
			} catch (IOException e) {
				new ExceptionDialog(list.getParent(), e, "Failed to retrieve databases.");
			}
		}
		items.sort((e1, e2) -> e1.compareTo(e2));
	}
	
	/**
	 * @return The {@link ObjectProperty} used to indicate if the welcome screen is done.
	 */
	public ObjectProperty<Graph> dbProperty() {
		return dbProperty;
	}
	
	/**
	 * Handle loading the database based on the selection in the list.
	 * Shows a {@link ProgressDialog} when loading the db.
	 */
	private void loadDB() {
		if (!getDBPath().equals(DEFAULT_DB_PATH)) {
			if (getDBPath().equals(selectDB)) {
				File dir = selectDirectory();
				if (dir == null) {
					return;
				}
				items.removeIf(item -> !item.equals(selectDB));
				scanDirectory(dir.getAbsolutePath());
			} else {
				progressDialog = new ProgressDialog(list.getParent());
				dbload.restart();
			}
		}
	}
	
	/**
	 * Sets up the {@link DirectoryChooser}.
	 * @return The selected directory, or null if none is chosen.
	 */
	private File selectDirectory() {
		dirChooser.setTitle(selectDB);
		return dirChooser.showDialog(list.getScene().getWindow());
	}
}
