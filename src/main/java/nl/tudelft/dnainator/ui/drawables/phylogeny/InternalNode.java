package nl.tudelft.dnainator.ui.drawables.phylogeny;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;

import java.util.List;

/**
 * This class represents internal nodes in the phylogenetic tree. Each internal node has a list
 * of children (even though in our case the tree is always binary). It binds its own inactive
 * property to the AND of its children's inactive properties to automatically update itself.
 */
public class InternalNode extends AbstractNode {
	private List<AbstractNode> children;

	/**
	 * Constructs a new {@link InternalNode}.
	 * @param children The children that form the subtree of which this {@link InternalNode} is
	 *                 the root node.
	 * @param x This node's center x-coordinate.
	 * @param y This node's center y-coordinate.
	 * @param dim This node's dimensions.
	 */
	public InternalNode(List<AbstractNode> children, double x, double y, double dim) {
		super(x, y, dim);
		this.children = children;
		bindAllChildrenProperties();
	}

	private void bindAllChildrenProperties() {
		inactiveProperty().bind(
				Bindings.createBooleanBinding(() -> children.stream()
						.allMatch(AbstractNode::getInactive), children.stream()
						.map(AbstractNode::inactiveProperty).toArray(BooleanProperty[]::new)));
	}

	@Override
	public void onMouseClicked() {
		setInactive(!getInactive());
	}

	@Override
	public void setInactive(boolean state) {
		for (AbstractNode child : children) {
			child.setInactive(state);
		}
	}

	@Override
	protected void addStyle(String style) {
		getStyleClass().add(style);
		// The root node has no incoming edge.
		if (incomingEdge != null) {
			incomingEdge.getStyleClass().add(style + "-edge");
		}
	}

	@Override
	protected void removeStyles() {
		getStyleClass().clear();
		if (incomingEdge != null) {
			incomingEdge.getStyleClass().clear();
		}
	}
}
