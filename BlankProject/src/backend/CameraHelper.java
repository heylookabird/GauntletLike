package backend;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class CameraHelper {

	private static final String TAG = CameraHelper.class.getName();
	private final float MAX_ZOOM_IN = .16f;
	private final float MAX_ZOOM_OUT = 2.5f;
	private Vector2 position;
	public float zoom;
	private Vector2 targetSpot;
	private AbstractGameObject target;
	private static final float rate = 2;
	Rectangle rect;
	private float zoomTarget;
	private boolean zooming;
	Vector2 centerPoint;

	public CameraHelper() {
		position = new Vector2();
		zoom = Constants.defaultZoom;
		zoomTarget = zoom;
		targetSpot = new Vector2();
		rect = new Rectangle(-Constants.viewportWidth / 2, -Constants.viewportHeight / 2, Constants.viewportWidth,
				Constants.viewportHeight);
		centerPoint = new Vector2();

	}
	
	public Vector2 getCenterPoint(){
		return centerPoint;
	}

	public void update(float deltaTime) {
		float x = 0, y = 0;
		for (ManipulatableObject obj : LevelStage.playerControlledObjects) {
			x += obj.position.x;
			y += obj.position.y;
		}
		
		centerPoint.set(x / LevelStage.playerControlledObjects.size, y
				/ LevelStage.playerControlledObjects.size);

		float deltaX = (centerPoint.x - position.x) / 10, deltaY = (centerPoint.y - position.y ) / 10;
		position.x += deltaX;
		position.y += deltaY;
		rect.x += deltaX;
		rect.y += deltaY;

		boolean allOnScreen = true;
		for (ManipulatableObject obj : LevelStage.playerControlledObjects) {
			if (!rect.contains(obj.bounds)) {
				allOnScreen = false;
				obj.clampInRectangle(rect);

			}
		}
		if (!allOnScreen) {
			position.x -= deltaX;
			position.y -= deltaY;
			rect.x -= deltaX;
			rect.y -= deltaY;
		}
	}

	

	public boolean onScreen(AbstractGameObject obj) {
		// Checks if camera bounds overlaps with
		if (obj.bounds.overlaps(rect))
			return true;

		return false;

	}

	public void setPosition(float x, float y) {
		this.position.set(x, y);
		/*targetSpot = new Vector2(position);
		rect.setPosition(x - Constants.viewportWidth / 2 - 5, y
				- Constants.viewportHeight / 2);
*/
	}

	public Vector2 getPosition() {
		return position;
	}

	public void addZoom(float amount) {
		setZoom(zoom + amount, false);

	}

	public void setZoom(float zoom, boolean setZoomTarget) {

		this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
		zoomTarget = setZoomTarget ? this.zoom : zoomTarget;

	}

	public float getZoom() {
		return zoom;
	}

	public AbstractGameObject getTarget() {
		return target;
	}

	public boolean hasTarget() {
		return target != null;
	}

	public boolean hasTarget(AbstractGameObject target) {
		return hasTarget() && this.target.equals(target);
	}

	public void applyTo(OrthographicCamera camera) {
		camera.position.x = this.position.x;
		camera.position.y = this.position.y;
		camera.zoom = zoom;
		camera.update();

	}

	public void destroy() {
		target = null;
		targetSpot = null;
		position = null;
	}

	public void movePositionBy(Vector2 deltaPos) {
		moveTowardsPosition(deltaPos.add(position));
	}

	public void moveTowardsPosition(Vector2 targetSpot) {

		this.targetSpot = targetSpot;
	}

	public void zoomBy(float zoomBy) {
		transitionToZoom(zoom + zoomBy);
	}

	public void transitionToZoom(float zoomTo) {

		// timePassed * rate
		zoomTarget = zoomTo;
	}

	public boolean isWithin(float f) {

		if (position.dst(target.position) < f) {
			return true;
		}
		return false;
	}

	public void render(SpriteBatch batch) {
		//batch.draw(Assets.instance.background.bg, rect.x, rect.y, rect.width, rect.height);
	}
}
