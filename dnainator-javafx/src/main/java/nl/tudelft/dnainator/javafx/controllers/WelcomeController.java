package nl.tudelft.dnainator.javafx.controllers;

import java.io.File;

import org.neo4j.io.fs.FileUtils;

import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.services.DBLoadService;
import nl.tudelft.dnainator.javafx.services.DirectoryLoadService;
import nl.tudelft.dnainator.javafx.widgets.dialogs.ExceptionDialog;
import nl.tudelft.dnainator.javafx.widgets.dialogs.ProgressDialog;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
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
	private ObjectProperty<Graph> currentDatabase;
	private DBLoadService dbload;
	private DirectoryLoadService dirload;
	private DirectoryChooser dirChooser;
	private ProgressDialog progressDialog;
	private ObservableList<String> databases;
	@SuppressWarnings("unused") @FXML private Button deleteButton;
	@SuppressWarnings("unused") @FXML private Button loadButton;
	@SuppressWarnings("unused") @FXML private ListView<String> list;
	@SuppressWarnings("unused") @FXML private String selectDB;
	
	@SuppressWarnings("unused") @FXML 
	private void deleteButtonAction(ActionEvent a) {
		try {
			FileUtils.deleteRecursively(new File(dbload.getDatabase()));
			databases.remove(dbload.getDatabase());
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
		currentDatabase = new SimpleObjectProperty<>(this, "graph");
		dirChooser = new DirectoryChooser();
		dbload = new DBLoadService();
		dirload = new DirectoryLoadService();
		databases = FXCollections.observableArrayList();

		initDBLoad();
		initDirLoad();
		initSelection();
	}

	private void initDBLoad() {
		dbload.setOnFailed(e -> {
			progressDialog.close();
			new ExceptionDialog(list.getParent(), dbload.getException(),
					"Database is already in use, please choose another.");
		});
		dbload.setOnRunning(e -> progressDialog.show());
		dbload.setOnSucceeded(e -> {
			progressDialog.close();
			currentDatabase.setValue(dbload.getValue());
		});
	}

	private void initDirLoad() {
		dirload.setOnFailed(e -> {
			new ExceptionDialog(list.getParent(), dirload.getException(),
					"Could not load directories.");
		});
		dirload.setOnSucceeded(e -> {
			databases.clear();
			databases.add(selectDB);
			databases.addAll(dirload.getValue());
		});
		dirload.restart();
	}

	private void initSelection() {
		list.setItems(databases);
		list.getSelectionModel().select(dbload.getDatabase());
		list.getSelectionModel().selectedItemProperty().addListener((obj, oldV, newV) -> {
			deleteButton.setDisable(true);
			if (newV != selectDB && newV != null) {
				deleteButton.setDisable(false);
			}
			dbload.setDatabase(newV);
		});
	}
	
	/**
	 * @return The {@link ObjectProperty} used to indicate if the welcome screen is done.
	 */
	public ObjectProperty<Graph> currentDBProperty() {
		return currentDatabase;
	}
	
	/**
	 * Handle loading the database based on the selection in the list.
	 * Shows a {@link ProgressDialog} when loading the db.
	 */
	private void loadDB() {
		if (dbload.getDatabase().equals(selectDB)) {
			File dir = selectDirectory();
			if (dir == null) {
				return;
			}
			dirload.setDirectory(dir.getAbsolutePath());
			dirload.restart();
		} else {
			progressDialog = new ProgressDialog(list.getParent());
			dbload.restart();
			dirload.restart();
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
