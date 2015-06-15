package nl.tudelft.dnainator.javafx.widgets;

import java.util.Collection;
import java.util.NoSuchElementException;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import nl.tudelft.dnainator.core.EnrichedSequenceNode;
import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.views.StrainView;

/**
 * A widget that shows several {@link Control}s to interact with the {@link StrainView}.
 */
public class StrainControl extends VBox {
	private static final double PADDING = 10;
	private static final double WIDTH = 200;
	private static final int GENE_LENGTH = 5;
	private static final String NODE = "Jump to node...";
	private static final String RANK = "Jump to rank...";
	private static final String ANNOTATION = "Jump to annotation...";
	private static final String ERROR = "Invalid input!";
	private static final String INVALIDPROMPT = "jump-invalid-prompt";
	private StrainView strainView;
	private Slider stepper;
	private TextField jumpTo;
	private int numberInput;
	private String previousInput;
	private Collection<String> attachedAnnotations;
	private Graph curGraph;

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
		curGraph = strainView.getStrain().getGraph();
		
		// Wait for strain view to be created.
		Platform.runLater(this::createJumpToNode);
		setupStepper();
		getChildren().addAll(jumpTo, stepper);
	}

	/**
	 * Setup the jump to node {@link TextField}.
	 * @return the text field of the jump to node.
	 */
	private void createJumpToNode() {
		setupTextField(NODE);
		jumpTo.setOnAction(e -> jumpToNode());
	}
	
	private void jumpToNode() {
		String inputText = jumpTo.getCharacters().toString();
		if (isInteger(inputText) && curGraph.getNode(inputText) != null) {
			EnrichedSequenceNode reqNode = curGraph.getNode(inputText);
			resetPromptText(NODE);
			strainView.setPan(-reqNode.getRank() * strainView.getStrain().getRankWidth() 
					- strainView.getTranslateX(), 0);
			strainView.zoomInMax();
			strainView.translateY(-strainView.getStrain().getClusters()
					.get(inputText).getTranslateY() * strainView.getStrain().getRankWidth());
		} else {
			promptInvalid();
		}
	}
	
	/**
	 * Setup the jump to rank {@link TextField}.
	 * @return the text field of the jump to rank.
	 */
	private void createJumpToRank() {
		setupTextField(RANK);
		jumpTo.setOnAction(e -> jumpToRank());
	}
	
	private void jumpToRank() {
		if (isInteger(jumpTo.getCharacters().toString()) 
				&& curGraph.getRank(numberInput).size() != 0) {
			resetPromptText(RANK);
			strainView.setPan(-numberInput * strainView.getStrain().getRankWidth() 
					- strainView.getTranslateX(), 0);
			strainView.zoomInMax();
		} else {
			promptInvalid();
		}
	}
	
	/**
	 * Setup the jump to annotation {@link TextField}.
	 * @return the text field of the jump to annotation.
	 */
	private void createJumpToAnnotation() {
		setupTextField(ANNOTATION);
		jumpTo.setOnAction(e -> jumpToAnnotation());
	}
	
	private void jumpToAnnotation() {
		String inputText = jumpTo.getCharacters().toString();
		if (!isInteger(inputText)) {
			resetPromptText(ANNOTATION);
			if (previousInput != null && previousInput.equals(inputText) 
					&& attachedAnnotations != null && !attachedAnnotations.isEmpty()) {
				jumpToNextAnnotationNode();
				return;
			}
			jumpToAnnotationNode(inputText);
			previousInput = inputText;
		} else {
			promptInvalid();
		}
	}
	
	/**
	 * Search for a node with an annotation and jump to the first found.
	 * @param inputText the name of the annotation.
	 */
	private void jumpToAnnotationNode(String inputText) {
		try {
			Annotation annotation = curGraph.getAnnotations().getAll().stream().filter(a -> 
				a.getGeneName().toLowerCase().contains(inputText.toLowerCase()) 
				&& inputText.length() > GENE_LENGTH).findFirst().get();
			attachedAnnotations = annotation.getAnnotatedNodes();
			jumpToNextAnnotationNode();
		} catch (NoSuchElementException nse) {
			promptInvalid();
		}
	}
	
	/**
	 * Jump to the next node which was attached to the same annotation.
	 */
	private void jumpToNextAnnotationNode() {
		String next = attachedAnnotations.iterator().next();
		attachedAnnotations.remove(next);
		strainView.setPan(-curGraph.getNode(next).getRank() 
				* strainView.getStrain().getRankWidth() - strainView.getTranslateX(), 0);
		strainView.zoomInMax();
	}
	
	private void setupTextField(String name) {
		if (jumpTo.getPromptText().equals(name) && jumpTo.isVisible()) {
			jumpTo.setVisible(!jumpTo.isVisible());
			getChildren().remove(jumpTo);
		} else {
			jumpTo.setVisible(true);
			jumpTo.clear();
			jumpTo.setPrefColumnCount(name.length());
			resetPromptText(name);
			getChildren().clear();
			getChildren().addAll(jumpTo, stepper);
			requestFocus();
		}
	}
	
	private void resetPromptText(String name) {
		jumpTo.getStyleClass().remove(INVALIDPROMPT);
		jumpTo.setPromptText(name);
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
	
	/**
	 * Changes the prompt text of the {@link TextField} and marks it in red.
	 */
	private void promptInvalid() {
		jumpTo.clear();
		jumpTo.setPromptText(ERROR);
		jumpTo.getStyleClass().add(INVALIDPROMPT);
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
		createJumpToNode();
	}
	
	/**
	 * Toggle the jump to rank text field.
	 */
	public void toggleJumpRank() {
		createJumpToRank();
	}
	
	/**
	 * Toggle the jump to rank text field.
	 */
	public void toggleJumpAnnotation() {
		createJumpToAnnotation();
	}
}