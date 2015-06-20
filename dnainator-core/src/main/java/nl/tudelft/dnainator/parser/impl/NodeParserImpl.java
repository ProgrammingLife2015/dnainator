package nl.tudelft.dnainator.parser.impl;

import net.sf.jfasta.FASTAElement;
import net.sf.jfasta.FASTAFileReader;
import net.sf.jfasta.impl.FASTAElementIterator;
import net.sf.jfasta.impl.FASTAFileReaderImpl;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.SequenceNodeFactory;
import nl.tudelft.dnainator.core.impl.SequenceNodeFactoryImpl;
import nl.tudelft.dnainator.parser.BufferedParser;
import nl.tudelft.dnainator.parser.HeaderParser;
import nl.tudelft.dnainator.parser.exceptions.InvalidHeaderFormatException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Integer.parseInt;

/**
 * A {@link BufferedParser} for nodes which uses JFASTA's parser internally.
 */
public class NodeParserImpl extends BufferedParser<SequenceNode> {

	private FASTAElementIterator it;
	private SequenceNodeFactory sf;
	private FASTAFileReader fr;

	/**
	 * Constructs a new NodeParserImpl.
	 *
	 * @param f  The {@link File} from which to read.
	 * @throws IOException If something goes wrong constructing.
	 */
	public NodeParserImpl(File f) throws IOException {
		this(new SequenceNodeFactoryImpl(), f);
	}

	/**
	 * Constructs a new NodeParserImpl.
	 *
	 * @param f  The {@link File} from which to read.
	 * @param sf The {@link SequenceNodeFactory} used to created {@link SequenceNode}s.
	 * @throws IOException If something goes wrong constructing.
	 */
	public NodeParserImpl(SequenceNodeFactory sf, File f) throws IOException {
		this(sf, new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8")));
	}

	/**
	 * Constructs a new JFASTAParser.
	 *
	 * @param br The {@link BufferedReader} from which to read.
	 * @param sf The {@link SequenceNodeFactory} used to created {@link SequenceNode}s.
	 * @throws IOException If something goes wrong constructing.
	 */
	public NodeParserImpl(SequenceNodeFactory sf, BufferedReader br) throws IOException {
		super(br);
		this.sf = sf;
		fr = new FASTAFileReaderImpl(this.br);
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

	@Override
	public void close() throws IOException {
		fr.close();
		super.close();
	}
}