package nl.tudelft.dnainator.graph.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.annotation.Range;
import nl.tudelft.dnainator.graph.impl.properties.AnnotationProperties;
import nl.tudelft.dnainator.graph.impl.properties.SequenceProperties;

/**
 * An {@link Annotation} which is stored in the Neo4j database.
 */
public class Neo4jAnnotation implements Annotation {
	private String geneName;
	private Range range;
	private boolean isSense;
	private Collection<String> annotatedNodes;

	/**
	 * Constructs a new Neo4jAnnotation.
	 * @param service the service for accessing the database
	 * @param delegate the node to delegate to.
	 */
	public Neo4jAnnotation(GraphDatabaseService service, Node delegate) {
		try (Transaction tx = service.beginTx()) {
			this.geneName = (String) delegate.getProperty(AnnotationProperties.ID.name());
			this.range = new Range((int) delegate.getProperty(AnnotationProperties.STARTREF.name()),
					(Integer) delegate.getProperty(AnnotationProperties.ENDREF.name()));
			this.isSense = (Boolean) delegate.getProperty(AnnotationProperties.SENSE.name());
			this.annotatedNodes = new ArrayList<>();
			delegate.getRelationships(Direction.INCOMING, RelTypes.ANNOTATED).forEach(e ->
				annotatedNodes.add(
					(String) e.getStartNode().getProperty(SequenceProperties.ID.name())
				)
			);
			tx.success();
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

	@Override
	public Collection<String> getAnnotatedNodes() {
		return annotatedNodes;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Annotation)) {
			return false;
		}
		Annotation a = (Annotation) other;
		return a.getGeneName().equals(this.getGeneName()) && a.getStart() == this.getStart()
				&& a.getEnd() == this.getEnd() && a.isSense() == this.isSense();
	}

	@Override
	public int hashCode() {
		return getGeneName().hashCode() + getStart() + getEnd() + ((Boolean) isSense()).hashCode();
	}

}
