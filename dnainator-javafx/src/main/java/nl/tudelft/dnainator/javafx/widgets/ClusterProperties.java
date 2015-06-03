package nl.tudelft.dnainator.javafx.widgets;

import java.util.List;

/**
 * Interface for properties of {@link ClusterDrawable}s.
 * It contains all the information attached to a {@link ClusterDrawable}.
 */
public interface ClusterProperties extends Propertyable {
	
	/**
	 * @return The ids of a {@link ClusterDrawable}.
	 */
	List<String> getIds();
	
	/**
	 * @return	the start references of a {@link ClusterDrawable}.
	 */
	List<Integer> getStartRefs();
	
	/**
	 * @return	the end references of a {@link ClusterDrawable}.
	 */
	List<Integer> getEndRefs();

	/**
	 * @return	the base sequences of a {@link ClusterDrawable}.
	 */
	List<String> getSequences();
}
