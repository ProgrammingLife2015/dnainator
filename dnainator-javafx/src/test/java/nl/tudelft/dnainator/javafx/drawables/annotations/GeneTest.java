package nl.tudelft.dnainator.javafx.drawables.annotations;

import nl.tudelft.dnainator.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit.ApplicationTest;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test creating an annotation connection.
 */
public class GeneTest extends ApplicationTest {

	private Gene gene;
	@Mock private Annotation an;
	
	@Override
	public void start(Stage stage) throws Exception {
	}
	
	/**
	 * Set up common variables.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	
		Mockito.when(an.getGeneName()).thenReturn("a gene");
		// CHECKSTYLE.OFF: MagicNumber
		Mockito.when(an.getStart()).thenReturn(0);
		Mockito.when(an.getEnd()).thenReturn(50);
		// CHECKSTYLE.ON: MagicNumber
		Mockito.when(an.isSense()).thenReturn(true);
		Mockito.when(an.isMutation()).thenReturn(false);
	}
	
	/**
	 * Test creating a Gene which is not a mutation.
	 */
	@Test
	public void testCreateNoMutation() {	
		gene = new Gene(an);
		Mockito.verify(an).getGeneName();
		Mockito.verify(an).getStart();
		Mockito.verify(an).getEnd();
		Mockito.verify(an).isSense();
		Mockito.verify(an).isMutation();
		
		assertEquals("annotation", gene.getStyleClass().get(0));
	}
	
	/**
	 * Test creating a Gene which is a mutation.
	 */
	@Test
	public void testCreateMutation() {
		Mockito.when(an.isMutation()).thenReturn(true);
		gene = new Gene(an);
		
		Mockito.verify(an).getGeneName();
		Mockito.verify(an).getStart();
		Mockito.verify(an).getEnd();
		Mockito.verify(an).isSense();
		Mockito.verify(an).isMutation();
		
		assertEquals("dr-mutation", gene.getStyleClass().get(0));
	}

	/**
	 * Test adding and removing a css style.
	 */
	@Test
	public void testAddAndRemoveStyle() {
		gene = new Gene(an);
		// Add style.
		gene.getChildren().add(new Text("some gene"));
		gene.addStyle("some style");
		assertFalse(gene.getChildren().get(0).getStyleClass().isEmpty());
		
		// Remove the added style.
		gene.removeStyle("some style");
		assertTrue(gene.getChildren().get(0).getStyleClass().isEmpty());
	}
	
}
