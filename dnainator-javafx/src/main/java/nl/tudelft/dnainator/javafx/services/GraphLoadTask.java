package nl.tudelft.dnainator.javafx.services;

import nl.tudelft.dnainator.annotation.AnnotationCollection;
import nl.tudelft.dnainator.annotation.impl.AnnotationCollectionImpl;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.impl.Edge;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.GraphBuilder;
import nl.tudelft.dnainator.graph.impl.Neo4jBatchBuilder;
import nl.tudelft.dnainator.parser.Iterator;
import nl.tudelft.dnainator.parser.TreeParser;
import nl.tudelft.dnainator.parser.exceptions.ParseException;
import nl.tudelft.dnainator.parser.impl.AnnotationIterator;
import nl.tudelft.dnainator.parser.impl.DRMutationIterator;
import nl.tudelft.dnainator.parser.impl.EdgeIterator;
import nl.tudelft.dnainator.parser.impl.NodeIterator;
import nl.tudelft.dnainator.tree.TreeNode;

import java.io.IOException;

import javafx.concurrent.Task;

/**
 * This class is instantiated by a {@link GraphLoadService in order to load a sequence graph.
 */
public class GraphLoadTask extends Task<Graph> {
	private static final int STEPS = 5;
	private GraphLoadService service;

	/**
	 * Create a new GraphLoadTask, which will be run by the specified service.
	 * @param service	the service
	 */
	public GraphLoadTask(GraphLoadService service) {
		this.service = service;
	}

	/**
	 * Construct an phylogeny tree by filling it with the newick files of the executing service.
	 * @return		the phylogeny tree
	 * @throws IOException	when the newick file was invalid
	 */
	private TreeNode constructTree() throws IOException {
		return new TreeParser(service.getNewickFile()).parse();
	}

	/**
	 * Construct an annotation collection by filling it with
	 * the annotation and DR mutation files of the executing service.
	 * @return		the filled annotation collection
	 * @throws IOException	when the annotation or mutation file was invalid
	 */
	private AnnotationCollection constructAnnotations() throws IOException {
		AnnotationCollection annotations;
		annotations = new AnnotationCollectionImpl(new AnnotationIterator(service.getGffFile()));
		if (service.getDrFile() != null) {
			annotations.addAnnotations(new DRMutationIterator(service.getDrFile()));
		}
		return annotations;
	}

	/**
	 * Construct a graph by filling the specified graph builder
	 * with the node and edge files of the executing service.
	 * @param builder	the graph builder
	 * @return		the constructed graph builder
	 * @throws IOException	when the edge or node file was invalid
	 */
	private GraphBuilder constructGraph(GraphBuilder builder) throws IOException {
		Iterator<SequenceNode> np = new NodeIterator(service.getNodeFile());
		Iterator<Edge<?>> ep = new EdgeIterator(service.getEdgeFile());
		return builder.constructGraph(np, ep);
	}
	
	@Override
	protected Graph call() throws IOException, ParseException {
		if (!service.canLoad()) {
			throw new IOException("Not all required fields are filled in!");
		}
		int progress = 0;

		TreeNode node = constructTree();
		updateProgress(++progress, STEPS);

		AnnotationCollection annotations = constructAnnotations();
		updateProgress(++progress, STEPS);

		// This constructs a graph builder, which is used to insert all nodes in one batch.
		// This has significantly improved loading times.
		GraphBuilder builder = new Neo4jBatchBuilder(service.getDatabase(), annotations, node);
		updateProgress(++progress, STEPS);

		constructGraph(builder);
		updateProgress(++progress, STEPS);

		Graph graph = builder.build();
		updateProgress(++progress, STEPS);

		return graph;
	}
}
