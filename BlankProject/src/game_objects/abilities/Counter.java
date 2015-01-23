package game_objects.abilities;

import backend.Assets;
import backend.LevelStage;
import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;

public class Counter extends AbstractAbility {

	public Counter(ManipulatableObject parent, float x, float y, float width,
			float height) {
		super(parent, x, y, width, height);
		lifeTimer = 4;
		parent.invulnerable = true;
		parent.stopMove();

		parent.stun(lifeTimer);

	}

	@Override
	public void postDeathEffects() {
		parent.invulnerable = false;
		parent.stunTimer = 0;
		super.postDeathEffects();
	}

	@Override
	public void interact(AbstractGameObject couple) {

		if (couple instanceof AbstractAbility) {

			AbstractAbility obj = (AbstractAbility) couple;
			if (parent.teamObjects != obj.parent.teamObjects) {
				System.out.println("Countered!");
				lifeTimer = 0;
				AOE deathAttack = new AOE(Assets.instance.effects.explosion, parent, 1, .4f, .6f,
						position.x, position.y, 3, 3, false);

				LevelStage.interactables.add(deathAttack);
			}
		}
	}

}
