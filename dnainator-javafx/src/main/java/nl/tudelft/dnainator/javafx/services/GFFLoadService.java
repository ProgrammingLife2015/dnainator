package nl.tudelft.dnainator.javafx.services;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import nl.tudelft.dnainator.annotation.AnnotationCollection;
import nl.tudelft.dnainator.annotation.impl.AnnotationCollectionFactoryImpl;
import nl.tudelft.dnainator.parser.AnnotationParser;
import nl.tudelft.dnainator.parser.impl.GFF3AnnotationParser;

import java.io.IOException;

/**
 * This class enables mutation loading to be executed in the background.
 */
public class GFFLoadService extends Service<AnnotationCollection> {
	private ObjectProperty<String> gffFilePath =
			new SimpleObjectProperty<>(this, "gffFilePath");

	/**
	 * Sets the GFF filename to the specified value.
	 * @param fileName The new filename.
	 */
	public final void setGffFilePath(String fileName) {
		gffFilePath.set(fileName);
	}

	/**
	 * @return The filename of the GFF file.
	 */
	public final String getGffFilePath() {
		return gffFilePath.get();
	}

	/**
	 * @return The GFF filename property.
	 */
	public ObjectProperty<String> gffFilePathProperty() {
		return gffFilePath;
	}

	@Override
	protected Task<AnnotationCollection> createTask() {
		return new Task<AnnotationCollection>() {
			@Override
			protected AnnotationCollection call() {
				AnnotationCollection annotations = null;
				try {
					AnnotationParser as = new GFF3AnnotationParser(gffFilePath.get());
					annotations = new AnnotationCollectionFactoryImpl().build(as);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return annotations;
			}
		};
	}
}
