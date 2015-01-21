package game_objects.abilities;

import backend.Assets;
import backend.LevelStage;
import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;

public class ExplodingArrow extends Arrow {

	public ExplodingArrow(ManipulatableObject parent, int damage,
			float xVelocity, float yVelocity) {
		super(parent, damage, xVelocity, yVelocity);
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

	@Override
	public void removeThyself() {
		AOE deathAttack = new AOE(Assets.instance.effects.explosion, parent, 1,
				position.x, position.y);

		LevelStage.interactables.add(deathAttack);
	}

}
