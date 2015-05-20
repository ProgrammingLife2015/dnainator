package nl.tudelft.dnainator.ui.drawables;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Rectangle;

/**
 * A node for use in a phylogenetic tree.
 */
public class PhylogeneticNode extends Rectangle {
	private DoubleProperty centerX = new SimpleDoubleProperty(0.0, "centerX");
	private DoubleProperty centerY = new SimpleDoubleProperty(0.0, "centerY");
	private PhylogeneticEdge incomingEdge;

	/**
	 * Constructs a new {@link PhylogeneticNode}.
	 * @param x This node's center x-coordinate.
	 * @param y This node's center y-coordinate.
	 * @param dim This node's dimensions.
	 */
	public PhylogeneticNode(double x, double y, double dim) {
		super(x - dim / 2, y - dim / 2, dim, dim);
		getStyleClass().add("phylogenetic-node");

		setCenterX(x);
		setCenterY(y);
	}

	/**
	 * @param x The center x-coordinate to set.
	 */
	public final void setCenterX(double x) {
		centerX.set(x);
	}

	/**
	 * @return The center x-coordinate.
	 */
	public final double getCenterX() {
		return centerX.get();
	}

	/**
	 * @return The center x property.
	 */
	public DoubleProperty centerXProperty() {
		return centerX;
	}

	/**
	 * @param y The center y-coordinate to set.
	 */
	public final void setCenterY(double y) {
		centerY.set(y);
	}

	/**
	 * @return The center y-coordinate.
	 */
	public final double getCenterY() {
		return centerY.get();
	}

	/**
	 * @return The center y property.
	 */
	public DoubleProperty centerYProperty() {
		return centerY;
	}

	/**
	 * @param edge This {@link PhylogeneticNode}'s incoming {@link PhylogeneticEdge}.
	 */
	public void setIncomingEdge(PhylogeneticEdge edge) {
		this.incomingEdge = edge;
	}

	/**
	 * @return This {@link PhylogeneticNode}'s incoming {@link PhylogeneticEdge}.
	 */
	public PhylogeneticEdge getIncomingEdge() {
		return this.incomingEdge;
	}
}
