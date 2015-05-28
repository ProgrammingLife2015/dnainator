package nl.tudelft.dnainator.ui.drawables.phylogeny;

import nl.tudelft.dnainator.tree.TreeNode;
import nl.tudelft.dnainator.ui.AllColorsInUseException;
import nl.tudelft.dnainator.ui.ColorServer;
import nl.tudelft.dnainator.ui.widgets.dialogs.ExceptionDialog;

/**
 * This class represents leaf nodes in the phylogenetic tree. Each leaf node has a reference to
 * a label, displaying the source of the DNA strain it represents.
 */
public class LeafNode extends AbstractNode {
	private TreeNode node;
	private boolean highlighted;
	private Label label;

	/**
	 * Constructs a new {@link LeafNode}.
	 * @param node The {@link TreeNode} this leaf represents.
	 * @param label This node's label.
	 * @param x This node's center x-coordinate.
	 * @param y This node's center y-coordinate.
	 * @param dim This node's dimensions.
	 */
	public LeafNode(TreeNode node, Label label, double x, double y, double dim) {
		super(x, y, dim);
		this.node = node;
		this.highlighted = false;
		this.label = label;
	}

	@Override
	public void onMouseClicked() {
		if (getInactive()) {
			return;
		}

		removeStyles();
		if (!highlighted) {
			try {
				String color = ColorServer.getInstance().getColor(node.getName());
				addStyle(color);
				highlighted = true;
			} catch (AllColorsInUseException e) {
				new ExceptionDialog(null, e, "The maximum amount of colors are in use");
			}
		} else {
			ColorServer.getInstance().revokeColor(node.getName());
			highlighted = false;
		}
	}

	@Override
	public void setInactive(boolean state) {
		if (highlighted) {
			removeStyles();
			ColorServer.getInstance().revokeColor(node.getName());
			highlighted = false;
		}
		inactive.set(state);
	}

	@Override
	protected void addStyle(String style) {
		getStyleClass().add(style);
		incomingEdge.getStyleClass().add(style + "-edge");
		label.getStyleClass().add(style);
	}

	@Override
	protected void removeStyles() {
		getStyleClass().clear();
		incomingEdge.getStyleClass().clear();
		label.getStyleClass().clear();
	}
}
