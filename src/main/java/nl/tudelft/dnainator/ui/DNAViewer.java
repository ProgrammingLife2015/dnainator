package nl.tudelft.dnainator.ui;

import nl.tudelft.dnainator.graph.DNAGraph;

import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.Viewer;

/**
 * The viewer is responsible for managing the interactable views of the the strain graph.
 * Use the addDefaultView() method to obtain a view.
 */
public class DNAViewer extends Viewer {
	/**
	 * Creates a new GraphViewer.
	 */
	public DNAViewer() {
		this(new DNAGraph(), ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
	}

	/**
	 * Creates a new GraphViewer.
	 * @param graph	The graph of strains.
	 * @param model	The ThreadingModel to use.
	 */
	public DNAViewer(Graph graph, ThreadingModel model) {
		super(graph, model);

		this.enableAutoLayout();
		this.setCloseFramePolicy(Viewer.CloseFramePolicy.EXIT);
	}
}
