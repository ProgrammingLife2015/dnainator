package nl.tudelft.dnainator.javafx.services;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import nl.tudelft.dnainator.annotation.AnnotationCollection;
import nl.tudelft.dnainator.annotation.impl.AnnotationCollectionFactoryImpl;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.impl.Neo4jBatchBuilder;
import nl.tudelft.dnainator.javafx.utils.AppConfig;
import nl.tudelft.dnainator.parser.AnnotationParser;
import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.parser.NodeParser;
import nl.tudelft.dnainator.parser.exceptions.ParseException;
import nl.tudelft.dnainator.parser.impl.EdgeParserImpl;
import nl.tudelft.dnainator.parser.impl.GFF3AnnotationParser;
import nl.tudelft.dnainator.parser.impl.NodeParserImpl;
import nl.tudelft.dnainator.tree.TreeNode;

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

	private ObjectProperty<TreeNode> phylogeneticTree =
			new SimpleObjectProperty<>(this, "phylogeneticTree");
	private StringProperty nodePath = new SimpleStringProperty(this, "nodePath");
	private StringProperty edgePath = new SimpleStringProperty(this, "edgePath");
	private StringProperty gffPath = new SimpleStringProperty(this, "gffPath");
	private StringProperty database = new SimpleStringProperty(this, "database");

	/**
	 * Construct a GraphLoadService with a default database path.
	 */
	public GraphLoadService() {
		setDatabase(DB_PATH);
		nodePathProperty().setValue(AppConfig.getInstance().getNodePath());
		edgePathProperty().set(AppConfig.getInstance().getEdgePath());
		gffPathProperty().set(AppConfig.getInstance().getGffPath());
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
	public final void setGffPath(String fileName) {
		gffPath.set(fileName);
	}

	/**
	 * @return The filename of the GFF file.
	 */
	public final String getGffPath() {
		return gffPath.get();
	}

	/**
	 * @return The GFF filename property.
	 */
	public StringProperty gffPathProperty() {
		return gffPath;
	}
	
	/**
	 * @param path The node file path to load.
	 */
	public final void setNodePath(String path) {
		nodePath.set(path);
	}

	/**
	 * @return The node file path to load, if any.
	 */
	public final String getNodePath() {
		return nodePath.get();
	}

	/**
	 * @return The node file path property.
	 */
	public StringProperty nodePathProperty() {
		return nodePath;
	}

	/**
	 * @param path The edge file path to load.
	 */
	public final void setEdgePath(String path) {
		edgePath.set(path);
	}

	/**
	 * @return The edge file path to load, if any.
	 */
	public final String getEdgePath() {
		return edgePath.get();
	}

	/**
	 * @return The edge file path property.
	 */
	public StringProperty edgePathProperty() {
		return edgePath;
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
	public StringProperty databaseProperty() {
		return database;
	}

	/**
	 * Sets the tree, needed for building the graph.
	 * @param value the tree.
	 */
	public void setTree(TreeNode value) {
		phylogeneticTree.setValue(value);
	}

	@Override
	protected Task<Graph> createTask() {
		return new Task<Graph>() {
			@Override
			protected Graph call() throws IOException, ParseException {
				AnnotationCollection annotations;
				if (gffPath.getValue() == null) {
					annotations = new AnnotationCollectionFactoryImpl().build();
				} else {
					AnnotationParser as = new GFF3AnnotationParser(gffPath.get());
					annotations = new AnnotationCollectionFactoryImpl().build(as);
				}
				EdgeParser ep = new EdgeParserImpl(getEdgePath());
				NodeParser np = new NodeParserImpl(getNodePath());

				return new Neo4jBatchBuilder(database.get(), annotations, phylogeneticTree.get())
					.constructGraph(np, ep)
					.build();
			}
		};
	}

	@Override
	public void restart() {
		super.restart();
		AppConfig.getInstance().setNodePath(nodePath.get());
		AppConfig.getInstance().setEdgePath(edgePath.get());
		AppConfig.getInstance().setGffPath(gffPath.get());
		AppConfig.getInstance().flush();
	}
}
