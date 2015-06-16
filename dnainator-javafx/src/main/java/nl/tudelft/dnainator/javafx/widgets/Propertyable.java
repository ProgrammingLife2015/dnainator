package nl.tudelft.dnainator.javafx.widgets;

import nl.tudelft.dnainator.core.PropertyType;

import java.util.Map;

/**
 * An interface that when implemented by a drawable, enables the property pane to
 * request and display information from that drawable.
 */
public interface Propertyable {
	/**
	 * @return The properties of the {@link Propertyable} element.
	 */
	Map<PropertyType, String> getPropertyMap();
}
