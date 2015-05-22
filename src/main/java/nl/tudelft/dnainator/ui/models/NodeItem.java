package nl.tudelft.dnainator.ui.models;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.ui.drawables.DrawableEdge;
import nl.tudelft.dnainator.ui.widgets.contexts.NodeContext;

/**
 * The drawable node is the JavaFX counterpart of {@link SequenceNode}.
 */
public class NodeItem extends ModelItem {
	private static final double RADIUS = 3;
	private static final Paint FILL = Color.rgb(242, 173, 12);
	private SequenceNode node;
	// Should be moved to ModelItem
	private Group edges;
	
	/**
	 * Instantiate a new NodeItem with default radius and default fill.
	 * @param parent	the parent of this {@link NodeItem}
	 * @param node		The {@link SequenceNode} this NodeItem represents.
	 */
	public NodeItem(ModelItem parent, SequenceNode node) {
		this(parent, node, RADIUS, FILL);
	}

	/**
	 * Instantiate a new NodeItem with default fill.
	 * @param parent	the parent of this {@link NodeItem}
	 * @param node		The {@link SequenceNode} this NodeItem represents.
	 * @param radius	This NodeItem's radius.
	 */
	public NodeItem(ModelItem parent, SequenceNode node, double radius) {
		this(parent, node, radius, FILL);
	}

	/**
	 * Instantiate a new NodeItem.
	 * @param parent	the parent of this {@link NodeItem}
	 * @param node		The {@link SequenceNode} this NodeItem represents.
	 * @param radius	This NodeItem's radius.
	 * @param fill		This NodeItem's interior color.
	 */
	public NodeItem(ModelItem parent, SequenceNode node, double radius, Paint fill) {
		super(parent, node.getRank());
		this.node = node;
		this.edges = new Group();

		getStyleClass().add("drawable-node");
		getContent().getChildren().add(edges);
		getContent().getChildren().add(new Circle(radius, fill));
		setOnContextMenuRequested(e -> {
			NodeContext.getInstance().show(NodeItem.this, e.getScreenX(), e.getScreenY());
			e.consume();
		});
	}
	
	/**
	 * @return This NodeItem's {@link SequenceNode}.
	 */
	public SequenceNode getSequenceNode() {
		return node;
	}

	@Override
	public void update(Bounds b) {
		// FIXME: it's outgoing edges now
		if (edges.getChildren().size() < getSequenceNode().getOutgoing().size()) {
			for (String e : node.getOutgoing()) {
				NodeItem o = getNodes().get(e);
				if (o != null) {
					edges.getChildren().add(new DrawableEdge(o, this));
				}
			}
		}
	}
}
