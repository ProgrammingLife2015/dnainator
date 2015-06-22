package nl.tudelft.dnainator.graph.impl.command;

import java.util.Iterator;

import nl.tudelft.dnainator.graph.impl.NodeLabels;
import nl.tudelft.dnainator.graph.impl.RelTypes;
import nl.tudelft.dnainator.graph.impl.query.BubbleSkipper;
import nl.tudelft.dnainator.graph.interestingness.Scores;

import org.neo4j.collection.primitive.Primitive;
import org.neo4j.collection.primitive.PrimitiveLongIterator;
import org.neo4j.collection.primitive.PrimitiveLongSet;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.traversal.InitialBranchState;
import org.neo4j.graphdb.traversal.Uniqueness;

import static nl.tudelft.dnainator.graph.impl.properties.SequenceProperties.BASE_DIST;
import static nl.tudelft.dnainator.graph.impl.properties.SequenceProperties.RANK;
import static nl.tudelft.dnainator.graph.impl.Neo4jUtil.incoming;
import static nl.tudelft.dnainator.graph.impl.Neo4jUtil.inDegree;
import static nl.tudelft.dnainator.graph.impl.Neo4jUtil.outgoing;
import static nl.tudelft.dnainator.graph.impl.Neo4jUtil.outDegree;
import static org.neo4j.helpers.collection.IteratorUtil.loop;

/**
 * The {@link AnalyzeCommand} creates a topological ordering and
 * ranks the nodes in the Neo4j database accordingly.
 */
public class AnalyzeCommand implements Command {
	private ResourceIterator<Node> roots;
	private PrimitiveLongSet bubbleSources;

	/**
	 * Create a new {@link AnalyzeCommand} that will
	 * start ranking from the specified roots.
	 * @param roots	the roots
	 */
	public AnalyzeCommand(ResourceIterator<Node> roots) {
		this.roots = roots;
		this.bubbleSources = Primitive.longSet();
	}

	/**
	 * Return a topological ordering on the specified database service.
	 * @param service	the database service
	 * @return		a topological ordering, starting from the roots
	 */
	@SuppressWarnings("unchecked")
	public Iterable<Node> topologicalOrder(GraphDatabaseService service) {
		return service.traversalDescription()
				// Depth first order, for creating bubbles.
				.depthFirst()
				.expand(new TopologicalPathExpander(), InitialBranchState.NO_STATE)
				// We manage uniqueness for ourselves.
				.uniqueness(Uniqueness.NONE)
				.traverse(loop(roots))
				.nodes();
	}

	/**
	 * Attempts to find a bouble from the given source.
	 * @param service the database service.
	 * @param source the node to start from.
	 * @return a breadth first directed traversal, starting from the given source.
	 */
	public Iterable<Node> bubbleTraverser(GraphDatabaseService service, Node source) {
		return service.traversalDescription()
				.breadthFirst()
				// Skip nested bubbles.
				.expand(BubbleSkipper.get())
				.traverse(source)
				.nodes();
	}

	@Override
	public void execute(GraphDatabaseService service) {
		for (Node n : topologicalOrder(service)) {
			rankDest(n);
			if (!bubbleSources.contains(n.getId()) && outDegree(n) >= 2) {
				System.out.println("--> Begin Recursion level: 0");;
				tryBubble(service, n, 0);
				System.out.println("--> End Recursion level: 0");;
			}
		}
	}

	private void tryBubble(GraphDatabaseService service, Node start, int recursionLevel) {
		System.out.println("Try bubble for source: " + start.getProperty("ID"));;
		bubbleSources.add(start.getId());
		PrimitiveLongSet pathNodes = Primitive.longSet();
		PrimitiveLongSet endRelationships = Primitive.longSet();
		Iterator<Node> it = bubbleTraverser(service, start).iterator();
		advancePaths(endRelationships, it.next()); // Skip source node.
		while (it.hasNext()) {
			Node n = it.next();
			System.out.println("Current node: " + n.getProperty("ID"));;
			if (convergentPaths(service, endRelationships)) {
				System.out.println("Try bubble: " + start.getProperty("ID") + ", " + n.getProperty("ID"));;
				if (inDegree(n) != endRelationships.size()) {
					System.out.println("In-degree not equal to number of paths, giving up on: " + start.getProperty("ID"));;
					return;
				}
				if (!isSimpleBubble(service, pathNodes, start.getId(), n.getId())) {
					System.out.println("Not a simple bubble.");;
					return;
				}
				System.out.println("Found bubble: " + start.getProperty("ID") + ", " + n.getProperty("ID"));;
				start.addLabel(NodeLabels.BUBBLE_SOURCE);
				start.createRelationshipTo(n, RelTypes.BUBBLE_SOURCE_OF);
				return;
			}
			pathNodes.add(n.getId());
			if (outDegree(n) >= 2) {
				System.out.println("--> Begin Recursion level: " + (recursionLevel + 1));;
				tryBubble(service, n, recursionLevel + 1);
				System.out.println("--> End Recursion level: " + (recursionLevel + 1));;
			}
			advancePaths(endRelationships, n);
		}
		System.out.println("Giving up for source: " + start.getProperty("ID"));;
	}

	private boolean isSimpleBubble(GraphDatabaseService service,
			PrimitiveLongSet pathNodes, long source, long sink) {
		PrimitiveLongIterator it = pathNodes.iterator();
		while (it.hasNext()) {
			long id = it.next();
			Node n = service.getNodeById(id);
			for (Relationship inout : n.getRelationships(RelTypes.NEXT)) {
				System.out.println("Test foreign relationship: " + inout.getStartNode().getProperty("ID") + " -> " + inout.getEndNode().getProperty("ID"));
				long otherID = inout.getOtherNode(n).getId();
				if (otherID != sink && otherID != source && !pathNodes.contains(otherID)) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean convergentPaths(GraphDatabaseService service,
			PrimitiveLongSet endRelationships) {
		PrimitiveLongIterator it = endRelationships.iterator();
		long prev = service.getRelationshipById(it.next()).getEndNode().getId();
		while (it.hasNext()) {
			long inID = service.getRelationshipById(it.next()).getEndNode().getId();
			if (inID != prev) {
				return false;
			}
		}
		return true;
	}

	private void advancePaths(PrimitiveLongSet endRelationships,
			Node n) {
		for (Relationship in : incoming(n)) {
			// Remove it, part of advancing the paths.
			endRelationships.remove(in.getId());
		}
		for (Relationship out : outgoing(n)) {
			endRelationships.add(out.getId());
		}
	}

	private void rankDest(Node n) {
		int baseSource = (int) n.getProperty(BASE_DIST.name())
				+ (int) n.getProperty(Scores.SEQ_LENGTH.name());
		int rankSource = (int) n.getProperty(RANK.name()) + 1;

		for (Relationship r : outgoing(n)) {
			Node dest = r.getEndNode();

			if ((int) dest.getProperty(BASE_DIST.name()) < baseSource) {
				dest.setProperty(BASE_DIST.name(), baseSource);
			}
			if ((int) dest.getProperty(RANK.name()) < rankSource) {
				dest.setProperty(RANK.name(), rankSource);
			}
		}
	}
}
