package nl.tudelft.dnainator.ui.drawables.phylogeny;

/**
 * This class represents leaf nodes in the phylogenetic tree. Each leaf node has a reference to
 * a label, displaying the source of the DNA strain it represents.
 */
public class LeafNode extends AbstractNode {
	private Label label;

	/**
	 * Constructs a new {@link LeafNode}.
	 * @param label This node's label.
	 * @param x This node's center x-coordinate.
	 * @param y This node's center y-coordinate.
	 * @param dim This node's dimensions.
	 */
	public LeafNode(Label label, double x, double y, double dim) {
		super(x, y, dim);
		this.label = label;
	}
}
