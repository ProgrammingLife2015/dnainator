package nl.tudelft.dnainator.ui.models;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import nl.tudelft.dnainator.core.SequenceNode;
<<<<<<< HEAD:src/main/java/nl/tudelft/dnainator/ui/drawables/DrawableNode.java
import nl.tudelft.dnainator.ui.models.ModelItem;
=======
import nl.tudelft.dnainator.ui.drawables.DrawableEdge;
import nl.tudelft.dnainator.ui.widgets.Propertyable;
>>>>>>> Add some basic cluster visualization, rename DrawableNode to NodeItem.:src/main/java/nl/tudelft/dnainator/ui/models/NodeItem.java
import nl.tudelft.dnainator.ui.widgets.contexts.NodeContext;

/**
 * The drawable node is the JavaFX counterpart of {@link SequenceNode}.
 */
<<<<<<< HEAD:src/main/java/nl/tudelft/dnainator/ui/drawables/DrawableNode.java
public class DrawableNode extends ModelItem {
=======
public class NodeItem extends ModelItem implements Propertyable {
	private static final double RADIUS = 3;
>>>>>>> Add some basic cluster visualization, rename DrawableNode to NodeItem.:src/main/java/nl/tudelft/dnainator/ui/models/NodeItem.java
	private static final String TYPE = "Node";
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
	 * @param node The {@link SequenceNode} this NodeItem represents.
	 * @param radius This NodeItem's radius.
	 * @param fill This NodeItem's interior color.
	 */
	public NodeItem(ModelItem parent, SequenceNode node, double radius, Paint fill) {
		super(parent);
		this.node = node;
		this.edges = new Group();

		bindLocalToRoot(parent.localToRootProperty());

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
		if (edges.getChildren().size() < getSequenceNode().getIncoming().size()) {
			for (String e : node.getIncoming()) {
				NodeItem o = getNodes().get(e);
				if (o != null) {
					edges.getChildren().add(new DrawableEdge(this, o));
				}
			}
		}
	}
	
	public String getType() {
		return TYPE;
	}

	@Override
	public List<String> getSources() {
		ArrayList<String> res = new ArrayList<>();
		res.add(node.getSource());
		return res;
	}

	@Override
	public String getNodeId() {
		return getSequenceNode().getId();
	}
}
