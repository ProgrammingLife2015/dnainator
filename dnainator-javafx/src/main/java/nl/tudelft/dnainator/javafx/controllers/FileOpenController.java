package nl.tudelft.dnainator.javafx.controllers;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.services.GraphLoadService;
import nl.tudelft.dnainator.javafx.widgets.animations.RightSlideAnimation;
import nl.tudelft.dnainator.javafx.widgets.animations.SlidingAnimation;
import nl.tudelft.dnainator.javafx.widgets.animations.TransitionAnimation.Position;
import nl.tudelft.dnainator.javafx.widgets.dialogs.ExceptionDialog;
import nl.tudelft.dnainator.javafx.widgets.dialogs.ProgressDialog;

import org.neo4j.io.fs.FileUtils;

import java.io.File;

/**
 * Controls the file open pane on the left side of the application. It offers options
 * to open node, edge and newick files.
 */
public class FileOpenController {
	public static final String EDGE = ".edge.graph";
	public static final String NODE = ".node.graph";
	public static final String NEWICK = ".nwk";
	public static final String GFF = ".gff";
	public static final String DR = ".txt";

	private static final int WIDTH = 550;
	private static final int ANIM_DURATION = 250;

	@SuppressWarnings("unused") @FXML private AnchorPane container;
	@SuppressWarnings("unused") @FXML private TextField nodeField;
	@SuppressWarnings("unused") @FXML private TextField edgeField;
	@SuppressWarnings("unused") @FXML private TextField newickField;
	@SuppressWarnings("unused") @FXML private TextField gffField;
	@SuppressWarnings("unused") @FXML private TextField drField;
	@SuppressWarnings("unused") @FXML private Label curNodeLabel;
	@SuppressWarnings("unused") @FXML private Label curEdgeLabel;
	@SuppressWarnings("unused") @FXML private Label curNewickLabel;
	@SuppressWarnings("unused") @FXML private Label curGffLabel;
	@SuppressWarnings("unused") @FXML private Label curDrLabel;
	@SuppressWarnings("unused") @FXML private Button openButton;

	private FileChooser fileChooser;
	private ProgressDialog progressDialog;
	private SlidingAnimation animation;

	private GraphLoadService graphLoadService;
	private ObjectProperty<Graph> graphProperty;

	/*
	 * Sets up the services, filechooser and treeproperty.
	 */
	@SuppressWarnings("unused") @FXML
	private void initialize() {
		fileChooser = new FileChooser();
		graphProperty = new SimpleObjectProperty<>(this, "graph");
		setupServices();
		
		animation = new RightSlideAnimation(container, WIDTH, ANIM_DURATION, Position.LEFT);
		bindDisabledFieldsAndButtons();
	}
	
	/**
	 * Setup the {@link GraphLoadService}.
	 */
	private void setupServices() {
		graphLoadService = new GraphLoadService();
		graphLoadService.progressProperty().addListener((e, oldV, newV) -> {
			progressDialog.setProgress(newV.doubleValue());
		});
		graphLoadService.setOnCancelled(e -> {
			progressDialog.close();
			try {
				FileUtils.deleteRecursively(new File(graphLoadService.getDatabase()));
			} catch (Exception exc) {
				new ExceptionDialog(container.getParent(), exc, "Failed to delete database.");
			}
		});
		graphLoadService.setOnFailed(e -> {
			progressDialog.close();
			new ExceptionDialog(container.getParent(), graphLoadService.getException(),
					"Error loading graph files!");
		});
		graphLoadService.setOnRunning(e -> progressDialog.show());
		graphLoadService.setOnSucceeded(e -> {
			progressDialog.close();
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
		File nodeFile = selectFile("Node file", NODE,
					nodeField, graphLoadService.nodeFileProperty());
		if (nodeFile != null) {
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
		File edgeFile = selectFile("Edge file", EDGE,
					edgeField, graphLoadService.edgeFileProperty());
		if (edgeFile != null) {
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
		selectFile("Newick file", NEWICK, newickField, graphLoadService.newickFileProperty());
	}

	/*
	 * If the GFF textfield is clicked, open the filechooser and if a file is selected,
	 * fill in the GFF textfield.
	 */
	@SuppressWarnings("unused") @FXML
	private void onGFFFieldClicked() {
		selectFile("GFF file", GFF, gffField, graphLoadService.gffFileProperty());
	}

	@SuppressWarnings("unused") @FXML
	private void onDRFieldClicked() {
		selectFile("DR mutations file", DR, drField, graphLoadService.drFileProperty());
	}

	/*
	 * If the open button is clicked, open the files if selected and hide the pane. Clears the
	 * text fields and updates the current file labels if files are opened.
	 */
	@SuppressWarnings("unused") @FXML
	private void onOpenAction() {
		progressDialog = new ProgressDialog(container.getScene().getWindow(), graphLoadService);
		resetTextFields();
		animation.toggle();
		if (graphLoadService.canLoad()) {
			graphLoadService.setDatabase(graphLoadService.getNewPath());
			graphLoadService.restart();
		}
	}

	/* Clears the files, textfields and hides the pane. */
	@SuppressWarnings("unused") @FXML
	private void onCancelAction(ActionEvent actionEvent) {
		animation.toggle();
		graphLoadService.setGffFile(null);
		graphLoadService.setNodeFile(null);
		graphLoadService.setEdgeFile(null);
		graphLoadService.setNewickFile(null);
		graphLoadService.setDRFile(null);
		graphLoadService.cancel();
		resetTextFields();
	}

	private void resetTextFields() {
		nodeField.clear();
		edgeField.clear();
		newickField.clear();
		gffField.clear();
		drField.clear();
	}

	/**
	 * Sets up the {@link FileChooser} to use have the specified title and to use the
	 * given extension as a filter.
	 *
	 * @param title          The title of the {@link FileChooser}.
	 * @param extension      The value to filter for the
	 *                       {@link javafx.stage.FileChooser.ExtensionFilter}.
	 * @param field          The {@link TextField} that has to be updated
	 * @param objectProperty The file property that has to be set
	 * @return The selected file, or null if none is chosen.
	 */
	private File selectFile(String title, String extension,
				TextField field, ObjectProperty<File> objectProperty) {
		File file = showFileChooser(title, extension);
		if (file != null) {
			objectProperty.setValue(file);
			field.setText(file.getAbsolutePath());
		}
		return file;
	}

	/**
	 * Show the file chooser.
	 * @param title		the title
	 * @param extension	the extension filter
	 * @return		the selected file or null
	 */
	public File showFileChooser(String title, String extension) {
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
	public File openEdgeFile(String path) {
		return new File(path.substring(0, path.length() - NODE.length()).concat(EDGE));
	}

	/**
	 * @param path Creates a node file from the path of an edge file. This requires
	 *             the node file to be in the same directory and to have the same name
	 *             as the edge file.
	 * @return The edge file.
	 */
	public File openNodeFile(String path) {
		return new File(path.substring(0, path.length() - EDGE.length()).concat(NODE));
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
