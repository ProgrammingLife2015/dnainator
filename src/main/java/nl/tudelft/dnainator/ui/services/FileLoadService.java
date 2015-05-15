package nl.tudelft.dnainator.ui.services;

import java.io.File;
import java.io.IOException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.impl.Neo4jSingleton;
import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.parser.NodeParser;
import nl.tudelft.dnainator.parser.TreeParser;
import nl.tudelft.dnainator.parser.exceptions.ParseException;
import nl.tudelft.dnainator.parser.impl.EdgeParserImpl;
import nl.tudelft.dnainator.parser.impl.NodeParserImpl;

/**
 * A JavaFX background service to load files into graphs.
 * <p>
 * Please note that none of the properties are initialized (beyond being
 * set to <code>null</code>) upon instantiation.
 * </p>
 */
public class FileLoadService extends Service<Graph> {
	private ObjectProperty<File> nodeFile = new SimpleObjectProperty<>(this, "nodeFile");
	private ObjectProperty<File> edgeFile = new SimpleObjectProperty<>(this, "edgeFile");
	private ObjectProperty<File> treeFile = new SimpleObjectProperty<>(this, "treeFile");
	private ObjectProperty<String> database = new SimpleObjectProperty<>(this, "database");

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

	/**
	 * @param f The tree file to load.
	 */
	public final void setTreeFile(File f) {
		treeFile.set(f);
	}

	/**
	 * @return The tree file to load, if any.
	 */
	public final File getTreeFile() {
		return treeFile.get();
	}

	/**
	 * @return The tree file property.
	 */
	public ObjectProperty<File> treeFileProperty() {
		return treeFile;
	}

	/**
	 * @param g	The database to use.
	 */
	public final void setDatabase(String g) {
		database.set(g);
	}

	/**
	 * @return The database to use, if any.
	 */
	public final String getDatabase() {
		return database.get();
	}

	/**
	 * @return The database property.
	 */
	public ObjectProperty<String> databaseProperty() {
		return database;
	}

	@Override
	protected Task<Graph> createTask() {
		return new Task<Graph>() {
			@Override
			protected Graph call() throws IOException, ParseException {
				Graph gb;
				if (database.get() == null) {
					Neo4jSingleton.getInstance().deleteDatabase();
					gb = Neo4jSingleton.getInstance().getDatabase();
				} else {
					gb = Neo4jSingleton.getInstance().getDatabase(database.get());
				}
				EdgeParser ep = new EdgeParserImpl(getEdgeFile());
				NodeParser np = new NodeParserImpl(getNodeFile());
				TreeParser tp = new TreeParser(getTreeFile());

				gb.constructGraph(np, ep);
				tp.parse().toString();

				ep.close();
				np.close();

				return gb;
			}
		};
	}
}
