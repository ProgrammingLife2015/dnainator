package nl.tudelft.dnainator.ui.views;

import java.util.List;

import javafx.geometry.Point2D;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.ui.drawables.DrawableNode;
import nl.tudelft.dnainator.ui.layouts.TopoLayout;
import nl.tudelft.dnainator.ui.models.GraphModel;

/**
 * The SequenceView views the data from the sequence height, i.e.
 * it displays the sequences of the DNA.
 */
public class SequenceView extends View {

	/**
	 * Instantiates a new SequenceView object.
	 * @param model The {@link GraphModel} whose data to display.
	 */
	public SequenceView(GraphModel model) {
		super(model);
	}

	@Override
	public void redraw() {
		System.out.println("Redrawing...");

		drawNodes();
		//drawEdges();
		System.out.println("Done!");
	}

	private void drawNodes() {
		// CHECKSTYLE.OFF: MagicNumber
		for (int i = 0; i < 100; i++) {
		// CHECKSTYLE.ON: MagicNumer
			List<SequenceNode> nodes = model.getRank(i);
			int size = nodes.size();
			for (int j = 0; j < size; j++) {
				Point2D coords = TopoLayout.transform(i, size, j);

				getChildren().add(new DrawableNode(nodes.get(j), coords.getX(), coords.getY()));
			}
		}
	}

//	private void drawEdges() {
//		double x = 50.0, y = 50.0;
//
//		for (Edge<Integer> e : model.getEdges()) {
//			DrawableNode src = (DrawableNode) root.getChildren().get(e.getSource());
//			DrawableNode dst = (DrawableNode) root.getChildren().get(e.getDest());
//			root.getChildren().add(new DrawableEdge(src, dst));
//			x += 50.0;
//			y += 50.0;
//		}
//	}
}
