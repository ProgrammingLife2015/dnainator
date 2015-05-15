package nl.tudelft.dnainator.parser;

import nl.tudelft.dnainator.tree.TreeNode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TreeParser {
	/* (:,); with lookahead and lookbehind to retain the delimiters.
	 * For example, (name:0.04,name2:0.04) is split to:
	  * "(", "name", ":", "0.04", ",", "name2", ":", "0.04", ")". */
	private static final String REGEX = "(((?<=;)|(?=;))|((?<=\\()|(?=\\())|((?<=\\))|(?=\\)))"
			+ "|((?<=:)|(?=:))|((?<=,)|(?=,)))";

	private File treeFile;
	private TreeNode root;
	private TreeNode current;
	private TreeNode child;

	public TreeParser(File treeFile) throws IOException {
		this.treeFile = treeFile;
		parse();
	}

	public TreeNode parse() throws IOException {
		root = new TreeNode(null);
		current = root;

		try (BufferedReader br = new BufferedReader(new FileReader(treeFile))) {
			return build(br.readLine());
		}
	}

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