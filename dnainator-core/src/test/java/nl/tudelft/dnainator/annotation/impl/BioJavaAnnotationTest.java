package nl.tudelft.dnainator.annotation.impl;

import org.biojava.nbio.genome.parsers.gff.Feature;
import org.biojava.nbio.genome.parsers.gff.Location;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

/**
 * Test the BioJava adapter for annotations.
 * This class mostly tests whether the functions are correctly proxied.
 */
@RunWith(MockitoJUnitRunner.class)
public class BioJavaAnnotationTest {
	private BioJavaAnnotation annotation;
	
	@Mock private Feature feature;
	@Mock private Location location;
	
	/**
	 * Test constructing a BioJavaAnnotation.
	 */
	@Test
	public void testConstruction() {
		annotation = new BioJavaAnnotation(feature);
		annotation.getGeneName();
		Mockito.verify(feature, Mockito.times(1)).getAttribute("displayName");
	}

	/**
	 * Test retrieving the range of a BioJavaAnnotation.
	 */
	@Test
	public void testRange() {
		Mockito.when(feature.location()).thenReturn(location);
		annotation = new BioJavaAnnotation(feature);
		annotation.getRange();
		Mockito.verify(location, Mockito.times(1)).bioStart();
		Mockito.verify(location, Mockito.times(1)).bioEnd();
	}
	
	/**
	 * Test retrieving the nodes this BioJavaAnnotation annotates.
	 */
	@Test
	public void testAnnotated() {
		annotation = new BioJavaAnnotation(feature);
		assertTrue(annotation.getAnnotatedNodes().isEmpty());
	}

	/**
	 * Test retrieving whether this BioJavaAnnotation is sense or not.
	 */
	@Test
	public void testSense() {
		Mockito.when(feature.location()).thenReturn(location);
		annotation = new BioJavaAnnotation(feature);
		annotation.isSense();
		Mockito.verify(location, Mockito.times(1)).bioStrand();
	}
}
