package nl.tudelft.dnainator.javafx.views;

import javafx.geometry.Point2D;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.ColorServer;
import nl.tudelft.dnainator.javafx.drawables.strains.Strain;

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
		getChildren().add(strain);
		updateStrain();
	}

	private void updateStrain() {
		strain.update(cameraToWorld(getLayoutBounds()), scale.getMxx());
	}

	@Override
	public void pan(Point2D delta) {
		super.pan(delta);
		updateStrain();
	}

	/**
	 * Zoom based on center for stepper.
	 * @param delta the amount to zoom in.
	 */
	public void zoom(double delta) {
		zoom(delta, getCenter());
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
	
	/**
	 * Sets the panning of the {@link StrainView}.
	 * @param x the amount to pan on the x axis.
	 * @param y the amount to pan on the y axis.
	 */
	public void setPan(double x, double y) {
		scale.setToIdentity();
		translate.setX(x);
		translate.setY(y);
	}
	
	/**
	 * Zoom the maximum amount.
	 */
	public void zoomInMax() {
		Point2D world;
		try {
			world = scale.inverseTransform(getCenter().getX() - toCenter.getX() - translate.getX(),
					getCenter().getY() - toCenter.getY() - translate.getY());
			Transform newScale = scale.createConcatenation(new Scale(ZOOM_IN_BOUND, ZOOM_IN_BOUND,
										world.getX(), world.getY()));
				scale.setToTransform(newScale);
		} catch (NonInvertibleTransformException e) {
			e.printStackTrace();
		}
		strain.update(cameraToWorld(getLayoutBounds()), scale.getMxx());
	}
	
	
	/**
	 * Get the {@link Strain} of the {@link StrainView}.
	 * @return the strain.
	 */
	public Strain getStrain() {
		return strain;
	}
}
