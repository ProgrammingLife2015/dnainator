package nl.tudelft.dnainator.ui.views;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import nl.tudelft.dnainator.ui.widgets.ExceptionDialog;

/**
 * This class is the View part of the MVC pattern.
 */
public class View extends Pane {
	/**
	 * Creates a new view instance.
	 */
	public View() {
		loadFXML();
		getStyleClass().add("view");
	}

	private void loadFXML() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/fxml/view.fxml"));
		fxmlLoader.setRoot(this);

		try {
			fxmlLoader.load();
		} catch (IOException e) {
			new ExceptionDialog(e, "Can not load the View!");
		}
	}
}
