package nl.tudelft.dnainator.annotation;

import nl.tudelft.dnainator.annotation.impl.DRMutation;
import nl.tudelft.dnainator.parser.impl.DRMutationIterator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;

/**
 * Test class for the {@link DRMutationFactory}.
 */
@RunWith(MockitoJUnitRunner.class)
public class DRMutationFactoryTest {
	@Mock private static AnnotationCollection annotations;

	private void createDRMutations() throws IOException, URISyntaxException {
		DRMutationFactory factory = new DRMutationFactory();
		File testFile = new File(getClass().getResource("/annotations/dr_test.txt").toURI());
		annotations = factory.build(annotations, new DRMutationIterator(testFile));
	}

	/**
	 * Tests the build method.
	 * @throws IOException Thrown if reading fails.
	 * @throws URISyntaxException Thrown if creating the file fails.
	 */
	@Test
	public void testBuild() throws IOException, URISyntaxException {
		createDRMutations();
		Mockito.verify(annotations, times(1)).addAnnotation(any(DRMutation.class));
	}
}
