package nl.tudelft.dnainator.graph.impl.properties;

import org.neo4j.graphdb.Label;

/**
 * Static labels for Neo4j nodes.
 */
public enum NodeLabels implements Label {
	ANCESTOR,
	ANNOTATION,
	DRMUTATION,
	SOURCE,
	NODE
}
