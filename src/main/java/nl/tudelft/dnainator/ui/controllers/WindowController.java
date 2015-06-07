package nl.tudelft.dnainator.ui.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import nl.tudelft.dnainator.ui.ColorServer;
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
	private FileOpenController fileOpenerController;
	@SuppressWarnings("unused") @FXML
	private WelcomeController welcomeController;
	private StrainView strainView;

	@SuppressWarnings("unused") @FXML
	private void initialize() {
		welcomeController.doneProperty().addListener((obj, oldV, newV) -> {
			createViews();
			welcomeController.doneProperty().unbind();
		});
	}

	private void createViews() {
		ColorServer colorServer = new ColorServer();
		strainView = new StrainView(colorServer);
		PhylogeneticView phyloView = new PhylogeneticView(colorServer);
		phyloView.rootProperty().bind(fileOpenerController.treeProperty());
		SplitPane splitPane = new SplitPane(strainView, phyloView);
		splitPane.setOrientation(Orientation.VERTICAL);
		root.setCenter(splitPane);
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
