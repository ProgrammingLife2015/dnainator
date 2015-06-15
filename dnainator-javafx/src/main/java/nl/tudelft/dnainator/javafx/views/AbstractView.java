package nl.tudelft.dnainator.javafx.views;

import java.io.IOException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import nl.tudelft.dnainator.javafx.widgets.Propertyable;
import nl.tudelft.dnainator.javafx.widgets.dialogs.ExceptionDialog;

/**
 * An abstract class for common functionality needed by all the views.
 */
public abstract class AbstractView extends Pane {
	private static final String FXML = "/fxml/view.fxml";
	private static final double SCALE = 5;
	protected static final double ZOOM_FACTOR = 1e-3;
	protected static final int ZOOM_IN_STEP = 40;
	protected static final int ZOOM_OUT_STEP = -80;
	protected static final double ZOOM_IN_BOUND = 15;
	protected static final double ZOOM_OUT_BOUND = 0.5;
	protected Affine scale;
	protected Translate toCenter;
	protected Translate translate;

	private static ObjectProperty<Propertyable> lastClicked = 
			new SimpleObjectProperty<>(AbstractView.class, "lastClicked");

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
	protected void zoom(double delta, Point2D center) {
		double zoom = 1 + delta * ZOOM_FACTOR;
		Transform newScale = computeZoom(zoom, center);
		if (newScale.getMyy() > ZOOM_OUT_BOUND && newScale.getMxx() < ZOOM_IN_BOUND) {
			scale.setToTransform(newScale);
		}
	}
	
	/**
	 * Compute the transformation to be applied for the zoom.
	 * @param zoom    the scale of the zoom.
	 * @param center  the center of the zoom, in camera space
	 * @return the transformation for the zoom.
	 */
	protected Transform computeZoom(double zoom, Point2D center) {
		Point2D world;
		Transform newScale = scale;
		try {
			world = scale.inverseTransform(center.getX() - toCenter.getX() - translate.getX(),
					center.getY() - toCenter.getY() - translate.getY());
			newScale = scale.createConcatenation(new Scale(zoom, zoom,
										world.getX(), world.getY()));
		} catch (NonInvertibleTransformException e) {
			e.printStackTrace();
		}
		return newScale;
	}

	/**
	 * Zoom in for scrolling with the mouse.
	 * @param x the x position of the cursor.
	 * @param y the y position of the cursor.
	 */
	public void zoomInScroll(double x, double y) {
		zoom(ZOOM_IN_STEP, new Point2D(x, y));
	}
	
	/**
	 * Zoom out for scrolling with the mouse.
	 * @param x the x position of the cursor.
	 * @param y the y position of the cursor.
	 */
	public void zoomOutScroll(double x, double y) {
		zoom(ZOOM_OUT_STEP, new Point2D(x, y));
	}
	
	/**
	 * Default zoom method. Zooms in the camera by a default amount and to the center of the view.
	 */
	public void zoomIn() {
		zoom(ZOOM_IN_STEP, getCenter());
	}

	/**
	 * Default zoom method. Zooms out the camera by a default amount and to the center of the view.
	 */
	public void zoomOut() {
		zoom(ZOOM_OUT_STEP, getCenter());
	}

	/**
	 * Resets the zoom level to the default value.
	 */
	public void resetZoom() {
		scale.setToIdentity();
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

	/**
	 * @param p The last clicked {@link Propertyable}.
	 */
	public static final void setLastClicked(Propertyable p) {
		lastClicked.set(p);
	}

	/**
	 * @return The last clicked {@link Propertyable}, if any.
	 */
	public static final Propertyable getLastClicked() {
		return lastClicked.get();
	}

	/**
	 * @return The last clicked property.
	 */
	public static ObjectProperty<Propertyable> lastClickedProperty() {
		return lastClicked;
	}
}
