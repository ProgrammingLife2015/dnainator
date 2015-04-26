package nl.tudelft.dnainator.parser;

import nl.tudelft.dnainator.core.Sequence;
import nl.tudelft.dnainator.core.SequenceFactory;

public class ParseDemo {

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

	public static void main(String[] args) {
		GraphParser gp = new GraphParser("10_strains_graph/simple_graph",
										 new JFASTAParser(new PrintSequenceFactory()), null);
		try {
			gp.parse();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidHeaderFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
