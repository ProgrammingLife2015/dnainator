package nl.tudelft.dnainator.ui.views;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Scale;
import nl.tudelft.dnainator.tree.TreeNode;
import nl.tudelft.dnainator.ui.drawables.phylogeny.AbstractNode;
import nl.tudelft.dnainator.ui.drawables.phylogeny.Edge;
import nl.tudelft.dnainator.ui.drawables.phylogeny.InternalNode;
import nl.tudelft.dnainator.ui.drawables.phylogeny.LeafNode;

/**
 * An implementation of {@link AbstractView} for displaying a phylogenetic tree.
 */
public class PhylogeneticView extends AbstractView {
	private static final int SCALE = 1;
	private static final double LEVELWIDTH = 150;
	private static final double LEAFHEIGHT = 30;
	private ObjectProperty<TreeNode> rootProperty = new SimpleObjectProperty<>(null, "root");

	/**
	 * Constructs a new {@link PhylogeneticView}.
	 */
	public PhylogeneticView() {
		super();
		rootProperty().addListener((obj, oldV, newV) -> redraw());
	}

	@Override
	public Affine getScale() {
		return new Affine(new Scale(SCALE, SCALE));
	}

	private void redraw() {
		getChildren().clear();
		AbstractNode root = draw(null, getRoot(), 0, 0);
		setTransforms(root);
		getChildren().add(root);
	}

	private AbstractNode draw(AbstractNode parent, TreeNode node, double xOffset, double yOffset ) {
		if (node.getChildren().size() == 0) {
			return new LeafNode(parent, node, xOffset, yOffset);
		}

		AbstractNode self = new InternalNode(parent, xOffset, yOffset);
		self.getChildren().add(draw(self, node.getChildren().get(0), LEVELWIDTH, LEAFHEIGHT));
		self.getChildren().add(draw(self, node.getChildren().get(1), LEVELWIDTH, -LEAFHEIGHT));
		return self;
		//List<TreeNode> children = node.getChildren();
		//if (children.size() == 0) {
		//	return drawLeaf(node);
		//}
		//return drawInternal(children);
	}

	/*private AbstractNode drawInternal(List<TreeNode> children) {
		List<AbstractNode> drawnChildren = new ArrayList<>();

		// Draw all children and attach an outgoing edge.
		for (int i = 0; i < children.size(); i++) {
			AbstractNode node = draw(children.get(i));
			//Edge edge = new Edge(node);

			drawnChildren.add(node);
			//node.setIncomingEdge(edge);
			//group.getChildren().addAll(edge, node);
		}

		// Draw self and attach children's edges to it.
		InternalNode root = new InternalNode(drawnChildren, LEVELWIDTH, SQUARE);
		//for (AbstractNode child : drawnChildren) {
		//	child.getIncomingEdge().bindTo(root);
		//}
		return root;
	}
*/
	/**
	 * @param root The {@link TreeNode} to set as root.
	 */
	public final void setRoot(TreeNode root) {
		rootProperty.set(root);
	}

	/**
	 * @return The root node, if any.
	 */
	public final TreeNode getRoot() {
		return rootProperty.get();
	}

	/**
	 * @return The root node property.
	 */
	public ObjectProperty<TreeNode> rootProperty() {
		return rootProperty;
	}

}
