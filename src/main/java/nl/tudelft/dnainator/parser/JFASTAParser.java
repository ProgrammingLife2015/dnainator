package nl.tudelft.dnainator.parser;

import net.sf.jfasta.FASTAElement;
import net.sf.jfasta.FASTAFileReader;
import net.sf.jfasta.impl.FASTAElementIterator;
import net.sf.jfasta.impl.FASTAFileReaderImpl;
import nl.tudelft.dnainator.core.Sequence;
import nl.tudelft.dnainator.core.SequenceFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link NodeParser} which uses JFASTA's parser internally.
 */
public class JFASTAParser extends NodeParser {

	/**
	 * Constructs a new JFASTAParser.
	 *
	 * @param sf The {@link SequenceFactory} used to created {@link Sequence}s.
	 */
	public JFASTAParser(SequenceFactory sf) {
		super(sf);
	}

	@Override
	public Map<String, Sequence> parse(FileInputStream fIn)
			throws NumberFormatException, InvalidHeaderFormatException {
		try (FASTAFileReader fr = new FASTAFileReaderImpl(fIn)) {
			Map<String, Sequence> result = new HashMap<>();
			FASTAElementIterator it = fr.getIterator();
			while (it.hasNext()) {
				FASTAElement next = it.next();
				HeaderParser p = new HeaderParser(next.getHeader());
				sf.setContent(next.getSequence());
				result.put(p.next(), p.fill(sf));
			}
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
