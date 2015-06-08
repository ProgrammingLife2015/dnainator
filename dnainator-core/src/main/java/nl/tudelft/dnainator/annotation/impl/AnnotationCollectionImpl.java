package nl.tudelft.dnainator.annotation.impl;

import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.annotation.AnnotationCollection;
import nl.tudelft.dnainator.annotation.Range;
import nl.tudelft.dnainator.parser.AnnotationParser;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A default implementation of an {@link AnnotationCollection}, which can
 * store and return annotations.
 */
public class AnnotationCollectionImpl implements AnnotationCollection {
	// This can be optimized using an IntervalTree.
	private List<Annotation> annotationStore;
	
	/**
	 * Construct an empty AnnotationCollection.
	 */
	public AnnotationCollectionImpl() {
		this.annotationStore = new LinkedList<>();
	}

	/**
	 * Construct an AnnotationCollection, which will be filled using the given source.
	 * This is equivalent to first constructing an empty AnnotationCollection, and then
	 * using addAnnotations.
	 * @param source The source for getting the annotations.
	 */
	public AnnotationCollectionImpl(AnnotationParser source) {
		this();
		addAnnotations(source);
	}

	@Override
	public void addAnnotation(Annotation a) {
		annotationStore.add(a);
	}

	@Override
	public Collection<Annotation> getAll() {
		return annotationStore;
	}

	@Override
	public Collection<Annotation> getSubrange(Range r) {
		Range exclusive = r.getExclusiveEnd();
		return annotationStore.stream()
				.filter(a -> a.getRange().compareTo(exclusive) == 0)
				.collect(Collectors.toList());
	}
}
