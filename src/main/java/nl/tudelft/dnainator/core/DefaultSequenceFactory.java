package nl.tudelft.dnainator.core;

/**
 * A {@link SequenceFactory} to build a {@link DefaultSequenceNode}.
 */
public class DefaultSequenceFactory implements SequenceFactory {

	@Override
	public SequenceNode build(String id, String refs, int startPos, int endPos, String sequence) {
		return new DefaultSequenceNode(Integer.parseInt(id), refs, startPos, endPos, sequence);
	}

}
