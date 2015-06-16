package nl.tudelft.dnainator.javafx.controllers;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.tree.TreeNode;
import nl.tudelft.dnainator.javafx.services.GraphLoadService;
import nl.tudelft.dnainator.javafx.widgets.animations.RightSlideAnimation;
import nl.tudelft.dnainator.javafx.widgets.animations.SlidingAnimation;
import nl.tudelft.dnainator.javafx.widgets.animations.TransitionAnimation.Position;
import nl.tudelft.dnainator.javafx.widgets.dialogs.ExceptionDialog;
import nl.tudelft.dnainator.javafx.widgets.dialogs.ProgressDialog;

import java.io.File;

/**
 * Controls the file open pane on the left side of the application. It offers options
 * to open node, edge and newick files.
 */
public class FileOpenController {
	private static final String EDGE = ".edge.graph";
	private static final String NODE = ".node.graph";
	private static final String NEWICK = ".nwk";
	private static final String GFF = ".gff";

	private static final int WIDTH = 550;
	private static final int ANIM_DURATION = 250;

	@SuppressWarnings("unused") @FXML private AnchorPane container;
	@SuppressWarnings("unused") @FXML private TextField nodeField;
	@SuppressWarnings("unused") @FXML private TextField edgeField;
	@SuppressWarnings("unused") @FXML private TextField newickField;
	@SuppressWarnings("unused") @FXML private TextField gffField;
	@SuppressWarnings("unused") @FXML private Label curNodeLabel;
	@SuppressWarnings("unused") @FXML private Label curEdgeLabel;
	@SuppressWarnings("unused") @FXML private Label curNewickLabel;
	@SuppressWarnings("unused") @FXML private Label curGffLabel;
	@SuppressWarnings("unused") @FXML private Button openButton;

	private FileChooser fileChooser;
	private ProgressDialog progressDialog;
	private SlidingAnimation animation;

	private GraphLoadService graphLoadService;

	private ListProperty<String> dbPathProperty;
	private ObjectProperty<Graph> graphProperty;
	// FIXME: REMOVE THIS WHEN PERSISTENCE WORKS
	private ObjectProperty<TreeNode> treeProperty;

	/*
	 * Sets up the services, filechooser and treeproperty.
	 */
	@SuppressWarnings("unused") @FXML
	private void initialize() {
		fileChooser = new FileChooser();
		dbPathProperty = new SimpleListProperty<>(this, "dbpath");
		graphProperty = new SimpleObjectProperty<>(this, "graph");
		// FIXME: REMOVE THIS WHEN PERSISTENCE WORKS
		treeProperty = new SimpleObjectProperty<>(this, "tree");
		setupServices();
		
		animation = new RightSlideAnimation(container, WIDTH, ANIM_DURATION, Position.LEFT);
		bindDisabledFieldsAndButtons();
	}
	
	/**
	 * Setup the {@link GraphLoadService}.
	 */
	private void setupServices() {
		graphLoadService = new GraphLoadService();
		graphLoadService.setOnFailed(e -> {
			progressDialog.close();
			new ExceptionDialog(container.getParent(), graphLoadService.getException(),
					"Error loading graph files!");
		});
		graphLoadService.setOnRunning(e -> progressDialog.show());
		graphLoadService.setOnSucceeded(e -> {
			progressDialog.close();
			// FIXME: REMOVE THIS WHEN PERSISTENCE WORKS
			treeProperty.setValue(graphLoadService.treeProperty().get());
			graphProperty.setValue(graphLoadService.getValue());
		});
	}

	/*
	 * Disables the openbutton as long as not all field are filled in.
	 */
	private void bindDisabledFieldsAndButtons() {
		BooleanBinding emptyGraphFile = nodeField.textProperty().isEmpty()
				.or(edgeField.textProperty().isEmpty()).or(newickField.textProperty().isEmpty()
						.or(gffField.textProperty().isEmpty()));
		openButton.disableProperty().bind(emptyGraphFile);
	}

	/*
	 * If the node textfield is clicked, open the filechooser and if a file is selected, try
	 * to fill in the edge textfield as well.
	 */
	@SuppressWarnings("unused") @FXML
	private void onNodeFieldClicked() {
		File nodeFile = selectFile("Node file", NODE);
		if (nodeFile != null) {
			graphLoadService.setNodeFile(nodeFile);
			nodeField.setText(graphLoadService.getNodeFile().getAbsolutePath());
			graphLoadService.setEdgeFile(openEdgeFile(nodeFile.getPath()));
			edgeField.setText(graphLoadService.getEdgeFile().getAbsolutePath());
		}
	}

	/*
	 * If the edge textfield is clicked, open the filechooser and if a file is selected, try
	 * to fill in the node textfield as well.
	 */
	@SuppressWarnings("unused") @FXML
	private void onEdgeFieldClicked() {
		File edgeFile = selectFile("Edge file", EDGE);
		if (edgeFile != null) {
			graphLoadService.setEdgeFile(edgeFile);
			edgeField.setText(graphLoadService.getEdgeFile().getAbsolutePath());
			graphLoadService.setNodeFile(openNodeFile(edgeFile.getPath()));
			nodeField.setText(graphLoadService.getNodeFile().getAbsolutePath());
		}
	}

	/*
	 * If the newick textfield is clicked, open the filechooser and if a file is selected,
	 * fill in the newick textfield.
	 */
	@SuppressWarnings("unused") @FXML
	private void onNewickFieldClicked() {
		File newickFile = selectFile("Newick file", NEWICK);
		if (newickFile != null) {
			graphLoadService.setNewickFile(newickFile);
			newickField.setText(graphLoadService.getNewickFile().getAbsolutePath());
		}
	}

	/*
	 * If the GFF textfield is clicked, open the filechooser and if a file is selected,
	 * fill in the GFF textfield.
	 */
	@SuppressWarnings("unused") @FXML
	private void onGFFFieldClicked() {
		File gffFile = selectFile("GFF file", GFF);
		if (gffFile != null) {
			graphLoadService.setGffFilePath(gffFile.getAbsolutePath());
			gffField.setText(graphLoadService.getGffFilePath());
		}
	}

	/*
	 * If the open button is clicked, open the files if selected and hide the pane. Clears the
	 * text fields and updates the current file labels if files are opened.
	 */
	@SuppressWarnings("unused") @FXML
	private void onOpenAction() {
		progressDialog = new ProgressDialog(container.getParent());
		resetTextFields();
		animation.toggle();
		if (graphLoadService.getGffFilePath() != null
				&& graphLoadService.getNodeFile() != null
				&& graphLoadService.getEdgeFile() != null
				&& graphLoadService.getNewickFile() != null) {
			graphLoadService.setDatabase(graphLoadService.getNewPath(dbPathProperty.getValue()));
			graphLoadService.restart();

			curNewickLabel.setText(graphLoadService.getNewickFile().getAbsolutePath());
			curGffLabel.setText(graphLoadService.getGffFilePath());
			curNodeLabel.setText(graphLoadService.getNodeFile().getAbsolutePath());
			curEdgeLabel.setText(graphLoadService.getEdgeFile().getAbsolutePath());
		}
	}

	/* Clears the files, textfields and hides the pane. */
	@SuppressWarnings("unused") @FXML
	private void onCancelAction(ActionEvent actionEvent) {
		animation.toggle();
		graphLoadService.setGffFilePath(null);
		graphLoadService.setNodeFile(null);
		graphLoadService.setEdgeFile(null);
		graphLoadService.setNewickFile(null);
		resetTextFields();
	}

	private void resetTextFields() {
		nodeField.clear();
		edgeField.clear();
		newickField.clear();
		gffField.clear();
	}

	/**
	 * Sets up the {@link FileChooser} to use have the specified title and to use the
	 * given extension as a filter.
	 *
	 * @param title     The title of the {@link FileChooser}.
	 * @param extension The value to filter for the
	 *                  {@link javafx.stage.FileChooser.ExtensionFilter}.
	 * @return The selected file, or null if none is chosen.
	 */
	private File selectFile(String title, String extension) {
		fileChooser.setTitle(title);
		fileChooser.getExtensionFilters().setAll(
				new FileChooser.ExtensionFilter(title, "*" + extension));
		return fileChooser.showOpenDialog(container.getScene().getWindow());
	}

	/**
	 * @param path Creates an edge file from the path of a node file. This requires
	 *             the edge file to be in the same directory and to have the same name
	 *             as the node file.
	 * @return The edge file.
	 */
	private File openEdgeFile(String path) {
		return new File(path.substring(0, path.length() - NODE.length()).concat(EDGE));
	}

	/**
	 * @param path Creates a node file from the path of an edge file. This requires
	 *             the node file to be in the same directory and to have the same name
	 *             as the edge file.
	 * @return The edge file.
	 */
	private File openNodeFile(String path) {
		return new File(path.substring(0, path.length() - EDGE.length()).concat(NODE));
	}

	/**
	 * @return The {@link TreeNode} property.
	 */
	public ObjectProperty<TreeNode> treeProperty() {
		return treeProperty;
	}
	
	/**
	 * @return The {@link Graph} property.
	 */
	public ObjectProperty<Graph> graphProperty() {
		return graphProperty;
	}

	/**
	 * @return the dbPathProperty.
	 */
	public ListProperty<String> dbPathProperty() {
		return dbPathProperty;
	}
	
	/**
	 * Toggles the pane, showing or hiding it with a sliding animation.
	 */
	public void toggle() {
		animation.toggle();
	}
}
