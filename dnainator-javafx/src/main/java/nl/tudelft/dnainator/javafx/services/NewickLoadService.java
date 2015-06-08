package nl.tudelft.dnainator.javafx.services;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import nl.tudelft.dnainator.parser.TreeParser;
import nl.tudelft.dnainator.tree.TreeNode;

import java.io.File;

/**
 * A JavaFX background service to load files into trees.
 * <p>
 * Please note that none of the properties are initialized (beyond being
 * set to <code>null</code>) upon instantiation.
 * </p>
 */
public class NewickLoadService extends Service<TreeNode> {
	private ObjectProperty<File> newickFile = new SimpleObjectProperty<>(this, "newickFile");

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

	@Override
	protected Task<TreeNode> createTask() {
		return new Task<TreeNode>() {
			@Override
			protected TreeNode call() throws Exception {
				TreeParser tp = new TreeParser(getNewickFile());
				return tp.parse();
			}
		};
	}
}
