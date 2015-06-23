package nl.tudelft.dnainator.javafx.controllers;

import java.io.File;

import nl.tudelft.dnainator.javafx.services.DirectoryListingService;
import org.neo4j.io.fs.FileUtils;

import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.services.DBLoadService;
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
	private ObservableList<String> databases;

	private DBLoadService dbload;
	private DirectoryListingService dirload;

	private DirectoryChooser dirChooser;
	private ProgressDialog progressDialog;

	@SuppressWarnings("unused") @FXML private Button deleteButton;
	@SuppressWarnings("unused") @FXML private Button loadButton;
	@SuppressWarnings("unused") @FXML private ListView<String> dblist;
	@SuppressWarnings("unused") @FXML private String selectDB;

	@SuppressWarnings("unused") @FXML
	private void initialize() {
		currentDatabase = new SimpleObjectProperty<>(this, "graph");
		dirChooser = new DirectoryChooser();
		dbload = new DBLoadService();
		dirload = new DirectoryListingService();
		databases = FXCollections.observableArrayList();

		initDBLoad();
		initDirLoad();
		initSelection();
	}

	private void initDBLoad() {
		dbload.setOnCancelled(e -> {
			progressDialog.close();
		});
		dbload.setOnFailed(e -> {
			progressDialog.close();
			new ExceptionDialog(dblist.getParent(), dbload.getException(),
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
			new ExceptionDialog(dblist.getParent(), dirload.getException(),
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
		dblist.setItems(databases);
		dblist.getSelectionModel().select(dbload.getDatabase());
		dblist.getSelectionModel().selectedItemProperty().addListener((obj, oldV, newV) -> {
			deleteButton.setDisable(true);
			if (newV != selectDB && newV != null) {
				deleteButton.setDisable(false);
			}
			dbload.setDatabase(newV);
		});
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
			progressDialog = new ProgressDialog(dblist.getScene().getWindow(), dbload);
			dbload.restart();
			dirload.restart();
		}
	}

	/**
	 * @return The {@link ObjectProperty} used to indicate if the welcome screen is done.
	 */
	public ObjectProperty<Graph> currentDBProperty() {
		return currentDatabase;
	}
	
	/**
	 * Sets up the {@link DirectoryChooser}.
	 * @return The selected directory, or null if none is chosen.
	 */
	private File selectDirectory() {
		dirChooser.setTitle(selectDB);
		return dirChooser.showDialog(dblist.getScene().getWindow());
	}

	@SuppressWarnings("unused") @FXML 
	private void deleteButtonAction(ActionEvent a) {
		try {
			FileUtils.deleteRecursively(new File(dbload.getDatabase()));
			databases.remove(dbload.getDatabase());
		} catch (Exception e) {
			new ExceptionDialog(dblist.getParent(), e, "Failed to delete database.");
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
}
