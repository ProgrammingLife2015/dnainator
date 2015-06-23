package nl.tudelft.dnainator.javafx.services;


import nl.tudelft.dnainator.javafx.DNAinator;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.StreamSupport;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * A JavaFX service that loads all the directories from a specific path.
 */
public class DirectoryLoadService extends Service<ObservableList<String>> {
	private static final String CORE = "neostore";
	private ObjectProperty<String> directory = new SimpleObjectProperty<>(this, "database");

	/**
	 * Construct a DirectoryLoadService with a default directory path.
	 */
	public DirectoryLoadService() {
		setDirectory(DNAinator.DEFAULT_DB_PATH);
	}

	/**
	 * @param scanpath	The directory to scan.
	 */
	public final void setDirectory(String scanpath) {
		directory.set(scanpath);
	}

	/**
	 * @return The directory to scan, if any.
	 */
	public final String getDirectory() {
		return directory.get();
	}

	/**
	 * @return The directory property.
	 */
	public ObjectProperty<String> directoryProperty() {
		return directory;
	}
	
	/**
	 * Scan the directory containing the default location of databases.
	 * If the default directory does not exist, create it.
	 * Adds all the directories found to the welcomescreen's list of selectables.
	 * @throws IOException 
	 */
	private ObservableList<String> scanDirectory(String dbpath) throws IOException {
		ObservableList<String> databases = FXCollections.observableArrayList();
		if (!Files.exists(Paths.get(dbpath)) && new File(dbpath).mkdirs()) {
			return databases;
		} else {
			try (DirectoryStream<Path> ds = Files.newDirectoryStream(Paths.get(dbpath))) {
				StreamSupport.stream(ds.spliterator(), false)
					.filter(Files::isDirectory)
					.filter(e -> Files.exists(Paths.get(e.toString() + File.separator + CORE)))
					.forEach(e -> databases.add(e.toString()));
			} catch (IOException e) {
				throw new IOException("Failed to retrieve databases.");
			}
		}
		databases.sort((e1, e2) -> e1.compareTo(e2));
		return databases;
	}

	@Override
	protected Task<ObservableList<String>> createTask() {
		return new Task<ObservableList<String>>() {
			@Override
			protected ObservableList<String> call() throws IOException {
				return scanDirectory(getDirectory());
			}
		};
	}
}
