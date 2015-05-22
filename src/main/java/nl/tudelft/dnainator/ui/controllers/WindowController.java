package nl.tudelft.dnainator.ui.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import nl.tudelft.dnainator.tree.TreeNode;
import nl.tudelft.dnainator.ui.services.GraphLoadService;
import nl.tudelft.dnainator.ui.services.NewickLoadService;
import nl.tudelft.dnainator.ui.views.View;
import nl.tudelft.dnainator.ui.widgets.PhylogeneticTree;
import nl.tudelft.dnainator.ui.widgets.dialogs.AboutDialog;
import nl.tudelft.dnainator.ui.widgets.dialogs.ExceptionDialog;
import nl.tudelft.dnainator.ui.widgets.dialogs.OpenDialog;
import nl.tudelft.dnainator.ui.widgets.dialogs.ProgressDialog;

import java.util.Optional;

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
	private GraphLoadService graphLoadService;
	private NewickLoadService newickLoadService;
	private ProgressDialog progressDialog;

	/**
	 * Constructs a WindowController object, creating a {@link GraphLoadService}
	 * and a {@link NewickLoadService} to go with it.
	 */
	@FXML
	private void initialize() {
		graphLoadService = new GraphLoadService();

		graphLoadService.setOnFailed(e ->
				new ExceptionDialog(root, graphLoadService.getException(),
						"Error loading graph files!"));
		graphLoadService.setOnRunning(e -> progressDialog.show());
		graphLoadService.setOnSucceeded(e -> progressDialog.close());

		newickLoadService = new NewickLoadService();
		newickLoadService.setOnFailed(e ->
				new ExceptionDialog(root, newickLoadService.getException(),
						"Error loading newick file!"));
		newickLoadService.setOnSucceeded(e -> {
			TreeNode rootNode = newickLoadService.getValue();
			phyloView.setContent(new PhylogeneticTree(rootNode));
		});
	}

	@FXML
	private void openButtonAction(ActionEvent e) {
		progressDialog = new ProgressDialog(root, graphLoadService);
		Optional<ButtonType> result = new OpenDialog(root, graphLoadService, newickLoadService)
				.showAndWait();
		if (result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
			restart();
		} else {
			reset();
		}
	}

	private void restart() {
		if (graphLoadService.nodeFileProperty().isNotNull().get()
				&& graphLoadService.edgeFileProperty().isNotNull().get()) {
			graphLoadService.restart();
		}

		if (newickLoadService.newickFileProperty().isNotNull().get()) {
			newickLoadService.restart();
		}
	}

	private void reset() {
		// set to null to avoid being unable to open a file afterwards.
		graphLoadService.setNodeFile(null);
		graphLoadService.setEdgeFile(null);
		newickLoadService.setNewickFile(null);
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
