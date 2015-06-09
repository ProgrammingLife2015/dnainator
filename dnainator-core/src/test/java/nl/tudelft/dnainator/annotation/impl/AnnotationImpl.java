package nl.tudelft.dnainator.annotation.impl;

import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.annotation.Range;

/**
 * A default implementation of an Annotation.
 */
public class AnnotationImpl implements Annotation {
	private String name;
	private Range range;
	private boolean sense;

	/**
	 * Construct a new annotation with the given parameters.
	 * @param name the name of the gene.
	 * @param start the start coordinate.
	 * @param end the end coordinate.
	 * @param sense whether this gene is sense or not (positive/negative)
	 */
	public AnnotationImpl(String name, int start, int end, boolean sense) {
		this(name, new Range(start, end), sense);
	}

	/**
	 * Construct a new annotation with the given parameters.
	 * @param name the name of the gene.
	 * @param range the range of this annotation (start and end coordinates).
	 * @param sense whether this gene is sense or not (positive/negative)
	 */
	public AnnotationImpl(String name, Range range, boolean sense) {
		this.name = name;
		this.range = range;
		this.sense = sense;
	}

	@Override
	public String getGeneName() {
		return name;
	}

	@Override
	public Range getRange() {
		return range;
	}

	@Override
	public boolean isSense() {
		return sense;
	}

	@Override
	public String toString() {
		return "<AnnotationImpl " + name + ", " + range.toString() + ">";
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Annotation)) {
			return false;
		}
		Annotation a = (Annotation) other;
		return a.getGeneName().equals(this.getGeneName()) && a.getStart() == this.getStart()
				&& a.getEnd() == this.getEnd() && a.isSense() == this.isSense();
	}

	@Override
	public int hashCode() {
		return getGeneName().hashCode() + getStart() + getEnd() + ((Boolean) isSense()).hashCode();
	}
}
