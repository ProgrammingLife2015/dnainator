package nl.tudelft.dnainator.ui.drawables;

import nl.tudelft.dnainator.core.SequenceNode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * The drawable node is the JavaFX counterpart of {@link SequenceNode}.
 */
public class DrawableNode extends Circle {
	private static final double RADIUS = 20;
	private static final Paint FILL = Color.rgb(242, 173, 12);
	private SequenceNode node;

	/**
	 * Instantiate a new DrawableNode with default radius and default fill.
	 * @param node The {@link SequenceNode} this DrawableNode represents.
	 * @param x This DrawableNode's x-coordinate.
	 * @param y This DrawableNode's y-coordinate.
	 */
	public DrawableNode(SequenceNode node, double x, double y) {
		this(node, x, y, RADIUS, FILL);
	}

	/**
	 * Instantiate a new DrawableNode with default fill.
	 * @param node The {@link SequenceNode} this DrawableNode represents.
	 * @param x This DrawableNode's x-coordinate.
	 * @param y This DrawableNode's y-coordinate.
	 * @param radius This DrawableNode's radius.
	 */
	public DrawableNode(SequenceNode node, double x, double y, double radius) {
		this(node, x, y, radius, FILL);
	}

	/**
	 * Instantiate a new DrawableNode.
	 * @param node The {@link SequenceNode} this DrawableNode represents.
	 * @param x This DrawableNode's x-coordinate.
	 * @param y This DrawableNode's y-coordinate.
	 * @param radius This DrawableNode's radius.
	 * @param fill This DrawableNode's interior color.
	 */
	public DrawableNode(SequenceNode node, double x, double y, double radius, Paint fill) {
		super(x, y, radius, fill);
		this.node = node;

		getStyleClass().add("drawable-node");
	}

	/**
	 * @return This DrawableNode's {@link SequenceNode}.
	 */
	public SequenceNode getSequenceNode() {
		return node;
	}
}
