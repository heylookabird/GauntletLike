package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.ManipulatableObject.STATE;

public class Charge extends AbstractAbility {

	public Charge(ManipulatableObject parent, float x, float y, float width,
			float height) {
		super(parent, x, y, width, height);
		removesItself = true;
		lifeTimer = 1.5f;
		parent.stun(lifeTimer);

		if(parent.facing == DIRECTION.UP){
			parent.velocity.y = 3;
		}else if(parent.facing == DIRECTION.DOWN){
			parent.velocity.y = -3;
		}else if(parent.facing == DIRECTION.LEFT){
			parent.velocity.x = -3;
		}else if(parent.facing == DIRECTION.RIGHT){
			parent.velocity.y = 3;
		}
	}
	@Override
	public void update(float deltaTime) {
		bounds.setPosition(parent.position);
		super.update(deltaTime);
	}
	@Override
	public void postDeathEffects() {
		parent.state = STATE.NOT_MOVING;
		parent.velocity.set(0, 0);
		
		super.postDeathEffects();
	}
	
	@Override
	public void interact(AbstractGameObject couple) {

		super.interact(couple);
	}

}
