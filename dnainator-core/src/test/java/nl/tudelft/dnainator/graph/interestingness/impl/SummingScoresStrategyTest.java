package nl.tudelft.dnainator.graph.interestingness.impl;

import static org.junit.Assert.assertEquals;
import nl.tudelft.dnainator.graph.interestingness.ScoreContainer;
import nl.tudelft.dnainator.graph.interestingness.Scores;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests the {@link SummingScoresStrategy}.
 */
public class SummingScoresStrategyTest {
	private static final int TEST_SEQ_LENGTH = 5;
	private static final int TEST_DR_MUT = 1;
	private SummingScoresStrategy strategy;
	private ScoreContainer container;

	/**
	 * setup the test.
	 */
	@Before
	public void setUp() {
		this.strategy = new SummingScoresStrategy();
		this.container = Mockito.mock(ScoreContainer.class);
		Mockito.when(container.getScore(Scores.SEQ_LENGTH)).thenReturn(TEST_SEQ_LENGTH);
		Mockito.when(container.getScore(Scores.DR_MUT)).thenReturn(TEST_DR_MUT);
	}

	/**
	 * Tests whether it returns the sum of all scores.
	 */
	@Test
	public void testSum() {
		int expected = Scores.SEQ_LENGTH.applyImportanceModifier(TEST_SEQ_LENGTH)
				+ Scores.DR_MUT.applyImportanceModifier(TEST_DR_MUT);
		assertEquals(expected, strategy.compute(container));
	}

}
