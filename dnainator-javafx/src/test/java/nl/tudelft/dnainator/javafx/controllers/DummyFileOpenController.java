package nl.tudelft.dnainator.javafx.controllers;

import org.mockito.Mockito;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a dummy class, for testing purposes.
 */
public class DummyFileOpenController extends FileOpenController {
	private Map<String, String> extensionmap;

	/**
	 * Constructs a new dummy file open controller, used for testing.
	 */
	public DummyFileOpenController() {
		extensionmap = new HashMap<>();
		extensionmap.put(FileOpenController.NODE, "test.node.graph");
		extensionmap.put(FileOpenController.EDGE, "test.edge.graph");
		extensionmap.put(FileOpenController.NEWICK, "test.nwk");
		extensionmap.put(FileOpenController.GFF, "test.gff");
		extensionmap.put(FileOpenController.DR, "test.txt");
	}

	@Override
	public File showFileChooser(String title, String extension) {
		return createMock(extension);
	}

	@Override
	public File openNodeFile(String path) {
		return createMock(FileOpenController.NODE);
	}

	@Override
	public File openEdgeFile(String path) {
		return createMock(FileOpenController.EDGE);
	}

	private File createMock(String extension) {
		File file = Mockito.mock(File.class);
		Mockito.when(file.getAbsolutePath()).thenReturn(extensionmap.get(extension));
		return file;
	}

	/**
	 * Return the extensionmap.
	 * @return the extension map
	 */
	public Map<String, String> getMap() {
		return extensionmap;
	}
}
