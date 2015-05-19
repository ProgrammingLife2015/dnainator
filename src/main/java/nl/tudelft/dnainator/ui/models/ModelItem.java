package nl.tudelft.dnainator.ui.models;

import java.util.Map;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Transform;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.ui.widgets.Propertyable;

/**
 * The abstract {@link ModelItem} represents a single object in the viewable model.
 * This class follows the composite design pattern,
 * together with the {@link CompositeItem} and the leaf class, {@link RankItem}.
 *
 * Every {@link ModelItem} can hold JavaFX content that can be drawn to the screen.
 * What content is drawn will change dynamically based on the scale of the viewport.
 *
 * In order to determine the positioning of the JavaFX content,
 * every {@link ModelItem} holds a rootToItem transform property, which should be bound to
 * the concatenation of all parent rootToItem properties when instantiating a
 * concrete subclass.
 */
public abstract class ModelItem extends Pane implements Propertyable {
	public static final int CLUSTER_SIZE = 20;
	public static final int RANK_WIDTH = 10;

	static final int NO_CLUSTERS = 330;
	static final int NO_RANKS = 10;

	private ModelItem parent;
	private Group content;
	private ObjectProperty<Transform> localToRoot;

	/**
	 * Base constructor for a {@link ModelItem}.
	 * Every {@link ModelItem} needs a reference to its parent.
	 * @param parent	the parent of this {@link ModelItem}
	 */
	public ModelItem(ModelItem parent) {
		this.parent = parent;
		this.content = new Group();
		this.localToRoot = new SimpleObjectProperty<>();

		getChildren().add(content);
	}

	/**
	 * This method binds localToRoot to the concatenated transforms of the parent and child.
	 * Every subclass, except the root, should bind its parent for correct positioning.
	 * @param parent	the parent transform
	 */
	public void bindLocalToRoot(ObjectProperty<Transform> parent) {
		ObjectBinding<Transform> transform = new ObjectBinding<Transform>() {
			{
				super.bind(parent);
				super.bind(localToParentTransformProperty());
				localToRootProperty().set(computeValue());
			}
			@Override
			protected Transform computeValue() {
				return parent.get().createConcatenation(getLocalToParentTransform());
			}
		};
		localToRootProperty().bind(transform);
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
	 * Return the root of this {@link ModelItem}.
	 * @return	the root {@link ModelItem}
	 */
	public ModelItem getRoot() {
		return parent.getRoot();
	}

	/**
	 * Return the concatenation of transforms from the root to this item.
	 * @return	a concatenation of transforms
	 */
	public Transform getLocalToRoot() {
		return localToRoot.get();
	}

	/**
	 * Set the concatenation of transforms from the root to this item.
	 * @param t	a concatenation of transforms
	 */
	public void setLocalToRoot(Transform t) {
		localToRoot.set(t);
	}

	/**
	 * Return the property containing the concatenation of transforms from the root to this item.
	 * @return	a concatenation of transforms
	 */
	public ObjectProperty<Transform> localToRootProperty() {
		return localToRoot;
	}

	/**
	 * Transform a given bounding box b from local coordinates to root coordinates.
	 * @param b	the bounds to transform
	 * @return	the transformed bounds
	 */
	public Bounds localToRoot(Bounds b) {
		return getLocalToRoot().transform(b);
	}

	/**
	 * Transform a given point p from local coordinates to root coordinates.
	 * @param p	the point to transform
	 * @return	the transformed point
	 */
	public Point2D localToRoot(Point2D p) {
		return getLocalToRoot().transform(p);
	}

	/**
	 * Check whether this object intersects with the given viewport bounds.
	 * @param b	the given viewport bounds
	 * @return	true when (partially) in viewport, false otherwise
	 */
	public boolean isInViewport(Bounds b) {
		return b.intersects(localToRoot(getContent().getBoundsInLocal()));
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

	@Override
	public String getNodeId() {
		return "placeholder";
	}
}
