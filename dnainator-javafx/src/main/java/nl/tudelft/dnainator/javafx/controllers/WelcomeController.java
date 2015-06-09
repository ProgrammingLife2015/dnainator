package nl.tudelft.dnainator.javafx.controllers;

import java.io.File;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
	private BooleanProperty done = new SimpleBooleanProperty(false, "done");
	private DirectoryChooser dirChooser;
	@SuppressWarnings("unused") @FXML
	private ListView<String> list;
	@SuppressWarnings("unused") @FXML
	private void startButtonAction(ActionEvent e) {
		if (DBPath == "Select new database...") {
			selectDirectory("Select new database...");
		}
		done.setValue(true);
	}
	private String DBPath;

	@SuppressWarnings("unused") @FXML
	private void onMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && DBPath == "Select new database...") {
			File dir = selectDirectory("Select new database...");
			setDBPath(dir.getAbsolutePath());
			items.add(dir.getAbsolutePath());
			System.out.println(this.DBPath);
		}
	}
	
	@SuppressWarnings("unused") @FXML
	private void initialize() {
		dirChooser = new DirectoryChooser();
		items = FXCollections.observableArrayList(
				"Select new database...",
				"nl/tudelft/DNAinator/db/10_strains",
				"nl/tudelft/DNAinator/db/38_strains",
				"nl/tudelft/DNAinator/db/100_strains");
		list.setItems(items);
		list.getSelectionModel().selectedItemProperty().addListener((obj, oldV, newV) -> {
			setDBPath((String) newV);
			System.out.println(this.DBPath);
		});
		//System.out.println(list.getSelectionModel().getSelectedItem().toString());
	}
	
	/**
	 * @return The BooleanProperty used to indicate if the welcome screen is done.
	 */
	public BooleanProperty doneProperty() {
		return done;
	}
	
	/**
	 * Sets DBPath.
	 * @param path.
	 */
	public void setDBPath(String path) {
		this.DBPath = path;
	}
	
	/**
	 * Gets DBPath.
	 * @return the DBPath.
	 */
	public String getDBPath() {
		return DBPath;
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
