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
	private String drug;

	/**
	 * Construct a new drug resistance annotation.
	 * @param geneName	the name of the gene on which this mutation occurs
	 * @param type		the type of mutation
	 * @param change	the change caused
	 * @param filter	the filter
	 * @param position	the start position of the mutation
	 * @param drug      the drug to which the gene is resistant
	 */
	public DRMutation(String geneName, String type, String change, String filter,
	                  int position, String drug) {
		this.geneName = geneName;
		this.type = type;
		this.change = change;
		this.filter = filter;
		this.position = position;
		this.drug = drug;
	}

	/**
	 * @return the geneName
	 */
	public String getGeneName() {
		return geneName;
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

	/**
	 * @return the drug
	 */
	public String getDrug() {
		return drug;
	}

	@Override
	public String toString() {
		return geneName + ": " + type + " " + change + " " + filter + " " + position + " " + drug;
	}
}
