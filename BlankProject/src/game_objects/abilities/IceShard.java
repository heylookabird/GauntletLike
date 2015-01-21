package game_objects.abilities;

import backend.Assets;
import backend.LevelStage;
import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.Wall;
import game_objects.ManipulatableObject.DIRECTION;

public class IceShard extends AbstractAbility {
	DIRECTION direction;
	float startingSize;
	int maxSpikes = 9, currentSpike;

	public IceShard(ManipulatableObject parent, int damage, float x, float y,
			float startingSize, int spikeNum, DIRECTION direction) {
		super(parent, x, y, startingSize, startingSize);
		this.setAnimation(Assets.instance.effects.iceShard);
		this.direction = direction;
		this.startingSize = startingSize;
		this.lifeTimer = .1f;
		currentSpike = spikeNum;
	}
	
	//constructor with this initially and things will multiply from there
	public IceShard(ManipulatableObject parent, int damage, float x, float y,
			float startingSize) {
		super(parent, x, y, startingSize, startingSize);
		this.setAnimation(Assets.instance.effects.explosion);
		direction = parent.facing;
		this.startingSize = startingSize;
		this.lifeTimer = .1f;
		currentSpike = 0;
	}
	
	

	@Override
	public void postDeathEffects() {
		currentSpike++;

		if (currentSpike < maxSpikes) {
			IceShard attack = new IceShard(parent, damage, position.x,
					position.y, startingSize, currentSpike, direction);

			switch (direction) {
			case UP:
				attack.position.y += bounds.width;
				break;

			case DOWN:
				attack.position.y -= attack.bounds.width;
				break;

			case RIGHT:
				attack.position.x += bounds.width;
				break;

			case LEFT:
				attack.position.x -= attack.bounds.width;
				break;
			}

			LevelStage.interactables.add(attack);
		}
	}
	
	@Override
	public void interact(AbstractGameObject couple) {
		super.interact(couple);
		
		if (couple instanceof ManipulatableObject) {
			ManipulatableObject obj = (ManipulatableObject) couple;
			if (!this.isSameTeam(obj)) {
				obj.takeHitFor(damage, this);
			}
		}
	}

}
