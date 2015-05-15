package nl.tudelft.dnainator.core.impl;

import org.neo4j.graphdb.Node;

import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.SequenceNodeFactory;

public class Neo4jSequenceNodeFactory implements SequenceNodeFactory {

	@Override
	public SequenceNode build(String id, String refs, int startPos, int endPos,
			String sequence) {
		return new SequenceNodeFactoryImpl().build(id, refs, startPos, endPos, sequence);
	}

	public SequenceNode build(Node node) {
		String id       = (String) node.getProperty("id");
		String source   = (String) node.getProperty("source");
		int startref    = (int) node.getProperty("start");
		int endref      = (int) node.getProperty("end");
		String sequence = (String) node.getProperty("sequence");
		int rank		= (int) node.getProperty("dist");
		return new SequenceNodeImpl(id, source, startref, endref, sequence, rank);
	}
}
