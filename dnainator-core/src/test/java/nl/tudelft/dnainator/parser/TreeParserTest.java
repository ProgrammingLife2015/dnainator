package nl.tudelft.dnainator.parser;

import nl.tudelft.dnainator.tree.TreeNode;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests the {@link TreeParser}.
 */
public class TreeParserTest {

	private File getFile(boolean correct) {
		if (correct) {
			return new File(getClass().getResource("/newick/correct.nwk").getFile());
		} else {
			return new File(getClass().getResource("/newick/wrong.nwk").getFile());
		}
	}

	/**
	 * Tests empty input.
	 */
	@Test(expected = NoSuchElementException.class)
	public void testParseNewickEmpty() {
		try {
			TreeParser tp = new TreeParser(getFile(false));
			tp.parse();
		} catch (IOException e) {
			fail("Shouldn't happen");
		}
	}


	/**
	 * Tests a good weather situation, where the input is of the correct format.
	 */
	@Test
	public void testParseNewickGood() {
		try {
			//CHECKSTYLE.OFF: MagicNumber
			TreeParser tp = new TreeParser(getFile(true));
			TreeNode root = tp.parse();
			assertEquals(root.getChildren().size(), 2);
			assertEquals(root.getName(), null);
			assertTrue(Double.compare(root.getDistance(), 0.1) == 0);
			assertEquals(root.getParent(), null);
			//CHECKSTYLE.ON: MagicNumber
		} catch (IOException e) {
			fail("Shouldn't happen.");
		}
	}
}
