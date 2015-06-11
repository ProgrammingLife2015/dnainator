package nl.tudelft.dnainator.javafx.services;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import nl.tudelft.dnainator.annotation.AnnotationCollection;
import nl.tudelft.dnainator.annotation.impl.AnnotationCollectionFactoryImpl;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.GraphBuilder;
import nl.tudelft.dnainator.graph.impl.Neo4jBatchBuilder;
import nl.tudelft.dnainator.graph.impl.Neo4jGraph;
import nl.tudelft.dnainator.parser.AnnotationParser;
import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.parser.NodeParser;
import nl.tudelft.dnainator.parser.exceptions.ParseException;
import nl.tudelft.dnainator.parser.impl.EdgeParserImpl;
import nl.tudelft.dnainator.parser.impl.GFF3AnnotationParser;
import nl.tudelft.dnainator.parser.impl.NodeParserImpl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * A JavaFX background service to load files into graphs.
 * <p>
 * Please note that none of the properties are initialized (beyond being
 * set to <code>null</code>) upon instantiation.
 * </p>
 */
public class GraphLoadService extends Service<Graph> {
	private static final String DB_PATH = "target" + File.separator + "db" 
			+ File.separator + "dna-graph-";
	
	private ObjectProperty<String> database = new SimpleObjectProperty<>(this, "database");
	private ObjectProperty<String> gffFilePath =
			new SimpleObjectProperty<>(this, "gffFilePath");
	private ObjectProperty<File> nodeFile = new SimpleObjectProperty<>(this, "nodeFile");
	private ObjectProperty<File> edgeFile = new SimpleObjectProperty<>(this, "edgeFile");

	/**
	 * Construct a GraphLoadService with a default database path.
	 */
	public GraphLoadService() {
		setDatabase(DB_PATH);
	}

	/**
	 * Checks what paths are already in use and constructs an unique path,
	 * that does not collide with existing ones.
	 * @param paths the existing database paths.
	 * @return unique path for the database.
	 */
	public String getNewPath(List<String> paths) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("YYYYMMdd-HHmmss");
		String newPath = DB_PATH + (LocalDateTime.now().format(format));
		paths.add(newPath);
		return newPath;
	}

	/**
	 * Sets the GFF filename to the specified value.
	 * @param fileName The new filename.
	 */
	public final void setGffFilePath(String fileName) {
		gffFilePath.set(fileName);
	}

	/**
	 * @return The filename of the GFF file.
	 */
	public final String getGffFilePath() {
		return gffFilePath.get();
	}

	/**
	 * @return The GFF filename property.
	 */
	public ObjectProperty<String> gffFilePathProperty() {
		return gffFilePath;
	}
	
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
				AnnotationCollection annotations;
				if (gffFilePath.getValue() == null) {
					annotations = new AnnotationCollectionFactoryImpl().build();
				} else {
					AnnotationParser as = new GFF3AnnotationParser(gffFilePath.get());
					annotations = new AnnotationCollectionFactoryImpl().build(as);
				}

				GraphBuilder gb;
				gb = new Neo4jBatchBuilder(database.get(), annotations);

				EdgeParser ep = new EdgeParserImpl(getEdgeFile());
				NodeParser np = new NodeParserImpl(getNodeFile());
				gb.constructGraph(np, ep);

				ep.close();
				np.close();

				return new Neo4jGraph(database.get());
			}
		};
	}
}
