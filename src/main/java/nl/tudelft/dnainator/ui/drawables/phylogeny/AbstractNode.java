package nl.tudelft.dnainator.ui.drawables.phylogeny;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Rectangle;

/**
 * An abstract node for use in a phylogenetic tree. It provides the basic implementation
 * for a node in the phylogenetic tree, as each node will need to have one incoming edge (except
 * the root...) and a pair of (x,y) coordinates.
 */
public abstract class AbstractNode extends Rectangle {
	private static final String INACTIVE = "inactive";
	protected BooleanProperty inactive = new SimpleBooleanProperty(false, "inactive");
	private DoubleProperty centerX = new SimpleDoubleProperty(0.0, "centerX");
	private DoubleProperty centerY = new SimpleDoubleProperty(0.0, "centerY");
	protected Edge incomingEdge;

	/**
	 * Constructs a new {@link AbstractNode}.
	 * @param x This node's center x-coordinate.
	 * @param y This node's center y-coordinate.
	 * @param dim This node's dimensions.
	 */
	public AbstractNode(double x, double y, double dim) {
		super(x - dim / 2, y - dim / 2, dim, dim);

		setCenterX(x);
		setCenterY(y);
		setOnMouseClicked(e -> onMouseClicked());

		inactiveProperty().addListener((obj, oldV, newV) -> {
			if (newV) {
				addStyle(INACTIVE);
			} else {
				removeStyles();
			}
		});
	}

	/**
	 * This function is called when the {@link AbstractNode} receives a mouse click. Internal nodes
	 * treat this differently from leaf nodes.
	 */
	public abstract void onMouseClicked();

	/**
	 * Sets the inactive state of this {@link AbstractNode}. Note that only
	 * leaf nodes can change their state; their parents automatically update
	 * themselves if both children are inactive.
	 * @param state The new state of this {@link AbstractNode}.
	 */
	public abstract void setInactive(boolean state);

	/**
	 * Adds a CSS class to the {@link AbstractNode}.
	 * @param style The CSS class to add.
	 */
	protected abstract void addStyle(String style);

	/**
	 * Removes all CSS classes from the {@link AbstractNode}.
	 */
	protected abstract void removeStyles();

	/**
	 * @return The inactive state.
	 */
	public final boolean getInactive() {
		return inactive.get();
	}

	/**
	 * @return The inactive property.
	 */
	public BooleanProperty inactiveProperty() {
		return inactive;
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
	 * @param edge This {@link AbstractNode}'s incoming {@link Edge}.
	 */
	public void setIncomingEdge(Edge edge) {
		this.incomingEdge = edge;
	}

	/**
	 * @return This {@link AbstractNode}'s incoming {@link Edge}.
	 */
	public Edge getIncomingEdge() {
		return this.incomingEdge;
	}
}
