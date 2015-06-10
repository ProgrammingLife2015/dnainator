package nl.tudelft.dnainator.javafx.controllers;

import java.io.File;

import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.services.DBLoadService;
import nl.tudelft.dnainator.javafx.widgets.dialogs.ExceptionDialog;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;

/**
 * Controller for the welcome screen.
 */
public class WelcomeController {
	private static final String DEFAULT_DB_PATH = "db";
	private ObservableList<String> items;
	private ObjectProperty<Graph> dbProperty;
	private DBLoadService dbload;
	private DirectoryChooser dirChooser;
	@SuppressWarnings("unused") @FXML
	private ListView<String> list;
	@SuppressWarnings("unused") @FXML
	private void startButtonAction(ActionEvent e) {
		if (DBPath == "Select new database...") {
			selectDirectory("Select new database...");
		}
		dbload.restart();
	}
	private String DBPath;

	@SuppressWarnings("unused") @FXML
	private void onMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && DBPath == "Select new database...") {
			File dir = selectDirectory("Select new database...");
			if (dir == null) {
				return;
			}
			dbload.setDatabase(dir.getAbsolutePath());
			items.add(dir.getAbsolutePath());
			list.getSelectionModel().select(getDBPath());
			System.out.println(this.DBPath);
		}
	}
	
	@SuppressWarnings("unused") @FXML
	private void initialize() {
		dirChooser = new DirectoryChooser();
		dbload = new DBLoadService();
		dbload.setOnFailed(e ->
			new ExceptionDialog(list.getParent(), dbload.getException(),
					"Database is already in use, please choose another."));
		dbload.setOnSucceeded(e -> dbProperty.setValue(dbload.getValue()));
		
		dbProperty = new SimpleObjectProperty<>(this, "graph");
		items = FXCollections.observableArrayList(
				"Select new database...",
				getDBPath());
		list.setItems(items);
		list.getSelectionModel().select(getDBPath());
		list.getSelectionModel().selectedItemProperty().addListener((obj, oldV, newV) -> {
			dbload.setDatabase(newV);
			setDBPath();
			System.out.println(this.DBPath);
		});
	}
	
	private void setDBPath() {
		this.DBPath = getDBPath();
	}
	
	private String getDBPath() {
		return dbload.getDatabase();
	}
	
	/**
	 * @return The {@link ObjectProperty} used to indicate if the welcome screen is done.
	 */
	public ObjectProperty<Graph> dbProperty() {
		return dbProperty;
	}
	
	/**
	 * Sets up the {@link DirectoryChooser}.
	 *
	 * @param title     The title of the {@link DirectoryChooser}.
	 * @return The selected directory, or null if none is chosen.
	 */
	private File selectDirectory(String title) {
		dirChooser.setTitle(title);
		return dirChooser.showDialog(list.getScene().getWindow());
	}
}
