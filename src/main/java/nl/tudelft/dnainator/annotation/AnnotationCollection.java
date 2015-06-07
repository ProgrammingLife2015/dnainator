package nl.tudelft.dnainator.annotation;

import java.util.Collection;

import nl.tudelft.dnainator.parser.AnnotationParser;

/**
 * An abstract interface for storing annotations.
 */
public interface AnnotationCollection {

	/**
	 * Add annotations from a given source.
	 * @param source The source of the annotations.
	 */
	default void addAnnotations(AnnotationParser source) {
		while (source.hasNext()) {
			addAnnotation(source.next());
		}
	}

	/**
	 * Add an annotation to the collection.
	 * @param a The annotation to be added.
	 */
	void addAnnotation(Annotation a);

	/**
	 * @return all of the annotations in this collection.
	 */
	default Collection<Annotation> getAll() {
		return getSubrange(Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * Return all annotations covered by the given range.
	 * @param begin The beginning of the range
	 * @param end The end of the range
	 * @return all annotations covered.
	 */
	default Collection<Annotation> getSubrange(int begin, int end) {
		return getSubrange(new Range(begin, end));
	}

	/**
	 * Return all annotations covered by the given range.
	 * @param r The range
	 * @return all annotations covered.
	 */
	Collection<Annotation> getSubrange(Range r);

}
