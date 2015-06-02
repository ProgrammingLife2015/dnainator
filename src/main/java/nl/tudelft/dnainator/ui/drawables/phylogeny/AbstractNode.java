package nl.tudelft.dnainator.ui.drawables.phylogeny;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * An abstract node for use in a phylogenetic tree. It provides the basic implementation
 * for a node in the phylogenetic tree, and forms an abstract interface for both leaf and
 * internal nodes.
 */
public abstract class AbstractNode extends Group {
	protected static final int DIM = 8;
	protected DoubleProperty margin = new SimpleDoubleProperty(0, "margin");
	protected Shape shape;

	/**
	 * Constructs a new {@link AbstractNode}.
	 */
	public AbstractNode() {
		this.shape = getShape();
		getChildren().add(this.shape);
		this.shape.setOnMouseClicked(e -> onMouseClicked());
	}

	/**
	 * @return the shape of this node. Default is a centered rectangle.
	 */
	public Shape getShape() {
		return new Rectangle(0 - DIM / 2, 0 - DIM / 2, DIM, DIM);
	}

	/**
	 * This function is called when the {@link AbstractNode} receives a mouse click. Internal nodes
	 * treat this differently from leaf nodes.
	 */
	public abstract void onMouseClicked();

	/**
	 * Adds a CSS class to the {@link AbstractNode}.
	 * @param style The CSS class to add.
	 */
	protected void addStyle(String style) {
		shape.getStyleClass().add(style);
	}

	/**
	 * Removes all CSS classes from the {@link AbstractNode}.
	 */
	protected void removeStyles() {
		shape.getStyleClass().clear();
	}

	/**
	 * @return The margin of the node for its siblings.
	 */
	public final double getMargin() {
		return margin.get();
	}

	/**
	 * @return The margin property.
	 */
	public DoubleProperty marginProperty() {
		return margin;
	}
}
