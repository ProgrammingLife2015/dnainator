package nl.tudelft.dnainator.ui.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import nl.tudelft.dnainator.core.DefaultSequenceFactory;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.Neo4jGraphDatabase;
import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.parser.NodeParser;
import nl.tudelft.dnainator.parser.buffered.DefaultEdgeParser;
import nl.tudelft.dnainator.parser.buffered.JFASTANodeParser;
import nl.tudelft.dnainator.parser.exceptions.ParseException;

/**
 * A JavaFX background service to load files into graphs.
 * <p>
 * Please note that none of the properties are initialized (beyond being
 * set to <code>null</code>) upon instantiation.
 * </p>
 */
public class FileLoadService extends Service<Void> {
	private ObjectProperty<File> nodeFile = new SimpleObjectProperty<File>(this, "nodeFile");
	private ObjectProperty<File> edgeFile = new SimpleObjectProperty<File>(this, "edgeFile");

	/**
	 * @param f The node file to load.
	 */
	public final void setNodeFile(File f) {
		nodeFile.set(f);
	}

	/**
	 * @return The node file to load, if any.
	 */
	public final File getNodeFile() {
		return nodeFile.get();
	}

	/**
	 * @return The node file property.
	 */
	public ObjectProperty<File> nodeFileProperty() {
		return nodeFile;
	}

	/**
	 * @param f The edge file to load.
	 */
	public final void setEdgeFile(File f) {
		edgeFile.set(f);
	}

	/**
	 * @return The edge file to load, if any.
	 */
	public final File getEdgeFile() {
		return edgeFile.get();
	}

	/**
	 * @return The edge file property.
	 */
	public ObjectProperty<File> edgeFileProperty() {
		return edgeFile;
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {
			@Override
			protected Void call() throws IOException, ParseException {
				Graph gb = Neo4jGraphDatabase.getInstance();
				EdgeParser ep = new DefaultEdgeParser(new BufferedReader(new InputStreamReader(
						new FileInputStream(getEdgeFile()), "UTF-8")));
				NodeParser np = new JFASTANodeParser(new DefaultSequenceFactory(),
						new BufferedReader(new InputStreamReader(
								new FileInputStream(getNodeFile()), "UTF-8")));
				gb.constructGraph(np, ep);

				return null;
			}
		};
	}
}
