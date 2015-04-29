package nl.tudelft.dnainator.graph;

import org.graphstream.algorithm.generator.Generator;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * The graph that contains our DNA samples.
 */
public class DNAGraph extends SingleGraph {
	private static final int N_NODES = 100;

	/**
	 * Constructs a new graph.
	 */
	public DNAGraph() {
		this("Tree", new TreeGenerator());
	}
	
	/**
	 * Constructs a new graph with a given name and generator.
	 * @param name	name of the graph
	 * @param gen	generator of this graph
	 */
	public DNAGraph(String name, Generator gen) {
		super("Tree");
		
		gen.addSink(this);
		gen.begin();
		for (int i = 0; i < N_NODES; i++) {
			gen.nextEvents();
		}
		gen.end();
	}
}
