package nl.tudelft.dnainator.parser.impl;

import net.sf.jfasta.FASTAElement;
import net.sf.jfasta.FASTAFileReader;
import net.sf.jfasta.impl.FASTAElementIterator;
import net.sf.jfasta.impl.FASTAFileReaderImpl;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.SequenceNodeFactory;
import nl.tudelft.dnainator.core.impl.SequenceNodeFactoryImpl;
import nl.tudelft.dnainator.parser.BufferedNodeParser;
import nl.tudelft.dnainator.parser.HeaderParser;
import nl.tudelft.dnainator.parser.exceptions.InvalidHeaderFormatException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Integer.parseInt;

/**
 * A {@link BufferedNodeParser} which uses JFASTA's parser internally.
 */
public class NodeParserImpl extends BufferedNodeParser {

	private FASTAElementIterator it;

	/**
	 * Constructs a new NodeParserImpl.
	 *
	 * @param path  The path from which to read.
	 * @throws IOException If something goes wrong constructing.
	 */
	public NodeParserImpl(String path) throws IOException {
		this(new SequenceNodeFactoryImpl(), path);
	}

	/**
	 * Constructs a new NodeParserImpl.
	 *
	 * @param path  The path from which to read.
	 * @param sf The {@link SequenceNodeFactory} used to created {@link SequenceNode}s.
	 * @throws IOException If something goes wrong constructing.
	 */
	public NodeParserImpl(SequenceNodeFactory sf, String path) throws IOException {
		this(sf, new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8")));
	}

	/**
	 * Constructs a new JFASTAParser.
	 *
	 * @param br The {@link BufferedReader} from which to read.
	 * @param sf The {@link SequenceNodeFactory} used to created {@link SequenceNode}s.
	 * @throws IOException If something goes wrong constructing.
	 */
	public NodeParserImpl(SequenceNodeFactory sf, BufferedReader br) throws IOException {
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