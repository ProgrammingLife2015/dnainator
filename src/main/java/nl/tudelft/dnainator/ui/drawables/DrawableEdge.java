package nl.tudelft.dnainator.ui.drawables;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.shape.Line;
import nl.tudelft.dnainator.ui.models.NodeItem;
import nl.tudelft.dnainator.ui.widgets.contexts.EdgeContext;

/**
 * The drawable edge is the JavaFX counterpart of
 * {@link nl.tudelft.dnainator.core.Edge}.
 */
public class DrawableEdge extends Line {
	/**
	 * Instantiate a new DrawableEdge.
	 * @param src This edge's source node.
	 * @param dest This edge's destination node.
	 */
	public DrawableEdge(NodeItem src, NodeItem dest) {
		getStyleClass().add("drawable-edge");
		setOnContextMenuRequested(e -> {
			EdgeContext.getInstance().show(DrawableEdge.this, e.getScreenX(), e.getScreenY());
			e.consume();
		});
		bind(src, dest);
	}
	
	private void bind(NodeItem src, NodeItem dest) {
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
}
