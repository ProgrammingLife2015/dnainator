package nl.tudelft.dnainator.javafx.controllers;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.tree.TreeNode;
import nl.tudelft.dnainator.javafx.services.GFFLoadService;
import nl.tudelft.dnainator.javafx.services.GraphLoadService;
import nl.tudelft.dnainator.javafx.services.NewickLoadService;
import nl.tudelft.dnainator.javafx.widgets.animations.LeftSlideAnimation;
import nl.tudelft.dnainator.javafx.widgets.animations.SlidingAnimation;
import nl.tudelft.dnainator.javafx.widgets.animations.TransitionAnimation.Position;
import nl.tudelft.dnainator.javafx.widgets.dialogs.ExceptionDialog;
import nl.tudelft.dnainator.javafx.widgets.dialogs.ProgressDialog;
import org.neo4j.io.fs.FileUtils;

import java.io.File;

/**
 * Controls the file open pane on the left side of the application. It offers options
 * to open node, edge and newick files. FIXME: will be sliding in on command.
 */
public class FileOpenController {
	private static final String EDGE = ".edge.graph";
	private static final String NODE = ".node.graph";
	private static final String NEWICK = ".nwk";
	private static final String GFF = ".gff";

	private static final int WIDTH = 550;
	private static final int ANIM_DURATION = 250;

	@SuppressWarnings("unused") @FXML private GridPane fileOpenPane;
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
	private NewickLoadService newickLoadService;
	private GFFLoadService gffLoadService;

	private ObjectProperty<Graph> graphProperty;
	private ObjectProperty<TreeNode> treeProperty;

	/*
	 * Sets up the services, filechooser and treeproperty.
	 */
	@SuppressWarnings("unused") @FXML
	private void initialize() {
		fileChooser = new FileChooser();
		graphProperty = new SimpleObjectProperty<>(this, "graph");
		treeProperty = new SimpleObjectProperty<>(this, "tree");

		gffLoadService = new GFFLoadService();
		gffLoadService.setOnFailed(e -> new ExceptionDialog(fileOpenPane.getParent(),
						gffLoadService.getException(), "Error loading annotations file!"));
		gffLoadService.setOnRunning(e -> progressDialog.show());
		gffLoadService.setOnSucceeded(e -> {
			graphLoadService.setAnnotations(gffLoadService.getValue());
			graphLoadService.restart();
		});

		graphLoadService = new GraphLoadService();
		graphLoadService.setOnFailed(e -> new ExceptionDialog(fileOpenPane.getParent(),
						graphLoadService.getException(), "Error loading graph files!"));
		graphLoadService.setOnSucceeded(e -> {
			graphProperty.setValue(graphLoadService.getValue());
			progressDialog.close();
		});

		newickLoadService = new NewickLoadService();
		newickLoadService.setOnFailed(e -> new ExceptionDialog(fileOpenPane.getParent(),
						newickLoadService.getException(), "Error loading newick file!"));
		newickLoadService.setOnSucceeded(e -> treeProperty.setValue(newickLoadService.getValue()));

		animation = new LeftSlideAnimation(fileOpenPane, WIDTH, ANIM_DURATION, Position.LEFT);
		bindDisabledFieldsAndButtons();
	}

	/*
	 * Disables the openbutton when either no newick file or no node file is selected.
	 */
	private void bindDisabledFieldsAndButtons() {
		BooleanBinding emptyGraphFile = nodeField.textProperty().isEmpty()
				.or(edgeField.textProperty().isEmpty());
		gffField.disableProperty().bind(graphProperty.isNull().and(emptyGraphFile));
		// At least both graph files or the newick file must be filled.
		openButton.disableProperty().bind(emptyGraphFile
				.and(newickField.textProperty().isEmpty())
				.and(gffField.disableProperty()));
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
			newickLoadService.setNewickFile(newickFile);
			newickField.setText(newickLoadService.getNewickFile().getAbsolutePath());
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
			gffLoadService.setGffFilePath(gffFile.getAbsolutePath());
			gffField.setText(gffLoadService.getGffFilePath());
		}
	}

	/*
	 * If the open button is clicked, open the files if selected and hide the pane. Clears the
	 * text fields and updates the current file labels if files are opened.
	 */
	@SuppressWarnings("unused") @FXML
	private void onOpenAction() {
		progressDialog = new ProgressDialog(fileOpenPane.getParent());
		resetTextFields();
		animation.toggle();
		if (gffLoadService.getGffFilePath() != null
				&& graphLoadService.getNodeFile() != null
				&& graphLoadService.getEdgeFile() != null) {
			// TODO: replace this with the ability to specify a db path and
			//       a check whether this path is already in use by Neo4j.
			try {
				FileUtils.deleteRecursively(new File(graphLoadService.getDatabase()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			gffLoadService.restart();
			curGffLabel.setText(gffLoadService.getGffFilePath());
			curNodeLabel.setText(graphLoadService.getNodeFile().getAbsolutePath());
			curEdgeLabel.setText(graphLoadService.getEdgeFile().getAbsolutePath());
		}
		if (newickLoadService.getNewickFile() != null) {
			newickLoadService.restart();
			curNewickLabel.setText(newickLoadService.getNewickFile().getAbsolutePath());
		}
	}

	/* Clears the files, textfields and hides the pane. */
	@SuppressWarnings("unused") @FXML
	private void onCancelAction(ActionEvent actionEvent) {
		animation.toggle();
		graphLoadService.setNodeFile(null);
		graphLoadService.setEdgeFile(null);
		newickLoadService.setNewickFile(null);
		gffLoadService.setGffFilePath(null);
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
		return fileChooser.showOpenDialog(fileOpenPane.getScene().getWindow());
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
	 * Toggles the pane, showing or hiding it with a sliding animation.
	 */
	public void toggle() {
		animation.toggle();
	}
}
