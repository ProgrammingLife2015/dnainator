package nl.tudelft.dnainator.javafx;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

import java.util.BitSet;

/**
 * The {@link ColorServer} is responsible for managing colors used in the phylogenetic tree and the
 * DNA strains.
 */
public class ColorServer {
	private static final int COLORS = 21;
	private String[] colors;
	private ObservableMap<String, String> map;
	private BitSet occupied;

	/**
	 * Instantiates a new {@link ColorServer}.
	 */
	public ColorServer() {
		colors = new String[COLORS];
		map = FXCollections.observableHashMap();
		occupied = new BitSet(COLORS);
		setColors();
	}

	private void setColors() {
		for (int i = 0; i < COLORS; i++) {
			colors[i] = "color-" + i;
		}
	}

	/**
	 * Returns the style class associated to <code>source</code>, or <code>null</code>.
	 * @param source The source whose color to find.
	 * @return The style class associated to the source strain.
	 */
	public String getColor(String source) {
		if (map.containsKey(source)) {
			return map.get(source);
		} else {
			return null;
		}
	}

	/**
	 * @param source The source of the strain to highlight.
	 * @throws AllColorsInUseException If all colors are currently in use.
	 */
	public void askColor(String source) throws AllColorsInUseException {
		if (getColor(source) != null) {
			return;
		} else if (occupied.cardinality() == COLORS) {
			throw new AllColorsInUseException();
		}

		int index = occupied.nextClearBit(0);
		occupied.set(index, true);
		map.put(source, colors[index]);
	}

	/**
	 * Tells the {@link ColorServer} that the color belonging to the source is not used anymore.
	 * @param source The source whose color is now unused.
	 */
	public void revokeColor(String source) {
		if (!map.containsKey(source)) {
			return;
		}

		String color = map.remove(source);
		int index = Character.getNumericValue(color.charAt(color.length() - 1));
		occupied.set(index, false);
	}

	/**
	 * Adds a {@link MapChangeListener} to the {@link ObservableMap}.
	 * @param listener The {@link MapChangeListener} to add.
	 */
	public void addListener(MapChangeListener<String, String> listener) {
		map.addListener(listener);
	}
}
