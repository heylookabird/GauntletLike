package game_objects.weapons;

import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.abilities.BasicMelee;
import game_objects.abilities.Slash;
import game_objects.abilities.Teleport;
import game_objects.abilities.ThrowSword;
import backend.Assets;
import backend.Calc;
import backend.LevelStage;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class DualBlades extends AbstractWeapon {
	private TextureRegion sword2;
	private boolean dualBlade = true;
	private float throwSpeed = .5f;
	private ThrowSword secondary;

	public DualBlades(ManipulatableObject parent, float width, float height,
			Vector2 positionOffset) {
		super(parent, width, height, positionOffset);
		origin.set(0, dimension.y / 2);
		defaultAttackCooldown = 1f;
		image = Assets.instance.weapons.sword;
		sword2 = Assets.instance.weapons.sword;
		moveUp();
		ability1CoolDown = 3;
		ability2CoolDown = 2;
		ability3CoolDown = 5;
		ability4CoolDown = 3;
		dualBlade = true;
		name = "Dual Blades";
				

	}

	@Override
	protected void defaultAttackInit(DIRECTION direction) {

		defaultAttack = new BasicMelee(parent, 3, .5f, direction);

		LevelStage.interactables.add(defaultAttack);
	}

	@Override
	public void ability1(float direction) {
		DIRECTION dir = Calc.degreesToDirection(direction);

		BasicMelee attack = new BasicMelee(parent, 4, 1, dir, 2);

		LevelStage.interactables.add(attack);
	}

	@Override
	public void ability2(float direction) {
		Slash attack;
		DIRECTION dir = Calc.degreesToDirection(direction);

		if (dualBlade)
			attack = new Slash(parent, dir, 3, true);
		else
			attack = new Slash(parent, dir, 3, false);

		LevelStage.interactables.add(attack);

	}

	@Override
	public void ability3(float direction) {
		DIRECTION dir = Calc.degreesToDirection(direction);

		if (this.dualBlade) {

			int arrowDamage = 2;
			if (dir == DIRECTION.LEFT) {
				secondary = new ThrowSword(parent, arrowDamage, -throwSpeed, 0, 1);
				secondary.setKnockBackAngle(180);
			} else if (dir == DIRECTION.RIGHT) {
				secondary = new ThrowSword(parent, arrowDamage, throwSpeed, 0, 1);
				secondary.setKnockBackAngle(0);

			} else if (dir == DIRECTION.DOWN) {
				secondary = new ThrowSword(parent, arrowDamage, 0, -throwSpeed, 1);
				secondary.setKnockBackAngle(270);

			} else if (dir == DIRECTION.UP) {
				secondary = new ThrowSword(parent, arrowDamage, 0, throwSpeed, 1);
				secondary.setKnockBackAngle(90);

			}

			LevelStage.interactables.add(secondary);

			dualBlade = false;
		} else {
			if (secondary.activate()) {
				parent.removePassive(secondary);
				dualBlade = true;
			}
		}
	}

	@Override
	public void ability4(float direction) {
		if (dualBlade) {

			dualBlade = false;
			secondary = new ThrowSword(parent, 4, 0, 0, 2);
			LevelStage.interactables.add(secondary);
		} else {
			if (secondary.activate()) {
				parent.removePassive(secondary);
				dualBlade = true;

			}

		}
		

	}

	@Override
	protected void defaultAttackInit(Vector2 rightJoyStick) {
		boolean xAxis = Math.abs(rightJoyStick.x) > Math.abs(rightJoyStick.y);

		if (xAxis) {
			if (rightJoyStick.x < 0) {
				defaultAttackInit(DIRECTION.LEFT);
			} else if (rightJoyStick.x > 0)
				defaultAttackInit(DIRECTION.RIGHT);
		} else {
			if (rightJoyStick.y < 0)
				defaultAttackInit(DIRECTION.UP);
			else if (rightJoyStick.y > 0)
				defaultAttackInit(DIRECTION.DOWN);
		}
	}

}
