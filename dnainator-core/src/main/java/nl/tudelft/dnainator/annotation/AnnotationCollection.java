package nl.tudelft.dnainator.annotation;

import nl.tudelft.dnainator.core.Range;
import nl.tudelft.dnainator.parser.Iterator;

import java.io.IOException;
import java.util.Collection;

/**
 * An abstract interface for storing annotations.
 */
public interface AnnotationCollection {

	/**
	 * Add annotations from a given source.
	 * @param source The source of the annotations.
	 */
	default void addAnnotations(Iterator<Annotation> source) {
		try {
			while (source.hasNext()) {
				addAnnotation(source.next());
			}
		} catch (IOException e) {
			e.printStackTrace();
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
