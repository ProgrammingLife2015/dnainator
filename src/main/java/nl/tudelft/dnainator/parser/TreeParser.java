package nl.tudelft.dnainator.parser;

import nl.tudelft.dnainator.tree.TreeNode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class parses newick files to {@link TreeNode} items. It returns a {@link TreeNode}
 * that is the root of the phylogenetic tree.
 */
public class TreeParser {
	/* (:,); with lookahead and lookbehind to retain the delimiters.
	 * For example, (name:0.04,name2:0.04) is split to:
	 * "(", "name", ":", "0.04", ",", "name2", ":", "0.04", ")".
	 */
	private static final String REGEX = "(((?<=;)|(?=;))|((?<=\\()|(?=\\())|((?<=\\))|(?=\\)))"
			+ "|((?<=:)|(?=:))|((?<=,)|(?=,)))";

	private File treeFile;
	private TreeNode root;
	private TreeNode current;
	private TreeNode child;

	/**
	 * Creates a new TreeParser, that will parse the passed file.
	 * @param treeFile The file to parse.
	 * @throws IOException Thrown if parsing throws an exception.
	 */
	public TreeParser(File treeFile) throws IOException {
		this.treeFile = treeFile;
		parse();
	}

	/**
	 * Parses the newick file to a {@link TreeNode} that represents a phylogenetic tree.
	 * @return The root of the phylogenetic tree.
	 * @throws IOException Thrown if the file is invalid.
	 */
	public TreeNode parse() throws IOException {
		root = new TreeNode(null);
		current = root;

		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(treeFile), "UTF-8"))) {
			return build(br.readLine());
		}
	}

	/**
	 * Builds a {@link TreeNode} that represents the root of the phylogenetic tree.
	 * @param s The {@link String} to parse.
	 * @return The root of the phylogenetic tree.
	 */
	public TreeNode build(String s) {
		String[] tokens = s.split(REGEX);
		boolean isDistance = false;

		for (String token : tokens) {
			if (isDistance) {
				isDistance = false;
				current.setDistance(Double.parseDouble(token));
			} else {
				isDistance = processToken(token);
			}
		}
		return root;
	}

	private boolean processToken(String token) {
		switch (token) {
			case "(":
				child = new TreeNode(current);
				current = child;
				return false;
			case ":":
				return true;
			case ",":
				child = new TreeNode(current.getParent());
				current = child;
				return false;
			case ")":
				current = current.getParent();
				return false;
			case ";":
				return false;
			default:
				current.setName(token);
				return false;
		}
	}
}