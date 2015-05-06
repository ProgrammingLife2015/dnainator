package nl.tudelft.dnainator.core.impl;

import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.SequenceNodeFactory;

/**
 * A {@link SequenceNodeFactory} to build a {@link SequenceNodeImpl}.
 */
public class SequenceNodeFactoryImpl implements SequenceNodeFactory {

	@Override
	public SequenceNode build(String id, String refs, int startPos, int endPos, String sequence) {
		return new SequenceNodeImpl(id, refs, startPos, endPos, sequence);
	}

}
