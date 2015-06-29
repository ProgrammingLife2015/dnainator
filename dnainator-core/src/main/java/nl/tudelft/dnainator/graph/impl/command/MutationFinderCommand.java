package nl.tudelft.dnainator.graph.impl.command;

import nl.tudelft.dnainator.graph.impl.RelTypes;
import nl.tudelft.dnainator.graph.interestingness.Scores;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Transaction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MutationFinderCommand implements Command {
	private Map<Node, Set<Node>> sources;
	private Node mutation;
	
	public MutationFinderCommand(Node mutation) {
		this.mutation = mutation;
		this.sources = new HashMap<>();
		
	}
	
	@Override
	public void execute(GraphDatabaseService service) {
		Set<Node> commonancestors = new HashSet<>();
		try (Transaction tx = service.beginTx()) {
			for (Path p : service.traversalDescription()
				.breadthFirst()
				.relationships(RelTypes.SOURCE, Direction.OUTGOING)
				.relationships(RelTypes.ANCESTOR_OF, Direction.INCOMING)
				.evaluator(new PhyloEvaluator())
				.traverse(mutation)
			) {
				commonancestors.add(p.endNode());
			}
			mutation.setProperty(Scores.INDEP_MUT.name(), commonancestors.size());
			tx.success();
		}
	}
}
