package nl.tudelft.dnainator.ui.widgets;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import nl.tudelft.dnainator.core.SequenceNode;

/**
 * Creates a {@link ContextMenu} for {@link SequenceNode}s. It needs to be singleton because
 * only one context menu may exist per context, thus only one instance of this class may
 * exist at a time.
 */
public final class NodeContext extends ContextMenu {
	private static NodeContext instance = new NodeContext();

	/**
	 * Creates the {@link ContextMenu}.
	 */
	private NodeContext() {
		MenuItem nodeFoo = new MenuItem("NodeFoo");
		MenuItem nodeBar = new MenuItem("NodeBar");
		MenuItem nodeBaz = new MenuItem("NodeBaz");

		getItems().addAll(nodeFoo, nodeBar, nodeBaz);
	}

	/**
	 * @return The instance of the {@link NodeContext}.
	 */
	public static NodeContext getInstance() {
		return instance;
	}
}
