package nl.tudelft.dnainator.annotation;

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
		assertEquals("name", mutation.getGeneName());
		assertEquals("type", mutation.getType());
		assertEquals("change", mutation.getChange());
		assertEquals("filter", mutation.getFilter());
		assertEquals(0, mutation.getPosition());
		assertEquals("drug", mutation.getDrug());
	}
}
