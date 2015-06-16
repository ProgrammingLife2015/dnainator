package nl.tudelft.dnainator.javafx.controllers;

import nl.tudelft.dnainator.javafx.ColorServer;
import nl.tudelft.dnainator.javafx.views.AbstractView;
import nl.tudelft.dnainator.javafx.views.PhylogeneticView;
import nl.tudelft.dnainator.javafx.views.StrainView;
import nl.tudelft.dnainator.javafx.widgets.dialogs.AboutDialog;
import nl.tudelft.dnainator.parser.TreeParser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;



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
	private PropertyPaneController propertyPaneController;

	@SuppressWarnings("unused") @FXML
	private void initialize() {
		ColorServer colorServer = new ColorServer();
		fileOpenController.dbPathProperty().set(welcomeController.getListedPaths());
		fileOpenController.graphProperty().addListener((obj, oldV, newV) -> {
			strainView = new StrainView(colorServer, newV);
			phyloView = new PhylogeneticView(colorServer);
			try {
				phyloView.setRoot(((TreeParser) newV).parse());
			} catch (Exception e) {
				e.printStackTrace();
			}
			constructView();
		});
		welcomeController.dbProperty().addListener((obj, oldV, newV) -> {
			strainView = new StrainView(colorServer, newV);
			phyloView = new PhylogeneticView(colorServer);
			try {
				phyloView.setRoot(((TreeParser) newV).parse());
			} catch (Exception e) {
				e.printStackTrace();
			}
			constructView();
		});
	}
	
	private void constructView() {
		SplitPane splitPane = new SplitPane(strainView, phyloView);
		splitPane.setOrientation(Orientation.VERTICAL);
		root.setCenter(splitPane);
		
		AbstractView.lastClickedProperty().addListener(
				(ob, ov, nv) -> propertyPaneController.update(nv));
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
	
	@SuppressWarnings("unused") @FXML
	private void toggleProperties(ActionEvent e) {
		propertyPaneController.toggle();
	}
}
