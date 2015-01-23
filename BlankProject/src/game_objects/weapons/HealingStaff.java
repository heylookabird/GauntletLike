package game_objects.weapons;

import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.abilities.Heal;
import game_objects.abilities.IceShard;
import game_objects.abilities.MeatHook;
import backend.Assets;
import backend.LevelStage;

import com.badlogic.gdx.math.Vector2;

public class HealingStaff extends AbstractWeapon {

	private int healRate = 3;
	private float speed = .5f;

	public HealingStaff(ManipulatableObject parent, float width, float height,
			Vector2 positionOffset) {
		super(parent, width, height, positionOffset);
		origin.set(0, dimension.y / 2);
		defaultAttackCooldown = 5f;
		image = Assets.instance.weapons.sword;
		moveUp();

		name = "Healing Staff";
	}

	@Override
	protected void defaultAttackInit(DIRECTION direction) {

		if(direction == DIRECTION.LEFT)
			defaultAttack = new Heal(parent, healRate, -speed, 0);

			else if(direction == DIRECTION.RIGHT)
			defaultAttack = new Heal(parent, healRate, speed, 0);
			
			else if(direction == DIRECTION.DOWN)
				defaultAttack = new Heal(parent, healRate, 0, -speed);
			else if(direction == DIRECTION.UP)
				defaultAttack = new Heal(parent, healRate, 0, speed);
		
		LevelStage.interactables.add(defaultAttack);
	}

	@Override
	public void ability1(DIRECTION direction) {
		Heal attack = new Heal(parent, healRate);
		
		LevelStage.interactables.add(attack);
	}

	@Override
	public void ability2(DIRECTION direction) {

		IceShard attack = new IceShard(parent, 1, parent.position.x, parent.position.y, 1, 0, parent.facing);
		LevelStage.interactables.add(attack);
	}

	@Override
	public void ability3(DIRECTION direction) {
		MeatHook attack = new MeatHook(parent, 0, direction, .125f);
		LevelStage.interactables.add(attack);

	}

	@Override
	public void ability4(DIRECTION direction) {

			//LevelStage.interactables.add(attack);
		
	}

}
