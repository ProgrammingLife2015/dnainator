package nl.tudelft.dnainator.parser;

import nl.tudelft.dnainator.core.Sequence;
import nl.tudelft.dnainator.core.SequenceFactory;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * An implementation for parser testing purposes. Should not be used.
 */
final class ParseDemo {

	private ParseDemo() {
	}

	/**
	 * Prints the sequence to standard output, doesn't build anything.
	 */
	static class PrintSequenceFactory implements SequenceFactory {

		@Override
		public void setContent(String content) {
			System.out.println(content);
		}

		@Override
		public Sequence build(String refs, int startPos, int endPos) {
			System.out.println("Refs: " + refs + ", start: " + startPos + ", end: " + endPos);
			return null;
		}
	}

	/**
	 * Main method.
	 *
	 * @param args Command line arguments. Not used.
	 */
	public static void main(String[] args) {
		try {
			GraphParser gp = new GraphParser("10_strains_graph/simple_graph",
					new JFASTAParser(new PrintSequenceFactory()), null);
			gp.parse();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidHeaderFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
