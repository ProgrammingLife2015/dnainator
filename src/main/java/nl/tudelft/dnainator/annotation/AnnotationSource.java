package nl.tudelft.dnainator.annotation;

/**
 * A source for Annotations.
 */
public interface AnnotationSource {

	/**
	 * @return whether there are annotations left in this source.
	 */
	boolean hasNext();

	/**
	 * @return the next annotation.
	 */
	Annotation next();
	
}
