package nl.tudelft.dnainator.annotation;

import nl.tudelft.dnainator.annotation.impl.DRMutation;
import nl.tudelft.dnainator.parser.Parser;

import java.io.IOException;

/**
 * This factory adds the known drug resistant mutations to the {@link AnnotationCollection}.
 */
public class DRMutationFactory {

	/**
	 * Parses and adds the known drug resistant mutations to the passed
	 * {@link AnnotationCollection}.
	 * @param annotations The annotations to which the {@link DRMutation}s are added.
	 * @param parser The parser that parses the drug resistant mutations file.
	 * @return The {@link AnnotationCollection} with the drug resistant mutations added to it.
	 */
	public AnnotationCollection build(AnnotationCollection annotations, Parser<DRMutation> parser) {
		try {
			while (parser.hasNext()) {
				DRMutation m = parser.next();
				annotations.addAnnotation(m);
			}
			parser.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return annotations;
	}
}
