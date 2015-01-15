package game_objects.abilities;

import backend.Assets;
import backend.LevelStage;
import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;

public class Arrow extends AbstractAbility{
	private boolean attackFinished = false;
	public Arrow(ManipulatableObject parent, int damage, float xVelocity, float yVelocity) {
		super(parent, parent.position.x, parent.position.y, 1, 1);
		this.damage = damage;
		removesItself = true;
		this.lifeTimer = 1f;
		this.setImage(Assets.instance.weapons.sword);
		this.velocity.set(xVelocity, yVelocity);
		
		double angle = Math.atan2(velocity.y, velocity.x);
		rotation = (float) Math.toDegrees(angle);
		this.stunTime = .05f;
	}
	
	@Override
	public void postDeathEffects(){
		attackFinished = true;
		//LevelStage.uncollidableObjects.add(this);
		velocity.set(0, 0);
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
