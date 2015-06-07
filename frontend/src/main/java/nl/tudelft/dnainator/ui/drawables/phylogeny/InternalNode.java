package nl.tudelft.dnainator.ui.drawables.phylogeny;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents internal nodes in the phylogenetic tree. Each internal node has a list
 * of children (even though in our case the tree is always binary).
 * It can be collapsed by means of a clicklistener.
 */
public class InternalNode extends AbstractNode {
	private static final double LEVELWIDTH = 150;
	private List<AbstractNode> children;
	private List<Edge> outgoingEdges;

	/**
	 * Constructs a new {@link InternalNode}.
	 * @param children The children nodes of this InternalNode.
	 */
	public InternalNode(List<AbstractNode> children) {
		this.children = children;
		this.outgoingEdges = new ArrayList<>();
		bindMargins();
		bindLeafCount();

		// Position the children.
		DoubleBinding rangeBegin = marginProperty().divide(2).negate();
		for (AbstractNode child : children) {
			child.translateYProperty().bind(rangeBegin.add(child.marginProperty().divide(2)));
			child.translateXProperty().set(LEVELWIDTH);
			rangeBegin = rangeBegin.add(child.marginProperty());
			Edge e = new Edge(child);
			this.outgoingEdges.add(e);
		}
		this.getChildren().addAll(0, outgoingEdges);
		// Add the nodes after the edges, so that they're on top.
		this.getChildren().addAll(children);
	}

	/**
	 * Bind this margin property to the sum of the children margin properties.
	 */
	protected void bindMargins() {
		this.marginProperty().bind(Bindings.createDoubleBinding(() -> children.stream()
				.collect(Collectors.summingDouble(AbstractNode::getMargin)), children.stream()
				.map(AbstractNode::marginProperty).toArray(DoubleProperty[]::new)));
	}

	private void bindLeafCount() {
		this.leafCountProperty().bind(Bindings.createIntegerBinding(() -> children.stream()
				.collect(Collectors.summingInt(AbstractNode::getLeafCount)), children.stream()
				.map(AbstractNode::leafCountProperty).toArray(IntegerProperty[]::new)));
	}

	@Override
	public void onMouseClicked() {
		new CollapsedNode(this);
	}
}
