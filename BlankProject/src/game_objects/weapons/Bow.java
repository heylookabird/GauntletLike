package game_objects.weapons;

import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.abilities.AOE;
import game_objects.abilities.Arrow;
import game_objects.abilities.ExplodingArrow;
import game_objects.abilities.Hook;
import game_objects.abilities.Net;
import game_objects.abilities.PowerArrow;
import game_objects.abilities.ProxyArrow;
import game_objects.abilities.TrapArrow;
import backend.Assets;
import backend.Calc;
import backend.LevelStage;

import com.badlogic.gdx.math.Vector2;

public class Bow extends AbstractWeapon {

	private float arrowSpeed;
	private int arrowDamage;
	private boolean trapOut = false;
	private TrapArrow exploding;

	public Bow(ManipulatableObject parent, float width, float height,
			Vector2 positionOffset) {
		super(parent, width, height, positionOffset);
		origin.set(0, dimension.y / 2);
		defaultAttackCooldown = 5f;
		image = Assets.instance.weapons.sword;
		moveUp();
		arrowSpeed = .5f;
		arrowDamage = 3;
		this.name = "Bow";
		
		this.ability1CoolDown = 3;
		this.ability2CoolDown = 6;
		this.ability3CoolDown = 4;
		this.ability4CoolDown = 10;
	}

	public void setPlayerBow() {
		this.defaultAttackCooldown = .4f;
	}

	@Override
	protected void defaultAttackInit(DIRECTION direction) {

		if (direction == DIRECTION.LEFT)
			defaultAttack = new Arrow(parent, arrowDamage, -arrowSpeed, 0);

		else if (direction == DIRECTION.RIGHT)
			defaultAttack = new Arrow(parent, arrowDamage, arrowSpeed, 0);

		else if (direction == DIRECTION.DOWN)
			defaultAttack = new Arrow(parent, arrowDamage, 0, -arrowSpeed);
		else if (direction == DIRECTION.UP)
			defaultAttack = new Arrow(parent, arrowDamage, 0, arrowSpeed);

		LevelStage.interactables.add(defaultAttack);
	}

	@Override
	protected void defaultAttackInit() {
		// Vector's filled in with proper velocity speed
		Vector2 temp = new Vector2();

		// CHECK IF MOVING VERTICALLY
		if (parent.velocity.y < 0) {
			temp.y = -arrowSpeed;
		} else if (parent.velocity.y > 0) {
			temp.y = arrowSpeed;
		}

		// CHECK IF MOVING SIDEWAYS
		if (parent.velocity.x < 0) {
			temp.x = -arrowSpeed;
			// System.out.println("FACING LEFT");
		} else if (parent.velocity.x > 0) {
			temp.x = arrowSpeed;
		}

		// IF NOT MOVING CHECK BY DIRECTION
		if (velocity.isZero()) {
			// CHECK IF MOVING VERTICALLY
			if (parent.facing == DIRECTION.DOWN) {
				temp.y = -arrowSpeed;
			} else if (parent.facing == DIRECTION.UP) {
				temp.y = arrowSpeed;
			}

			// CHECK IF MOVING SIDEWAYS
			if (parent.facing == DIRECTION.LEFT) {
				temp.x = -arrowSpeed;
				// System.out.println("FACING LEFT");
			} else if (parent.facing == DIRECTION.RIGHT) {
				temp.x = arrowSpeed;
			}
		}

		// Initialize Attack
		defaultAttack = new Arrow(parent, arrowDamage, temp.x, temp.y);
		LevelStage.interactables.add(defaultAttack);
	}

	@Override
	protected void defaultAttackInit(Vector2 rightJoyStick) {
		float angle = Calc.atan2(rightJoyStick, new Vector2());
		float speedX = Calc.cos(angle) * -arrowSpeed;
		float speedY = Calc.sin(angle) * arrowSpeed;
		defaultAttack = new Arrow(parent, arrowDamage, speedX, speedY);
		
		LevelStage.interactables.add(defaultAttack);
	}

	@Override
	public void ability1(float direction) {

		Hook attack = new Hook(parent, direction, 10);
		// to shoot the arrow in correct spot
		/*switch (dir) {

		case UP:
			attack = new Hook(parent, 1, 0, arrowSpeed);
			break;

		case DOWN:
			attack = new ExplodingArrow(parent, 1, 0, -arrowSpeed);
			break;

		case RIGHT:
			attack = new ExplodingArrow(parent, 1, arrowSpeed, 0);
			break;

		case LEFT:
			attack = new ExplodingArrow(parent, 1, -arrowSpeed, 0);
			break;
		}*/
		

		LevelStage.interactables.add(attack);
	}

	@Override
	public void ability2(float direction) {
		ExplodingArrow attack = null;
		float speed = arrowSpeed/2f;
		DIRECTION dir = Calc.degreesToDirection(direction);
		switch (dir) {

		case UP:
			attack = new ExplodingArrow(parent, 1, 0, arrowSpeed){
				@Override
				public void removeThyself() {
					super.removeThyself();
					Net deathAttack = new Net(Assets.instance.effects.iceExplosion, parent, 1, .3f, .5f,
							position.x, position.y, 1, 1, false);

					LevelStage.interactables.add(deathAttack);
				}
			};
			break;

		case DOWN:
			attack = new ExplodingArrow(parent, 1, 0, -arrowSpeed){
				@Override
				public void removeThyself() {
					super.removeThyself();
					Net deathAttack = new Net(Assets.instance.effects.iceExplosion, parent, 1, .3f, .5f,
							position.x, position.y, 1, 1, false);

					LevelStage.interactables.add(deathAttack);
				}
			};
			break;

		case RIGHT:
			attack = new ExplodingArrow(parent, 1, arrowSpeed, 0){
				@Override
				public void removeThyself() {
					super.removeThyself();
					Net deathAttack = new Net(Assets.instance.effects.iceExplosion, parent, 1, .3f, .5f,
							position.x, position.y, 1, 1, false);

					LevelStage.interactables.add(deathAttack);
				}
			};
			break;

		case LEFT:
			attack = new ExplodingArrow(parent, 1, -arrowSpeed, 0){
				@Override
				public void removeThyself() {
					super.removeThyself();
					Net deathAttack = new Net(Assets.instance.effects.iceExplosion, parent, 1, .3f, .5f,
							position.x, position.y, 1, 1, false);

					LevelStage.interactables.add(deathAttack);
				}
			};
			break;
		}
		
		LevelStage.interactables.add(attack);


	}

	@Override
	public void ability3(float direction) {
		Arrow attack = null;
		float speed = arrowSpeed/3f;
		DIRECTION dir = Calc.degreesToDirection(direction);
		// to shoot the arrow in correct spot
		switch (dir) {

		case UP:
			attack = new ProxyArrow(parent, 1, 0, speed);
			break;

		case DOWN:
			attack = new ProxyArrow(parent, 1, 0, -speed);
			break;

		case RIGHT:
			attack = new ProxyArrow(parent, 1, speed, 0);
			break;

		case LEFT:
			attack = new ProxyArrow(parent, 1, -speed, 0);
			break;
		}

		LevelStage.interactables.add(attack);
	}

	@Override
	public void ability4(float direction) {		
		PowerArrow attack = null;
		DIRECTION dir = Calc.degreesToDirection(direction);
		float arrowSpeed = this.arrowSpeed * 4;
		int damage = 5;
		// to shoot the arrow in correct spot
		switch (dir) {

		case UP:
			attack = new PowerArrow(parent, damage, 0, arrowSpeed);
			break;

		case DOWN:
			attack = new PowerArrow(parent, damage, 0, -arrowSpeed);
			break;

		case RIGHT:
			attack = new PowerArrow(parent, damage, arrowSpeed, 0);
			break;

		case LEFT:
			attack = new PowerArrow(parent, damage, -arrowSpeed, 0);
			break;
		}

		LevelStage.interactables.add(attack);
	}
	
}
