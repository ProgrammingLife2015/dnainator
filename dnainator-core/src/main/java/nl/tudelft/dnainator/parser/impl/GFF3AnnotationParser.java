package nl.tudelft.dnainator.parser.impl;

import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.annotation.impl.BioJavaAnnotation;
import nl.tudelft.dnainator.parser.Parser;

import org.biojava.nbio.genome.parsers.gff.Feature;
import org.biojava.nbio.genome.parsers.gff.FeatureI;
import org.biojava.nbio.genome.parsers.gff.FeatureList;
import org.biojava.nbio.genome.parsers.gff.GFF3Reader;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An annotationparser which parses GFF3 files, using BioJava's
 * implementation internally.
 */
public class GFF3AnnotationParser implements Parser<Annotation> {
	private Iterator<FeatureI> delegate;
	private FeatureI next;
	private boolean needsNext = true;

	/**
	 * Constructs a new {@link Parser} reading from the file with the given filename.
	 * @param filename The filename of the file to parse the annotations from.
	 * @throws IOException When an error occurs reading the file.
	 */
	public GFF3AnnotationParser(String filename) throws IOException {
		FeatureList eager = GFF3Reader.read(filename);
		delegate = eager.iterator();
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
