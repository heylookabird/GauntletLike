package game_objects.abilities;

import backend.Calc;
import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.ManipulatableObject.STATE;

public class BasicMelee extends AbstractAbility {

	public BasicMelee(ManipulatableObject parent, int damage) {
		super(parent, parent.position.x, parent.position.y, 1, 1);
		this.damage = damage;
		removesItself = true;
		initDebug();
	}

	public BasicMelee(ManipulatableObject parent, int damage,
			float hitBoxTimer, DIRECTION direction) {

		super(parent, parent.position.x, parent.position.y, 1, 1);
		this.damage = damage;
		removesItself = true;
		parent.stopMove();
		initDebug();
		lifeTimer = hitBoxTimer;
		
		knockbackSpeed = 4;
		knockbackTime = .2f;
		deletionTime = hitBoxTimer;
		if (direction == DIRECTION.LEFT) {
			// position.set(parent.position.x - dimension.x, position.y);
			// parent.moveLeft();
			position.set(position.x - parent.dimension.x,
					(position.y + parent.dimension.y / 2) - dimension.y / 2);
			knockbackAngle = 180;

		} else if (direction == DIRECTION.RIGHT) {
			// position.set(parent.position.x + dimension.x, position.y);
			// parent.moveRight();
			position.set(parent.position.x + parent.dimension.x,
					(position.y + parent.dimension.y / 2) - dimension.y / 2);
			knockbackAngle = 0;

		} else if (direction == DIRECTION.UP) {
			// position.set(parent.position.x, position.y + dimension.y);
			// parent.moveUp();
			position.set(position.x + parent.dimension.x / 2 - dimension.x / 2,
					position.y + parent.dimension.y);
			knockbackAngle = 90;

		} else if (direction == DIRECTION.DOWN) {
			// position.set(parent.position.x, position.y - dimension.y);
			// parent.moveDown();
			position.set(position.x + parent.dimension.x / 2 - dimension.x / 2,
					position.y - parent.dimension.y);
			knockbackAngle = 270;


		}
		parent.stopMove();
		parent.state = STATE.ATTACKING;
	}

	public BasicMelee(ManipulatableObject parent, int damage,
			float hitBoxTimer, DIRECTION direction, float size) {
		super(parent, parent.position.x, parent.position.y, size, size);
		this.damage = damage;
		removesItself = true;
		parent.stopMove();
		initDebug();
		lifeTimer = hitBoxTimer;
		if (direction == DIRECTION.LEFT) {
			// position.set(parent.position.x - dimension.x, position.y);
			// parent.moveLeft();
			position.set(position.x - dimension.x,
					(position.y + parent.dimension.y / 2) - dimension.y / 2);
		} else if (direction == DIRECTION.RIGHT) {
			// position.set(parent.position.x + dimension.x, position.y);
			// parent.moveRight();
			position.set(parent.position.x + parent.dimension.x,
					(position.y + parent.dimension.y / 2) - dimension.y / 2);
		} else if (direction == DIRECTION.UP) {
			// position.set(parent.position.x, position.y + dimension.y);
			// parent.moveUp();
			position.set(position.x + parent.dimension.x / 2 - dimension.x / 2,
					position.y + parent.dimension.y);
		} else if (direction == DIRECTION.DOWN) {
			// position.set(parent.position.x, position.y - dimension.y);
			// parent.moveDown();
			position.set(position.x + parent.dimension.x / 2 - dimension.x / 2,
					position.y - dimension.y);

		}
		
		this.defaultKnockBackAngle(direction);
		parent.stopMove();
		parent.state = STATE.ATTACKING;
	}

	@Override
	public void update(float deltaTime) {
		position.x += parent.deltax;
		position.y += parent.deltay;
		Calc.sin(Calc.atan2(position, getCenter()));
		super.update(deltaTime);
	}

	@Override
	protected void removeThyself() {
		parent.checkStopMove();
		parent.stopMove();
		super.removeThyself();
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

}
