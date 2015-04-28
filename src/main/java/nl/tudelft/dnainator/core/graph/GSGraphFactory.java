package nl.tudelft.dnainator.core.graph;

import nl.tudelft.dnainator.core.Sequence;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * This class realizes a graphfactory using GraphStream as it's backend.
 */
public class GSGraphFactory implements GraphFactory {
	private SingleGraph graph;
	private int edgecount;
	
	/**
	 * This factory constructs a GraphStream graph with the given title.
	 * @param title	the title of the graph
	 */
	public GSGraphFactory(String title) {
		graph = new SingleGraph(title);
	}
	@Override
	public void addEdge(int l, int r) {
		graph.addEdge(Integer.toString(edgecount++), l, r);
	}

	@Override
	public void addNode(Sequence s) {
		graph.addNode(s.getId());
		graph.addAttribute("x", s.getStart());
		graph.addAttribute("sequence", s);
	}
	
	/**
	 * Return the constructed GraphStream graph.
	 * @return	the constructed graph
	 */
	public Graph getGSGraph() {
		return graph;
	}
}
