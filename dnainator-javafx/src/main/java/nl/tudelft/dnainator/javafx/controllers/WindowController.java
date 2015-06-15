package nl.tudelft.dnainator.javafx.controllers;

import nl.tudelft.dnainator.javafx.ColorServer;
import nl.tudelft.dnainator.javafx.views.AbstractView;
import nl.tudelft.dnainator.javafx.views.PhylogeneticView;
import nl.tudelft.dnainator.javafx.views.StrainView;
import nl.tudelft.dnainator.javafx.widgets.dialogs.AboutDialog;
import nl.tudelft.dnainator.javafx.widgets.dialogs.HotkeyHelpDialog;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Menu;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

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
	@SuppressWarnings("unused") @FXML
	private PropertyPaneController propertyPaneController;
	@SuppressWarnings("unused") @FXML
	private Menu menuFile;
	@SuppressWarnings("unused") @FXML
	private Menu menuView;

	private StrainView strainView;
	private PhylogeneticView phyloView;

	@SuppressWarnings("unused") @FXML
	private void initialize() {
		ColorServer colorServer = new ColorServer();
		fileOpenController.dbPathProperty().set(welcomeController.getListedPaths());
		fileOpenController.graphProperty().addListener((obj, oldV, newV) -> {
			strainView = new StrainView(colorServer, newV);
			phyloView = new PhylogeneticView(colorServer, newV.getTree());
			constructView(strainView, phyloView);
		});
		welcomeController.dbProperty().addListener((obj, oldV, newV) -> {
			strainView = new StrainView(colorServer, newV);
			phyloView = new PhylogeneticView(colorServer, newV.getTree());
			constructView(strainView, phyloView);
		});
	}
	
	private void constructView(Pane strainView, Pane phyloView) {
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
	private void hotkeyHelpAction(ActionEvent e) {
		HotkeyHelpDialog hotkeyHelp = new HotkeyHelpDialog(root, menuFile, menuView);
		hotkeyHelp.show();
	}
	
	@SuppressWarnings("unused") @FXML
	private void exitAction(ActionEvent e) {
		Platform.exit();
	}

	@SuppressWarnings("unused") @FXML
	private void zoomInAction(ActionEvent e) {
		if (strainView != null) {
			strainView.zoomIn();
		}
	}

	@SuppressWarnings("unused") @FXML
	private void zoomOutAction(ActionEvent e) {
		if (strainView != null) {
			strainView.zoomOut();
		}
	}

	@SuppressWarnings("unused") @FXML
	private void resetZoomAction(ActionEvent e) {
		if (strainView != null) {
			strainView.resetZoom();
		}
	}

	@SuppressWarnings("unused") @FXML
	private void resetPositionAction(ActionEvent e) {
		if (strainView != null) {
			strainView.resetTranslate();
		}
	}
	
	@SuppressWarnings("unused") @FXML
	private void toggleProperties(ActionEvent e) {
		propertyPaneController.toggle();
	}
	
	@SuppressWarnings("unused") @FXML
	private void toggleStepperAction(ActionEvent e) {
		if (strainView.getStrainControl() != null) {
			strainView.getStrainControl().toggleStepper();
		}
	}
	
	@SuppressWarnings("unused") @FXML
	private void jumpNodeAction(ActionEvent e) {
		if (strainView.getStrainControl() != null) {
			strainView.getStrainControl().toggleJumpNode();
		}
	}
	
	@SuppressWarnings("unused") @FXML
	private void jumpRankAction(ActionEvent e) {
		if (strainView.getStrainControl() != null) {
			strainView.getStrainControl().toggleJumpRank();
		}
	}
	
	@SuppressWarnings("unused") @FXML
	private void jumpAnnotationAction(ActionEvent e) {
		if (strainView.getStrainControl() != null) {
			strainView.getStrainControl().toggleJumpAnnotation();
		}
	}
}
