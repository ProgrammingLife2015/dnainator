package nl.tudelft.dnainator.parser.buffered;

import static java.lang.Integer.parseInt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import net.sf.jfasta.FASTAElement;
import net.sf.jfasta.FASTAFileReader;
import net.sf.jfasta.impl.FASTAElementIterator;
import net.sf.jfasta.impl.FASTAFileReaderImpl;
import nl.tudelft.dnainator.core.DefaultSequenceFactory;
import nl.tudelft.dnainator.core.SequenceFactory;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.parser.HeaderParser;
import nl.tudelft.dnainator.parser.exceptions.InvalidHeaderFormatException;

/**
 * A {@link BufferedNodeParser} which uses JFASTA's parser internally.
 */
public class JFASTANodeParser extends BufferedNodeParser {

	private FASTAElementIterator it;

	/**
	 * Constructs a new JFASTAParser.
	 *
	 * @param f  The {@link File} from which to read.
	 * @throws IOException If something goes wrong constructing.
	 */
	public JFASTANodeParser(File f) throws IOException {
		this(new DefaultSequenceFactory(), f);
	}

	/**
	 * Constructs a new JFASTAParser.
	 *
	 * @param f  The {@link File} from which to read.
	 * @param sf The {@link SequenceFactory} used to created {@link SequenceNode}s.
	 * @throws IOException If something goes wrong constructing.
	 */
	public JFASTANodeParser(SequenceFactory sf, File f) throws IOException {
		this(sf, new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8")));
	}

	/**
	 * Constructs a new JFASTAParser.
	 *
	 * @param br The {@link BufferedReader} from which to read.
	 * @param sf The {@link SequenceFactory} used to created {@link SequenceNode}s.
	 * @throws IOException If something goes wrong constructing.
	 */
	public JFASTANodeParser(SequenceFactory sf, BufferedReader br) throws IOException {
		super(sf, br);
		FASTAFileReader fr = new FASTAFileReaderImpl(this.br);
		it = fr.getIterator();
	}

	@Override
	public boolean hasNext() throws IOException {
		return it.hasNext();
	}

	@Override
	public SequenceNode next() throws IOException, InvalidHeaderFormatException {
		FASTAElement next = it.next();
		HeaderParser p = new HeaderParser(next.getHeader());
		return sf.build(p.next(), p.next(), parseInt(p.next()),
				parseInt(p.next()), next.getSequence());
	}

}