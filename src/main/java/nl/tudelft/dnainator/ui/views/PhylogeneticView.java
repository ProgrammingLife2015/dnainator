package nl.tudelft.dnainator.ui.views;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Group;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Scale;
import nl.tudelft.dnainator.tree.TreeNode;
import nl.tudelft.dnainator.ui.drawables.phylogeny.AbstractNode;
import nl.tudelft.dnainator.ui.drawables.phylogeny.Edge;
import nl.tudelft.dnainator.ui.drawables.phylogeny.InternalNode;
import nl.tudelft.dnainator.ui.drawables.phylogeny.Label;
import nl.tudelft.dnainator.ui.drawables.phylogeny.LeafNode;

/**
 * An implementation of {@link AbstractView} for displaying a phylogenetic tree.
 */
public class PhylogeneticView extends AbstractView {
	private static final int SCALE = 1;
	private static final double LEAFHEIGHT = 30;
	private static final int MARGIN = 30;
	private static final double LEVELWIDTH = 3 * MARGIN;
	private static final int SQUARE = 8;
	private static final int OFFSET = 8;
	private ObjectProperty<TreeNode> rootProperty = new SimpleObjectProperty<>(null, "root");
	private double currentLeafY;
	private Group group;

	/**
	 * Constructs a new {@link PhylogeneticView}.
	 */
	public PhylogeneticView() {
		super();
		group = new Group();
		setTransforms(group);
		getChildren().add(group);
		rootProperty().addListener((obj, oldV, newV) -> redraw());
	}

	@Override
	public Affine getScale() {
		return new Affine(new Scale(SCALE, SCALE));
	}

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

	private void redraw() {
		this.currentLeafY = MARGIN;
		group.getChildren().clear();
		group.getChildren().add(draw(getRoot(), MARGIN, 0));
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
		group.getChildren().add(label);
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
			group.getChildren().addAll(edge, node);
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
