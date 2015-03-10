package game_objects.abilities;

import backend.Assets;
import backend.Calc;
import backend.LevelStage;
import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;

public class Arrow extends AbstractAbility {
	protected boolean attackFinished = false;

	public Arrow(ManipulatableObject parent, int damage, float xVelocity,
			float yVelocity) {
		super(parent, parent.position.x, parent.position.y, 1, 1);
		this.damage = damage;
		removesItself = true;
		this.lifeTimer = .5f;
		this.setImage(Assets.instance.weapons.sword);
		this.velocity.set(xVelocity, yVelocity);

		double angle = Math.atan2(velocity.y, velocity.x);
		rotation = (float) Math.toDegrees(angle);
		this.knockbackTime = .8f;
		this.knockbackSpeed = 7;
	}
	public Arrow(ManipulatableObject parent, int damage, float x, float y, float width, float height, float speed, float angle) {
		super(parent, x, y, width, height, angle, speed);
		this.damage = damage;
		removesItself = true;
		this.lifeTimer = .5f;
		this.setImage(Assets.instance.weapons.sword);

		this.knockbackTime = .8f;
		this.knockbackSpeed = 7;
	}
	public Arrow(ManipulatableObject parent, int damage, float x, float y, float width, float height, float speed, float angle, float lifeTimer) {
		super(parent, x, y, width, height, angle, speed);
		this.damage = damage;
		removesItself = true;
		this.lifeTimer = lifeTimer;
		this.setImage(Assets.instance.weapons.sword);

		this.knockbackTime = .8f;
		this.knockbackSpeed = 7;
	}

	@Override
	public void postDeathEffects() {
		attackFinished = true;
		// LevelStage.uncollidableObjects.add(this);
		velocity.set(0, 0);
	}

	@Override
	public void update(float deltaTime) {
		if (!attackFinished)
			super.update(deltaTime);
		

	}

	public boolean arrowStopped() {
		return attackFinished;
	}

	@Override
	public void interact(AbstractGameObject couple) {
		super.interact(couple);

		if (couple instanceof ManipulatableObject) {
			ManipulatableObject obj = (ManipulatableObject) couple;
			if (!this.isSameTeam(obj)) {
				obj.takeHitFor(damage, this);
				lifeTimer = -1;
			}
		}
	}
}
