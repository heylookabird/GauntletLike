package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import backend.Assets;
import backend.LevelStage;

public class ThrowSword extends AbstractAbility {
	private boolean attackFinished = false;
	private int entrance;

	public ThrowSword(ManipulatableObject parent, int damage, float xVelocity,
			float yVelocity, int entranceAbility) {
		super(parent, parent.position.x, parent.position.y, 1, 1);
		this.damage = damage;
		removesItself = true;
		this.lifeTimer = .3f;
		this.setImage(Assets.instance.weapons.sword);
		this.velocity.set(xVelocity, yVelocity);

		double angle = Math.atan2(velocity.y, velocity.x);
		rotation = (float) Math.toDegrees(angle);
		this.stunTime = .05f;
		this.entrance = entranceAbility;
	}

	@Override
	public void postDeathEffects() {
		attackFinished = true;
		parent.addPassive(this);
		velocity.set(0, 0);
		super.postDeathEffects();
	}
	
	public boolean activate(){
		if(attackFinished){
			Teleport attack = new Teleport(parent, position, 1, entrance);
			LevelStage.interactables.add(attack);
			return true;
		}else return false;
	}
	

	@Override
	public void update(float deltaTime) {
		if (!attackFinished)
			super.update(deltaTime);
	}

	@Override
	public void interact(AbstractGameObject couple) {
		super.interact(couple);

		if (couple instanceof ManipulatableObject) {
			ManipulatableObject obj = (ManipulatableObject) couple;
			if (!this.isSameTeam(obj)) {
				boolean newObj = isFirstInteraction(obj);

				if (newObj)
					obj.takeHitFor(damage, this);
			}
		}
	}

}
