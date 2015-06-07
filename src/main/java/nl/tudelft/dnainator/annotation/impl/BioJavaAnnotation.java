package nl.tudelft.dnainator.annotation.impl;

import org.biojava.nbio.genome.parsers.gff.Feature;

import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.annotation.Range;

/**
 * An adapter for BioJava's datastructure for parsed GFF annotations.
 */
public class BioJavaAnnotation implements Annotation {
	private Feature delegate;

	/**
	 * Create a new BioJavaAnnotation with the given delegate.
	 * @param feature the delegate.
	 */
	public BioJavaAnnotation(Feature feature) {
		this.delegate = feature;
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
	public String toString() {
		return delegate.getAttributes().toString();
	}
}
