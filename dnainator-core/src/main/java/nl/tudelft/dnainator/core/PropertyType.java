package nl.tudelft.dnainator.core;

/**
 * All displable property labels should implement this interface.
 */
public interface PropertyType {
	/**
	 * @return	the fixed name of this property
	 */
	String name();

	/**
	 * @return	the pretty description of this property
	 */
	String description();
}
