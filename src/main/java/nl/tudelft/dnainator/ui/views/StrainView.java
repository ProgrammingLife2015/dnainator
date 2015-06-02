package nl.tudelft.dnainator.ui.views;

import javafx.geometry.Point2D;
import nl.tudelft.dnainator.ui.models.GraphItem;
import nl.tudelft.dnainator.ui.widgets.contexts.ViewContext;

/**
 * An implementation of {@link AbstractView} for displaying DNA strains.
 */
public class StrainView extends AbstractView {
	private GraphItem gi;

	/**
	 * Creates a new strain view instance.
	 */
	public StrainView() {
		super();

		setOnContextMenuRequested(e -> {
			ViewContext.getInstance().show(StrainView.this, e.getScreenX(), e.getScreenY());
			e.consume();
		});

		gi = new GraphItem();
		setTransforms(gi);
		getChildren().add(gi);
	}

	@Override
	public void pan(Point2D delta) {
		super.pan(delta);
		gi.update(cameraToWorld(getLayoutBounds()));
	}

	@Override
	public void zoom(double delta, Point2D center) {
		super.zoom(delta, center);
		gi.update(cameraToWorld(getLayoutBounds()));
	}
}
