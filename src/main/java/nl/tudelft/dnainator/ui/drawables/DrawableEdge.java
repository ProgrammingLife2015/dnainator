package nl.tudelft.dnainator.ui.drawables;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.shape.Line;
import nl.tudelft.dnainator.ui.models.DrawableNode.NodeItem;
import nl.tudelft.dnainator.ui.widgets.Propertyable;
import nl.tudelft.dnainator.ui.widgets.contexts.EdgeContext;

/**
 * The drawable edge is the JavaFX counterpart of
 * {@link nl.tudelft.dnainator.core.Edge}.
 */
public class DrawableEdge extends Line implements Propertyable {
	private static final String TYPE = "Edge";
	private NodeItem src;
	private NodeItem dst;

	/**
	 * Instantiate a new DrawableEdge.
	 * @param src This edge's source node.
	 * @param dest This edge's destination node.
	 */
	public DrawableEdge(NodeItem src, NodeItem dest) {
		this.src = src;
		this.dst = dst;
		
		getStyleClass().add("drawable-edge");
		setOnContextMenuRequested(e -> {
			EdgeContext.getInstance().show(DrawableEdge.this, e.getScreenX(), e.getScreenY());
			e.consume();
		});
		startXProperty().bind(src.layoutXProperty());
		startYProperty().bind(src.layoutYProperty());
		endXProperty().bind(new DoubleBinding() {
			{
				super.bind(src.localToRootProperty());
				super.bind(dest.localToRootProperty());
			}
			@Override
			protected double computeValue() {
				return (dest.getLocalToRoot().getTx() + dest.getLayoutX())
					- (src.getLocalToRoot().getTx() + src.getLayoutX());
			}
		});
		endYProperty().bind(new DoubleBinding() {
			{
				super.bind(src.localToRootProperty());
				super.bind(dest.localToRootProperty());
			}
			@Override
			protected double computeValue() {
				return (dest.getLocalToRoot().getTy() + dest.getLayoutY())
					- (src.getLocalToRoot().getTy() + src.getLayoutY());
			}
		});
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public List<String> getSources() {
		ArrayList<String> res = new ArrayList<>();
		res.add(src.getSequenceNode().getSource());
		res.add(dst.getSequenceNode().getSource());
		return res;
	}

	@Override
	public String getNodeId() {
		return "placeholder";
	}
}
