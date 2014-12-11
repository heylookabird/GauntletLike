package backend;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class LevelStage {
	
	public static Array<ManipulatableObject> playerControlledObjects = new Array<ManipulatableObject>();
	public static Array<AbstractGameObject> frontObjects = new Array<AbstractGameObject>();
	public static Array<AbstractGameObject> backObjects = new Array<AbstractGameObject>();
	public static Array<ManipulatableObject> enemyControlledObjects = new Array<ManipulatableObject>();
	public static Array<AbstractGameObject> uncollidableObjects = new Array<AbstractGameObject>();
	//For objects like the portal
	public static Array<AbstractGameObject> interactables = new Array<AbstractGameObject>();
	
	
	
	public LevelStage(){

	}
	
	public static void addPlayer(ManipulatableObject obj){
		playerControlledObjects.add(obj);
		InputManager.inputManager.addObject(obj);
	}
	public static void render(SpriteBatch batch){

		//render portal
		for(AbstractGameObject interactableObject: interactables){
			interactableObject.render(batch);
		}
		//Render all of the manipulatable objects
		for(ManipulatableObject object: LevelStage.playerControlledObjects){
			object.render(batch);
		}
		
		//Render all of the enemy controlled objects
		for(ManipulatableObject object: LevelStage.enemyControlledObjects){
			object.render(batch);
		}
		for(int i = LevelStage.backObjects.size - 1; i >= 0; i--){
			backObjects.get(i).render(batch);
		}
		//render all of the terrain
		for(AbstractGameObject platform: LevelStage.frontObjects){
			platform.render(batch);
		}
		
		
		
		for(AbstractGameObject uncollidable: LevelStage.uncollidableObjects){
			uncollidable.render(batch);
		}
	}
	public static void destroy(){
		//Clear all the arrays
		playerControlledObjects.clear();
		enemyControlledObjects.clear();
		
		interactables.clear();
		frontObjects.clear();
		backObjects.clear();
		uncollidableObjects.clear();

	}
	public static void update(float deltaTime){
		
		//Iterate and update all enemies, players, controllable objects
		for(ManipulatableObject object: LevelStage.playerControlledObjects){
			object.update(deltaTime);
		}
		//Render all of the enemy controlled objects
		for(ManipulatableObject object: LevelStage.enemyControlledObjects){
			object.update(deltaTime);
		}
		for(int i = LevelStage.backObjects.size - 1; i >= 0; i--){
			backObjects.get(i).update(deltaTime);
		}
		//render all of the terrain
		for(AbstractGameObject platform: LevelStage.frontObjects){
			platform.update(deltaTime);
		}
		
		for(AbstractGameObject interactableObject: interactables){
			interactableObject.update(deltaTime);
		}
		for(AbstractGameObject uncollidable: LevelStage.uncollidableObjects){
			uncollidable.update(deltaTime);
		}
		
		
	}
	
}
