package nl.tudelft.dnainator.parser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jfasta.FASTAElement;
import net.sf.jfasta.FASTAFileReader;
import net.sf.jfasta.impl.FASTAElementIterator;
import net.sf.jfasta.impl.FASTAFileReaderImpl;
import nl.tudelft.dnainator.core.Sequence;
import nl.tudelft.dnainator.core.SequenceFactory;

/**
 * A {@link NodeParser} which uses JFASTA's parser internally.
 */
public class JFASTAParser extends NodeParser {

	/**
	 * Constructs a new JFASTAParser.
	 * @param sf The {@link SequenceFactory} used to created {@link Sequence}s.
	 */
	public JFASTAParser(SequenceFactory sf) {
		super(sf);
	}

	public Map<String, Sequence> parse(File f)
			throws NumberFormatException, InvalidHeaderFormatException {
		try (FASTAFileReader fr = new FASTAFileReaderImpl(f)) {
			Map<String, Sequence> result = new HashMap<String, Sequence>();
			FASTAElementIterator it = fr.getIterator();
			while (it.hasNext()) {
				FASTAElement next = it.next();
				HeaderParser p = new HeaderParser(next.getHeader());
				sf.setContent(next.getSequence());
				result.put(p.next(), p.fill(sf));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
