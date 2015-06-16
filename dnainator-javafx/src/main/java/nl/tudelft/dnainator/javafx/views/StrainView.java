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
	private void setPan(double x, double y) {
		scale.setTx(0);
		scale.setTy(0);
		translate.setX(x * scale.getMxx());
		translate.setY(y * scale.getMxx());
		updateStrain();
	}

	/**
	 * Sets the panning to a specific rank in the {@link Strain}.
	 * @param rank The rank to pan to.
	 */
	public void gotoRank(int rank) {
		setPan(-rank * strain.getRankWidth(), 0);
	}
	
	/**
	 * Center view on a node given the id.
	 * @param id the id of the node.
	 */
	public void centerNodeVertically(String id) {
		translate.setY(-strain.getClusters()
				.get(id).getTranslateY() * strain.getRankWidth());
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
