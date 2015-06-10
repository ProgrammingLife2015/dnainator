package nl.tudelft.dnainator.javafx.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import nl.tudelft.dnainator.javafx.ColorServer;
import nl.tudelft.dnainator.javafx.views.PhylogeneticView;
import nl.tudelft.dnainator.javafx.views.StrainView;
import nl.tudelft.dnainator.javafx.widgets.dialogs.AboutDialog;

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
	private FileOpenController fileOpenController;
	@SuppressWarnings("unused") @FXML
	private WelcomeController welcomeController;
	private StrainView strainView;
	private PhylogeneticView phyloView;

	@SuppressWarnings("unused") @FXML
	private void initialize() {
		ColorServer colorServer = new ColorServer();
		fileOpenController.dbPathProperty().set(welcomeController.getListedPaths());
		fileOpenController.doneProperty().addListener((obj, oldV, newV) -> 
			welcomeController.doneProperty().setValue(true));
		fileOpenController.graphProperty().addListener((obj, oldV, newV) -> {
			strainView = new StrainView(colorServer, newV);
		});
		fileOpenController.treeProperty().addListener((obj, oldV, newV) -> {
			phyloView = new PhylogeneticView(colorServer);
			phyloView.rootProperty().set(newV);
		});
		welcomeController.doneProperty().addListener((obj, oldV, newV) -> {
			SplitPane splitPane = new SplitPane(strainView, phyloView);
			splitPane.setOrientation(Orientation.VERTICAL);
			root.setCenter(splitPane);
		});
		welcomeController.dbProperty().addListener((obj, oldV, newV) -> {
			strainView = new StrainView(colorServer, newV);
			phyloView = new PhylogeneticView(colorServer);
		});
	}

	@SuppressWarnings("unused") @FXML
	private void openButtonAction() {
		fileOpenController.toggle();
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
	private void zoomInAction(ActionEvent e) {
		strainView.zoomIn();
	}

	@SuppressWarnings("unused") @FXML
	private void zoomOutAction(ActionEvent e) {
		strainView.zoomOut();
	}

	@SuppressWarnings("unused") @FXML
	private void resetZoomAction(ActionEvent e) {
		strainView.resetZoom();
	}

	@SuppressWarnings("unused") @FXML
	private void resetPositionAction(ActionEvent e) {
		strainView.resetTranslate();
	}
}
