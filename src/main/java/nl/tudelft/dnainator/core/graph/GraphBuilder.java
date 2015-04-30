package nl.tudelft.dnainator.core.graph;

import java.io.IOException;

import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.parser.exceptions.InvalidEdgeFormatException;
import nl.tudelft.dnainator.parser.exceptions.InvalidHeaderFormatException;
import nl.tudelft.dnainator.parser.NodeParser;
import nl.tudelft.dnainator.util.Edge;

/**
 * Interface for constructing graphs.
 */
public interface GraphBuilder {

	/**
	 * Add an {@link Edge} to the graph.
	 * @param e The edge containing the source and destination node.
	 */
	void addEdge(Edge<Integer> e);

	/**
	 * Add a {@link SequenceNode} to the graph.
	 * @param s The node to be added.
	 */
	void addNode(SequenceNode s);

	/**
	 * Construct a graph using the supplied parsers as a source for nodes and edges.
	 * @param np The {@link NodeParser} to supply the nodes.
	 * @param ep The {@link EdgeParser} to supply the edges.
	 * @param gb The {@link GraphBuilder} used to add the nodes and edges.
	 * @throws IOException If something goes wrong with IO while parsing.
	 * @throws InvalidHeaderFormatException If the header of a node is of an invalid format.
	 * @throws InvalidEdgeFormatException If the edge file contains invalid lines.
	 */
	static void constructGraph(NodeParser np, EdgeParser ep, GraphBuilder gb)
			throws IOException, InvalidHeaderFormatException, InvalidEdgeFormatException {
		while (np.hasNext()) {
			gb.addNode(np.next());
		}
		while (ep.hasNext()) {
			gb.addEdge(ep.next());
		}
	}
}
