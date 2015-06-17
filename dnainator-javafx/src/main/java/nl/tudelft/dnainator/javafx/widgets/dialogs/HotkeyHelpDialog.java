package nl.tudelft.dnainator.javafx.widgets.dialogs;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

/**
 * Creates an {@link Alert} showing information regarding all the shortcuts of the controls.
 */
public class HotkeyHelpDialog extends Alert {
	private Node parent;
	private static final String STYLE = "/style.css";
	private static final int COLUMN_WIDTH = 75;
	private static final int WIDTH = 300;
	private GridPane content;
	private int nextRow = 0;

	/**
	 * Instantitates a new hotkey help dialog.
	 * @param parent   The parent Node of this dialog.
	 * @param menus    The menus carrying the shortcuts.
	 */
	public HotkeyHelpDialog(Node parent, Menu... menus) {
		super(Alert.AlertType.NONE);
		this.parent = parent;
		setupDialog(menus);
	}
	
	private void setupDialog(Menu... menus) {
		this.setTitle("Hotkeys");
		this.getDialogPane().getStylesheets().add(getClass().getResource(STYLE).toString());
		this.initOwner(parent.getScene().getWindow());
		this.initModality(Modality.NONE);
		this.getDialogPane().setPrefWidth(WIDTH);
		setupContentGrid();
		this.setHeaderText("Hotkeys");
		setupContent(menus);
		
		getDialogPane().setContent(content);
		
		ButtonType close = new ButtonType("Close", ButtonData.CANCEL_CLOSE);
		this.getDialogPane().getButtonTypes().add(close);
	}
	
	private void setupContent(Menu... menus) {
		for (Menu menu : menus) {
			menu.getItems().forEach(menuItem -> {
				if (menuItem.getAccelerator() != null) {
					setupTitles(menuItem.getText());
					setupControls(menuItem.getAccelerator().getDisplayText());
				}
			});
		}
	}
	
	private void setupContentGrid() {
		content = new GridPane();
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(COLUMN_WIDTH);
		content.getColumnConstraints().add(col1);
	}

	private void setupTitles(String title) {
		Label entry = new Label();
		entry.setText(title);
		entry.getStyleClass().add("hotkey-title");
		content.add(entry, 0, nextRow);
	}
	
	private void setupControls(String control) {
		Label controlEntry = new Label();
		controlEntry.setText(control);
		controlEntry.getStyleClass().add("hotkey-control");
		content.add(controlEntry, 1, nextRow++);
	}
}
