package nl.tudelft.dnainator.ui.controllers;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import nl.tudelft.dnainator.graph.impl.Neo4jSingleton;
import nl.tudelft.dnainator.ui.services.FileLoadService;
import nl.tudelft.dnainator.ui.widgets.ExceptionDialog;
import nl.tudelft.dnainator.ui.widgets.ProgressDialog;

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
	@FXML private VBox sidebar;
	@FXML private GraphController viewerController;
	private FileLoadService loadService;

	/**
	 * Constructs a WindowController object, creating
	 * a {@link FileLoadService} to go with it.
	 */
	@FXML
	private void initialize() {
		loadService = new FileLoadService();
		ProgressDialog progressDialog = new ProgressDialog(loadService);

		loadService.setOnFailed(e ->
				new ExceptionDialog(loadService.getException(), "Error loading file!"));
		loadService.setOnRunning(e -> progressDialog.show());
		loadService.setOnSucceeded(e -> {
			viewerController.getActiveView().redraw();
			progressDialog.close();
		});
		loadService.setOnCancelled(e -> {
			try {
				Neo4jSingleton.getInstance().stopDatabase(Neo4jSingleton.DB_PATH);
			} catch (IOException ioe) {
				new ExceptionDialog(ioe, "Error cancelling database!");
			}
		});
	}

	@FXML
	private void openButtonAction(ActionEvent e) {
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
}
