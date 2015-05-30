package nl.tudelft.dnainator.ui.drawables.phylogeny;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import nl.tudelft.dnainator.tree.TreeNode;

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
	 */
	public InternalNode(AbstractNode parent, double xOffset, double yOffset) {
		super(parent, xOffset, yOffset);
		//bindInactiveProperties();
	}

	private void bindInactiveProperties() {
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
