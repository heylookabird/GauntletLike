package game_objects.abilities;

import backend.Assets;
import backend.LevelStage;
import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;

public class HolyShield extends AbstractAbility {
	float shieldHealth, maxShield;
	boolean original;

	public HolyShield(ManipulatableObject parent, float shield, float size,
			boolean orig) {
		super(parent, parent.position.x, parent.position.y, size, size);
		this.shieldHealth = shield;
		this.maxShield = shield;
		original = orig;

		this.setImage(Assets.instance.weapons.sword);
		System.out.println("Healing abi 3");

		parent.invulnerable = true;

		removesItself = false;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		position.set(parent.position.x + parent.bounds.width / 2f
				- bounds.width / 2f, parent.position.y + parent.bounds.height
				/ 2f - bounds.height / 2f);
		bounds.setPosition(parent.position.x, parent.position.y);

		if (shieldHealth <= 0) {
			removeThyself();
			postDeathEffects();
		}
	}

	@Override
	public void postDeathEffects() {
		if (original) {
			if (parent.findClosestAlly() != null) {
				ManipulatableObject ally = parent.findClosestAlly();
				if (!ally.invulnerable) {
					HolyShield smaller = new HolyShield(
							ally, maxShield, bounds.x,
							false);

					LevelStage.interactables.add(smaller);
				}
			}
		}

		parent.invulnerable = false;
	}

	@Override
	public void interact(AbstractGameObject couple) {
		super.interact(couple);

		if (couple instanceof AbstractAbility) {
			AbstractAbility obj = (AbstractAbility) couple;
			if (!this.isSameTeam(obj.parent)) {
				boolean newObj = isFirstInteraction(obj.parent);

				if (newObj)
					shieldHealth -= obj.damage;
			}
		}
	}
}
