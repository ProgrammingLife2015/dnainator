package nl.tudelft.dnainator.ui.drawables.phylogeny;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * An abstract node for use in a phylogenetic tree. It provides the basic implementation
 * for a node in the phylogenetic tree, as each node will need to have one incoming edge (except
 * the root...) and a pair of (x,y) coordinates.
 */
public abstract class AbstractNode extends Group {
	private static final String INACTIVE = "inactive";
	private static final double LEVELWIDTH = 150;
	private static final int DIM = 8;
	protected BooleanProperty inactive = new SimpleBooleanProperty(false, "inactive");
	protected DoubleProperty margin = new SimpleDoubleProperty(0, "margin");
	protected Shape shape;
	protected Edge incomingEdge;

	/**
	 * Constructs a new {@link AbstractNode}.
	 */
	public AbstractNode() {
		this.shape = new Rectangle(0 - DIM / 2, 0 - DIM / 2, DIM, DIM);
		getChildren().add(this.shape);
		this.shape.setOnMouseClicked(e -> onMouseClicked());

		this.translateXProperty().set(LEVELWIDTH);
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
	 * @return The margin of the node for its siblings.
	 */
	public final double getMargin() {
		return margin.get();
	}

	/**
	 * @return The inactive property.
	 */
	public BooleanProperty inactiveProperty() {
		return inactive;
	}

	/**
	 * @return The inactive property.
	 */
	public DoubleProperty marginProperty() {
		return margin;
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
