package nl.tudelft.dnainator.javafx.views;

import javafx.geometry.Point2D;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.ColorServer;
import nl.tudelft.dnainator.javafx.drawables.strains.Strain;
import nl.tudelft.dnainator.javafx.widgets.StrainControl;

/**
 * An implementation of {@link AbstractView} for displaying DNA strains.
 */
public class StrainView extends AbstractView {
	private Strain strain;
	private StrainControl control;

	/**
	 * Creates a new strain view instance.
	 * @param colorServer The {@link ColorServer} to communicate with.
	 * @param graph The Graph that holds the strains.
	 */
	public StrainView(ColorServer colorServer, Graph graph) {
		super();
		strain = new Strain(colorServer, graph);

		setTransforms(strain);
		setupStrainControl();
		getChildren().addAll(strain, control);
		updateStrain();
	}

	private void setupStrainControl() {
		control = new StrainControl(this);
		control.translateXProperty().bind(widthProperty().subtract(control.widthProperty()));
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
		resetZoom();
		translateX(x);
		translateY(y);
	}
	
	/**
	 * Translate the {@link StrainView} on the x axis.
	 * @param x the amount to translate horizontally.
	 */
	public void translateX(double x) {
		translate.setX(x);
	}
	
	/**
	 * Translate the {@link StrainView} on the y axis.
	 * @param y the amount to translate vertically.
	 */
	public void translateY(double y) {
		translate.setY(y);
	}
	
	/**
	 * Zoom the maximum amount.
	 */
	public void zoomInMax() {
		scale.setToTransform(computeZoom(ZOOM_IN_BOUND, getCenter()));
		strain.update(cameraToWorld(getLayoutBounds()), scale.getMxx());
	}
	
	/**
	 * Get the {@link Strain} of the {@link StrainView}.
	 * @return the strain.
	 */
	public Strain getStrain() {
		return strain;
	}
	
	/**
	 * @return the {@link StrainControl} of the {@link StrainView}.
	 */
	public StrainControl getStrainControl() {
		return control;
	}
}
