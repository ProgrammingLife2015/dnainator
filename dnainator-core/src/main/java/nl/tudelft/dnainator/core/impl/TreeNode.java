package nl.tudelft.dnainator.core.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an item in the phylogenetic tree. The
 * {@link nl.tudelft.dnainator.parser.impl.TreeParser} returns the root item, via which all
 * children can be accessed.
 */
public class TreeNode {
	private List<TreeNode> children;
	private double distance;
	private String name;
	private TreeNode parent;

	/**
	 * Creates a new TreeNode.
	 * @param parent The parent of this TreeNode.
	 */
	public TreeNode(TreeNode parent) {
		setParent(parent);
		children = new ArrayList<>();
	}

	/**
	 * Adds a child to this TreeNode.
	 * @param child The child to add.
	 */
	public void addChild(TreeNode child) {
		children.add(child);
	}

	/**
	 * @return The children of this TreeNode.
	 */
	public List<TreeNode> getChildren() {
		return children;
	}

	/**
	 * @return The distance of this TreeNode.
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * @return The name of this TreeNode.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The parent of this TreeNode, or null if there is no parent.
	 */
	public TreeNode getParent() {
		return parent;
	}

	/**
	 * Sets the distance of this TreeNode.
	 * @param distance The new distance.
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * Sets the name of this TreeNode.
	 * @param name The new name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the parent of this TreeNode and adds this TreeNode to its children if it is not null.
	 * @param parent The parent TreeNode.
	 */
	public void setParent(TreeNode parent) {
		this.parent = parent;
		if (parent != null) {
			this.parent.addChild(this);
		}
	}
}
