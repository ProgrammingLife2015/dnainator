package nl.tudelft.dnainator.javafx.widgets;

import java.util.List;

/**
 * An interface that when implemented by a drawable, enables the property pane to
 * request and display information from that drawable.
 */
public interface Propertyable {
	
	/**
	 * @return This {@link ModelItem}'s type.
	 */
	String getType();

	/**
	 * @return This {@link ModelItem}'s sources.
	 */
	List<String> getSources();
}
