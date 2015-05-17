package nl.tudelft.dnainator.ui.views;

import java.io.File;
import java.io.IOException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import nl.tudelft.dnainator.graph.impl.Neo4jSingleton;
import nl.tudelft.dnainator.ui.models.GraphItem;
import nl.tudelft.dnainator.ui.models.ModelItem;
import nl.tudelft.dnainator.ui.widgets.Propertyable;
import nl.tudelft.dnainator.ui.widgets.contexts.ViewContext;
import nl.tudelft.dnainator.ui.widgets.dialogs.ExceptionDialog;

import org.neo4j.io.fs.FileUtils;

/**
 * This class is the View part of the MVC pattern.
 */
public class View extends Pane {
	private static final double SCALE = .1;
	private ObjectProperty<Propertyable> lastClicked;
	private Affine scale;
	private Translate toCenter;
	private Translate translate;
	private ModelItem mi;

	/**
	 * Creates a new view instance.
	 */
	public View() {
		try {
			FileUtils.deleteRecursively(new File(Neo4jSingleton.DB_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}

		loadFXML();
		getStyleClass().add("view");
		setOnContextMenuRequested(e -> {
			ViewContext.getInstance().show(View.this, e.getScreenX(), e.getScreenY());
			e.consume();
		});

		lastClicked = new SimpleObjectProperty<>(this, "lastClicked");

		toCenter = new Translate();
		widthProperty().addListener((o, v1, v2) -> toCenter.setX(v2.intValue() / 2));
		heightProperty().addListener((o, v1, v2) -> toCenter.setY(v2.intValue() / 2));

		translate = new Translate();
		scale = new Affine(new Scale(SCALE, SCALE));

		mi = new GraphItem();
		mi.getTransforms().add(toCenter);
		mi.getTransforms().add(translate);
		mi.getTransforms().add(scale);
		getChildren().add(mi);
	}

	private void loadFXML() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/fxml/view.fxml"));
		fxmlLoader.setRoot(this);

		try {
			fxmlLoader.load();
		} catch (IOException e) {
			new ExceptionDialog(null, e, "Can not load the View!");
		}
	}

	/**
	 * Returns the concatenated transform from world coordinates to camera coordinates.
	 * @return	the concatenated transform
	 */
	private Transform worldToCamera() {
		return toCenter.createConcatenation(translate).createConcatenation(scale);
	}

	/**
	 * Transforms a given bounding box from camera coordinates to world coordinates.
	 * @param b	the given bounding box
	 * @return	the transformed bounding box
	 */
	public Bounds cameraToWorld(Bounds b) {
		Bounds world = null;
		try {
			world = worldToCamera().inverseTransform(b);
		} catch (NonInvertibleTransformException e) {
			e.printStackTrace();
		}
		return world;
	}

	/**
	 * Pan the camera by the amount given by the delta vector.
	 * @param delta	the delta vector
	 */
	public void pan(Point2D delta) {
		translate.setX(translate.getX() + delta.getX());
		translate.setY(translate.getY() + delta.getY());
		mi.update(cameraToWorld(getLayoutBounds()));
	}

	/**
	 * Zoom the camera by the amount given by zoom.
	 * @param zoom		the amount to zoom in
	 * @param center	the center of the zoom, in camera space
	 */
	public void zoom(Double zoom, Point2D center) {
		try {
			center = scale.inverseTransform(center.getX() - toCenter.getX() - translate.getX(),
							center.getY() - toCenter.getY() - translate.getY());
			scale.append(new Scale(1 + zoom, 1 + zoom, center.getX(), center.getY()));
		} catch (NonInvertibleTransformException e) {
			e.printStackTrace();
		}
		mi.update(cameraToWorld(getLayoutBounds()));
	}

	/**
	 * @param p The last clicked {@link Propertyable}.
	 */
	public final void setLastClicked(Propertyable p) {
		lastClicked.set(p);
	}

	/**
	 * @return The last clicked {@link Propertyable}, if any.
	 */
	public final Propertyable getLastClicked() {
		return lastClicked.get();
	}

	/**
	 * @return The last clicked property.
	 */
	public ObjectProperty<Propertyable> lastClickedProperty() {
		return lastClicked;
	}
}
