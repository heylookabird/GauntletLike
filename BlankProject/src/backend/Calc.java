package backend;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Calc {
	
	public static float atan2(Vector2 origin, Vector2 target){
		
		return (float)(Math.toDegrees(Math.atan2(target.y - origin.y, target.x - origin.x)));
	}
	
	public static float atan2(Vector2 origin){
		System.out.println(origin);
		float x = (float)(Math.toDegrees(Math.atan2(origin.y, origin.x)));
		if(x < 0){
			x = 360 + x;
		}
		System.out.println(x);

		return x;
	}

	public static ManipulatableObject findClosestEnemy(AbstractGameObject origin, Array<ManipulatableObject> targetArray){
		ManipulatableObject target;
		if(targetArray.size > 0)
			target = targetArray.first();
		else
			return null;
		
		float shortest = 100000;
		for(int i = 0; i < targetArray.size; i++){
			float originX = origin.position.x + origin.origin.x, originY = origin.position.y + origin.origin.y, 
					enOriginX = targetArray.get(i).position.x + targetArray.get(i).origin.x,
					enOriginY = targetArray.get(i).position.y + targetArray.get(i).origin.y;
			
			float distance = (originX - enOriginX) * (originX - enOriginX) + (originY - enOriginY)* (originY - enOriginY);
			if(distance < shortest){
				target = targetArray.get(i);
				shortest = distance;
			}
			
		}
		
		return target;
	}

	public static float sin(float angle) {
		return (float)Math.sin(Math.toRadians(angle));
	}

	public static float cos(float angle) {

		return (float)Math.cos(Math.toRadians(angle));
	}

	public static ManipulatableObject findClosestEnemy(AbstractGameObject origin,
			Array<ManipulatableObject> targetArray,
			Array<ManipulatableObject> objectsAlreadyHit) {
		
		ManipulatableObject target;
	
		if(targetArray.size > 0)
			target = null;
		else 
			return null;
		
		float shortest = 100000;
		ManipulatableObject obj = null;
		for(int i = 0; i < targetArray.size; i++){
			obj = targetArray.get(i);
			float originX = origin.position.x + origin.origin.x, originY = origin.position.y + origin.origin.y, 
					enOriginX = targetArray.get(i).position.x + targetArray.get(i).origin.x,
					enOriginY = targetArray.get(i).position.y + targetArray.get(i).origin.y;
			
			float distance = (originX - enOriginX) * (originX - enOriginX) + (originY - enOriginY)* (originY - enOriginY);
			boolean alreadyHit = false;
			for(int j = 0; j < objectsAlreadyHit.size; j++){
				if(objectsAlreadyHit.get(j) == obj)
					alreadyHit = true;
			}
			
			if(distance < shortest && !alreadyHit){
				target = targetArray.get(i);
				shortest = distance;
			}
			
		}
		
		return target;
		
	}

	public static float directionToDegrees(DIRECTION direction) {
		if(direction == DIRECTION.RIGHT)
			return 0;
		else if(direction == DIRECTION.LEFT)
			return 180;
		else if(direction == DIRECTION.UP)
			return 90;
		else if(direction == DIRECTION.DOWN)
			return 270;
		
		return 0;
	}

	public static DIRECTION degreesToDirection(float direction) {
		if(direction < 0){
			direction = 360 + direction;
		}
		System.out.println(direction);
		if(direction > 45 && direction < 135){
			return DIRECTION.UP;
		}else if(direction > 135 && direction < 225){
			return DIRECTION.LEFT;
		}else if(direction > 225 && direction < 315){
			return DIRECTION.DOWN;
		}else if(direction < 45 || direction > 315){
			return DIRECTION.RIGHT;
		}
		return null;
	}
}
