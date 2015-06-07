package nl.tudelft.dnainator.annotation.impl;

import nl.tudelft.dnainator.annotation.AnnotationCollection;
import nl.tudelft.dnainator.annotation.AnnotationCollectionFactory;

/**
 * The default {@link AnnotationCollectionFactory}, which constructs
 * a (default) {@link AnnotationCollection}.
 */
public class AnnotationCollectionFactoryImpl implements
		AnnotationCollectionFactory {

	@Override
	public AnnotationCollection build() {
		return new AnnotationCollectionImpl();
	}

}
