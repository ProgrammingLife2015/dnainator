package nl.tudelft.dnainator.parser.impl;

import net.sf.jfasta.FASTAElement;
import net.sf.jfasta.FASTAFileReader;
import net.sf.jfasta.impl.FASTAElementIterator;
import net.sf.jfasta.impl.FASTAFileReaderImpl;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.SequenceNodeFactory;
import nl.tudelft.dnainator.core.impl.SequenceNodeFactoryImpl;
import nl.tudelft.dnainator.parser.BufferedIterator;
import nl.tudelft.dnainator.parser.HeaderParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Integer.parseInt;

/**
 * A buffered {@link NodeIterator} that parses node files.
 */
public class NodeIterator extends BufferedIterator<SequenceNode> {

	private FASTAElementIterator it;
	private SequenceNodeFactory sf;
	private FASTAFileReader fr;

	/**
	 * Constructs a {@link NodeIterator}, which reads from the given {@link File}.
	 * @param f  The {@link File} from which to read.
	 * @throws IOException	when file is not found or encoding is invalid
	 */
	public NodeIterator(File f) throws IOException {
		this(new SequenceNodeFactoryImpl(), f);
	}

	/**
	 * Constructs a {@link EdgeIterator}, which reads from the given {@link BufferedReader}.
	 * @param sf The {@link SequenceNodeFactory} used to created {@link SequenceNode}s.
	 * @param f  The {@link File} from which to read.
	 * @throws IOException	when file is not found or encoding is invalid
	 */
	public NodeIterator(SequenceNodeFactory sf, File f) throws IOException {
		this(sf, new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8")));
	}

	/**
	 * Constructs a new {@link NodeIterator}.
	 * @param br The {@link BufferedReader} from which to read.
	 * @param sf The {@link SequenceNodeFactory} used to created {@link SequenceNode}s.
	 * @throws IOException	when file is not found or encoding is invalid
	 */
	public NodeIterator(SequenceNodeFactory sf, BufferedReader br) throws IOException {
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
	public SequenceNode next() throws IOException {
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