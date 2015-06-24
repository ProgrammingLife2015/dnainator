package nl.tudelft.dnainator.javafx.drawables.phylogeny;

import javafx.collections.MapChangeListener;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import nl.tudelft.dnainator.tree.TreeNode;
import nl.tudelft.dnainator.javafx.AllColorsInUseException;
import nl.tudelft.dnainator.javafx.ColorServer;
import nl.tudelft.dnainator.javafx.widgets.dialogs.ExceptionDialog;

/**
 * This class represents leaf nodes in the phylogenetic tree. Each leaf node has a reference to
 * a label, displaying the source of the DNA strain it represents.
 */
public class LeafNode extends AbstractNode {
	protected static final double LEAFHEIGHT = 300;
	protected static final int LABEL_X_OFFSET = 80;
	protected static final int LABEL_Y_OFFSET = 40;
	private TreeNode node;
	private ColorServer colorServer;
	private boolean highlighted;

	/**
	 * Constructs a new {@link LeafNode}.
	 * @param node The original treenode.
	 * @param colorServer The {@link ColorServer} to bind to for colors.
	 */
	public LeafNode(TreeNode node, ColorServer colorServer) {
		this.node = node;
		Text label = new Text(LABEL_X_OFFSET, LABEL_Y_OFFSET, node.getName());
		label.onMouseClickedProperty().bind(shape.onMouseClickedProperty());
		this.colorServer = colorServer;
		this.colorServer.addListener(change -> {
			if (change.getKey().equals(node.getName())) {
				onColorServerChanged(change);
			}
		});
		this.highlighted = false;
		this.marginProperty().set(LEAFHEIGHT);
		this.leafCountProperty().set(1);

		label.setTextAlignment(TextAlignment.CENTER);
		getChildren().add(label);
	}

	private void onColorServerChanged(
			MapChangeListener.Change<? extends String, ? extends String> change) {
		if (change.wasAdded()) {
			highlighted = true;
			addStyle(change.getValueAdded());
		} else {
			highlighted = false;
			removeStyle(change.getValueRemoved());
		}
	}

	@Override
	public void onMouseClicked() {
		if (!highlighted) {
			try {
				colorServer.askColor(node.getName());
			} catch (AllColorsInUseException e) {
				new ExceptionDialog(null, e, "All colors are currently in use!");
			}
		} else {
			colorServer.revokeColor(node.getName());
		}
	}
}
