package nl.tudelft.dnainator.parser;

import java.io.File;
import java.util.Map;

import nl.tudelft.dnainator.core.Sequence;
import nl.tudelft.dnainator.core.SequenceFactory;

public abstract class NodeParser {

	protected SequenceFactory sf;

	public NodeParser(SequenceFactory sf) {
		this.sf = sf;
	}

	public abstract Map<String, Sequence> parse(File f) throws NumberFormatException, InvalidHeaderFormatException;
}
