package nl.tudelft.dnainator.graph;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.stream.PipeBase;
import org.graphstream.stream.sync.SinkTime;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.layout.Layout;

/**
 * This class will (eventually) compute the layout for a graph of DNA Sequences.
 * For now this class calculates a tree layout based on node index.
 */
public class DNALayout extends PipeBase implements Layout {
	private SinkTime sinkTime;
	private long lastStepTime;
	private int nodeMoved;
	private Graph internalGraph;
	private static final int SCALE = 4;
	
	/**
	 * Creates a new DNALayout given a certain graph.
	 * @param g	the graph that needs a layout
	 */
	public DNALayout(Graph g) {
		this(g, new SinkTime());
	}

	/**
	 * Creates a new DNALayout given a certain graph.
	 * @param g	the graph that needs a layout
	 * @param t	the internal timer of this layout
	 */
	public DNALayout(Graph g, SinkTime t) {
		sinkTime = t;
		sourceTime.setSinkTime(sinkTime);
		internalGraph = g;
	}


	@Override
	public String getLayoutAlgorithmName() {
		return "DNALayout";
	}

	@Override
	public long getLastStepTime() {
		return lastStepTime;
	}


	@Override
	public int getNodeMovedCount() {
		return nodeMoved;
	}

	@Override
	public void nodeAdded(String sourceId, long timeId, String nodeId) {
		Node n = internalGraph.getNode(nodeId);
		sendNodeAttributeChanged(sourceId, n.getId(), "xyz", null, pos(n));
	}

	@Override
	public void nodeRemoved(String graphId, long timeId, String nodeId) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getStabilization() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public double getStabilizationLimit() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Point3 getLowPoint() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Point3 getHiPoint() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int getSteps() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public double getQuality() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public double getForce() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}


	@Override
	public void setForce(double value) {
		// TODO Auto-generated method stub

	}


	@Override
	public void setStabilizationLimit(double value) {
		// TODO Auto-generated method stub

	}


	@Override
	public void setQuality(double qualityLevel) {
		// TODO Auto-generated method stub

	}


	@Override
	public void setSendNodeInfos(boolean send) {
		// TODO Auto-generated method stub

	}


	@Override
	public void shake() {
		// TODO Auto-generated method stub

	}


	@Override
	public void moveNode(String id, double x, double y, double z) {
		// TODO Auto-generated method stub

	}


	@Override
	public void freezeNode(String id, boolean frozen) {
		// TODO Auto-generated method stub

	}


	@Override
	public void compute() {
		nodeMoved = 0;
		for (Node n : internalGraph) {
			sendNodeAttributeChanged(sourceId, n.getId(), "xyz", null, pos(n));
		}
	}

	private double[] pos(Node n) {
		int depth = (int) (Math.log10(n.getIndex() + 1) / Math.log10(2));
		int leafs = (int) Math.pow(2, depth);
		double x = (double) depth / SCALE;
		double y = (double) (n.getIndex() + 1 - leafs) / leafs - 1.f / SCALE;
		return new double[] {x, y, 0.};
	}
}
