package nl.tudelft.dnainator.ui.drawables.phylogeny;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

/**
 * An abstract node for use in a phylogenetic tree. It provides the basic implementation
 * for a node in the phylogenetic tree, as each node will need to have one incoming edge (except
 * the root...) and a pair of (x,y) coordinates.
 */
public abstract class AbstractNode extends Group {
	private static final String INACTIVE = "inactive";
	private static final int DIM = 8;
	protected BooleanProperty inactive = new SimpleBooleanProperty(false, "inactive");
	protected Edge incomingEdge;

	/**
	 * Constructs a new {@link AbstractNode}.
	 */
	public AbstractNode(AbstractNode parent, double xOffset, double yOffset) {
		getChildren().add(new Rectangle(DIM, DIM, DIM, DIM));

		if (parent != null) {
			translateXProperty().bind(parent.translateXProperty().add(xOffset));
			translateYProperty().bind(parent.translateYProperty().add(yOffset));
		}
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
