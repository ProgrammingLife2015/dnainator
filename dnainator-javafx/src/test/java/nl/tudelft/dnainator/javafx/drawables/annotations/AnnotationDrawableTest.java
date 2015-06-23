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
public class AnnotationDrawableTest extends ApplicationTest {

	private AnnotationDrawable annotation;
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
	
		Mockito.when(an.getGeneName()).thenReturn("a annotation");
		// CHECKSTYLE.OFF: MagicNumber
		Mockito.when(an.getStart()).thenReturn(0);
		Mockito.when(an.getEnd()).thenReturn(50);
		// CHECKSTYLE.ON: MagicNumber
		Mockito.when(an.isSense()).thenReturn(true);
		Mockito.when(an.isMutation()).thenReturn(false);
	}
	
	/**
	 * Test creating a AnnotationDrawable which is not a mutation.
	 */
	@Test
	public void testCreateNoMutation() {	
		annotation = new AnnotationDrawable(an);
		Mockito.verify(an).getGeneName();
		Mockito.verify(an).getStart();
		Mockito.verify(an).getEnd();
		Mockito.verify(an).isSense();
		Mockito.verify(an).isMutation();
		
		assertEquals("annotation", annotation.getStyleClass().get(0));
	}
	
	/**
	 * Test creating a AnnotationDrawable which is a mutation.
	 */
	@Test
	public void testCreateMutation() {
		Mockito.when(an.isMutation()).thenReturn(true);
		annotation = new AnnotationDrawable(an);
		
		Mockito.verify(an).getGeneName();
		Mockito.verify(an).getStart();
		Mockito.verify(an).getEnd();
		Mockito.verify(an).isSense();
		Mockito.verify(an).isMutation();
		
		assertEquals("dr-mutation", annotation.getStyleClass().get(0));
	}

	/**
	 * Test adding and removing a css style.
	 */
	@Test
	public void testAddAndRemoveStyle() {
		annotation = new AnnotationDrawable(an);
		// Add style.
		annotation.getChildren().add(new Text("some annotation"));
		annotation.addStyle("some style");
		assertFalse(annotation.getChildren().get(0).getStyleClass().isEmpty());
		
		// Remove the added style.
		annotation.removeStyle("some style");
		assertTrue(annotation.getChildren().get(0).getStyleClass().isEmpty());
	}
	
}
