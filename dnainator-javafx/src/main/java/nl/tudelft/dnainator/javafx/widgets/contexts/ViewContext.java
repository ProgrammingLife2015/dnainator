package nl.tudelft.dnainator.javafx.widgets.contexts;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * Creates a {@link ContextMenu} for the general window. It needs to be singleton because only one
 * context menu may exist per context, thus only one instance of this class may exist at a time.
 */
public final class ViewContext extends ContextMenu {
	private static ViewContext instance = new ViewContext();

	private ViewContext() {
		MenuItem generalFoo = new MenuItem("GeneralFoo");
		MenuItem generalBar = new MenuItem("GeneralBar");
		MenuItem generalBaz = new MenuItem("GeneralBaz");

		getItems().addAll(generalFoo, generalBar, generalBaz);
	}

	/**
	 * @return The instance of the {@link ViewContext}.
	 */
	public static ViewContext getInstance() {
		return instance;
	}
}
