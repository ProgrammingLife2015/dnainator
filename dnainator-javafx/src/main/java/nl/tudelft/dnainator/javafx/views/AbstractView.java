package nl.tudelft.dnainator.javafx.views;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import nl.tudelft.dnainator.javafx.widgets.dialogs.ExceptionDialog;

import java.io.IOException;

/**
 * An abstract class for common functionality needed by all the views.
 */
public abstract class AbstractView extends Pane {
	private static final String FXML = "/fxml/view.fxml";
	private static final double SCALE = .1;
	protected static final double ZOOM_FACTOR = 1e-3;
	protected static final int DEFAULT_ZOOM_IN = 55;
	protected static final int DEFAULT_ZOOM_OUT = -150;
	protected Affine scale;
	protected Translate toCenter;
	protected Translate translate;

	/**
	 * Constructs a new {@link AbstractView}. Sets up the necessary transforms and
	 * translations for the panning and zooming.
	 */
	public AbstractView() {
		loadFXML();
		getStyleClass().add("view");

		toCenter = new Translate();
		widthProperty().addListener((o, v1, v2) -> toCenter.setX(v2.intValue() / 2));
		heightProperty().addListener((o, v1, v2) -> toCenter.setY(v2.intValue() / 2));

		translate = new Translate();
		scale = getScale();
	}

	/**
	 * @return The scale used to scale the contents of the {@link AbstractView}.
	 */
	public Affine getScale() {
		return new Affine(new Scale(SCALE, SCALE));
	}

	/**
	 * Adds all the transforms required for zooming and panning to the root child.
	 * @param group The root child of the {@link AbstractView}.
	 */
	public void setTransforms(Group group) {
		group.getTransforms().add(toCenter);
		group.getTransforms().add(translate);
		group.getTransforms().add(scale);
	}

	private void loadFXML() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML));
		fxmlLoader.setRoot(this);

		try {
			fxmlLoader.load();
		} catch (IOException e) {
			new ExceptionDialog(null, e, "Can not load the view!");
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
	}

	/**
	 * Zoom the camera by the amount given by zoom.
	 * @param delta		the amount to zoom in
	 * @param center	the center of the zoom, in camera space
	 */
	public void zoom(double delta, Point2D center) {
		try {
			center = scale.inverseTransform(center.getX() - toCenter.getX() - translate.getX(),
					center.getY() - toCenter.getY() - translate.getY());
			double zoom = 1 + delta * ZOOM_FACTOR;
			scale.append(new Scale(zoom, zoom, center.getX(), center.getY()));
		} catch (NonInvertibleTransformException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Default zoom method. Zooms in the camera by a default amount and to the center of the view.
	 */
	public void zoomIn() {
		zoom(DEFAULT_ZOOM_IN, getCenter());
	}

	/**
	 * Default zoom method. Zooms out the camera by a default amount and to the center of the view.
	 */
	public void zoomOut() {
		zoom(DEFAULT_ZOOM_OUT, getCenter());
	}

	/**
	 * Resets the zoom level to the default value.
	 */
	public void resetZoom() {
		scale.setToTransform(getScale());
		worldToCamera();
	}

	/**
	 * Resets the translation of the view to the default value.
	 */
	public void resetTranslate() {
		translate.setX(0);
		translate.setY(0);
		scale.setTx(0);
		scale.setTy(0);
		worldToCamera();
	}

	/**
	 * @return The center {@link Point2D} of the view.
	 */
	public final Point2D getCenter() {
		return new Point2D(getWidth() / 2, getHeight() / 2);
	}
}
