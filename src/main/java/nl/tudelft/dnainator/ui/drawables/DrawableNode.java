package nl.tudelft.dnainator.ui.drawables;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.ui.models.ModelItem;
import nl.tudelft.dnainator.ui.widgets.contexts.NodeContext;

/**
 * The drawable node is the JavaFX counterpart of {@link SequenceNode}.
 */
public class DrawableNode extends ModelItem {
	private static final String TYPE = "Node";
	private static final double RADIUS = 3;
	private static final Paint FILL = Color.rgb(242, 173, 12);
	private SequenceNode node;

	/**
	 * Instantiate a new DrawableNode with default radius and default fill.
	 * @param parent	the parent of this {@link DrawableNode}
	 * @param node		The {@link SequenceNode} this DrawableNode represents.
	 */
	public DrawableNode(ModelItem parent, SequenceNode node) {
		this(parent, node, RADIUS, FILL);
	}

	/**
	 * Instantiate a new DrawableNode with default fill.
	 * @param parent	the parent of this {@link DrawableNode}
	 * @param node		The {@link SequenceNode} this DrawableNode represents.
	 * @param radius	This DrawableNode's radius.
	 */
	public DrawableNode(ModelItem parent, SequenceNode node, double radius) {
		this(parent, node, radius, FILL);
	}

	/**
	 * Instantiate a new DrawableNode.
	 * @param parent	the parent of this {@link DrawableNode}
	 * @param node The {@link SequenceNode} this DrawableNode represents.
	 * @param radius This DrawableNode's radius.
	 * @param fill This DrawableNode's interior color.
	 */
	public DrawableNode(ModelItem parent, SequenceNode node, double radius, Paint fill) {
		super(parent);
		this.node = node;

		bindLocalToRoot(parent.localToRootProperty());

		getContent().getChildren().add(new Circle(radius, fill));
		getStyleClass().add("drawable-node");
		setOnContextMenuRequested(e -> {
			NodeContext.getInstance().show(DrawableNode.this, e.getScreenX(), e.getScreenY());
			e.consume();
		});

		for (String e : node.getIncoming()) {
			DrawableNode o = getNodes().get(e);
			if (o != null) {
				Line l = new Line();
				l.startXProperty().bind(layoutXProperty());
				l.startYProperty().bind(layoutYProperty());
				l.endXProperty().bind(new DoubleBinding() {
					{
						super.bind(o.localToRootProperty());
						super.bind(localToRootProperty());
					}
					@Override
					protected double computeValue() {
						return (o.getLocalToRoot().getTx() + o.getLayoutX())
							- (getLocalToRoot().getTx() + getLayoutX());
					}
				});
				l.endYProperty().bind(new DoubleBinding() {
					{
						super.bind(o.localToRootProperty());
						super.bind(localToRootProperty());
					}
					@Override
					protected double computeValue() {
						return (o.getLocalToRoot().getTy() + o.getLayoutY())
							- (getLocalToRoot().getTy() + getLayoutY());
					}
				});
				getContent().getChildren().add(l);
			}
		}
	}

	/**
	 * @return This DrawableNode's {@link SequenceNode}.
	 */
	public SequenceNode getSequenceNode() {
		return node;
	}

	@Override
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
	public void update(Bounds b) {
		// TODO Auto-generated method stub
	}
}
