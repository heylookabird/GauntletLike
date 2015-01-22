package game_objects.abilities;

import backend.Assets;
import backend.LevelStage;
import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;

public class Mine extends AbstractAbility {

	public Mine(ManipulatableObject parent, int damage, float x, float y) {
		super(parent, x, y, 1, 1);
		this.damage = damage;
		this.setImage(Assets.instance.weapons.sword);


		this.knockbackSpeed = 10;
		this.removesItself = false;

	}

	public Mine(ManipulatableObject parent, int damage) {
		super(parent, parent.position.x, parent.position.y, 1, 1);
		this.removesItself = false;

		knockbackSpeed = 10;

		this.setImage(Assets.instance.weapons.sword);

	}

	@Override
	public void interact(AbstractGameObject couple) {

		if (couple instanceof ManipulatableObject) {
			ManipulatableObject temp = (ManipulatableObject) couple;

			if (!this.isSameTeam(temp)) {
				AOE deathAttack = new AOE(Assets.instance.effects.explosion,
						parent, 5, position.x, position.y);

				LevelStage.interactables.add(deathAttack);
				removeThyself();
			}
		}
		if (couple instanceof Arrow) {
			if (stateTime > .10f) {
				AOE deathAttack = new AOE(Assets.instance.effects.explosion,
						parent, 5, position.x, position.y);
				LevelStage.interactables.add(deathAttack);
				removeThyself();
			}
		}

	}
}
