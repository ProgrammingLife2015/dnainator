package nl.tudelft.dnainator.ui.widgets;

import java.util.List;

/**
 * An interface that when implemented by a {@link ModelItem}, enables the property pane to
 * request and display information from that {@link ModelItem}.
 */
public interface Propertyable {
	/**
	 * @return This {@link ModelItem}'s type.
	 */
	String getType();

	/**
	 * 
	 */
	String getNodeId();

	/**
	 * @return This {@link ModelItem}'s resources.
	 */
	List<String> getSources();
}
