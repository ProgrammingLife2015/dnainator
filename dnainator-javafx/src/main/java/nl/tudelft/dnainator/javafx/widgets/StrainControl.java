package nl.tudelft.dnainator.javafx.widgets;

import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
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
	private static final String SEQUENCE = "Jump to node...";
	private StrainView strainView;
	double x = 0.0;

	/**
	 * Instantiates a new {@link StrainControl}.
	 * @param strainView The {@link StrainView} to interact with.
	 */
	public StrainControl(StrainView strainView) {
		this.strainView = strainView;
		setPadding(new Insets(PADDING));
		setSpacing(PADDING);
		setMaxWidth(WIDTH);

		getChildren().addAll(
				createJumpToSequence(),
				createSlider());
	}

	private TextField createJumpToSequence() {
		TextField jumpToSequenceEntry = new TextField();
		
		jumpToSequenceEntry.setPrefColumnCount(SEQUENCE.length());
		jumpToSequenceEntry.setPromptText(SEQUENCE);
		jumpToSequenceEntry.setOnAction(e -> {
			Graph curGraph = strainView.getStrain().getGraph();
			String inputText = jumpToSequenceEntry.getCharacters().toString();
			if (isInteger(inputText)) {
				EnrichedSequenceNode reqNode = curGraph.getNode(inputText);
				strainView.setPan(-reqNode.getRank() * strainView.getStrain().getRankWidth() 
						- strainView.getTranslateX(), 0);
			}
		});
		return jumpToSequenceEntry;
	}

	private Slider createSlider() {
		Slider slider = new Slider();
		slider.setMin(0);
		slider.setMax(100);
		slider.setValue(0);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setMajorTickUnit(50);
		slider.setMinorTickCount(5);
		slider.setBlockIncrement(10);
		slider.valueProperty().addListener((obj, oldV, newV) -> {
			if (oldV.doubleValue() < newV.doubleValue()) {
				strainView.zoom(newV.doubleValue());
			} else {
				strainView.zoom(-(oldV.doubleValue() - newV.doubleValue()));
			}
		});
		return slider;
	}
	
	private boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}
