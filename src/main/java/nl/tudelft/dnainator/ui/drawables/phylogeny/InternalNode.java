package nl.tudelft.dnainator.ui.drawables.phylogeny;

import java.util.List;

/**
 * This class represents internal nodes in the phylogenetic tree. Each internal node has a list
 * of children (even though in our case the tree is always binary).
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
	}
}
