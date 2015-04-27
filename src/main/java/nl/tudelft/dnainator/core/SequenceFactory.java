package nl.tudelft.dnainator.core;

public interface SequenceFactory {

    void setContent(String content);

    Sequence build(String refs, int startPos, int endPos);
}
