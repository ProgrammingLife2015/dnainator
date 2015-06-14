package nl.tudelft.dnainator.javafx.widgets;

import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import nl.tudelft.dnainator.core.EnrichedSequenceNode;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.views.StrainView;

/**
 * A widget that shows several {@link Control}s to interact with the {@link StrainView}.
 */
public class StrainControl extends VBox {
	private static final double PADDING = 10;
	private static final double WIDTH = 200;
	private static final String NODE = "Jump to node...";
	private static final String RANK = "Jump to rank...";
	private static final String ERROR = "Invalid input!";
	private static final String MAGGLASS = "magnifying-glass.png";
	private StrainView strainView;
	private Slider stepper;
	private ImageView magGlass;
	private TextField jumpTo;
	private int numberInput;

	/**
	 * Instantiates a new {@link StrainControl}.
	 * @param strainView The {@link StrainView} to interact with.
	 */
	public StrainControl(StrainView strainView) {
		this.strainView = strainView;
		setPadding(new Insets(PADDING));
		setSpacing(PADDING);
		setMaxWidth(WIDTH);
		setPickOnBounds(false);
		jumpTo = new TextField();
		setupImage();
		setupStepper();
		
		getChildren().add(stepper);
	}

	/**
	 * Setup the jump to node {@link TextField}.
	 * @return the text field of the jump to node.
	 */
	private void createJumpToNode() {
		jumpTo.clear();
		jumpTo.setStyle("");
		jumpTo.setPrefColumnCount(NODE.length());
		jumpTo.setPromptText(NODE);
		jumpTo.setOnAction(e -> {
			Graph curGraph = strainView.getStrain().getGraph();
			String inputText = jumpTo.getCharacters().toString();
			if (isInteger(inputText) && curGraph.getNode(inputText) != null) {
				EnrichedSequenceNode reqNode = curGraph.getNode(inputText);
				strainView.setPan(-reqNode.getRank() * strainView.getStrain().getRankWidth() 
						- strainView.getTranslateX(), 0);
				strainView.zoomInMax();
				strainView.setTranslateY(-strainView.getStrain().getClusters()
						.get(inputText).getTranslateY() * strainView.getStrain().getRankWidth());
			} else {
				promptInvalid();
			}
		});
	}
	
	/**
	 * Setup the jump to rank {@link TextField}.
	 * @return the text field of the jump to rank.
	 */
	private void createJumpToRank() {
		jumpTo.clear();
		jumpTo.setStyle("");
		jumpTo.setPrefColumnCount(RANK.length());
		jumpTo.setPromptText(RANK);
		jumpTo.setOnAction(e -> {
			Graph curGraph = strainView.getStrain().getGraph();
			if (isInteger(jumpTo.getCharacters().toString()) 
					&& curGraph.getRank(numberInput).size() != 0) {
				strainView.setPan(-numberInput * strainView.getStrain().getRankWidth() 
						- strainView.getTranslateX(), 0);
				strainView.zoomInMax();
			} else {
				promptInvalid();
			}
		});
	}
	
	private void setupImage() {
		Image image = new Image(MAGGLASS);
		magGlass = new ImageView();
		magGlass.setImage(image);
		magGlass.setSmooth(true);
		magGlass.setCache(true);
		magGlass.setPreserveRatio(true);
		// CHECKSTYLE.OFF: MagicNumber
		magGlass.setFitWidth(20);
		magGlass.setTranslateY(-33);
		magGlass.setTranslateX(-29);
		// CHECKSTYLE.ON: MagicNumber
	}

	private void setupStepper() {
		stepper = new Slider();
		// CHECKSTYLE.OFF: MagicNumber
		stepper.setMin(0);
		stepper.setMax(100);
		stepper.setValue(0);
		stepper.setShowTickLabels(true);
		stepper.setShowTickMarks(true);
		stepper.setMajorTickUnit(50);
		stepper.setMinorTickCount(5);
		stepper.setBlockIncrement(10);
		// CHECKSTYLE.ON: MagicNumber
		stepper.valueProperty().addListener((obj, oldV, newV) -> {
			if (oldV.doubleValue() < newV.doubleValue()) {
				strainView.zoom(newV.doubleValue());
			} else {
				strainView.zoom(-(oldV.doubleValue()));
			}
		});
	}
	
	private void promptInvalid() {
		jumpTo.clear();
		jumpTo.setPromptText(ERROR);
		jumpTo.setStyle("-fx-prompt-text-fill: red;");
	}
	
	private boolean isInteger(String s) {
		try {
			numberInput = Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	/**
	 * Toggles visibility of the stepper.
	 */
	public void toggleStepper() {
		stepper.setVisible(!stepper.isVisible());
	}
	
	/**
	 * Toggle the jump to node text field.
	 */
	public void toggleJumpNode() {
		getChildren().clear();
		createJumpToNode();
		getChildren().addAll(jumpTo, magGlass, stepper);
		requestFocus();
	}
	
	/**
	 * Toggle the jump to rank text field.
	 */
	public void toggleJumpRank() {
		getChildren().clear();
		createJumpToRank();
		getChildren().addAll(jumpTo, magGlass, stepper);
		requestFocus();
	}
}
