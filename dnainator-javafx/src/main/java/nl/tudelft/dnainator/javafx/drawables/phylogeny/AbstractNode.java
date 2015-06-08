package nl.tudelft.dnainator.javafx.drawables.phylogeny;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import nl.tudelft.dnainator.javafx.drawables.Drawable;

/**
 * An abstract node for use in a phylogenetic tree. It provides the basic implementation
 * for a node in the phylogenetic tree, and forms an abstract interface for both leaf and
 * internal nodes.
 */
public abstract class AbstractNode extends Group implements Drawable {
	protected static final int DIM = 8;
	protected DoubleProperty margin = new SimpleDoubleProperty(0, "margin");
	protected IntegerProperty leafCount = new SimpleIntegerProperty(0, "leafCount");
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
	 * Allows each implementing class to define its own {@link Shape}.
	 * @return The {@link Shape} to use in the implementing class. Default is a centered rectangle.
	 */
	public Shape getShape() {
		return new Rectangle(0 - DIM / 2, 0 - DIM / 2, DIM, DIM);
	}

	/**
	 * This function is called when the {@link AbstractNode} receives a mouse click. Internal nodes
	 * treat this differently from leaf nodes.
	 */
	public abstract void onMouseClicked();

	@Override
	public void addStyle(String style) {
		for (Node child : getChildren()) {
			child.getStyleClass().add(style);
		}
	}

	@Override
	public void removeStyle(String style) {
		for (Node child : getChildren()) {
			child.getStyleClass().remove(style);
		}
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

	/**
	 * @return The number of leafs of this node.
	 */
	public final int getLeafCount() {
		return leafCount.get();
	}

	/**
	 * @return The leaf count property, which keeps track of the number
	 * of leafs in this node.
	 */
	public IntegerProperty leafCountProperty() {
		return leafCount;
	}
}
