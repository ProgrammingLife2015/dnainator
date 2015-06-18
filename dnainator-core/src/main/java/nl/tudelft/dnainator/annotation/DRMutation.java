package nl.tudelft.dnainator.annotation;

/**
 * This class represents a drug resistance annotation on a gene annotation.
 */
public class DRMutation {
	private String geneName;
	private String type;
	private String change;
	private String filter;
	private int position;

	/**
	 * Construct a new drug resistance annotation.
	 * @param geneName	the name of the gene on which this mutation occurs
	 * @param type		the type of mutation
	 * @param change	the change caused
	 * @param filter	the filter
	 * @param position	the start position of the mutation
	 */
	public DRMutation(String geneName, String type, String change, String filter, int position) {
		this.geneName = geneName;
		this.type = type;
		this.change = change;
		this.filter = filter;
		this.position = position;
	}

	/**
	 * @return the geneName
	 */
	public String getGeneName() {
		return geneName;
	}

	/**
	 * @param geneName the geneName to set
	 */
	public void setGeneName(String geneName) {
		this.geneName = geneName;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the change
	 */
	public String getChange() {
		return change;
	}

	/**
	 * @return the filter
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}
}
