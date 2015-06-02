package nl.tudelft.dnainator.ui.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import nl.tudelft.dnainator.ui.views.PhylogeneticView;
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
	@SuppressWarnings("unused") @FXML
	private BorderPane root;
	@SuppressWarnings("unused") @FXML
	private StrainView strainView;
	@SuppressWarnings("unused") @FXML
	private PhylogeneticView phyloView;
	@SuppressWarnings("unused") @FXML
	private FileOpenController fileOpenerController;

	/**
	 * Constructs a WindowController object, binding <code>rootProperty</code> of the
	 * {@link PhylogeneticView} the <code>treeProperty</code> of the {@link FileOpenController}.
	 */
	@SuppressWarnings("unused") @FXML
	private void initialize() {
		phyloView.rootProperty().bind(fileOpenerController.treeProperty());
	}

	@SuppressWarnings("unused") @FXML
	private void openButtonAction() {
		fileOpenerController.toggle();
	}

	@SuppressWarnings("unused") @FXML
	private void aboutUsAction(ActionEvent e) {
		AboutDialog about = new AboutDialog(root);
		about.showAndWait();
	}

	@SuppressWarnings("unused") @FXML
	private void exitAction(ActionEvent e) {
		Platform.exit();
	}
	
	@SuppressWarnings("unused") @FXML
	private void toRankAction(ActionEvent e) {
		strainView.setPan(-160000, 25);
	}

	@SuppressWarnings("unused") @FXML
	private void zoomInAction(ActionEvent e) {
		strainView.zoomIn();
	}

	@SuppressWarnings("unused") @FXML
	private void zoomOutAction(ActionEvent e) {
		strainView.zoomOut();
	}
}
