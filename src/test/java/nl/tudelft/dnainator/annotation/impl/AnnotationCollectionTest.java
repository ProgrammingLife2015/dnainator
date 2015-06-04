package nl.tudelft.dnainator.annotation.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Collection;

import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.annotation.AnnotationCollection;

import org.junit.Before;
import org.junit.Test;

/**
 * Test the annotation collection.
 */
public class AnnotationCollectionTest {
	private AnnotationCollection collection;
	private AnnotationImpl first;
	private AnnotationImpl middle;
	private AnnotationImpl last;

	/**
	 * get the instance of the annotations singleton.
	 */
	@Before
	public void setUp() {
		collection = new AnnotationCollectionImpl();
		//CHECKSTYLE.OFF: MagicNumber
		first = new AnnotationImpl("first", 0, 10, true);
		middle = new AnnotationImpl("middle", 5, 25, true);
		last = new AnnotationImpl("last", 20, 30, true);
		//CHECKSTYLE.ON: MagicNumber
		collection.addAnnotation(first);
		collection.addAnnotation(middle);
		collection.addAnnotation(last);
	}

	/**
	 * Test whether the end of the range is exclusive.
	 */
	@Test
	public void testGetAnnotationsRangeExclusive() {
		//CHECKSTYLE.OFF: MagicNumber
		Collection<Annotation> as = collection.getSubrange(1, 20);
		//CHECKSTYLE.ON: MagicNumber
		assertTrue(as.contains(first));
		assertTrue(as.contains(middle));
		assertFalse(as.contains(last));
	}

	/**
	 * Test whether the start of the range is inclusive.
	 */
	@Test
	public void testGetAnnotationsRangeInclusive() {
		//CHECKSTYLE.OFF: MagicNumber
		Collection<Annotation> as = collection.getSubrange(0, 21);
		//CHECKSTYLE.ON: MagicNumber
		assertTrue(as.contains(first));
		assertTrue(as.contains(middle));
		assertTrue(as.contains(last));
	}

}
