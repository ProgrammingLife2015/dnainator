package nl.tudelft.dnainator.ui.drawables.phylogeny;

import javafx.beans.binding.Bindings;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import nl.tudelft.dnainator.tree.TreeNode;
import nl.tudelft.dnainator.ui.AllColorsInUseException;
import nl.tudelft.dnainator.ui.ColorServer;
import nl.tudelft.dnainator.ui.widgets.dialogs.ExceptionDialog;

/**
 * This class represents leaf nodes in the phylogenetic tree. Each leaf node has a reference to
 * a label, displaying the source of the DNA strain it represents.
 */
public class LeafNode extends AbstractNode {
	private static final int OFFSET = 8;
	private TreeNode node;
	private Text label;
	private boolean highlighted;

	/**
	 * Constructs a new {@link LeafNode}.
	 */
	public LeafNode(AbstractNode parent, TreeNode node, double xOffset, double yOffset) {
		super(parent, xOffset, yOffset);
		this.node = node;
		this.label = new Text(node.getName());
		this.highlighted = false;

		label.setTextAlignment(TextAlignment.CENTER);
		getChildren().add(label);
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
