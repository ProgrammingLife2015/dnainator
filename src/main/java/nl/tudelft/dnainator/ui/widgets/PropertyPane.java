package nl.tudelft.dnainator.ui.widgets;

import java.io.IOException;
import java.util.List;

import nl.tudelft.dnainator.ui.widgets.dialogs.ExceptionDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * A pane displaying several properties of the displayed
 * DNA sequences.
 */
public class PropertyPane extends VBox {
	private static final String FXML = "/ui/fxml/propertypane.fxml";
	@FXML private Label label;
	@FXML private VBox vbox;

	/**
	 * Creates a new property pane.
	 */
	public PropertyPane() {
		loadFXML();
		getStyleClass().add("property-pane");
		label.setId("properties-label");
	}

	private void loadFXML() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this); // Necessary for the FXML injection.

		try {
			fxmlLoader.load();
		} catch (IOException e) {
			new ExceptionDialog(null, e, "Can not load the PropertyPane!");
		}
	}

	/**
	 * Updates the displayed information.
	 * @param p The new {@link Propertyable} whose information to display.
	 */
	public void update(Propertyable p) {
		label.setText(p.getType());
		vbox.getChildren().clear();

		updateSources(p);
	}

	private void updateSources(Propertyable p) {
		List<String> list = p.getSources();
		if (list == null) {
			return;
		}

		Label id = new Label("Sources");
		id.getStyleClass().add("property-header");
		vbox.getChildren().add(id);

		for (String s : list) {
			vbox.getChildren().add(new Label(s));
		}
	}
}
