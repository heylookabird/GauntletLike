package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.ManipulatableObject.STATE;
import backend.Assets;
import backend.LevelStage;

public class Slash extends AbstractAbility {
	private boolean doublehit;
	private float slashspeed = .2f;
	private DIRECTION facing;

	public Slash(ManipulatableObject parent, float x, float y, float width,
			float height, int damage, DIRECTION direction, boolean doublehit) {
		super(parent, x, y, width, height);
		this.doublehit = doublehit;
		this.damage = damage;
		this.removesItself = true;
		facing = direction;

		this.setImage(Assets.instance.weapons.sword);

		this.lifeTimer = .2f;

		if (direction == DIRECTION.LEFT) {
			position.set(parent.position.x, position.y);
			this.rotation = 180;
			// parent.moveLeft();
		} else if (direction == DIRECTION.RIGHT) {
			position.set(parent.position.x + dimension.x, position.y);

			// parent.moveRight();

		} else if (direction == DIRECTION.UP) {
			position.set(parent.position.x, position.y + parent.dimension.y);
			this.rotation = 90;

			// parent.moveUp();

		} else if (direction == DIRECTION.DOWN) {
			position.set(parent.position.x, position.y - parent.dimension.y);
			this.rotation = 270;

			// parent.moveDown();

		}
		parent.stopMove();
		parent.state = STATE.ATTACKING;
	}

	public Slash(ManipulatableObject parent, DIRECTION facing, int damage,
			boolean doublehit) {

		super(parent, 10, 10, 3, 3);// position will be set a few lines lower
									// anyways

		this.facing = facing;
		lifeTimer = .2f;
		this.damage = damage;
		this.doublehit = doublehit;

		this.setImage(Assets.instance.weapons.sword);
		
		this.defaultKnockBackAngle(facing);

		if (doublehit) {
			positionHit1();
		} else
			positionHit2();
		
		fixRotation(facing);
		priority = 2;

	}
	
	private void fixRotation(DIRECTION facing){
		if (facing == DIRECTION.LEFT) {
			this.rotation = 180;
			// parent.moveLeft();
		} else if (facing == DIRECTION.RIGHT) {

			// parent.moveRight();

		} else if (facing == DIRECTION.UP) {
			this.rotation = 90;

			// parent.moveUp();

		} else if (facing == DIRECTION.DOWN) {
			this.rotation = 270;
		}

	}

	private void positionHit2() {
		switch (facing) {

		// reposition to right
		case UP:
			position.set(parent.position.x + parent.bounds.width,
					parent.position.y);
			break;

		// reposition to bottom
		case RIGHT:
			position.set(parent.position.x, parent.position.y - bounds.height);
			break;

		case LEFT:
			position.set(parent.position.x - bounds.width, parent.position.y
					+ parent.bounds.height);
			break;

		case DOWN:
			position.set(parent.position.x - bounds.width, parent.position.y - bounds.height);
			break;
		}
	}

	private void positionHit1() {
		switch (facing) {

		// position to left so cross is in middle
		case UP:
			position.x = parent.position.x - bounds.width;
			position.y = parent.position.y;
			break;

		// position below to bring to left
		case LEFT:
			position.set(parent.position.x - bounds.width, parent.position.y - bounds.height);
			break;

		// position above to bring to right
		case RIGHT:
			position.set(parent.position.x, parent.position.y + bounds.height);
			break;

		case DOWN:
			position.set(parent.position.x + bounds.width, parent.position.y - bounds.height);
			break;
		}
	}

	@Override
	public void update(float deltaTime) {
		/*
		 * if(doublehit){ this.bounds.x += slashspeed; this.bounds.y -=
		 * slashspeed; }else{ this.bounds.x += slashspeed; this.bounds.y +=
		 * slashspeed; }
		 */

		switch (facing) {

		// started from left so slash diag up
		case UP:
			if (doublehit)
				velocity.set(slashspeed, slashspeed);
			else
				// started from right
				velocity.set(-slashspeed, slashspeed);

			break;
		// started from above
		case RIGHT:
			if (doublehit)
				velocity.set(slashspeed, -slashspeed);
			else
				// started from below
				velocity.set(slashspeed, slashspeed);
			break;

		// started from below
		case LEFT:
			if (doublehit)
				velocity.set(-slashspeed, slashspeed);
			else
				velocity.set(-slashspeed, -slashspeed);

			break;
		// started from right
		case DOWN:
			if (doublehit)
				velocity.set(-slashspeed, -slashspeed);
			else
				velocity.set(slashspeed, -slashspeed);
			break;
		}

		super.update(deltaTime);

	}

	@Override
	public void interact(AbstractGameObject couple) {

		if (couple instanceof ManipulatableObject) {
			ManipulatableObject obj = (ManipulatableObject) couple;

			if (!isSameTeam(obj)) {
				boolean newObj = isFirstInteraction(obj);

				if (newObj)
					obj.takeHitFor(damage, this);
			}
		}

	}

	@Override
	public void postDeathEffects() {

		// clears, repositions, and resets lifetimer
		if (doublehit) {
			this.objectsAlreadyHit.clear();
			this.timers.clear();
			lifeTimer = .2f;
			doublehit = false;

			positionHit2();

			LevelStage.interactables.add(this);

		}
	}

}
