package nl.tudelft.dnainator.ui;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@link ColorServer} is responsible for managing colors used in the phylogenetic tree and the
 * DNA strains.
 *
 * It keeps a {@link HashMap} of strain sources to CSS classes and uses a {@link BitSet} as a list
 * of occupied colors.
 */
public final class ColorServer {
	private static final String COLOR = "color-";
	private static final int COLORS = 21;
	private static ColorServer instance = new ColorServer();
	private	Map<String, String> colors;
	private BitSet occupied;

	private ColorServer() {
		colors = new HashMap<>(COLORS);
		occupied = new BitSet(COLORS);
	}

	/**
	 * @param source The source of the strain to highlight.
	 * @return The CSS class for an unused color.
	 * @throws AllColorsInUseException If all colors are currently in use.
	 */
	public String getColor(String source) throws AllColorsInUseException {
		if (colors.containsKey((source))) {
			return colors.get(source);
		} else if (occupied.cardinality() == COLORS) {
			throw new AllColorsInUseException();
		}

		int index = occupied.nextClearBit(0);
		occupied.set(index, true);
		String res = COLOR + index;
		colors.put(source, res);
		return res;
	}

	/**
	 * Tells the {@link ColorServer} that the color belonging to the source is not used anymore.
	 * @param source The source whose color is now unused.
	 */
	public void revokeColor(String source) {
		if (!colors.containsKey(source)) {
			return;
		}

		String color = colors.remove(source);
		int index = Character.getNumericValue(color.charAt(color.length() - 1));
		occupied.set(index, false);
	}

	/**
	 * @return The {@link ColorServer} instance.
	 */
	public static ColorServer getInstance() {
		return instance;
	}
}
