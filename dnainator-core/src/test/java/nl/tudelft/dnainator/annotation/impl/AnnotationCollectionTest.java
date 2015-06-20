package nl.tudelft.dnainator.annotation.impl;

import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.annotation.AnnotationCollection;
import nl.tudelft.dnainator.parser.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test the annotation collection.
 */
@RunWith(MockitoJUnitRunner.class)
public class AnnotationCollectionTest {
	private AnnotationCollection collection;
	private AnnotationImpl first;
	private AnnotationImpl middle;
	private AnnotationImpl last;

	@Mock private Iterator<Annotation> parser;

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

	/**
	 * Test parsing all annotations from an annotionparser specified in the constructor.
	 * @throws IOException when an error occurs during parsing
	 */
	@Test
	public void testAnnotationParser() throws IOException {
		Mockito.when(parser.hasNext()).thenReturn(false);
		AnnotationCollection ac = new AnnotationCollectionImpl(parser);
		Mockito.verify(parser, Mockito.times(1)).hasNext();
		Mockito.verify(parser, Mockito.times(0)).next();
		assertEquals(0, ac.getAll().size());
	}

	/**
	 * Test parsing all annotations from an annotionparser specified later.
	 * @throws IOException when an error occurs during parsing
	 */
	@Test
	public void testAnnotationParserAfterConstruction() throws IOException {
		int expected = collection.getAll().size();

		Mockito.when(parser.hasNext()).thenReturn(false);
		collection.addAnnotations(parser);
		Mockito.verify(parser, Mockito.times(1)).hasNext();
		Mockito.verify(parser, Mockito.times(0)).next();
		assertEquals(expected, collection.getAll().size());
	}
}
