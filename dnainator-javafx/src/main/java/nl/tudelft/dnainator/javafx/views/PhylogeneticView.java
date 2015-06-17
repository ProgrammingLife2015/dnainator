package nl.tudelft.dnainator.javafx.views;

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
	private ColorServer colorServer;

	/**
	 * Constructs a new {@link PhylogeneticView}.
	 * @param colorServer The {@link ColorServer} to communicate with.
	 * @param root The root {@link TreeNode}.
	 */
	public PhylogeneticView(ColorServer colorServer, TreeNode root) {
		super();
		this.colorServer = colorServer;
		redraw(root);
	}

	@Override
	public Affine getScale() {
		return new Affine(new Scale(SCALE, SCALE));
	}

	private void redraw(TreeNode node) {
		getChildren().clear();
		AbstractNode root = draw(node);
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
}
