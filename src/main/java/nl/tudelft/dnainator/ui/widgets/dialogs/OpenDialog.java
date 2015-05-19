package nl.tudelft.dnainator.ui.widgets.dialogs;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import nl.tudelft.dnainator.ui.services.GraphLoadService;
import nl.tudelft.dnainator.ui.services.NewickLoadService;

import java.io.File;

/**
 * Provides a way for the user to open node, edge and newick files in one place.
 */
public class OpenDialog extends Alert {
	private static final String EDGE = ".edge.graph";
	private static final String NODE = ".node.graph";
	private static final String NEWICK = ".nwk";
	private static final int TEXTFIELD_WIDTH = 400;
	private static final int GRID_PADDING = 10;

	private Node root;
	private GraphLoadService graphLoadService;
	private NewickLoadService newickLoadService;
	private TextField nodeField;
	private TextField edgeField;
	private TextField newickField;
	private Button openButton;

	/**
	 * Creates an OpenDialog of type {@link javafx.scene.control.Alert.AlertType}.NONE.
	 * It creates the content of the {@link Alert} and sets up all control.
	 *
	 * @param root          The root {@link Node} from where this dialog is created.
	 * @param graphService  The {@link GraphLoadService} used to generate the graph.
	 * @param newickService The {@link NewickLoadService} used to generate the phylogenetic tree.
	 */
	public OpenDialog(Node root, GraphLoadService graphService,
	                  NewickLoadService newickService) {
		super(AlertType.NONE);
		this.root = root;
		this.graphLoadService = graphService;
		this.newickLoadService = newickService;

		initOwner(root.getScene().getWindow());
		setTitle("Open files");
		setHeaderText("Please select the files to open.");

		ButtonType openButtonType = new ButtonType("Open", ButtonBar.ButtonData.OK_DONE);
		getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, openButtonType);
		openButton = (Button) getDialogPane().lookupButton(openButtonType);
		openButton.setDisable(true);

		setupGrid();
	}

	private void setupGrid() {
		GridPane grid = new GridPane();
		grid.setVgap(GRID_PADDING);
		grid.setHgap(GRID_PADDING);
		grid.setPadding(new Insets(GRID_PADDING));

		setupTextFields(grid);
		setupLabels(grid);

		getDialogPane().setContent(grid);
	}

	private void setupTextFields(GridPane grid) {
		nodeField = new TextField();
		nodeField.setPrefWidth(TEXTFIELD_WIDTH);
		nodeField.setPromptText("Node file (*" + NODE + ")");
		nodeField.setOnMouseClicked(e -> browseNodeListener());

		edgeField = new TextField();
		edgeField.setPromptText("Edge file (*" + EDGE + ")");
		edgeField.setOnMouseClicked(e -> browseEdgeListener());

		newickField = new TextField();
		newickField.setPromptText("Tree file (*" + NEWICK + ")");
		newickField.setOnMouseClicked(e -> browseNewickListener());

		grid.add(nodeField, 1, 0);
		grid.add(edgeField, 1, 1);
		grid.add(newickField, 1, 2);
	}

	private void setupLabels(GridPane grid) {
		grid.add(new Label("Nodes:"), 0, 0);
		grid.add(new Label("Edges:"), 0, 1);
		grid.add(new Label("Tree:"), 0, 2);
	}

	private void browseNodeListener() {
		File nodeFile = genFileChooser("Open node file", NODE)
				.showOpenDialog(root.getScene().getWindow());
		updateForNode(nodeFile);
	}

	private void browseEdgeListener() {
		File edgeFile = genFileChooser("Open edge file", EDGE)
				.showOpenDialog(root.getScene().getWindow());
		updateForEdge(edgeFile);
	}

	private void browseNewickListener() {
		File newickFile = genFileChooser("Open newick file", NEWICK)
				.showOpenDialog(root.getScene().getWindow());
		updateForNewick(newickFile);
	}

	private void updateForNode(File nodeFile) {
		if (nodeFile != null) {
			graphLoadService.setNodeFile(nodeFile);
			nodeField.setText(graphLoadService.getNodeFile().getAbsolutePath());
			graphLoadService.setEdgeFile(openEdgeFile(nodeFile.getPath()));
			edgeField.setText(graphLoadService.getEdgeFile().getAbsolutePath());
		}

		updateOpenButton();
	}

	private void updateForEdge(File edgeFile) {
		if (edgeFile != null) {
			graphLoadService.setEdgeFile(edgeFile);
			edgeField.setText(graphLoadService.getEdgeFile().getAbsolutePath());
			graphLoadService.setNodeFile(openNodeFile(edgeFile.getPath()));
			nodeField.setText(graphLoadService.getNodeFile().getAbsolutePath());
		}

		updateOpenButton();
	}

	private void updateForNewick(File newickFile) {
		if (newickFile != null) {
			newickLoadService.setNewickFile(newickFile);
			newickField.setText(newickLoadService.getNewickFile().getAbsolutePath());
		}

		updateOpenButton();
	}

	private FileChooser genFileChooser(String title, String extension) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(title);
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter(title, "*" + extension));
		return fileChooser;
	}

	private void updateOpenButton() {
		boolean isNodeFileSelected = graphLoadService.getNodeFile() != null;
		boolean isEdgeFileSelected = graphLoadService.getEdgeFile() != null;
		boolean isNewickFileSelected = newickLoadService.getNewickFile() != null;

		if (isNewickFileSelected || (isNodeFileSelected && isEdgeFileSelected)) {
			openButton.setDisable(false);
		} else {
			openButton.setDisable(true);
		}
	}

	private File openEdgeFile(String path) {
		return new File(path.substring(0, path.length() - NODE.length()).concat(EDGE));
	}

	private File openNodeFile(String path) {
		return new File(path.substring(0, path.length() - EDGE.length()).concat(NODE));
	}
}
