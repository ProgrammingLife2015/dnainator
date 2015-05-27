package nl.tudelft.dnainator.graph.impl.command;

import static nl.tudelft.dnainator.graph.impl.PropertyTypes.ID;
import static nl.tudelft.dnainator.graph.impl.PropertyTypes.RANK;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;

/**
 * The {@link IndexCommand} creates indices on a Neo4j database.
 */
public class IndexCommand implements Command {
	private Label nodeLabel;

	/**
	 * Create a new {@link IndexCommand} using the specified label.
	 * @param nodeLabel	the label
	 */
	public IndexCommand(Label nodeLabel) {
		this.nodeLabel = nodeLabel;
	}
	
	@Override
	public void execute(GraphDatabaseService service) {
		service.schema().getConstraints().forEach(e -> e.drop());
		service.schema().getIndexes().forEach(e -> e.drop());

		service.schema().constraintFor(nodeLabel)
			.assertPropertyIsUnique(ID.name())
			.create();

		// Generate an index on 'dist'
		service.schema().indexFor(nodeLabel)
			.on(RANK.name())
			.create();
		
	}
}
