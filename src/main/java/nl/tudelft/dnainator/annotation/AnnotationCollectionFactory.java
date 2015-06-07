package nl.tudelft.dnainator.annotation;

import nl.tudelft.dnainator.parser.AnnotationParser;

/**
 * Interface for abstracting construction of AnnotationCollections,
 * using the abstract factory method.
 */
public interface AnnotationCollectionFactory {

	/**
	 * @return The constructed AnnotationCollection.
	 */
	AnnotationCollection build();

	/**
	 * Utility factory method for passing the AnnotationSource right away.
	 * @param source the source for getting the annotations.
	 * @return The constructed AnnotationCollection.
	 */
	default AnnotationCollection build(AnnotationParser source) {
		AnnotationCollection as = build();
		as.addAnnotations(source);
		return as;
	}
}
