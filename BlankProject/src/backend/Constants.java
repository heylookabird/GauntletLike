package backend;

import game_objects.ManipulatableObject.DIRECTION;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Constants {

	public static final float defaultZoom = 1f;
	public static final float viewportWidth = 16.0f * 1.9f, viewportHeight = 9.0f * 1.9f;
	public static final float sceneCamX = viewportWidth / 2;
	public static final float sceneCamY = viewportHeight / 2;
	public static final float cameraPanVal = 200;
	public static final float bgViewportWidth = 1280;
	public static final float bgViewportHeight = 720;

	public static final float LEFT_LIMIT = -10;
	public static final float BOT_LIMIT = 0;
	public static final float RIGHT_LIMIT = 44.5f;
	public static final float TOP_LIMIT = Constants.viewportHeight;
	// Unprojects coordinates from screen and converts to virtual coordinates
	// that are based off the camera's units and resize
	public static Vector2 screenToCoords(OrthographicCamera camera, Vector3 pos) {
		camera.unproject(pos);
		return new Vector2(pos.x, pos.y);
	}

	public static boolean collides(Rectangle rect1, Rectangle rect2){
		if(rect1.contains(rect2))
			return true;
		
		return false;
	}
	
	public static boolean collides(float x, float y, Rectangle rect2){
		if(rect2.contains(x, y))
			return true;
		
		return false;
	}
	
	public static DIRECTION directionBetween(Vector2 parentPos, Vector2 targetPos){
		double angle = Math.toDegrees(Math.atan2(parentPos.y - targetPos.y, parentPos.x - targetPos.x));
		
		if(angle >= 315 && angle < 45){
			return DIRECTION.RIGHT;
		}else if(angle >= 45 && angle < 135){
			return DIRECTION.UP;
		}else if(angle >= 135 && angle < 225){
			return DIRECTION.UP;
		}
		
		return DIRECTION.DOWN;
		
		
	}

}
