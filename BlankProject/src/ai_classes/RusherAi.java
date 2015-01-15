package ai_classes;

import com.badlogic.gdx.math.Vector2;

import game_objects.ManipulatableObject;

public class RusherAi extends AbstractAi {

	public RusherAi(ManipulatableObject par) {
		super(par);

	}

	
	public void moveToTarget(ManipulatableObject target, float distance){
		align(target, true, distance);
		
		align(target, false, distance);
		

	}
	

	public void align(ManipulatableObject target, boolean x, float distance){
		if(x){
			if(parent.position.x + parent.bounds.width / 2 < target.position.x + target.dimension.x / 2 - distance){
				parent.moveRight();
			}else if(parent.position.x + parent.dimension.x / 2 > target.position.x + target.bounds.width / 2 + distance){
				parent.moveLeft();
			}else
				parent.stopMoveX();
		
		}
		else{
			if(parent.position.y + parent.bounds.height / 2 < target.position.y + target.dimension.y / 2 - distance){
				parent.moveUp();
			}else if(parent.position.y + parent.dimension.y / 2 > target.position.y + target.bounds.height / 2 + distance)
				parent.moveDown();
			else{
				parent.stopMoveY();
			}
		}
	}
	protected void makeNextDecision() {
		Vector2 temp = target.getCenter().sub(parent.getCenter());
		if(Math.abs(temp.x) + Math.abs(temp.y) < 10)
			parent.attack();
		moveToTarget(target, .5f);
		super.makeNextDecision();
	}
}
