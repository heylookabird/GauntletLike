package backend;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Calc {
	
	public static float atan2(Vector2 origin, Vector2 target){
		
		return (float)(Math.toDegrees(Math.atan2(target.y - origin.y, target.x - origin.x)));
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
}
