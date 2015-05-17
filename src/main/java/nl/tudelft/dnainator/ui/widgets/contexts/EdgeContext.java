package nl.tudelft.dnainator.ui.widgets.contexts;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import nl.tudelft.dnainator.ui.drawables.DrawableEdge;

/**
 * Creates a {@link ContextMenu} for {@link DrawableEdge}s. It needs to be singleton because
 * only one context menu may exist per context, thus only one instance of this class may
 * exist at a time.
 */
public final class EdgeContext extends ContextMenu {
	private static EdgeContext instance = new EdgeContext();

	/**
	 * Creates the {@link ContextMenu}.
	 */
	private EdgeContext() {
		MenuItem edgeFoo = new MenuItem("EdgeFoo");
		MenuItem edgeBar = new MenuItem("EdgeBar");
		MenuItem edgeBaz = new MenuItem("EdgeBaz");

		getItems().addAll(edgeFoo, edgeBar, edgeBaz);
	}

	/**
	 * @return The instance of the {@link EdgeContext}.
	 */
	public static EdgeContext getInstance() {
		return instance;
	}
}
