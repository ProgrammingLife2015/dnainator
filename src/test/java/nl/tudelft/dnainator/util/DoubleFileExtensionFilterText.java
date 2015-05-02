package nl.tudelft.dnainator.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link nl.tudelft.dnainator.util.DoubleFileExtensionFilter}.
 */
public class DoubleFileExtensionFilterText {
	private DoubleFileExtensionFilter subject;

	/**
	 * Initializes test environment.
	 */
	@Before
	public void setup() {
		subject = new DoubleFileExtensionFilter("Graphs", "node.graph");
	}

	/**
	 * Tests if the constructor throws an exception when
	 * no extensions are passed.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNoExt() {
		new DoubleFileExtensionFilter("Graphs", new String[0]);
	}

	/**
	 * Tests if the constructor throws an exception when
	 * a null-extension is passed.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNullExt() {
		String[] extensions = { "node.graph", null, "edge.graph" };
		new DoubleFileExtensionFilter("Graph", extensions);
	}

	/**
	 * Tests if the constructor throws an exception when
	 * an empty extension is passed.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorEmptyExt() {
		String[] extensions = { "node.graph", "", "edge.graph" };
		new DoubleFileExtensionFilter("Graph", extensions);
	}

	/**
	 * Tests if the constructor throws an exception when
	 * a single extension is passed.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorSingleExt() {
		String[] extensions = { "node.graph", "graph", "edge.graph" };
		new DoubleFileExtensionFilter("Graph", extensions);
	}

	/**
	 * Tests if the file filter accepts files with
	 * a double extension.
	 */
	@Test
	public void testAccept() {
		File f;
		try {
			f = File.createTempFile("10_strains", ".node.graph");
			assertTrue(subject.accept(f));
			f.delete();
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Tests if the file filter denies files with
	 * an incorrect double extension.
	 */
	@Test
	public void testDenyIncorrect() {
		File f;
		try {
			f = File.createTempFile("10_strains", ".tar.gz");
			assertFalse(subject.accept(f));
			f.delete();
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Tests if the file filter denies files with
	 * a single extension.
	 */
	@Test
	public void testDenySingle() {
		File f;
		try {
			f = File.createTempFile("10_strains", ".txt");
			assertFalse(subject.accept(f));
			f.delete();
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Tests if the file filter denies files with a
	 * partly correct extension.
	 */
	@Test
	public void testDenyPartOne() {
		File f;
		try {
			f = File.createTempFile("10_strains", ".node.gz");
			assertFalse(subject.accept(f));
			f.delete();
		} catch (IOException e) {
			fail(e.getMessage());
		}		
	}

	/**
	 * Tests if the file filter denies files with a
	 * partly correct extension.
	 */
	@Test
	public void testDenyPartTwo() {
		File f;
		try {
			f = File.createTempFile("10_strains", ".edge.graph");
			assertFalse(subject.accept(f));
			f.delete();
		} catch (IOException e) {
			fail(e.getMessage());
		}		
	}

	/**
	 * Tests getDescription().
	 */
	@Test
	public void testGetDescription() {
		assertEquals("Graphs", subject.getDescription());
	}

	/**
	 * Tests getExtensions().
	 */
	@Test
	public void testGetExtensions() {
		String[] expected = { "node.graph" };
		String[] extensions = subject.getExtensions();
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], extensions[i]);
		}
	}
}
