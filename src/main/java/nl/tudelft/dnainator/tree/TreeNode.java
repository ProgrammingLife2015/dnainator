package nl.tudelft.dnainator.tree;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
	private List<TreeNode> children;
	private double distance;
	private int id;
	private String name;
	private TreeNode parent;

	/**
	 * Creates a new TreeNode.
	 */
	public TreeNode(TreeNode parent) {
		setParent(parent);
		children = new ArrayList<>();
	}

	public void addChild(TreeNode child) {
		children.add(child);
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public double getDistance() {
		return distance;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public TreeNode getParent() {
		return parent;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
		if (parent != null) {
			this.parent.addChild(this);
		}
	}

	@Override
	public String toString() {
		int numParents = 0;
		TreeNode parent = getParent();
		while (parent != null) {
			parent = parent.getParent();
			numParents++;
		}

		String res = "";
		for (int i = 0; i < numParents; i++) {
			res += '\t';
		}
		res += name + " with children: \n";
		for (TreeNode child : children) {
			res += child.toString();
		}
		return res;
	}
}
