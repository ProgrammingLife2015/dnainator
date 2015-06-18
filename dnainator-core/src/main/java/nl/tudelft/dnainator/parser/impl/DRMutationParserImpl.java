package nl.tudelft.dnainator.parser.impl;

import nl.tudelft.dnainator.annotation.impl.DRMutation;
import nl.tudelft.dnainator.parser.DRMutationParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;

/**
 * An implementation for parsing the known drug resistant mutations file.
 */
public class DRMutationParserImpl implements DRMutationParser {
	private static final int TYPE_IDX = 0;
	private static final int CHANGE_IDX = 1;
	private static final int FILTER_IDX = 2;
	private static final int POSITION_IDX = 3;

	private BufferedReader br;
	private DRMutation current;
	private boolean needParse = true;

	/**
	 * Constructs a {@link DRMutation}, which reads from the given file.
	 * @param file The file to read from.
	 * @throws IOException when the file is not found or invalid.
	 */
	public DRMutationParserImpl(File file) throws IOException {
		br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		current = null;
		needParse = true;
	}

	@Override
	public boolean hasNext() throws IOException {
		if (needParse) {
			current = parse();
			needParse = false;
		}
		return current != null;
	}

	private DRMutation parse() throws IOException {
		String curLine = br.readLine();
		if (curLine == null) {
			return null;
		}
		/* Skip comments. */
		if (curLine.startsWith("##")) {
			return next();
		}

		int tab = curLine.indexOf('\t');
		int colon = curLine.indexOf(':');

		String gene = curLine.substring(0, tab);
		String drug = curLine.substring(tab + 1);

		String geneName = gene.substring(0, colon);
		String[] geneInfo = gene.substring(colon + 1).split(",");
		
		return new DRMutation(geneName, geneInfo[TYPE_IDX], geneInfo[CHANGE_IDX],
				geneInfo[FILTER_IDX], Integer.parseInt(geneInfo[POSITION_IDX]), drug);
	}

	@Override
	public DRMutation next() throws IOException {
		if (hasNext()) {
			needParse = true;
			return current;
		}
		throw new NoSuchElementException();
	}

	@Override
	public void close() throws IOException {
		br.close();
	}
}
