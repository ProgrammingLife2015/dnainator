package nl.tudelft.dnainator.ui.drawables.phylogeny;

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
	protected static final double LEAFHEIGHT = 30;
	protected static final int LABEL_X_OFFSET = 8;
	protected static final int LABEL_Y_OFFSET = 4;
	private TreeNode node;
	private Text label;
	private boolean highlighted;

	/**
	 * Constructs a new {@link LeafNode}.
	 * @param node The original treenode.
	 */
	public LeafNode(TreeNode node) {
		this.node = node;
		this.label = new Text(LABEL_X_OFFSET, LABEL_Y_OFFSET, node.getName());
		this.label.onMouseClickedProperty().bind(shape.onMouseClickedProperty());
		this.highlighted = false;
		this.marginProperty().set(LEAFHEIGHT);

		label.setTextAlignment(TextAlignment.CENTER);
		getChildren().add(label);
	}

	@Override
	public void onMouseClicked() {
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
	protected void addStyle(String style) {
		shape.getStyleClass().add(style);
		incomingEdge.getStyleClass().add(style + "-edge");
		label.getStyleClass().add(style);
	}

	@Override
	protected void removeStyles() {
		shape.getStyleClass().clear();
		incomingEdge.getStyleClass().clear();
		label.getStyleClass().clear();
	}
}
