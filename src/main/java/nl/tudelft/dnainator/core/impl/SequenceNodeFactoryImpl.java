package nl.tudelft.dnainator.core.impl;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.SequenceNodeFactory;

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
	public SequenceNode build(String id, List<String> refs, int startPos,
			int endPos, String sequence) {
		return new SequenceNodeImpl(id, refs, startPos, endPos, sequence);
	}

}
