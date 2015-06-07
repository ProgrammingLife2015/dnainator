package nl.tudelft.dnainator.parser;

import nl.tudelft.dnainator.annotation.Annotation;

/**
 * A source for Annotations.
 */
public interface AnnotationParser {

	/**
	 * @return whether there are annotations left in this source.
	 */
	boolean hasNext();

	/**
	 * @return the next annotation.
	 */
	Annotation next();

}
