package nl.tudelft.dnainator.parser.impl;

import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.annotation.impl.BioJavaAnnotation;
import nl.tudelft.dnainator.parser.Iterator;

import org.biojava.nbio.genome.parsers.gff.Feature;
import org.biojava.nbio.genome.parsers.gff.FeatureI;
import org.biojava.nbio.genome.parsers.gff.FeatureList;
import org.biojava.nbio.genome.parsers.gff.GFF3Reader;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * An annotation iterator which parses GFF3 files, using BioJava's implementation internally.
 */
public class AnnotationIterator implements Iterator<Annotation> {
	private java.util.Iterator<FeatureI> delegate;
	private FeatureI next;
	private boolean needsNext = true;

	/**
	 * Constructs a {@link AnnotationIterator}, which reads from the given {@link File}.
	 * @param file	The file to read from.
	 * @throws IOException	when file is not found or encoding is invalid
	 */
	public AnnotationIterator(File file) throws IOException {
		FeatureList reader = GFF3Reader.read(file.getAbsolutePath());
		delegate = reader.iterator();
		next = null;
	}

	private boolean eatNoneCDS() {
		do {
			if (!delegate.hasNext()) {
				next = null;
				return false;
			}
			next = delegate.next();
		} while (!next.type().equals("CDS"));
		return true;
	}

	@Override
	public boolean hasNext() {
		if (needsNext) {
			needsNext = false;
			return eatNoneCDS();
		}
		return next != null;
	}

	@Override
	public Annotation next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		needsNext = true;
		return new BioJavaAnnotation((Feature) next);
	}

	@Override
	public void close() throws IOException { }
}
