package nl.tudelft.dnainator.util;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.swing.SwingWorker;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import nl.tudelft.dnainator.core.DefaultSequenceFactory;
import nl.tudelft.dnainator.graph.GSGraphBuilder;
import nl.tudelft.dnainator.graph.GraphBuilder;
import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.parser.NodeParser;
import nl.tudelft.dnainator.parser.buffered.JFASTANodeParser;
import nl.tudelft.dnainator.parser.buffered.DefaultEdgeParser;
import nl.tudelft.dnainator.ui.Window;

/**
 * This class is called from the GUI as an intermediate layer between
 * itself and the parsers.
 */
public class FileLoader extends SwingWorker<Graph, Void> {
	private Window window;
	private File nodeFile;
	private File edgeFile;

	/**
	 * Constructs a FileLoader object.
	 * @param window The UI implementation to update.
	 * @param nodeFile File containing the nodes.
	 * @param edgeFile File containing the edges.
	 */
	public FileLoader(Window window, File nodeFile, File edgeFile) {
		this.window = window;
		this.nodeFile = nodeFile;
		this.edgeFile = edgeFile;
	}

	@Override
	public Graph doInBackground() {
		Graph g = new SingleGraph("Tree");
		GraphBuilder gb = new GSGraphBuilder(g);
		try {
			EdgeParser ep = new DefaultEdgeParser(new BufferedReader(new InputStreamReader(
								new FileInputStream(edgeFile), "UTF-8")));
			NodeParser np = new JFASTANodeParser(new DefaultSequenceFactory(), new BufferedReader(
								new InputStreamReader(new FileInputStream(nodeFile), "UTF-8")));
			gb.constructGraph(np, ep);
		} catch (Exception e) {
			window.spawnErrorDialog(e.getMessage(), "Error opening file");
		}
		return g;
	}
}
