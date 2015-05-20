package nl.tudelft.dnainator.ui.drawables;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.shape.Line;
import nl.tudelft.dnainator.ui.models.NodeItem;
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
		this.dst = dest;
		
		getStyleClass().add("drawable-edge");
		setOnContextMenuRequested(e -> {
			EdgeContext.getInstance().show(DrawableEdge.this, e.getScreenX(), e.getScreenY());
			e.consume();
		});
		
		startXProperty().bind(dest.translateXProperty().subtract(src.translateXProperty()));
		startYProperty().bind(dest.translateYProperty().subtract(src.translateYProperty()));
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
