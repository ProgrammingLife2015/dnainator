package nl.tudelft.dnainator.core;

/**
 * Interface that all sequences should implement.
 */
public interface Sequence {
	/**
	 * The id of this nucleotide sequence, for example it's node number.
	 * @return	the id of this sequence
	 */
	String getId();
	
	/**
	 * The assiocated nucleotide sequence.
	 * @return	the base sequence
	 */
	String getSequence();
	
	/**
	 * The associated start ref.
	 * @return	the start reference
	 */
	String getStart();
	
	/**
	 * The associated end ref.
	 * @return	the end reference
	 */
	String getEnd();
}
