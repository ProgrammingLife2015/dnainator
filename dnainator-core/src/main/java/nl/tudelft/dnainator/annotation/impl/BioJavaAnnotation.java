package nl.tudelft.dnainator.annotation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.annotation.DRMutation;
import nl.tudelft.dnainator.annotation.Range;

import org.biojava.nbio.genome.parsers.gff.Feature;

/**
 * An adapter for BioJava's datastructure for parsed GFF annotations.
 */
public class BioJavaAnnotation implements Annotation {
	private Feature delegate;
	private Collection<DRMutation> mutations;

	/**
	 * Create a new BioJavaAnnotation with the given delegate.
	 * @param feature the delegate.
	 */
	public BioJavaAnnotation(Feature feature) {
		this.delegate = feature;
		this.mutations = new ArrayList<>();
	}

	@Override
	public String getGeneName() {
		return delegate.getAttribute("displayName");
	}

	@Override
	public Range getRange() {
		return new Range(getStart(), getEnd());
	}

	@Override
	public int getStart() {
		/* TODO: should this be
		 * 1) bioStart
		 * 2) start
		 * 3) getBegin
		 */
		// Subtract by one, since coordinates start at 0.
		return delegate.location().bioStart() - 1;
	}

	@Override
	public int getEnd() {
		return delegate.location().bioEnd() - 1;
	}

	@Override
	public boolean isSense() {
		return delegate.location().bioStrand() == '+';
	}

	@Override
	public Collection<String> getAnnotatedNodes() {
		return Collections.emptyList();
	}

	@Override
	public String toString() {
		return delegate.getAttributes().toString();
	}

	@Override
	public void addDRMutation(DRMutation dr) {
		mutations.add(dr);
	}

	@Override
	public Collection<DRMutation> getDRMutations() {
		return mutations;
	}
}
