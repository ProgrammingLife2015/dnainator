package nl.tudelft.dnainator.parser;

import nl.tudelft.dnainator.core.SequenceGraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Parses the node and edge files to a {@link SequenceGraph}, where the
 * node file is in FASTA format, and the edge file specifies connected
 * nodes on each line.
 */
public class GraphParser {
	public static final String NODE_EXT = ".node.graph";
	public static final String EDGE_EXT = ".edge.graph";
	private InputStream nodeStream;
	private InputStream edgeStream;
	private NodeParser np;
	private EdgeParser ep;

	/**
	 * Construct a new {@link GraphParser} using the common basename of the
	 * node and edge files.
	 *
	 * @param basename The basename of the node and edge files.
	 * @param ep       The EdgeParser used to parse the edge file.
	 * @param np       The NodeParser used to parse the node file.
	 * @throws FileNotFoundException if one of the files was not found.
	 */
	public GraphParser(String basename, NodeParser np, EdgeParser ep) throws FileNotFoundException {
		this(basename + NODE_EXT, basename + EDGE_EXT, np, ep);
	}

	/**
	 * Construct a new {@link GraphParser} using the filenames of the
	 * node and edge files.
	 *
	 * @param nodeFilename The filename of the file containing the nodes.
	 * @param edgeFilename The filename of the file containing the edges.
	 * @param ep           The EdgeParser used to parse the edge file.
	 * @param np           The NodeParser used to parse the node file.
	 * @throws FileNotFoundException if one of the files was not found.
	 */
	public GraphParser(String nodeFilename, String edgeFilename, NodeParser np, EdgeParser ep)
			throws FileNotFoundException {
		this(new File(nodeFilename), new File(edgeFilename), np, ep);
	}

	/**
	 * Construct a new {@link GraphParser} using the file descriptors of the
	 * node and edge files.
	 * @param nodeFile The {@link File} containing the nodes in FASTA format.
	 * @param edgeFile The {@link File} containing the edges.
	 * @param np       The NodeParser used to parse the node file.
	 * @param ep       The EdgeParser used to parse the edge file.
	 * @throws FileNotFoundException if one of the files was not found.
	 */
	public GraphParser(File nodeFile, File edgeFile, NodeParser np, EdgeParser ep)
			throws FileNotFoundException {
		this(new FileInputStream(nodeFile), new FileInputStream(edgeFile), np, ep);
	}

	/**
	 * Create a new {@link GraphParser} using the {@link InputStream}s as a source
	 * for the nodes and edges.
	 * @param nodeStream An {@link InputStream} for reading the nodes in FASTA format.
	 * @param edgeStream An {@link InputStream} for reading the edges.
	 * @param np         The {@link NodeParser} used to parse the nodes.
	 * @param ep         The {@link EdgeParser} used to parse the edges.
	 */
	public GraphParser(InputStream nodeStream, InputStream edgeStream,
			NodeParser np, EdgeParser ep) {
		this.nodeStream = nodeStream;
		this.edgeStream = edgeStream;
		this.np = np;
		this.ep = ep;
	}

	/**
	 * Parse the files to a {@link SequenceGraph}.
	 *
	 * @return a {@link SequenceGraph} representing the graph given in the files.
	 * @throws NumberFormatException        if one of the nodes' start or end position can't be
	 *                                      parsed.
	 * @throws InvalidHeaderFormatException if one of the nodes' header's format is invalid.
	 * @throws IOException                  if either of the files could not be found or if closing
	 *                                      the streams fails.
	 */
	public SequenceGraph parse() throws NumberFormatException, InvalidHeaderFormatException,
			IOException {
		return ep.parse(np.parse(nodeStream), edgeStream);
	}
}
