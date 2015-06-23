package nl.tudelft.dnainator.javafx.widgets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import javafx.application.Platform;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.ColorMap;
import nl.tudelft.dnainator.javafx.drawables.strains.Strain;
import nl.tudelft.dnainator.javafx.views.StrainView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit.ApplicationTest;

/**
 * This class tests the implementation of {@link Minimap}.
 * The {@link Minimap} is a minimap in the {@link StrainView},
 * on which the user may click on for quick navigation.
 */
public class MinimapTest extends ApplicationTest {

	@Mock private Graph graph;
	@Mock private StrainView strainView;
	private Strain strain;
	private Minimap minimap;
	
	@Override
	public void start(Stage stage) throws Exception {
	}
	
	/**
	 * Set up common variables.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		// CHECKSTYLE.OFF: MagicNumber
		Mockito.when(graph.getMaxBasePairs()).thenReturn(0);
		// CHECKSTYLE.ON: MagicNumber
		strain = new Strain(new ColorMap(), graph);
		minimap = new Minimap(strain, graph, strainView);
	}
	
	/**
	 * Test creation of a minimap.
	 * Ensure that three children are present.
	 */
	@Test
	public void testCreate() {
		Platform.runLater(() -> {
			// CHECKSTYLE.OFF: MagicNumber
			assertEquals(2, minimap.getChildren().size());
			// CHECKSTYLE.ON: MagicNumber
		});
	}
	
	/**
	 * Test that the x coordinate changes when the min rank property has changed.
	 */
	@Test
	public void testOnMinRankChange() {
		Platform.runLater(() -> {
			// CHECKSTYLE.OFF: MagicNumber
			Rectangle view = (Rectangle) minimap.getChildren().get(1);
			// CHECKSTYLE.ON: MagicNumber
			assertNotNull(view);
			// CHECKSTYLE.OFF: MagicNumber
			strain.minRankProperty().set(4);
			strain.maxRankProperty().set(6);
			assertEquals(4, strain.minRankProperty().get());
			assertEquals(6, strain.maxRankProperty().get());
			// CHECKSTYLE.ON: MagicNumber
		});
	}
}
