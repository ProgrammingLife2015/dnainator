package nl.tudelft.dnainator.javafx.drawables.strains;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * This class tests the implementation of the Edge drawable,
 * that is used to draw edges between ClusterDrawables.
 */
@RunWith(MockitoJUnitRunner.class)
public class EdgeTest {

	private Edge edge;
	@Mock private ClusterDrawable src;
	@Mock private ClusterDrawable dest;
	
	/**
	 * Set up common variables.
	 */
	@Before
	public void setup() {
		edge = new Edge(src, dest);
	}

	/**
	 * Test whether an edge has only one direct child when creating an edge
	 * between two drawables and two children when between a drawable and a non-drawable.
	 */
	@Test
	public void testCreate() {
		Edge twoDrawables = new Edge(src, dest);
		String dest = "some dest";
		Edge oneDrawable = new Edge(src, dest);
		
		assertEquals(1, twoDrawables.getChildren().size());
		assertEquals(2, oneDrawable.getChildren().size());
	}
	
	/**
	 * Tests getting an edge.
	 */
	@Test
	public void testGetEdge() {
		assertNotNull(edge.getEdge());
	}

	/**
	 * Tests getting the propertymap.
	 */
	@Test
	public void testGetPropertymap() {
		assertNotNull(edge.getPropertyMap());
	}	
}
