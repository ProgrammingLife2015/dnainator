package nl.tudelft.dnainator.ui.views;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.util.Pair;
import nl.tudelft.dnainator.core.Edge;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.ui.drawables.DrawableEdge;
import nl.tudelft.dnainator.ui.drawables.DrawableNode;
import nl.tudelft.dnainator.ui.models.GraphModel;

/**
 * The SequenceView views the data from the sequence height, i.e.
 * it displays the sequences of the DNA.
 */
public class SequenceView extends View {

	/**
	 * Instantiates a new SequenceView object.
	 * @param root The root node to attach all drawables to.
	 * @param model The {@link GraphModel} whose data to display.
	 */
	public SequenceView(Pane root, GraphModel model) {
		super(root, model);
	}

	@Override
	public void redraw() { //Graph graph, Layout layout
		System.out.println("Redrawing...");

		drawNodes();
		drawEdges();
	}

	private void drawNodes() {
		double x = 50.0, y = 50.0;

		for (SequenceNode n : model.getNodes()) {
			root.getChildren().add(new DrawableNode(n, x, y));
			x += 50.0;
			y += 50.0;
		}
	}

	private void drawEdges() {
		double x = 50.0, y = 50.0;

		for (Edge<Integer> e : model.getEdges()) {
			DrawableNode src = (DrawableNode) root.getChildren().get(e.getSource());
			DrawableNode dst = (DrawableNode) root.getChildren().get(e.getDest());
			root.getChildren().add(new DrawableEdge(src, dst));
			x += 50.0;
			y += 50.0;
		}
	}
}
