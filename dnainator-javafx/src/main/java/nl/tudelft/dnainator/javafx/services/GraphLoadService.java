package nl.tudelft.dnainator.javafx.services;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import nl.tudelft.dnainator.graph.Graph;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
	private ObjectProperty<File> gffFile = new SimpleObjectProperty<>(this, "gffFile");
	private ObjectProperty<File> nodeFile = new SimpleObjectProperty<>(this, "nodeFile");
	private ObjectProperty<File> edgeFile = new SimpleObjectProperty<>(this, "edgeFile");
	private ObjectProperty<File> newickFile = new SimpleObjectProperty<>(this, "newickFile");
	private ObjectProperty<File> drFile = new SimpleObjectProperty<>(this, "drFile");

	/**
	 * Construct a GraphLoadService with a default database path.
	 */
	public GraphLoadService() {
		setDatabase(DB_PATH);
	}

	/**
	 * Checks what paths are already in use and constructs an unique path,
	 * that does not collide with existing ones.
	 * @return unique path for the database.
	 */
	public String getNewPath() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("YYYYMMdd-HHmmss");
		return DB_PATH + (LocalDateTime.now().format(format));
	}

	/**
	 * Indicates whether the service can start loading.
	 * @return	true when ready, false otherwise
	 */
	public boolean canLoad() {
		return getGffFile() != null && getNodeFile() != null
				&& getEdgeFile() != null && getNewickFile() != null;
	}

	/**
	 * Sets the GFF file to the specified value.
	 * @param file The new file.
	 */
	public final void setGffFile(File file) {
		gffFile.set(file);
	}

	/**
	 * @return The GFF file to load.
	 */
	public final File getGffFile() {
		return gffFile.get();
	}

	/**
	 * @return The GFF file property.
	 */
	public ObjectProperty<File> gffFileProperty() {
		return gffFile;
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

	/**
	 * @param f The newick file to load.
	 */
	public final void setNewickFile(File f) {
		newickFile.set(f);
	}

	/**
	 * @return The newick file to load, if any.
	 */
	public final File getNewickFile() {
		return newickFile.get();
	}

	/**
	 * @return The newick file property.
	 */
	public ObjectProperty<File> newickFileProperty() {
		return newickFile;
	}

	/**
	 * @return The drug resistant mutations file.
	 */
	public final File getDrFile() {
		return drFile.get();
	}

	/**
	 * Sets the drug resistant mutations file property.
	 * @param file The drug resistant mutations file.
	 */
	public final void setDRFile(File file) {
		drFile.set(file);
	}

	/**
	 * @return The drug resistant mutations file property.
	 */
	public ObjectProperty<File> drFileProperty() {
		return drFile;
	}

	@Override
	protected Task<Graph> createTask() {
		return new GraphLoadTask(this);
	}
}
