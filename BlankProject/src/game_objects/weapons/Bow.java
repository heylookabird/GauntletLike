package game_objects.weapons;

import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.abilities.Arrow;
import game_objects.abilities.ExplodingArrow;
import game_objects.abilities.Hook;
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
		Arrow attack = new ExplodingArrow(parent, 1, 0, 0);
		DIRECTION dir = Calc.degreesToDirection(direction);

		// to shoot the arrow in correct spot
		switch (dir) {

		case UP:
			attack.velocity.set(0, arrowSpeed);
			break;

		case DOWN:
			attack.velocity.set(0, -arrowSpeed);
			break;

		case RIGHT:
			attack.velocity.set(arrowSpeed, 0);
			break;

		case LEFT:
			attack.velocity.set(-arrowSpeed, 0);
			break;
		}

		LevelStage.interactables.add(attack);
	}

	@Override
	public void ability2(float direction) {
		float speed = arrowSpeed/2f;
		DIRECTION dir = Calc.degreesToDirection(direction);
		if (!trapOut) {
			exploding = new TrapArrow(parent, 3, 0, 0);
			switch (dir) {

			case UP:
				exploding.velocity.set(0, speed);
				break;

			case DOWN:
				exploding.velocity.set(0, -speed);
				break;

			case RIGHT:
				exploding.velocity.set(speed, 0);
				break;

			case LEFT:
				exploding.velocity.set(-speed, 0);
				break;
			}
			
			LevelStage.interactables.add(exploding);
			trapOut = true;
		}else{
			exploding.activate();
			trapOut = false;
		}
		
		

	}

	@Override
	public void ability3(float direction) {
		Arrow attack = new ProxyArrow(parent, 3, 0, 0);
		float speed = arrowSpeed/3f;
		DIRECTION dir = Calc.degreesToDirection(direction);
		System.out.println(dir);
		// to shoot the arrow in correct spot
		switch (dir) {

		case UP:
			attack.velocity.set(0, speed);
			break;

		case DOWN:
			attack.velocity.set(0, -speed);
			break;

		case RIGHT:
			attack.velocity.set(speed, 0);
			break;

		case LEFT:
			attack.velocity.set(-speed, 0);
			break;
		}

		LevelStage.interactables.add(attack);
	}

	@Override
	public void ability4(float direction) {
		float angle = direction;
		
		Hook attack = new Hook(parent, angle);

		// to shoot the arrow in correct spot
		

		LevelStage.interactables.add(attack);
	}
	
	
}
