package nl.tudelft.dnainator.core.impl;

import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.SequenceNodeFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * A {@link SequenceNodeFactory} to build a {@link SequenceNodeImpl}.
 */
public class SequenceNodeFactoryImpl implements SequenceNodeFactory {
	private static final Pattern COMMA_DELIM = Pattern.compile(",");

	@Override
	public SequenceNode build(String id, String refs, int startPos, int endPos, String sequence) {
		return build(id, splitOnComma(refs), startPos, endPos, sequence);
	}

	private List<String> splitOnComma(String refs) {
		return Arrays.asList(COMMA_DELIM.split(refs));
	}

	@Override
	public SequenceNode build(String id, Set<String> refs, int startPos,
			int endPos, String sequence) {
		return new SequenceNodeImpl(id, refs, startPos, endPos, sequence);
	}

}
