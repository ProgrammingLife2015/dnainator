package nl.tudelft.dnainator.annotation.impl;

import java.util.Collection;
import java.util.Collections;

import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.annotation.Range;

/**
 * This class represents a drug resistance annotation on a gene annotation.
 */
public class DRMutation implements Annotation {
	private String geneName;
	private String type;
	private String change;
	private String filter;
	private int position;
	private String drug;

	/**
	 * Construct a new drug resistance annotation.
	 * @param geneName	the name of the gene on which this mutation occurs
	 * @param type		the type of mutation
	 * @param change	the change caused
	 * @param filter	the filter
	 * @param position	the start position of the mutation
	 * @param drug      the drug to which the gene is resistant
	 */
	public DRMutation(String geneName, String type, String change, String filter,
	                  int position, String drug) {
		this.geneName = geneName;
		this.type = type;
		this.change = change;
		this.filter = filter;
		this.position = position;
		this.drug = drug;
	}

	@Override
	public String getGeneName() {
		return geneName + " -> type: " + type + " change: " + change
				+ " position: " + position + " drug: " + drug;
	}

	@Override
	public Range getRange() {
		return new Range(position, position);
	}

	@Override
	public boolean isMutation() {
		return true;
	}

	@Override
	public boolean isSense() {
		return false;
	}

	@Override
	public String toString() {
		return geneName + ": " + type + " " + change + " " + filter + " " + position + " " + drug;
	}

	@Override
	public Collection<String> getAnnotatedNodes() {
		return Collections.emptyList();
	}
}
