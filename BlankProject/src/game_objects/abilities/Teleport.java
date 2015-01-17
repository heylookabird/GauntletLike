package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import backend.Assets;
import backend.LevelStage;

import com.badlogic.gdx.math.Vector2;

public class Teleport extends AbstractAbility {
	float teleTime = .05f;
	boolean teleported = false;

	public Teleport(ManipulatableObject parent, int damage, DIRECTION facing,
			float distance) {

		super(parent, 10, 10, 2, 2);// position will be set a few lines lower
									// anyways

		lifeTimer = .3f;
		this.damage = damage;
		this.stunTime = .4f;

		switch (facing) {

		case UP:
			position.set(parent.position.x, parent.position.y
					+ parent.dimension.y + distance);
			break;

		case LEFT:
			position.set(parent.position.x - distance, parent.position.y);
			break;

		case RIGHT:
			position.set(parent.position.x + parent.dimension.x + distance,
					parent.position.y);
			break;

		case DOWN:
			position.set(parent.position.x, parent.position.y - distance);
			break;
		}
	}

	public Teleport(ManipulatableObject parent, Vector2 position, int damage) {
		super(parent, 10, 10, 2, 2);
		this.damage = damage;
		lifeTimer = .4f;
		this.damage = damage;
		this.stunTime = .4f;
		this.teleTime = lifeTimer;
		this.position.set(position);
		this.knockbackSpeed = 10;
		this.removesItself = false;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		if(stateTime > teleTime)
			teleport();
	}

	private void teleport() {

		if (LevelStage.isAreaFree(bounds)) {
			parent.position.set(this.position);
			teleported = true;
			removeThyself();
			postDeathEffects();
		}else{
			for(int i = 0; i < LevelStage.enemyControlledObjects.size; i++){
				if(bounds.contains(LevelStage.enemyControlledObjects.get(i).bounds)){
					LevelStage.enemyControlledObjects.get(i).takeHitFor(1, this);
				}
			}
		}
		
			
	}

	@Override
	public void interact(AbstractGameObject couple) {
		if (teleported) {
			ManipulatableObject obj = (ManipulatableObject) couple;
			if (!this.isSameTeam(obj)) {
				boolean newObj = isFirstInteraction(obj);

				if (newObj)
					obj.takeHitFor(damage, this);
			}
		}
	}
}
