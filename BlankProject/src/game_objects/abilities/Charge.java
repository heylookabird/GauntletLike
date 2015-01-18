package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.ManipulatableObject.STATE;

public class Charge extends AbstractAbility {

	private int speed;
	

	public Charge(ManipulatableObject parent, float x, float y, float width,
			float height) {
		super(parent, x, y, width, height);
		removesItself = true;
		lifeTimer = .5f;
		speed = 10;
		
		parent.stopMove();
		terminalVelocity.set(parent.terminalVelocity);
		parent.terminalVelocity.set(speed, speed);
		if(parent.facing == DIRECTION.UP){
			parent.velocity.y = speed;
		}else if(parent.facing == DIRECTION.DOWN){
			parent.velocity.y = -speed;
		}else if(parent.facing == DIRECTION.LEFT){
			parent.velocity.x = -speed;
		}else if(parent.facing == DIRECTION.RIGHT){
			parent.velocity.x = speed;
		}
		parent.stun(lifeTimer);


	}
	@Override
	public void update(float deltaTime) {
		bounds.setPosition(parent.position);
		super.update(deltaTime);
	}
	@Override
	public void postDeathEffects() {
		parent.terminalVelocity.set(terminalVelocity);
		parent.stopMove();
		
		super.postDeathEffects();
	}
	
	@Override
	public void interact(AbstractGameObject couple) {

		if(couple instanceof ManipulatableObject){
			ManipulatableObject obj = (ManipulatableObject) couple;
			
			if (!isSameTeam(obj)) {
				boolean newObj = isFirstInteraction(obj);

				if (newObj)
					obj.takeHitFor(damage, this);
				

			}
		}
		super.interact(couple);
	}

}
