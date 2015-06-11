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
	private static final int TEST_SCORE = 5;
	private SummingScoresStrategy strategy;
	private ScoreContainer container;

	/**
	 * setup the test.
	 */
	@Before
	public void setUp() {
		this.strategy = new SummingScoresStrategy();
		this.container = Mockito.mock(ScoreContainer.class);
		Mockito.when(container.getScore(Mockito.any())).thenReturn(TEST_SCORE);
	}

	/**
	 * Tests whether it returns the sum of all scores.
	 */
	@Test
	public void testSum() {
		assertEquals(strategy.compute(container), Scores.values().length * TEST_SCORE);
	}

}
