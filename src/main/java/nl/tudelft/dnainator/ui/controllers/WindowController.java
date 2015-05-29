package nl.tudelft.dnainator.ui.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import nl.tudelft.dnainator.ui.views.StrainView;
import nl.tudelft.dnainator.ui.widgets.dialogs.AboutDialog;

/**
 * The WindowController is a controller class for the main window.
 * <p>
 * It is automatically instantiated by the {@link javafx.fxml.FXMLLoader}
 * and thus should not be created manually.
 * </p>
 */
public class WindowController {
	@FXML private BorderPane root;
	@FXML private StrainView strainView;
	@FXML private ScrollPane phyloView;
	@FXML private FileOpenController fileOpenerController;

	/**
	 * Constructs a WindowController object, binding the content of the tree view to
	 * the <code>treeProperty</code> of the {@link FileOpenController}.
	 */
	@FXML
	private void initialize() {
		phyloView.contentProperty().bind(fileOpenerController.treeProperty());
	}

	@FXML
	private void openButtonAction() {
		fileOpenerController.toggle();
	}

	@FXML
	private void aboutUsAction(ActionEvent e) {
		AboutDialog about = new AboutDialog(root);
		about.showAndWait();
	}

	@FXML
	private void exitAction(ActionEvent e) {
		Platform.exit();
	}

	@FXML
	private void zoomInAction(ActionEvent e) {
		strainView.zoomIn();
	}

	@FXML
	private void zoomOutAction(ActionEvent e) {
		strainView.zoomOut();
	}
}
