package nl.tudelft.dnainator.annotation;

import nl.tudelft.dnainator.annotation.impl.DRMutation;

import nl.tudelft.dnainator.core.Range;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link DRMutation}s.
 */
public class DRMutationTest {
	/**
	 * Tests the creation and correct initialization of the {@link DRMutation}.
	 */
	@Test
	public void testDRMutation() {
		DRMutation mutation = new DRMutation("name", "type", "change", "filter", 0, "drug");
		String expected = "name -> type: type change: change position: 0 drug: drug";
		assertEquals(expected, mutation.getGeneName());
		assertEquals(new Range(0, 1), mutation.getRange());
	}
}
