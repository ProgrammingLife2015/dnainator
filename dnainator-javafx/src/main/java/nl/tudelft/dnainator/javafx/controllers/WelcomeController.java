package nl.tudelft.dnainator.javafx.controllers;

import java.io.File;

import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.services.DBLoadService;
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
	private BooleanProperty done = new SimpleBooleanProperty(false, "done");
	private ObjectProperty<Graph> graphProperty;
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
		done.setValue(true);
	}
	private String DBPath;

	@SuppressWarnings("unused") @FXML
	private void onMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && DBPath == "Select new database...") {
			File dir = selectDirectory("Select new database...");
			dbload.setDatabase(dir.getAbsolutePath());
			items.add(dir.getAbsolutePath());
			
			System.out.println(this.DBPath);
		}
	}
	
	@SuppressWarnings("unused") @FXML
	private void initialize() {
		dirChooser = new DirectoryChooser();
		dbload = new DBLoadService();
		
		dbload.setOnSucceeded(e -> graphProperty.setValue(dbload.getValue()));
		
		graphProperty = new SimpleObjectProperty<>(this, "graph");
		items = FXCollections.observableArrayList(
				"Select new database...",
				"nl/tudelft/DNAinator/db/10_strains",
				"nl/tudelft/DNAinator/db/38_strains",
				"nl/tudelft/DNAinator/db/100_strains");
		list.setItems(items);
		list.getSelectionModel().selectedItemProperty().addListener((obj, oldV, newV) -> {
			dbload.setDatabase((String) newV);
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
	 * @return The BooleanProperty used to indicate if the welcome screen is done.
	 */
	public ObjectProperty<Graph> graphProperty() {
		return graphProperty;
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
