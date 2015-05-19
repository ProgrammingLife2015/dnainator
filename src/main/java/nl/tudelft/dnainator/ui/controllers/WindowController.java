package nl.tudelft.dnainator.ui.controllers;

import java.io.File;
import java.io.FileNotFoundException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import nl.tudelft.dnainator.ui.services.GraphLoadService;
import nl.tudelft.dnainator.ui.services.NewickLoadService;
import nl.tudelft.dnainator.ui.views.View;
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
	private static final String TREE = ".nwk";
	@FXML private BorderPane root;
	@FXML private View view;
	private GraphLoadService graphLoadService;
	private NewickLoadService newickLoadService;
	private ProgressDialog progressDialog;

	/**
	 * Constructs a WindowController object, creating
	 * a {@link GraphLoadService} to go with it.
	 */
	@FXML
	private void initialize() {
		graphLoadService = new GraphLoadService();
		newickLoadService = new NewickLoadService();

		graphLoadService.setOnFailed(e ->
				new ExceptionDialog(root, graphLoadService.getException(), "Error loading file!"));
		graphLoadService.setOnRunning(e -> progressDialog.show());
		graphLoadService.setOnSucceeded(e -> progressDialog.close());

		newickLoadService = new NewickLoadService();
		newickLoadService.setOnFailed(e ->
				new ExceptionDialog(root, newickLoadService.getException(),
						"Error loading newick file!"));
	}

	@FXML
	private void openButtonAction(ActionEvent e) {
		progressDialog = new ProgressDialog(root, graphLoadService);
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open node file");
		chooser.getExtensionFilters().add(
				new ExtensionFilter("Graph files", "*.node.graph"));
		File nodeFile = chooser.showOpenDialog(root.getScene().getWindow());
		if (nodeFile == null) {
			return;
		}

		graphLoadService.setNodeFile(nodeFile);
		graphLoadService.setEdgeFile(openEdgeFile(nodeFile.getPath()));
		newickLoadService.setNewickFile(openTreeFile(nodeFile.getParent()));
		newickLoadService.restart();
		graphLoadService.restart();
	}

	private File openEdgeFile(String path) {
		return new File(path.substring(0, path.length() - EXT_LENGTH).concat(EDGE));
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

	private File openTreeFile(String parent) {
		File[] res = new File(parent).listFiles((dir, name) -> name.endsWith(TREE));
		if (res.length == 1) {
			return res[0];
		} else if (res.length > 1) {
			String msg = "Please make sure at most one .nwk file exists\n"
					+ "in the same directory as the node file";
			new ExceptionDialog(root, new IllegalStateException(msg),
					"Tree file could not be loaded");
		} else {
			String msg = "Please make sure a .nwk file exists in the same\n"
					+ "directory as the node file";
			new ExceptionDialog(root, new FileNotFoundException(msg), "Tree file not found!");
		}
		return null;
	}
}
