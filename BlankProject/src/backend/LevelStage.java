package backend;

import game_map_classes.GameMap;
import game_map_classes.TransitionBoundary;
import game_map_classes.WaveManager;
import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class LevelStage {
	
	public static Array<ManipulatableObject> playerControlledObjects = new Array<ManipulatableObject>();
	public static Array<AbstractGameObject> solidObjects = new Array<AbstractGameObject>();
	public static Array<AbstractGameObject> backObjects = new Array<AbstractGameObject>();
	public static Array<AbstractGameObject> enemyControlledObjects = new Array<AbstractGameObject>();
	public static Array<AbstractGameObject> uncollidableObjects = new Array<AbstractGameObject>();
	//For objects like the portal
	public static Array<AbstractGameObject> interactables = new Array<AbstractGameObject>();
	public static Array<TransitionBoundary> exitBounds = new Array<TransitionBoundary>();
	public static WaveManager waveManager = new WaveManager();
	private static boolean waves;
	//ints to keep track of where on the map the players are
	public static int currentX = 0, currentY = 0;
	
	
	public static String header;
	private static LevelLoader loader = new LevelLoader();
	public static GameMap map = new GameMap();
	
	
	public LevelStage(){
		currentX = 0;
		currentY = 0;
		
		loader = new LevelLoader();
		waveManager = new WaveManager();
	}
	
	public static void setHeader(String head){
		header = head;//this method needs to be called so that the Loader will know what type of world we want loaded
		//Assets.instance.readyMap(head);
		String asset = header + ".pack";
		Assets.instance.loadMapObjects(asset);
	}
	
	public static void activateWaveManager(){
		waves = true;
	}
	
	public static void deactivateWaveManager(){
		waves = false;
	}
	
	public static void loadTest(boolean players){
		System.out.println("LoadTest");
		loader.loadTestRoom(players);
	}
	
	public static void reposition(TransitionBoundary trigger) {
		//if this transition is the x direction go into this logic
		if(trigger.xDirection){
			//if mapMove is less than 1 in the x, then we moved left in the map
			//and reposition them on the right for the next level
			if(trigger.mapMove < 0){
				repositionAllToRight();
			}else{
				//we went to right bound and should end up on the left for the next area
				repositionAllToLeft();
			}
		}
		//if not in x, then in y and go to this logic
		else{
			//if mapMove > 0 then we go up in the map
			if(trigger.mapMove > 0){
				repositionAllToBottom();
			}else
				repositionAllToTop();
		}
	}
	
	private static void repositionAllToLeft() {
		float lowestX = 1000000f;
		for(TransitionBoundary boundaries: exitBounds){
			if(boundaries.bounds.x < lowestX){
				lowestX = boundaries.bounds.x;
				}
		}
		
		for(ManipulatableObject players: playerControlledObjects){
			//players.position.x = lowestX;
			
			//for testing
			players.position.set(0,0);
		}
	}

	private static void repositionAllToRight() {
		float biggestX = -1000000f;
		for(TransitionBoundary boundaries: exitBounds){
			if(boundaries.bounds.x < biggestX){
				biggestX = boundaries.bounds.x;
				}
		}
		
		for(ManipulatableObject players: playerControlledObjects){
			//players.position.x = biggestX - players.bounds.width;
			
			//for testing
			players.position.set(0,0);
		}
	}

	private static void repositionAllToBottom() {
		float lowestY = 1000000f;
		for(TransitionBoundary boundaries: exitBounds){
			if(boundaries.bounds.y < lowestY){
				lowestY = boundaries.bounds.y;
				}
		}
		
		for(ManipulatableObject players: playerControlledObjects){
			//players.position.y = lowestY;
			
			//for testing
			players.position.set(0,0);
		}
	}
	
	private static void repositionAllToTop() {
		float highestY = -100000f;
		for(TransitionBoundary boundaries: exitBounds){
			if(boundaries.bounds.y > highestY){
				highestY = boundaries.bounds.y;
				}
		}
		
		for(ManipulatableObject players: playerControlledObjects){
			//players.position.y = highestY - players.bounds.height;
			players.position.set(0,0);
		}
	}


	//currentX or currentY was changed in the TransitionBoundary class so we can assume the new levels will be loaded because the 
	//currentX and currentY 
	public static void load(){
		String file = header + "/" + currentX + "X" + currentY;
		loader.goToNextRoom(file, map.isCleared(currentX, currentY));
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
		for(ManipulatableObject object: playerControlledObjects){
			object.render(batch);
		}
		
		//Render all of the enemy controlled objects
		for(AbstractGameObject object: enemyControlledObjects){
			object.render(batch);
		}
		for(int i = backObjects.size - 1; i >= 0; i--){
			backObjects.get(i).render(batch);
		}
		//render all of the terrain
		for(AbstractGameObject platform: solidObjects){
			platform.render(batch);
		}
		
		
		
		for(AbstractGameObject uncollidable: uncollidableObjects){
			uncollidable.render(batch);
		}
	}
	public static void destroy(){
		//Clear all the arrays
		playerControlledObjects.clear();
		enemyControlledObjects.clear();
		map.clear();
		
		//resets currentX and currentY in case we want to use them again
		currentX = 0;
		currentY = 0;
		
		interactables.clear();
		solidObjects.clear();
		backObjects.clear();
		uncollidableObjects.clear();
		exitBounds.clear();
	}
	
	public static void clearRoom(){
		map.cleared(currentX, currentY);
		
		interactables.clear();
		solidObjects.clear();
		backObjects.clear();
		uncollidableObjects.clear();
		exitBounds.clear();
	}
	
	public static void update(float deltaTime){
		
		//Iterate and update all enemies, players, controllable objects
		for(ManipulatableObject object: playerControlledObjects){
			object.update(deltaTime);
		}
		//Render all of the enemy controlled objects
		for(AbstractGameObject object: enemyControlledObjects){
			object.update(deltaTime);
		}
		for(int i = backObjects.size - 1; i >= 0; i--){
			backObjects.get(i).update(deltaTime);
		}
		//render all of the terrain
		for(AbstractGameObject platform: solidObjects){
			platform.update(deltaTime);
		}
		
		for(AbstractGameObject interactableObject: interactables){
			interactableObject.update(deltaTime);
		}
		for(AbstractGameObject uncollidable: uncollidableObjects){
			uncollidable.update(deltaTime);
		}
		
		if(enemyControlledObjects.size == 0){
			for(TransitionBoundary bounds: exitBounds){
				bounds.update(deltaTime);
			}
		}
		if(waves)
			waveManager.update(deltaTime);
		
		
	}

	public static void testReposition() {
		for(int i = 0; i < playerControlledObjects.size; i++){
			playerControlledObjects.get(i).position.set(0,0);
		}
	}	
}
