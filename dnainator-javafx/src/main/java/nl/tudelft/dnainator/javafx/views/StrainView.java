package nl.tudelft.dnainator.javafx.views;

import javafx.geometry.Point2D;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.ColorServer;
import nl.tudelft.dnainator.javafx.drawables.strains.Strain;
import nl.tudelft.dnainator.javafx.widgets.contexts.ViewContext;

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

		setOnContextMenuRequested(e -> {
			ViewContext.getInstance().show(StrainView.this, e.getScreenX(), e.getScreenY());
			e.consume();
		});

		strain = new Strain(colorServer, graph);
		setTransforms(strain);
		getChildren().add(strain);
	}

	@Override
	public void pan(Point2D delta) {
		super.pan(delta);
		strain.update(cameraToWorld(getLayoutBounds()));
	}

	@Override
	public void zoom(double delta, Point2D center) {
		super.zoom(delta, center);
		strain.update(cameraToWorld(getLayoutBounds()));
	}

	@Override
	public void resetZoom() {
		super.resetZoom();
		strain.update(cameraToWorld(getLayoutBounds()));
	}

	@Override
	public void resetTranslate() {
		super.resetTranslate();
		strain.update(cameraToWorld(getLayoutBounds()));
	}
}
