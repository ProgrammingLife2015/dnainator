package nl.tudelft.dnainator.graph;

import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.impl.Edge;
import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.parser.NodeParser;
import nl.tudelft.dnainator.parser.exceptions.ParseException;

import java.io.IOException;

/**
 * Interface for constructing graphs.
 */
public interface GraphBuilder {

	/**
	 * Add an {@link Edge} to the graph.
	 * @param edge The edge containing the source and destination node.
	 * @return the {@link GraphBuilder}, after adding the edge.
	 */
	GraphBuilder addEdge(Edge<String> edge);

	/**
	 * Add a {@link SequenceNode} to the graph.
	 * @param s The node to be added.
	 * @return the {@link GraphBuilder}, after adding the node.
	 */
	GraphBuilder addNode(SequenceNode s);

	/**
	 * Construct a graph using the supplied parsers as a source for nodes and edges.
	 * @param np The {@link NodeParser} to supply the nodes.
	 * @param ep The {@link EdgeParser} to supply the edges.
	 * @throws IOException If something goes wrong with IO while parsing.
	 * @throws ParseException if one of the input files is invalid.
	 * @return the {@link GraphBuilder}, after adding the nodes and edges.
	 */
	default GraphBuilder constructGraph(NodeParser np, EdgeParser ep)
			throws IOException, ParseException {
		try {
			while (np.hasNext()) {
				addNode(np.next());
			}
			while (ep.hasNext()) {
				addEdge(ep.next());
			}
		} finally {
			ep.close();
			np.close();
		}
		return this;
	}

	/**
	 * Build the {@link Graph}.
	 * @return the constructed {@link Graph}.
	 */
	Graph build();
}
