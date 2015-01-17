package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import backend.Assets;
import backend.LevelStage;

public class ThrowSword extends AbstractAbility{
	private boolean attackFinished = false;
	public ThrowSword(ManipulatableObject parent, int damage, float xVelocity, float yVelocity) {
		super(parent, parent.position.x, parent.position.y, 1, 1);
		this.damage = damage;
		removesItself = true;
		this.lifeTimer = .3f;
		this.setImage(Assets.instance.weapons.sword);
		this.velocity.set(xVelocity, yVelocity);
		
		double angle = Math.atan2(velocity.y, velocity.x);
		rotation = (float) Math.toDegrees(angle);
		this.stunTime = .05f;
	}
	
	@Override
	public void postDeathEffects(){
		attackFinished = true;
		LevelStage.uncollidableObjects.add(this);
		velocity.set(0, 0);
		super.postDeathEffects();
	}
	
	
	@Override
	public void update(float deltaTime){
		if(!attackFinished)
			super.update(deltaTime);
	}
	
	@Override
	public void interact(AbstractGameObject couple) {
		
		
		ManipulatableObject obj = (ManipulatableObject) couple;
		if(!this.isSameTeam(obj)){
			boolean newObj = isFirstInteraction(obj);
		
			if(newObj)
				obj.takeHitFor(damage, this);
		}
	}

}
