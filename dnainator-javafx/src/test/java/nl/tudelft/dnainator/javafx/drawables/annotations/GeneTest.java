package nl.tudelft.dnainator.javafx.drawables.annotations;

import nl.tudelft.dnainator.annotation.Annotation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test creating a gene annotation.
 */
@RunWith(MockitoJUnitRunner.class)
public class GeneTest {
	
	@Mock private Annotation annotation;

	/**
	 * Test construction of an annotation.
	 */
	@Test
	public void test() {
		Gene gene = new Gene(annotation);
		gene.addStyle("yellow");
		gene.removeStyle("yellow");
		Mockito.verify(annotation, Mockito.times(1)).getGeneName();
		Mockito.verify(annotation, Mockito.times(1)).getRange();
		Mockito.verify(annotation, Mockito.times(1)).isSense();
	}

}
