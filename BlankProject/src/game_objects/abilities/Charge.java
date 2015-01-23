package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.abilities.effects.Apparition;

public class Charge extends AbstractAbility {

	private int speed;
	
	private float offsetX, offsetY;
	public Charge(ManipulatableObject parent, float x, float y, float width,
			float height, float offsetX, float offsetY) {
		super(parent, x, y, width, height);
		removesItself = true;
		lifeTimer = .5f;
		speed = 10;
		damage = 6;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		dimension.x += offsetX * 2;
		dimension.y += offsetY * 2;
		bounds.setSize(dimension.x, dimension.y);
		
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
		parent.invulnerable = true;
		priority = 3;

	}
	@Override
	public void update(float deltaTime) {
		position.set(parent.position.x - offsetX, parent.position.y - offsetY);
		bounds.x = parent.position.x;
		bounds.y = parent.position.y;
		
		if(stateTime > .1f){
			parent.addPassive(new Apparition(parent));
			stateTime = 0;
		}
		super.update(deltaTime);
	}
	@Override
	public void postDeathEffects() {
		parent.terminalVelocity.set(terminalVelocity);
		parent.stopMove();
		parent.invulnerable = false;
		
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
