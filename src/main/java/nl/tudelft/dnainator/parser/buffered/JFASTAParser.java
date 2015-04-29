package nl.tudelft.dnainator.parser.buffered;

import net.sf.jfasta.FASTAElement;
import net.sf.jfasta.FASTAFileReader;
import net.sf.jfasta.impl.FASTAElementIterator;
import net.sf.jfasta.impl.FASTAFileReaderImpl;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.SequenceFactory;
import nl.tudelft.dnainator.parser.HeaderParser;
import nl.tudelft.dnainator.parser.InvalidHeaderFormatException;

import java.io.BufferedReader;
import java.io.IOException;

import static java.lang.Integer.parseInt;

/**
 * A {@link BufferedNodeParser} which uses JFASTA's parser internally.
 */
public class JFASTAParser extends BufferedNodeParser {

	private FASTAFileReader fr;
	private FASTAElementIterator it;

	/**
	 * Constructs a new JFASTAParser.
	 *
	 * @param br The {@link BufferedReader} from which to read.
	 * @param sf The {@link SequenceFactory} used to created {@link SequenceNode}s.
	 * @throws IOException If something goes wrong constructing.
	 */
	public JFASTAParser(SequenceFactory sf, BufferedReader br) throws IOException {
		super(sf, br);
		fr = new FASTAFileReaderImpl(br);
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
		SequenceNode s = sf.build(p.next(), p.next(), parseInt(p.next()),
				parseInt(p.next()), next.getSequence());
		return s;
	}

}