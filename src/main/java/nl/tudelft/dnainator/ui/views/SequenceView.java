package nl.tudelft.dnainator.ui.views;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Point2D;
import nl.tudelft.dnainator.core.Edge;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.graph.Neo4jSingleton;
import nl.tudelft.dnainator.ui.drawables.DrawableEdge;
import nl.tudelft.dnainator.ui.drawables.DrawableNode;
import nl.tudelft.dnainator.ui.layouts.TopoLayout;
import nl.tudelft.dnainator.ui.models.GraphModel;

/**
 * The SequenceView views the data from the sequence height, i.e.
 * it displays the sequences of the DNA.
 */
public class SequenceView extends View {
	private Map<String, DrawableNode> drawableNodes;

	/**
	 * Instantiates a new SequenceView object.
	 * @param model The {@link GraphModel} whose data to display.
	 */
	public SequenceView(GraphModel model) {
		super(model);

		drawableNodes = new HashMap<>();

		getStyleClass().add("sequence-view");
	}

	@Override
	public void redraw() {
		System.out.println("Redrawing...");
		//drawGraph();

		drawNodes();
		drawEdges();

		group.getChildren().addAll(drawableNodes.values());
		System.out.println("Done!");
	}

//	private void drawGraph() {
//		List<Edge<String>> edges = Neo4jSingleton.getInstance().getDatabase().getEdges();
//		for (Edge<String> e : edges) {
//			SequenceNode srcNode = e.getSource();
//			SequenceNode dstNode = e.getDest();
//
//			Point2D srcCoords = layout.transform(srcNode.getRank());
//			Point2D dstCoords = layout.transform(dstNode.getRank());
//
//			DrawableNode src = new DrawableNode(srcNode, srcCoords.getX(), srcCoords.getY());
//			DrawableNode dst = new DrawableNode(dstNode, dstCoords.getX(), dstCoords.getY());
//			DrawableEdge de = new DrawableEdge(src, dst);
//
//			group.getChildren().addAll(src, dst, de);
//		}
//	}

	private void drawNodes() {
		List<List<SequenceNode>> ranks = model.getRanks();
		for (int i = 0; i < ranks.size(); i++) {
			List<SequenceNode> nodes = ranks.get(i);
			int size = nodes.size();
			for (int j = 0; j < size; j++) {
				Point2D coords = TopoLayout.transform(i, size, j);
				SequenceNode n = nodes.get(j);

				DrawableNode drawable = new DrawableNode(n, coords.getX(), coords.getY());
				drawableNodes.put(n.getId(), drawable);
			}
		}
	}

	private void drawEdges() {
		for (Edge<String> e : Neo4jSingleton.getInstance().getDatabase().getEdges()) {
			DrawableNode src = drawableNodes.get(e.getSource());
			DrawableNode dst = drawableNodes.get(e.getDest());
			if (src == null || dst == null) {
				continue;
			}
			DrawableEdge de = new DrawableEdge(src, dst);
			group.getChildren().add(de);
		}
	}
}
