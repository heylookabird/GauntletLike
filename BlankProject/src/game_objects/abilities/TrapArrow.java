package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import backend.Assets;
import backend.LevelStage;

public class TrapArrow extends Arrow {
	public TrapArrow(ManipulatableObject parent, int damage, float xVelocity,
			float yVelocity) {
		super(parent, damage, xVelocity, yVelocity);
		System.out.println("Initialized");
	}

	@Override
	public void postDeathEffects() {
		super.postDeathEffects();
		parent.addPassive(this);
	}
	
	public void activate(){
		
		AOE deathAttack = new AOE(Assets.instance.effects.explosion, parent, 3,
				position.x, position.y);

		LevelStage.interactables.add(deathAttack);
		
		
		if(!this.attackFinished){
			removeThyself();
		}else
			parent.removePassive(this);

	}

	@Override
	public void interact(AbstractGameObject couple) {
		super.interact(couple);

		if (couple instanceof ManipulatableObject) {
			ManipulatableObject obj = (ManipulatableObject) couple;
			if (!isSameTeam(obj)) {
				obj.takeHitFor(damage, this);
				lifeTimer = 0;
			}
		}

		if (couple instanceof Arrow) {
			if (!((AbstractAbility) couple).isSameTeam(parent)) {
				lifeTimer = 0;
				((Arrow) couple).lifeTimer = 0;
			}
		}
		}

}
