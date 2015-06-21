package nl.tudelft.dnainator.graph.impl;

import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.Assert;

import nl.tudelft.dnainator.core.EnrichedSequenceNode;

/**
 * Test utility methods for graph tests.
 */
public final class Neo4jTestUtils {

	private Neo4jTestUtils() {

	}

	/**
	 * assert in unordered manner.
	 * @param expected 
	 * @param actual 
	 */
	protected static void assertUnorderedIDEquals(Collection<String> expected,
			Collection<EnrichedSequenceNode> actual) {
		Assert.assertEquals(expected.stream().collect(Collectors.toSet()),
				actual.stream().map(sn -> sn.getId()).collect(Collectors.toSet()));
	}

}
