package game_objects.abilities;

import com.badlogic.gdx.math.Vector2;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.abilities.effects.Cold;
import backend.Assets;
import backend.LevelStage;

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
		this.lifeTimer = .2f;
		currentSpike = spikeNum;
		this.damage = damage;
		this.priority = 1;
	}
	
	//constructor with this initially and things will multiply from there
	public IceShard(ManipulatableObject parent, int damage, float x, float y,
			float startingSize) {
		super(parent, x, y, startingSize, startingSize);
		this.setAnimation(Assets.instance.effects.explosion);
		direction = parent.facing;
		this.startingSize = startingSize;
		this.lifeTimer = .2f;
		currentSpike = 0;
		this.damage = damage;
	}
	
	

	@Override
	public void postDeathEffects() {
		currentSpike++;

		if (currentSpike < maxSpikes && !this.cancelled) {
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
	public void update(float deltaTime){
		super.update(deltaTime);
		spread();
		
		
	}
	
	private void spread() {
		dimension.x += .05f;
		dimension.y += .05f;
		bounds.width = dimension.x;
		bounds.height = dimension.y;
	}

	@Override
	public void interact(AbstractGameObject couple) {
		super.interact(couple);
		
		if (couple instanceof ManipulatableObject) {
			ManipulatableObject obj = (ManipulatableObject) couple;
			if (!this.isSameTeam(obj)) {
				obj.takeHitFor(damage, this);
				cold(obj, .25f, .5f);
			}
		}
	}

}
