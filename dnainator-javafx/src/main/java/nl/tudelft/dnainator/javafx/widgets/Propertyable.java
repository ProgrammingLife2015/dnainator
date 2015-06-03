package nl.tudelft.dnainator.javafx.widgets;

import java.util.List;

/**
 * An interface that when implemented by a drawable, enables the property pane to
 * request and display information from that drawable.
 */
public interface Propertyable {
	
	/**
	 * @return The type of the {@link Propertyable} element.
	 */
	String getType();

	/**
	 * @return The sources of the {@link Propertyable} element.
	 */
	List<String> getSources();
}
