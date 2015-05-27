package nl.tudelft.dnainator.ui.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import nl.tudelft.dnainator.ui.services.GraphLoadService;
import nl.tudelft.dnainator.ui.services.NewickLoadService;
import nl.tudelft.dnainator.ui.views.View;
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
	@FXML private View view;
	@FXML private ScrollPane phyloView;
	@FXML private FileOpenController fileOpenerController;

	/**
	 * Constructs a WindowController object, creating a {@link GraphLoadService}
	 * and a {@link NewickLoadService} to go with it.
	 */
	@FXML
	private void initialize() {
		phyloView.contentProperty().bind(fileOpenerController.treeProperty());
	}

	@FXML
	private void openButtonAction(ActionEvent e) {
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
		view.zoomIn();
	}

	@FXML
	private void zoomOutAction(ActionEvent e) {
		view.zoomOut();
	}
}
