package nl.tudelft.dnainator.javafx.drawables.strains;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.ColorMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * Test the {@link Strain} of the model.
 * Also test its abstract base classes.
 */
@RunWith(MockitoJUnitRunner.class)
public class StrainTest {
	@Mock private Graph graph;
	@Mock private Node node;
	@Mock private Group content;
	private Strain strain;

	/**
	 * Initialize the graphitem.
	 */
	@Before
	public void setup() {
		content = new Group();
		// CHECKSTYLE.OFF: MagicNumber
		strain = new Strain(new ColorMap(), graph, content);
		// CHECKSTYLE.ON: MagicNumber
	}

	/**
	 * Test whether this item is updated correctly, given a specified viewport.
	 * The drawable we're testing currently extends to x = 40000, y = 20.
	 */
	@Test
	public void testUpdate() {
		// CHECKSTYLE.OFF: MagicNumber
		strain.update(new Rectangle(20000, 10000).getBoundsInLocal(), 1.0);
		verify(graph, atLeastOnce()).getRank(anyInt());

		strain.update(new Rectangle(5000, 2500).getBoundsInLocal(), 2.0);
		verify(graph, atLeastOnce()).getRank(anyInt());

		strain.update(new Rectangle(1000, 500).getBoundsInLocal(), 10.0);
		verify(graph, atLeastOnce()).getRank(anyInt());

		strain.update(new Rectangle(20000, 10000).getBoundsInLocal(), 1.4);
		verify(graph, atLeastOnce()).getRank(anyInt());
		// CHECKSTYLE.ON: MagicNumber
	}
}
