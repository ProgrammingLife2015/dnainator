package nl.tudelft.dnainator.javafx.services;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import nl.tudelft.dnainator.javafx.utils.AppConfig;
import nl.tudelft.dnainator.parser.TreeParser;
import nl.tudelft.dnainator.tree.TreeNode;

/**
 * A JavaFX background service to load files into trees.
 * <p>
 * Please note that none of the properties are initialized (beyond being
 * set to <code>null</code>) upon instantiation.
 * </p>
 */
public class NewickLoadService extends Service<TreeNode> {
	private StringProperty newickPath = new SimpleStringProperty(this, "newickPath");

	/**
	 * Creates the service storing the file path stored and parsing the file belonging to it,
	 * if it exists.
	 */
	public NewickLoadService() {
		newickPath.set(AppConfig.getInstance().getNewickPath());
	}

	/**
	 * @param path The newick path to load.
	 */
	public final void setNewickPath(String path) {
		newickPath.set(path);
	}

	/**
	 * @return The newick path to load, if any.
	 */
	public final String getNewickPath() {
		return newickPath.get();
	}

	/**
	 * @return The newick file path property.
	 */
	public StringProperty newickPathProperty() {
		return newickPath;
	}

	@Override
	protected Task<TreeNode> createTask() {
		return new Task<TreeNode>() {
			@Override
			protected TreeNode call() throws Exception {
				TreeParser tp = new TreeParser();
				return tp.parse(getNewickPath());
			}
		};
	}

	@Override
	public void restart() {
		super.restart();
		AppConfig.getInstance().setNewickPath(newickPath.get());
	}
}
