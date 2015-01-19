package ai_classes;

import com.badlogic.gdx.math.Vector2;

import game_objects.ManipulatableObject;

public class RusherAi extends AbstractAi {

	public RusherAi(ManipulatableObject par) {
		super(par);

	}

	
	@Override
	public void moveToTarget(ManipulatableObject target, float distance){
		align(target, true, distance);
		
		align(target, false, distance);
		

	}
	
	@Override
	protected void makeNextDecision() {
		Vector2 temp = target.getCenter().sub(parent.getCenter());
		if(Math.abs(temp.x) + Math.abs(temp.y) < 10)
			parent.attack();
		moveToTarget(target, 1.0f);
		super.makeNextDecision();
	}
}
