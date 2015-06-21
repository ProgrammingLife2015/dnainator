package nl.tudelft.dnainator.graph.impl.properties;

import org.neo4j.graphdb.RelationshipType;

/**
 * Edge relationship types.
 */
public enum RelTypes implements RelationshipType {
	ANCESTOR_OF,
	ANNOTATED,
	NEXT,
	SOURCE,
}
