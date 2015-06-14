package nl.tudelft.dnainator.javafx.views;

import javafx.geometry.Point2D;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.ColorServer;
import nl.tudelft.dnainator.javafx.drawables.strains.Strain;
import nl.tudelft.dnainator.javafx.widgets.Minimap;

/**
 * An implementation of {@link AbstractView} for displaying DNA strains.
 */
public class StrainView extends AbstractView {
	private Strain strain;

	/**
	 * Creates a new strain view instance.
	 * @param colorServer The {@link ColorServer} to communicate with.
	 * @param graph The Graph that holds the strains.
	 */
	public StrainView(ColorServer colorServer, Graph graph) {
		super();

		strain = new Strain(colorServer, graph);
		setTransforms(strain);
		getChildren().addAll(strain, setupMinimap(strain, graph));
		updateStrain();
	}

	private Minimap setupMinimap(Strain strain, Graph graph) {
		Minimap minimap = new Minimap(strain, graph);
		minimap.translateXProperty().bind(translateXProperty());
		minimap.translateYProperty().bind(heightProperty().subtract(minimap.heightProperty()));
		widthProperty().addListener((obj, oldV, newV) -> minimap.setPrefWidth(newV.doubleValue()));
		return minimap;
	}

	private void updateStrain() {
		strain.update(cameraToWorld(getLayoutBounds()), scale.getMxx());
	}

	@Override
	public void pan(Point2D delta) {
		super.pan(delta);
		updateStrain();
	}

	@Override
	public void zoom(double delta, Point2D center) {
		super.zoom(delta, center);
		updateStrain();
	}

	@Override
	public void resetZoom() {
		super.resetZoom();
		updateStrain();
	}

	@Override
	public void resetTranslate() {
		super.resetTranslate();
		updateStrain();
	}
}
