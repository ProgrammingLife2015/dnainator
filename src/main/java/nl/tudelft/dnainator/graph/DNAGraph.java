package nl.tudelft.dnainator.graph;

import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
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
		super("Random");

		Generator gen = new RandomGenerator(2);
		gen.addSink(this);
		gen.begin();
		for (int i = 0; i < N_NODES; i++) {
			gen.nextEvents();
		}
		gen.end();
	}
}
