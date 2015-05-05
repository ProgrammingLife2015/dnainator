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
import nl.tudelft.dnainator.graph.GraphBuilder;
import nl.tudelft.dnainator.graph.Neo4jSingleton;
import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.parser.NodeParser;
import nl.tudelft.dnainator.parser.buffered.DefaultEdgeParser;
import nl.tudelft.dnainator.parser.buffered.JFASTANodeParser;
import nl.tudelft.dnainator.parser.exceptions.ParseException;

import org.neo4j.io.fs.FileUtils;

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
					// FIXME: this is necessary because neo4j does not yet handle persistence
					FileUtils.deleteRecursively(new File(Neo4jSingleton.DB_PATH));
					gb = Neo4jSingleton.getInstance().getDatabase();
				} else {
					gb = Neo4jSingleton.getInstance().getDatabase(database.get());
				}
				EdgeParser ep = new DefaultEdgeParser(getEdgeFile());
				NodeParser np = new JFASTANodeParser(getNodeFile());
				gb.constructGraph(np, ep);

				return gb;
			}
		};
	}
}
