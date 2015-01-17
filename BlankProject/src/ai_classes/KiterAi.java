package ai_classes;

import game_objects.ManipulatableObject;

import com.badlogic.gdx.math.Vector2;

public class KiterAi extends AbstractAi {
	boolean alignedX = false, alignedY = false;
	public KiterAi(ManipulatableObject par) {
		super(par);
	}
	
	public void moveToTarget(ManipulatableObject target, float distance){
		checkAlignment(target);
		
		if(!alignedX && !alignedY){
			align(target, true, distance/2);
			align(target, false, distance/2);
		}else if (!alignedX){
			align(target, true, distance);
		}else if(!alignedY)
			align(target, false, distance);
	}
	
	private void checkAlignment(ManipulatableObject target) {
		Vector2 vector = target.getCenter();
		if(Math.abs(parent.getCenter().x - vector.x) < .5f){
			alignedX = true;
		}else{
			alignedX = false;
		}
		
		if(Math.abs(parent.getCenter().y - vector.y) < .5f){
			alignedY = true;
		}else{
			alignedY = false;
		}
	}

	@Override
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
		if(alignedX || alignedY)
			parent.attack();
		moveToTarget(target, 4.70f);
		super.makeNextDecision();
	}
}
