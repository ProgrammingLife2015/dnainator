package nl.tudelft.dnainator.graph;

import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;

/**
 * The main UI component: the interactable graph displaying all the strains.
 */
public class Graph extends SingleGraph {
	private static final int N_NODES = 100;
	private View view;

	/**
	 * Constructs a new graph.
	 */
	public Graph() {
		super("Random");

		Generator gen = new RandomGenerator(2);
		gen.addSink(this);
		gen.begin();
		for (int i = 0; i < N_NODES; i++) {
			gen.nextEvents();
		}
		gen.end();

		Viewer viewer = new Viewer(this, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer.enableAutoLayout();
		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.EXIT);

		view = viewer.addDefaultView(false);
		view.setVisible(true);
	}

	/**
	 * @return This graph's view.
	 */
	public View getView() {
		return view;
	}
}
