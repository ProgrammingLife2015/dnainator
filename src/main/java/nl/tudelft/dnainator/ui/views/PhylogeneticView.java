package nl.tudelft.dnainator.ui.views;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import nl.tudelft.dnainator.tree.TreeNode;
import nl.tudelft.dnainator.ui.drawables.PhylogeneticEdge;
import nl.tudelft.dnainator.ui.drawables.PhylogeneticLabel;
import nl.tudelft.dnainator.ui.drawables.PhylogeneticNode;

/**
 * A {@link Pane} that positions {@link TreeNode}s as in a phylogenetic tree.
 */
public class PhylogeneticView extends Pane {
	private static final double LEAFHEIGHT = 30;
	private static final int MARGIN = 30;
	private static final double LEVELWIDTH = 3 * MARGIN;
	private static final int SQUARE = 4;
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

	private PhylogeneticNode draw(TreeNode node, double x, double y) {
		List<TreeNode> children = node.getChildren();
		if (children.size() == 0) {
			return drawLeaf(node, x);
		}
		return drawInternal(node, children, x, y);
	}

	private PhylogeneticNode drawLeaf(TreeNode leaf, double x) {
		double y = currentLeafY;

		getChildren().add(new PhylogeneticLabel(x + OFFSET, y + OFFSET, leaf.getName()));
		currentLeafY += LEAFHEIGHT;

		return new PhylogeneticNode(x, y, SQUARE);
	}

	private PhylogeneticNode drawInternal(TreeNode root, List<TreeNode> children,
			double x, double y) {
		List<PhylogeneticNode> drawnChildren = new ArrayList<>();

		// Draw all children and attach an outgoing edge.
		for (int i = 0; i < children.size(); i++) {
			PhylogeneticNode node = draw(children.get(i), x + LEVELWIDTH, y + (i * LEAFHEIGHT));
			PhylogeneticEdge edge = new PhylogeneticEdge(node);

			drawnChildren.add(node);
			node.setIncomingEdge(edge);
			getChildren().addAll(edge, node);
		}

		PhylogeneticNode first = drawnChildren.get(0);
		PhylogeneticNode last = drawnChildren.get(drawnChildren.size() - 1);

		// Draw self and attach children's edges to it.
		PhylogeneticNode self = new PhylogeneticNode(x, first.getCenterY()
				+ (last.getCenterY() - first.getCenterY()) / 2, SQUARE);
		for (PhylogeneticNode child : drawnChildren) {
			child.getIncomingEdge().bindTo(self);
		}
		return self;
	}

}
