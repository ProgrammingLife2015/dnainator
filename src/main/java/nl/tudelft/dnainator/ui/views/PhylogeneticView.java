package nl.tudelft.dnainator.ui.views;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import nl.tudelft.dnainator.tree.TreeNode;
import nl.tudelft.dnainator.ui.drawables.phylogeny.AbstractNode;
import nl.tudelft.dnainator.ui.drawables.phylogeny.Edge;
import nl.tudelft.dnainator.ui.drawables.phylogeny.InternalNode;
import nl.tudelft.dnainator.ui.drawables.phylogeny.Label;
import nl.tudelft.dnainator.ui.drawables.phylogeny.LeafNode;

/**
 * A {@link Pane} that positions {@link TreeNode}s as in a phylogenetic tree.
 */
public class PhylogeneticView extends Pane {
	private static final double LEAFHEIGHT = 30;
	private static final int MARGIN = 30;
	private static final double LEVELWIDTH = 3 * MARGIN;
	private static final int SQUARE = 8;
	private static final int OFFSET = 8;
	private double currentLeafY;

	/**
	 * Constructs a new {@link PhylogeneticView}.
	 * 
	 * @param root The root of the phylogenetic tree.
	 */
	public PhylogeneticView(TreeNode root) {
		this.currentLeafY = MARGIN;

		setPadding(new Insets(MARGIN, 0, 0, MARGIN));
		getChildren().add(draw(root, MARGIN, 0));
	}

	private AbstractNode draw(TreeNode node, double x, double y) {
		List<TreeNode> children = node.getChildren();
		if (children.size() == 0) {
			return drawLeaf(node, x);
		}
		return drawInternal(children, x, y);
	}

	private AbstractNode drawLeaf(TreeNode leaf, double x) {
		double y = currentLeafY;

		Label label = new Label(x + OFFSET, y + OFFSET, leaf.getName());
		getChildren().add(label);
		currentLeafY += LEAFHEIGHT;

		return new LeafNode(leaf, label, x, y, SQUARE);
	}

	private AbstractNode drawInternal(List<TreeNode> children, double x, double y) {
		List<AbstractNode> drawnChildren = new ArrayList<>();

		// Draw all children and attach an outgoing edge.
		for (int i = 0; i < children.size(); i++) {
			AbstractNode node = draw(children.get(i), x + LEVELWIDTH, y + (i * LEAFHEIGHT));
			Edge edge = new Edge(node);

			drawnChildren.add(node);
			node.setIncomingEdge(edge);
			getChildren().addAll(edge, node);
		}

		AbstractNode first = drawnChildren.get(0);
		AbstractNode last = drawnChildren.get(drawnChildren.size() - 1);

		// Draw self and attach children's edges to it.
		InternalNode root = new InternalNode(drawnChildren, x, first.getCenterY()
				+ (last.getCenterY() - first.getCenterY()) / 2, SQUARE);
		for (AbstractNode child : drawnChildren) {
			child.getIncomingEdge().bindTo(root);
		}
		return root;
	}

}
