package game_map_classes;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import backend.LevelStage;

import com.badlogic.gdx.math.Rectangle;

public class TransitionBoundary extends AbstractGameObject{
	public int mapMove;
	public boolean xDirection;
/*	public TransitionBoundary(float x, float y, float width, float height, int amount, boolean xDir){
		xDirection = xDir;
		mapMove = amount;
		
		bounds = new Rectangle(x,y,width,height);
		printLocation();
		
	
	}
	
	private void printLocation() {
		System.out.println("Trasition Loc: " + bounds.x + " , " + bounds.y);
	}

	public TransitionBoundary(float x, float y, float width, float height, boolean xDir){
		mapMove = 1;
		xDirection = xDir;
		
		bounds = new Rectangle(x,y,width,height);
		
		printLocation();
	}
	
	private void transition(){
		//clears LevelStages arrays except for players, clearRoom method already adds the currentLevel into the array of cleared levels
		LevelStage.clearRoom();
		
		//checks which direction this particular bounds is meant for and changes the LevelStages currentLoc
		//so that it will load the correct location
		if(xDirection){
			LevelStage.currentX += mapMove;
		}
		else
			LevelStage.currentY += mapMove;
		
		
		//loads the new location
		LevelStage.load();
		
		LevelStage.reposition(this);;
	}
	
*/
	
	


	@Override
	public void update(float deltatime){
		for(ManipulatableObject player: LevelStage.playerControlledObjects){
			if(this.bounds.overlaps(player.bounds)){
				//transition();
				testTransition();
			}
		}
	}
	

	private void testTransition() {
		LevelStage.clearRoom();
		
		LevelStage.loadTest(true);
		
		LevelStage.testReposition();
	}
}
