package nl.tudelft.dnainator.core.graph;

import nl.tudelft.dnainator.core.Sequence;

import org.graphstream.graph.implementations.SingleGraph;

/**
 * This class realizes a graphfactory using GraphStream as it's backend.
 */
public class GSGraphFactory implements GraphFactory<SingleGraph> {
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
		graph.addNode(Integer.toString(s.getId()));
		graph.addAttribute("start", s.getStartRef());
		graph.addAttribute("end",   s.getEndRef());
		graph.addAttribute("source", s.getSource());
		graph.addAttribute("sequence", s.getSequence());
	}
	
	/**
	 * Return the constructed GraphStream graph.
	 * @return	the constructed graph
	 */
	public SingleGraph getGraph() {
		return graph;
	}
}
