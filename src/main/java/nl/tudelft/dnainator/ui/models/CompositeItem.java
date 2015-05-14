package nl.tudelft.dnainator.ui.models;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import nl.tudelft.dnainator.graph.Graph;

/**
 * The abstract {@link CompositeItem} represents an object in the viewable model,
 * that can not only hold content, but also children.
 *
 * The list of children can be dynamically changed, based on the zoom level.
 */
public abstract class CompositeItem extends ModelItem {
	private Group childRoot;
	private List<ModelItem> children;

	/**
	 * Base constructor for a {@link CompositeItem}.
	 * Every {@link CompositeItem} needs a reference to its graph.
	 * @param graph	a {@link Graph}
	 */
	public CompositeItem(Graph graph) {
		super(graph);

		childRoot = new Group();
		children = new ArrayList<>();
		getChildren().add(childRoot);
	}

	/**
	 * Return the root of the children of this {@link CompositeItem}.
	 * @return	the root of the children
	 */
	public Group getChildRoot() {
		return childRoot;
	}

	/**
	 * Set the root of the children of this {@link CompositeItem}.
	 * @param childroot	the new root of the children
	 */
	public void setChildRoot(Group childroot) {
		this.childRoot = childroot;
	}

	/**
	 * Return the list of childitems this {@link CompositeItem} has.
	 * @return	the list of child items
	 */
	public List<ModelItem> getChildItems() {
		return children;
	}

	/**
	 * Set the list of childitems this {@link CompositeItem} has.
	 * @param childItems	the new list of child items
	 */
	public void setChildItems(List<ModelItem> childItems) {
		this.children = childItems;
	}
}
