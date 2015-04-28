package nl.tudelft.dnainator.core.graph;

import org.biojava.nbio.structure.symmetry.utils.Graph;
import org.biojava.nbio.structure.symmetry.utils.SimpleGraph;

import nl.tudelft.dnainator.core.Sequence;

/**
 * This class realizes a graphfactory using BioJava as it's backend.
 */
public class BioGraphFactory implements GraphFactory {
	private SimpleGraph<Sequence> graph;
	
	/**
	 * This factory constructs a BioJava graph consisting of Sequence elements.
	 * @param title	the title of the graph
	 */
	public BioGraphFactory(String title) {
		graph = new SimpleGraph<>();
	}
	
	@Override
	public void addEdge(int l, int r) {
		graph.addEdge(graph.getVertex(l - 1), graph.getVertex(r - 1));
	}

	@Override
	public void addNode(Sequence s) {
		graph.addVertex(s);
	}
	
	/**
	 * Return the constructed GraphStream graph.
	 * @return	the constructed graph
	 */
	public Graph<Sequence> getGSGraph() {
		return graph;
	}
}
