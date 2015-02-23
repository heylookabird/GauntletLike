package game_objects.weapons;

import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.abilities.Beam;
import game_objects.abilities.CooldownAccelerate;
import game_objects.abilities.Heal;
import game_objects.abilities.HolyShield;
import backend.Assets;
import backend.Calc;
import backend.LevelStage;

import com.badlogic.gdx.math.Vector2;

public class HealingStaff extends AbstractWeapon {

	private int healRate = 3;
	private float speed = .5f;

	public HealingStaff(ManipulatableObject parent, float width, float height,
			Vector2 positionOffset) {
		super(parent, width, height, positionOffset);
		origin.set(0, dimension.y / 2);
		defaultAttackCooldown = 1f;
		image = Assets.instance.weapons.sword;
		moveUp();
		
		this.ability1CoolDown = 1;
		this.ability2CoolDown = 20;
		this.ability3CoolDown = 30;
		this.ability4CoolDown = 40;

		name = "Healing Staff";
	}

	@Override
	protected void defaultAttackInit(DIRECTION direction) {
		defaultAttack = new Beam(parent, direction, 4, true, 1, 1, 7, 1f);
		LevelStage.interactables.add(defaultAttack);
	}
	
	
	@Override
	protected void defaultAttackInit(Vector2 rightJoyStick) {
		Vector2 x = new Vector2(rightJoyStick);
		x.y *= -1;
		System.out.println("working");
		defaultAttack = new Beam(parent, Calc.atan2(x), 4, true, 1, 1, 7, 1f);
		LevelStage.interactables.add(defaultAttack);


	}
	@Override
	public void ability1(float direction) {
		Heal attack = new Heal(parent, healRate, 1, false);
		
		LevelStage.interactables.add(attack);
	}

	@Override
	public void ability2(float direction) {

		Heal attack = new Heal(parent, healRate, 3, true);
		LevelStage.interactables.add(attack);
	}

	@Override
	public void ability3(float direction) {
		HolyShield attack = new HolyShield(parent, 20, 3, true);
		LevelStage.interactables.add(attack);

	}

	@Override
	public void ability4(float direction) {
		System.out.println("ab4");
		CooldownAccelerate attack = new CooldownAccelerate(parent, 5f);
		LevelStage.interactables.add(attack);
		
	}

}
