package nl.tudelft.dnainator.core.graph;

import nl.tudelft.dnainator.core.Sequence;

import org.graphstream.stream.Sink;
import org.graphstream.stream.SourceBase;

/**
 * This class realizes a graphfactory using GraphStream as it's backend.
 */
public class GSGraphFactory extends SourceBase implements GraphFactory {
	private int edgecount;
	
	/**
	 * This factory constructs a GraphStream graph with the given title.
	 * @param g		the initial sink of the graphfactory
	 */
	public GSGraphFactory(Sink g) {
		this.addSink(g);
	}
	
	@Override
	public void addEdge(int l, int r) {
		sendEdgeAdded(this.sourceId, Integer.toString(edgecount++),
					  Integer.toString(l), Integer.toString(r), false);
	}

	@Override
	public void addNode(Sequence s) {
		String id = Integer.toString(s.getId());
		sendNodeAdded(this.sourceId, id);
		sendNodeAttributeAdded(this.sourceId, id, "start", s.getStartRef());
		sendNodeAttributeAdded(this.sourceId, id, "end", s.getEndRef());
		sendNodeAttributeAdded(this.sourceId, id, "sequence", s.getSource());
		sendNodeAttributeAdded(this.sourceId, id, "source", s.getSequence());
	}
}
