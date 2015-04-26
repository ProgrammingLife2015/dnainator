package nl.tudelft.dnainator.parser;

import java.io.File;

import nl.tudelft.dnainator.core.SequenceGraph;

/**
 * Parses the node and edge files to a {@link SequenceGraph}, where the
 * node file is in FASTA format, and the edge file specifies connected
 * nodes on each line.
 */
public class GraphParser {
	private File nodeFile;
	private File edgeFile;
	private NodeParser np;
	private EdgeParser ep;
	public static final String NODE_EXT = ".node.graph";
	public static final String EDGE_EXT = ".edge.graph";

	/**
	 * Construct a new {@link GraphParser} using the common basename of the
	 * node and edge files.
	 * @param basename The basename of the node and edge files.
	 * @param ep The EdgeParser used to parse the edge file.
	 * @param np The NodeParser used to parse the node file.
	 */
	public GraphParser(String basename, NodeParser np, EdgeParser ep) {
		this(basename + NODE_EXT, basename + EDGE_EXT, np, ep);
	}

	/**
	 * Construct a new {@link GraphParser} using the filenames of the
	 * node and edge files.
	 * @param nodeFilename The filename of the file containing the nodes.
	 * @param edgeFilename The filename of the file containing the edges.
	 * @param ep The EdgeParser used to parse the edge file.
	 * @param np The NodeParser used to parse the node file.
	 */
	public GraphParser(String nodeFilename, String edgeFilename, NodeParser np, EdgeParser ep) {
		this(new File(nodeFilename), new File(edgeFilename), np, ep);
	}

	/**
	 * Construct a new {@link GraphParser} using the file descriptors of the
	 * node and edge files.
	 * @param nodeFile The {@link File} containing the nodes in FASTA format.
	 * @param edgeFile The {@link File} containing the edges.
	 * @param ep The EdgeParser used to parse the edge file.
	 * @param np The NodeParser used to parse the node file.
	 */
	public GraphParser(File nodeFile, File edgeFile, NodeParser np, EdgeParser ep) {
		this.nodeFile = nodeFile;
		this.edgeFile = edgeFile;
		this.np = np;
		this.ep = ep;
	}

	/**
	 * Parse the files to a {@link SequenceGraph}.
	 * @return a {@link SequenceGraph} representing the graph given in the files.
	 * @throws NumberFormatException if one of the nodes' start or end position can't be parsed.
	 * @throws InvalidHeaderFormatException if one of the nodes' header's format is invalid.
	 */
	public SequenceGraph parse() throws NumberFormatException, InvalidHeaderFormatException {
		return ep.parse(np.parse(nodeFile), edgeFile);
	}
}
