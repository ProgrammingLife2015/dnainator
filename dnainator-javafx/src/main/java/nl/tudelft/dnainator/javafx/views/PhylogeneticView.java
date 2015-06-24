package nl.tudelft.dnainator.javafx.views;

import javafx.geometry.Point2D;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import nl.tudelft.dnainator.core.impl.TreeNode;
import nl.tudelft.dnainator.javafx.ColorMap;
import nl.tudelft.dnainator.javafx.drawables.phylogeny.AbstractNode;
import nl.tudelft.dnainator.javafx.drawables.phylogeny.InternalNode;
import nl.tudelft.dnainator.javafx.drawables.phylogeny.LeafNode;

import java.util.stream.Collectors;

/**
 * An implementation of {@link AbstractView} for displaying a phylogenetic tree.
 */
public class PhylogeneticView extends AbstractView {
	private static final double SCALE = 0.1;
	private static final double ZOOM_IN_BOUND = 0.5;
	private static final double ZOOM_OUT_BOUND = 0.015;
	private ColorMap colorMap;

	/**
	 * Constructs a new {@link PhylogeneticView}.
	 * @param colorMap The {@link ColorMap} to communicate with.
	 * @param root The root {@link TreeNode}.
	 */
	public PhylogeneticView(ColorMap colorMap, TreeNode root) {
		super();
		this.colorMap = colorMap;
		redraw(root);
	}

	@Override
	public Affine getScale() {
		return new Affine(new Scale(SCALE, SCALE));
	}

	@Override
	protected void zoom(double delta, Point2D center) {
		double zoom = 1 + delta * ZOOM_FACTOR;
		Transform newScale = computeZoom(zoom, center);
		if (newScale.getMyy() > ZOOM_OUT_BOUND && newScale.getMxx() < ZOOM_IN_BOUND) {
			scale.setToTransform(newScale);
		}
	}

	private void redraw(TreeNode node) {
		getChildren().clear();
		AbstractNode root = draw(node);
		setTransforms(root);
		getChildren().add(root);
	}

	private AbstractNode draw(TreeNode node) {
		if (node.getChildren().size() == 0) {
			return new LeafNode(node, colorMap);
		}

		return new InternalNode(node.getChildren().stream()
				.map(this::draw)
				.collect(Collectors.toList()));
	}
}
