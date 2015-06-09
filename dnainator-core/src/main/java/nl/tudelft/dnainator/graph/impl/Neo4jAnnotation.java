package nl.tudelft.dnainator.graph.impl;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.annotation.Range;

/**
 * An {@link Annotation} which is stored in the Neo4j database.
 */
public class Neo4jAnnotation implements Annotation {
	private String geneName;
	private Range range;
	private boolean isSense;

	/**
	 * Constructs a new Neo4jAnnotation.
	 * @param service the service for accessing the database
	 * @param delegate the node to delegate to.
	 */
	public Neo4jAnnotation(GraphDatabaseService service, Node delegate) {
		try (Transaction tx = service.beginTx()) {
			this.geneName = (String) delegate.getProperty(PropertyTypes.ID.name());
			this.range = new Range((Integer) delegate.getProperty(PropertyTypes.STARTREF.name()),
					(Integer) delegate.getProperty(PropertyTypes.ENDREF.name()));
			this.isSense = (Boolean) delegate.getProperty(PropertyTypes.SENSE.name());
		}
	}

	@Override
	public String getGeneName() {
		return geneName;
	}

	@Override
	public Range getRange() {
		return range;
	}

	@Override
	public boolean isSense() {
		return isSense;
	}

}
