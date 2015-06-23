package nl.tudelft.dnainator.javafx.drawables.phylogeny;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

/**
 * Test the {@link Edge} drawn in the phylogenetic tree.
 */
public class EdgeTest extends ApplicationTest {

	private Edge edge;
	
	@Override
	public void start(Stage stage) throws Exception {
	}
	
	/**
	 * Creates new database service.
	 */
	@Before
	public void setup() {
		edge = new Edge(new InternalNode(new ArrayList<>()));
	}
	
	/**
	 * Test the creation of an {@link Edge}.
	 */
	@Test
	public void testCreate() {
		// CHECKSTYLE.OFF: MagicNumber
		assertEquals(3, edge.getElements().size());
		// CHECKSTYLE.ON: MagicNumber
	}
}
