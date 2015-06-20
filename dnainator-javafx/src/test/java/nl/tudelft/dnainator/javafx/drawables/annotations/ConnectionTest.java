package nl.tudelft.dnainator.javafx.drawables.annotations;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import static org.junit.Assert.assertEquals;

/**
 * Test creating an annotation connection.
 */
@RunWith(MockitoJUnitRunner.class)
public class ConnectionTest {
	
	private DoubleProperty startX = new SimpleDoubleProperty();
	private DoubleProperty endX = new SimpleDoubleProperty();
	private DoubleProperty startY = new SimpleDoubleProperty();
	private DoubleProperty endY = new SimpleDoubleProperty();
	private static final double EPSILON = .001;

	/**
	 * Test creating an annotation connection.
	 */
	@Test
	public void testConstruction() {
		Connection a = new Connection(startX.add(0), startY.add(0), endX.add(0), endY.add(0));

		startX.setValue(1);
		assertEquals(1, a.getStartX(), EPSILON);
		endX.setValue(1);
		assertEquals(1, a.getEndX(), EPSILON);
		startY.setValue(1);
		assertEquals(1, a.getStartY(), EPSILON);
		endY.setValue(1);
		assertEquals(1, a.getEndY(), EPSILON);
	}

}
