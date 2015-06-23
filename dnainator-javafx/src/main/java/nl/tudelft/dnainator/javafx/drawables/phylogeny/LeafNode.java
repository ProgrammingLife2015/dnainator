package nl.tudelft.dnainator.javafx.drawables.phylogeny;

import javafx.collections.MapChangeListener;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import nl.tudelft.dnainator.tree.TreeNode;
import nl.tudelft.dnainator.javafx.exceptions.AllColorsInUseException;
import nl.tudelft.dnainator.javafx.ColorMap;
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
	private Text label;
	private ColorMap colorMap;
	private boolean highlighted;

	/**
	 * Constructs a new {@link LeafNode}.
	 * @param node The original treenode.
	 * @param colorMap The {@link ColorMap} to bind to for colors.
	 */
	public LeafNode(TreeNode node, ColorMap colorMap) {
		this.node = node;
		this.label = new Text(LABEL_X_OFFSET, LABEL_Y_OFFSET, node.getName());
		this.label.onMouseClickedProperty().bind(shape.onMouseClickedProperty());
		this.colorMap = colorMap;
		this.colorMap.addListener(change -> {
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
				colorMap.askColor(node.getName());
			} catch (AllColorsInUseException e) {
				new ExceptionDialog(null, e, "All colors are currently in use!");
			}
		} else {
			colorMap.revokeColor(node.getName());
		}
	}
}
