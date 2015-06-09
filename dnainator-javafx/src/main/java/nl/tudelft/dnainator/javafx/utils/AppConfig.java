package nl.tudelft.dnainator.javafx.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Utility class to save and load properties from the properties file.
 */
public final class AppConfig {
	private static AppConfig instance = new AppConfig();
	private static final String PROPERTIES = "/config.properties";
	private static final String NODE_KEY = "nodeFile";
	private static final String EDGE_KEY = "edgeFile";
	private static final String NEWICK_KEY = "newickFile";
	private static final String GFF_KEY = "gffFile";

	private Properties appProperties;
	private File propertyFile;

	/* Initialises the properties and loads existing ones. */
	private AppConfig() {
		appProperties = new Properties();
		propertyFile = new File(getClass().getResource(PROPERTIES).getFile());

		try (InputStream in = new FileInputStream(propertyFile)) {
			appProperties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the path for the opened node file. This method does not call {@link #flush()}!
	 * @param path The path for the node file.
	 */
	public void setNodePath(String path) {
		appProperties.setProperty(NODE_KEY, path);
	}

	/**
	 * Sets the path for the opened edge file. This method does not call {@link #flush()}!
	 * @param path The path for the edge file.
	 */
	public void setEdgePath(String path) {
		appProperties.setProperty(EDGE_KEY, path);
	}

	/**
	 * Sets the path for the opened newick file. This method does not call {@link #flush()}!
	 * @param path The path for the newick file.
	 */
	public void setNewickPath(String path) {
		appProperties.setProperty(NEWICK_KEY, path);
	}

	/**
	 * Sets the path for the opened gff file. This method does not call {@link #flush()}!
	 * @param path The path for the gff file.
	 */
	public void setGffPath(String path) {
		appProperties.setProperty(GFF_KEY, path);
	}

	/**
	 * Sets the property named <code>propertyKey</code> to <code>propertyValue</code>.
	 * This method does not call {@link #flush()}!
	 * @param propertyKey The name of the property.
	 * @param propertyValue The value of the property.
	 */
	public void setProperty(String propertyKey, String propertyValue) {
		appProperties.setProperty(propertyKey, propertyValue);
	}

	/**
	 * @return The path of the node file, or <code>null</code> if no path is stored.
	 */
	public String getNodePath() {
		return appProperties.getProperty(NODE_KEY);
	}

	/**
	 * @return The path of the edge file, or <code>null</code> if no path is stored.
	 */
	public String getEdgePath() {
		return appProperties.getProperty(EDGE_KEY);
	}

	/**
	 * @return The path of the newick file, or <code>null</code> if no path is stored.
	 */
	public String getNewickPath() {
		return appProperties.getProperty(NEWICK_KEY);
	}

	/**
	 * @return The path of the gff file, or <code>null</code> if no path is stored.
	 */
	public String getGffPath() {
		return appProperties.getProperty(GFF_KEY);
	}

	/**
	 * Returns the property saved as <code>propertyKey</code>.
	 * @param propertyKey The name of the property to look up.
	 * @return The value of the passed property key, or <code>null</code> if no such property
	 * is stored.
	 */
	public String getProperty(String propertyKey) {
		return appProperties.getProperty(propertyKey);
	}

	/**
	 * Flushes the properties to the properties file.
	 */
	public void flush() {
		try (OutputStream out = new FileOutputStream(propertyFile)) {
			appProperties.store(out, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return The instance of this class.
	 */
	public static AppConfig getInstance() {
		return instance;
	}
}
