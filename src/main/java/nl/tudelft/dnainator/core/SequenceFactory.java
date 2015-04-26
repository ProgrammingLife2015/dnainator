package nl.tudelft.dnainator.core;

public interface SequenceFactory {

	public void setContent(String content);

	public Sequence build(String refs, int startPos, int endPos);
}
