package game_objects.abilities.effects;

import game_objects.ManipulatableObject;

public class Poison extends Effect {
	float rate;

	public Poison(ManipulatableObject parent, float poisonRate, float time) {
		super(parent);
		rate = poisonRate;
		this.lifeTimer = time;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		parent.takeHitFor(rate, null);
		
	}

}
