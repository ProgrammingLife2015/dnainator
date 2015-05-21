package nl.tudelft.dnainator.ui.controllers;

import java.io.File;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import nl.tudelft.dnainator.graph.impl.Neo4jSingleton;
import nl.tudelft.dnainator.ui.services.FileLoadService;
import nl.tudelft.dnainator.ui.views.View;
import nl.tudelft.dnainator.ui.widgets.PropertyPane;
import nl.tudelft.dnainator.ui.widgets.dialogs.AboutDialog;
import nl.tudelft.dnainator.ui.widgets.dialogs.ExceptionDialog;
import nl.tudelft.dnainator.ui.widgets.dialogs.ProgressDialog;

/**
 * The WindowController is a controller class for the main window.
 * <p>
 * It is automatically instantiated by the {@link javafx.fxml.FXMLLoader}
 * and thus should not be created manually.
 * </p>
 */
public class WindowController {
	private static final int EXT_LENGTH = 11; // .node.graph
	private static final String EDGE = ".edge.graph";
	@FXML private BorderPane root;
	@FXML private View view;
	@FXML private PropertyPane propertyPane;
	private FileLoadService loadService;
	private ProgressDialog progressDialog;

	/**
	 * Constructs a WindowController object, creating
	 * a {@link FileLoadService} to go with it.
	 */
	@FXML
	private void initialize() {
		loadService = new FileLoadService();

		loadService.setOnFailed(e ->
				new ExceptionDialog(root, loadService.getException(), "Error loading file!"));
		loadService.setOnRunning(e -> progressDialog.showDialog());
		loadService.setOnSucceeded(e -> {
			progressDialog.close();
		});
		loadService.setOnCancelled(e -> {
			Neo4jSingleton.getInstance().deleteDatabase();
		});

		view.lastClickedProperty().addListener((ob, ov, nv) -> propertyPane.update(nv));
	}

	@FXML
	private void openButtonAction(ActionEvent e) {
		progressDialog = new ProgressDialog(root, loadService);
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open node file");
		chooser.getExtensionFilters().add(
				new ExtensionFilter("Graph files", "*.node.graph"));
		File nodeFile = chooser.showOpenDialog(root.getScene().getWindow());
		if (nodeFile == null) {
			return;
		}

		loadService.setNodeFile(nodeFile);
		loadService.setEdgeFile(openEdgeFile(nodeFile.getPath()));
		loadService.restart();
	}

	private File openEdgeFile(String path) {
		return new File(path.substring(0, path.length() - EXT_LENGTH).concat(EDGE));
	}

	@FXML
	private void toggleProperties(ActionEvent e) {
		propertyPane.toggle();
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
}
