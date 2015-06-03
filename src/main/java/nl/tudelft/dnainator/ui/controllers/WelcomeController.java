package nl.tudelft.dnainator.ui.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

/**
 * Controller for the welcome screen.
 */
public class WelcomeController {
	private BooleanProperty done = new SimpleBooleanProperty(false, "done");
	@SuppressWarnings("unused") @FXML
	private ListView list;
	@SuppressWarnings("unused") @FXML
	private void startButtonAction(ActionEvent e) {
		done.setValue(true);
	}

	@SuppressWarnings("unused") @FXML
	private void initialize() {
		ObservableList<String> items = FXCollections.observableArrayList(
				"nl/tudelft/DNAinator/db/10_strains",
				"nl/tudelft/DNAinator/db/38_strains",
				"nl/tudelft/DNAinator/db/100_strains");
		list.setItems(items);
	}
	/**
	 * @return The BooleanProperty used to indicate if the welcome screen is done.
	 */
	public BooleanProperty doneProperty() {
		return done;
	}
}
