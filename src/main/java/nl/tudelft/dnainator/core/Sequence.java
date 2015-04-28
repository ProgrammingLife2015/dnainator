package nl.tudelft.dnainator.core;

/**
 * Interface that all sequences should implement.
 */
public interface Sequence {
	/**
	 * The id of this nucleotide sequence, for example it's node number.
	 * @return	the id of this sequence
	 */
	int getId();
	
	/**
	 * The associated source.
	 * @return	the source
	 */
	String getSource();
	
	/**
	 * The associated start ref.
	 * @return	the start reference
	 */
	int getStartRef();
	
	/**
	 * The associated end ref.
	 * @return	the end reference
	 */
	int getEndRef();
	
	/**
	 * The assiocated nucleotide sequence.
	 * @return	the base sequence
	 */
	String getSequence();
}
