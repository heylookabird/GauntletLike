package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import backend.Assets;
import backend.LevelStage;

public class ProxyArrow extends Arrow {

	public ProxyArrow(ManipulatableObject parent, int damage, float xVelocity,
			float yVelocity) {
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
		super.removeThyself();
		Mine deathAttack = new Mine(parent, 5, position.x, position.y);

		LevelStage.interactables.add(deathAttack);
	}

}
