package nl.tudelft.dnainator.ui.models;



import java.util.Map;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import nl.tudelft.dnainator.graph.Graph;

/**
 * The abstract {@link ModelItem} represents a single object in the viewable model.
 * This class follows the composite design pattern,
 * together with the {@link CompositeItem} and the leaf class, {@link RankItem}.
 *
 * Every {@link ModelItem} can hold JavaFX content that can be drawn to the screen.
 * What content is drawn will change dynamically based on the scale of the viewport.
 *
 * In order to determine the positioning of the JavaFX content,
 * every modelitem holds a rootToItem transform property, which should be bound to
 * the concatenation of all parent rootToItem properties when instantiating a
 * concrete subclass.
 */
public abstract class ModelItem extends Group {
	public static final int RANK_WIDTH = 10;
	public static final int NO_CLUSTERS = 330;

	private ModelItem parent;
	private Group content;
	private int rank;

	/**
	 * Base constructor for a {@link ModelItem}.
	 * Every {@link ModelItem} needs a reference to its parent.
	 * @param parent	the parent of this {@link ModelItem}
	 * @param rank		the rank of this {@link ModelItem}
	 */
	public ModelItem(ModelItem parent, int rank) {
		this.parent = parent;
		this.content = new Group();
		this.rank = rank;

		getChildren().add(content);
	}

	/**
	 * Return the content of this {@link ModelItem}.
	 * @return	the content
	 */
	public Group getContent() {
		return content;
	}

	/**
	 * Return the underlying graph of this {@link ModelItem}.
	 * @return	the underlying graph
	 */
	public Graph getGraph() {
		return getRoot().getGraph();
	}

	/**
	 * Return the underlying map of DrawableNodes.
	 * Should be changed to ModelItems, so we can have edges to clusters.
	 * @return	a map from id to drawable / modelitem
	 */
	public Map<String, NodeItem> getNodes() {
		return getRoot().getNodes();
	}

	/**
	 * Return the rank of this {@link ModelItem}.
	 * @return	the rank
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * Return the root of this {@link ModelItem}.
	 * @return	the root {@link ModelItem}
	 */
	public ModelItem getRoot() {
		return parent.getRoot();
	}

	/**
	 * Check whether this object intersects with the given viewport bounds.
	 * @param b	the given viewport bounds
	 * @return	true when (partially) in viewport, false otherwise
	 */
	public boolean isInViewport(Bounds b) {
		return b.contains(getContent().localToParent(0, 0));
	}

	/**
	 * Update method that should be called after scaling.
	 * This method checks how zoomed in we are by transforming bounds to root coordinates,
	 * and then dynamically adds and deletes items in the JavaFX scene graph.
	 *
	 * TODO: check whether something is visible in the viewport!
	 * @param b	the bounds of the viewport to update
	 */
	public abstract void update(Bounds b);
}
