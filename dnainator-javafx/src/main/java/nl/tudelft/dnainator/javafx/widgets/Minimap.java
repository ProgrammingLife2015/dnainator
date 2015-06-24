package nl.tudelft.dnainator.javafx.widgets;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import nl.tudelft.dnainator.core.EnrichedSequenceNode;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.drawables.strains.Strain;
import nl.tudelft.dnainator.javafx.views.StrainView;

/**
 * The minimap gives a global overview of the DNA strains in the {@link StrainView}, based on the
 * amount of base pairs.
 */
public class Minimap extends Pane {
	private static final String VIEW_STYLE = "view";
	private static final String SPACER_STYLE = "spacer";
	private static final double HEIGHT = 50;
	private static final double SPACER_HEIGHT = 7;
	private Strain strain;
	private Graph graph;
	private StrainView strainView;
	private DoubleProperty widthPerBase = new SimpleDoubleProperty(0, "widthPerBase");

	/**
	 * Instantiates a new {@link Minimap}.
	 * @param strain The {@link Strain} to operate on.
	 * @param graph The {@link Graph} that the {@link Strain} operates on.
	 * @param strainView The {@link StrainView} that contains this minimap.
	 */
	public Minimap(Strain strain, Graph graph, StrainView strainView) {
		this.strain = strain;
		this.graph = graph;
		this.strainView = strainView;
		this.widthPerBase.bind(widthProperty().divide(graph.getMaxBasePairs()));
		setHeight(HEIGHT);

		drawSpacer();
		// Wait for the Strain to update its properties.
		Platform.runLater(this::drawViewport);

		setOnMouseClicked(this::onMouseClicked);
	}

	private void drawSpacer() {
		Rectangle spacer = new Rectangle();
		spacer.getStyleClass().add(SPACER_STYLE);
		spacer.widthProperty().bind(widthPerBase.multiply(graph.getMaxBasePairs()));
		spacer.setHeight(SPACER_HEIGHT);
		spacer.yProperty().bind(
				heightProperty().divide(2).subtract(spacer.heightProperty().divide(2)));
		getChildren().add(spacer);
	}

	private void drawViewport() {
		Rectangle view = new Rectangle();
		view.getStyleClass().add(VIEW_STYLE);
		view.heightProperty().bind(heightProperty());
		strain.minRankProperty().addListener((obj, ov, nv) -> {
			view.setX(getBasePairs(nv.intValue()) * widthPerBase.get());
		});
		strain.maxRankProperty().addListener((obj, ov, nv) -> {
			view.setWidth(getBasePairs(nv.intValue()) * widthPerBase.get() - view.getX());
		});
		getChildren().add(view);
	}

	private int getBasePairs(int rank) {
		return graph.getRank(rank).stream()
				.mapToInt(EnrichedSequenceNode::getBaseDistance)
				.max().orElse(0);
	}

	private void onMouseClicked(MouseEvent e) {
		int base = (int) (e.getX() / widthPerBase.get());
		int rank = graph.getRankFromBasePair(base);
		strainView.gotoRank(rank);
	}
}
