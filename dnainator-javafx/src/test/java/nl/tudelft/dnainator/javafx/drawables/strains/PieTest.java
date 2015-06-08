package nl.tudelft.dnainator.javafx.drawables.strains;

import javafx.scene.Group;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the implementation of the Pie drawable,
 * that is used to highlight sources in the strain view.
 */
public class PieTest {
	private Pie pie;

	/**
	 * Set up common variables.
	 */
	@Before
	public void setup() {
		pie = new Pie(1, Arrays.asList("1", "2", "3", "4"));
	}

	/**
	 * Verify that when a pie is created, it has only the styles specified in the constructor.
	 * Verify that this matches the number of arcs.
	 * Also verify that the number of direct children is always 2,
	 * since each pie has a circle as background and a group for its arcs.
	 */
	@Test
	public void testCreate() {
		Pie emptypie = new Pie(1, new ArrayList<String>());

		// CHECKSTYLE.OFF: MagicNumber
		assertEquals(2, emptypie.getChildren().size());
		assertEquals(0, emptypie.getStyles().size());
		assertEquals(0, getNumberArcs(emptypie));

		assertEquals(2, pie.getChildren().size());
		assertEquals(4, pie.getStyles().size());
		assertEquals(4, getNumberArcs(pie));
		// CHECKSTYLE.ON: MagicNumber
	}

	/**
	 * Verify that when a style is added, an arc is added to the pie.
	 */
	@Test
	public void testAddStyle() {
		pie.getStyles().add("default");

		// CHECKSTYLE.OFF: MagicNumber
		assertEquals(2, pie.getChildren().size());
		assertEquals(5, pie.getStyles().size());
		assertEquals(5, getNumberArcs(pie));
		// CHECKSTYLE.ON: MagicNumber
	}

	/**
	 * Verify that when a style is removed, the correct arc is also removed from the pie.
	 */
	@Test
	public void testRemoveStyle() {
		pie.getStyles().remove("4");

		// CHECKSTYLE.OFF: MagicNumber
		assertEquals(2, pie.getChildren().size());
		assertEquals(3, pie.getStyles().size());
		assertEquals(3, getNumberArcs(pie));
		// CHECKSTYLE.ON: MagicNumber
	}

	/**
	 * Verify that when the last style is removed, nothing strange happens.
	 */
	@Test
	public void testEmpty() {
		pie.getStyles().clear();

		// CHECKSTYLE.OFF: MagicNumber
		assertEquals(2, pie.getChildren().size());
		assertEquals(0, pie.getStyles().size());
		assertEquals(0, getNumberArcs(pie));
		// CHECKSTYLE.ON: MagicNumber
	}

	private int getNumberArcs(Pie pie) {
		return pie.getChildren().stream()
				.filter(e -> e instanceof Group)
				.mapToInt(e -> ((Group) e).getChildren().size())
				.sum();
	}
}
