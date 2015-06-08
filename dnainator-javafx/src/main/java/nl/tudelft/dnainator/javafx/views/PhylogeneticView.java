package nl.tudelft.dnainator.javafx.views;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Scale;
import nl.tudelft.dnainator.tree.TreeNode;
import nl.tudelft.dnainator.javafx.ColorServer;
import nl.tudelft.dnainator.javafx.drawables.phylogeny.AbstractNode;
import nl.tudelft.dnainator.javafx.drawables.phylogeny.InternalNode;
import nl.tudelft.dnainator.javafx.drawables.phylogeny.LeafNode;

import java.util.stream.Collectors;

/**
 * An implementation of {@link AbstractView} for displaying a phylogenetic tree.
 */
public class PhylogeneticView extends AbstractView {
	private static final int SCALE = 1;
	private ObjectProperty<TreeNode> rootProperty = new SimpleObjectProperty<>(null, "root");
	private ColorServer colorServer;

	/**
	 * Constructs a new {@link PhylogeneticView}.
	 * @param colorServer The {@link ColorServer} to communicate with.
	 */
	public PhylogeneticView(ColorServer colorServer) {
		super();
		this.colorServer = colorServer;
		rootProperty().addListener((obj, oldV, newV) -> redraw());
	}

	@Override
	public Affine getScale() {
		return new Affine(new Scale(SCALE, SCALE));
	}

	private void redraw() {
		getChildren().clear();
		AbstractNode root = draw(getRoot());
		setTransforms(root);
		getChildren().add(root);
	}

	private AbstractNode draw(TreeNode node) {
		if (node.getChildren().size() == 0) {
			return new LeafNode(node, colorServer);
		}

		AbstractNode self = new InternalNode(node.getChildren().stream()
				.map(this::draw)
				.collect(Collectors.toList()));
		return self;
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

}
