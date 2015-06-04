package nl.tudelft.dnainator.parser.impl;

import java.io.IOException;
import java.util.Iterator;

import org.biojava.nbio.genome.parsers.gff.Feature;
import org.biojava.nbio.genome.parsers.gff.FeatureI;
import org.biojava.nbio.genome.parsers.gff.FeatureList;
import org.biojava.nbio.genome.parsers.gff.GFF3Reader;

import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.annotation.impl.BioJavaAnnotation;
import nl.tudelft.dnainator.parser.AnnotationParser;

/**
 * An annotationparser which parses GFF3 files, using BioJava's
 * implementation internally.
 */
public class GFF3AnnotationParser implements AnnotationParser {
	private Iterator<FeatureI> delegate;
	private FeatureI next;

	/**
	 * Constructs a new {@link AnnotationParser} reading from the file with
	 * the given filename.
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
				return false;
			}
			next = delegate.next();
		} while (!next.type().equals("CDS"));
		return true;
	}

	@Override
	public boolean hasNext() {
		return eatNoneCDS();
	}

	@Override
	public Annotation next() {
		return new BioJavaAnnotation((Feature) next);
	}

	/**
	 * @param args args.
	 * @throws IOException exception.
	 */
	public static void main(String[] args) throws IOException {
		AnnotationParser p = new GFF3AnnotationParser("decorationV5_20130412.gff");
		while (p.hasNext()) {
			Annotation next = p.next();
			if (next.getGeneName().contains("0001")) {
				System.out.println(next.getStart());
			}
		}
	}
}
