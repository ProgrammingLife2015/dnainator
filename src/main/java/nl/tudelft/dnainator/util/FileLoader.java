package nl.tudelft.dnainator.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.SwingWorker;

import nl.tudelft.dnainator.core.DefaultSequenceFactory;
import nl.tudelft.dnainator.graph.GSGraphBuilder;
import nl.tudelft.dnainator.graph.GraphBuilder;
import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.parser.NodeParser;
import nl.tudelft.dnainator.parser.buffered.DefaultEdgeParser;
import nl.tudelft.dnainator.parser.buffered.JFASTANodeParser;
import nl.tudelft.dnainator.parser.exceptions.ParseException;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * This class is called from the GUI as an intermediate layer between
 * itself and the parsers.
 */
public class FileLoader extends SwingWorker<Graph, Void> {
	private File nodeFile;
	private File edgeFile;

	/**
	 * Constructs a FileLoader object.
	 * @param nodeFile File containing the nodes.
	 * @param edgeFile File containing the edges.
	 */
	public FileLoader(File nodeFile, File edgeFile) {
		this.nodeFile = nodeFile;
		this.edgeFile = edgeFile;
	}

	@Override
	public Graph doInBackground() throws IOException, ParseException {
		Graph g = new SingleGraph("DNA");
		GraphBuilder gb = new GSGraphBuilder(g);
		EdgeParser ep = new DefaultEdgeParser(new BufferedReader(new InputStreamReader(
				new FileInputStream(edgeFile), "UTF-8")));
		NodeParser np = new JFASTANodeParser(new DefaultSequenceFactory(), new BufferedReader(
				new InputStreamReader(new FileInputStream(nodeFile), "UTF-8")));
		gb.constructGraph(np, ep);

		return g;
	}
}
