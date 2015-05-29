package nl.tudelft.dnainator.ui.models;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Bounds;
import javafx.scene.Group;

/**
 * The abstract {@link CompositeItem} represents an object in the viewable model,
 * that can not only hold content, but also children.
 *
 * The list of children can be dynamically changed, based on the zoom level.
 */
public abstract class CompositeItem extends ModelItem {
	private Group childContent;
	private List<ModelItem> children;

	/**
	 * Base constructor for a {@link CompositeItem}.
	 * Every {@link CompositeItem} needs a reference to its parent.
	 * @param parent	the parent of this {@link CompositeItem}
	 * @param rank		the rank of this {@link ClusterItem}
	 */
	public CompositeItem(ModelItem parent, int rank) {
		super(parent, rank);

		childContent = new Group();
		children = new ArrayList<>();
		getChildren().add(childContent);
	}

	/**
	 * Return the root of the children of this {@link CompositeItem}.
	 * @return	the root of the children
	 */
	public Group getChildContent() {
		return childContent;
	}

	/**
	 * Return the list of childitems this {@link CompositeItem} has.
	 * @return	the list of child items
	 */
	public List<ModelItem> getChildItems() {
		return children;
	}

	public abstract void loadChildren(Bounds b);

	/**
	 * Toggle between displaying own content or children.
	 * @param visible	true for visible
	 */
	public void toggle(Bounds b, boolean visible) {
		if (visible && !getContent().isVisible()) {
			getChildContent().getChildren().clear();
			getContent().setVisible(true);
		}
		if (!visible) {
			loadChildren(b);
			getContent().setVisible(false);
			getChildContent().getChildren().addAll(getChildItems());
		}
	}

	/**
	 * Update visibility for this node and children.
	 * @param b	the bounds of the viewport
	 * @param t	the threshold for viewing
	 */
	public void update(Bounds b, Thresholds t) {
		if (b.getWidth() > t.get()) {
			toggle(b, true);
		} else {
			toggle(b, false);

			for (ModelItem m : getChildItems()) {
				m.update(b);
			}
		}
	}
}
