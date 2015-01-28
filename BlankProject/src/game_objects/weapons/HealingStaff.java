package game_objects.weapons;

import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.abilities.Beam;
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
		defaultAttackCooldown = 1f;
		image = Assets.instance.weapons.sword;
		moveUp();

		name = "Healing Staff";
	}

	@Override
	protected void defaultAttackInit(DIRECTION direction) {
		defaultAttack = new Beam(parent, direction, 4, true, 1, 1, 7, .3f);

		/*if(direction == DIRECTION.LEFT)
			defaultAttack = new Heal(parent, healRate, -speed, 0, true);

			else if(direction == DIRECTION.RIGHT)
			defaultAttack = new Heal(parent, healRate, speed, 0, true);
			
			else if(direction == DIRECTION.DOWN)
				defaultAttack = new Heal(parent, healRate, 0, -speed, true);
			else if(direction == DIRECTION.UP)
				defaultAttack = new Heal(parent, healRate, 0, speed, true);
		*/
		LevelStage.interactables.add(defaultAttack);
	}

	@Override
	public void ability1(float direction) {
		Heal attack = new Heal(parent, healRate, true);
		
		LevelStage.interactables.add(attack);
	}

	@Override
	public void ability2(float direction) {

		IceShard attack = new IceShard(parent, 1, parent.position.x, parent.position.y, 1, 0, parent.facing);
		LevelStage.interactables.add(attack);
	}

	@Override
	public void ability3(float direction) {
		MeatHook attack = new MeatHook(parent, 0, direction, .125f);
		LevelStage.interactables.add(attack);

	}

	@Override
	public void ability4(float direction) {

			//LevelStage.interactables.add(attack);
		
	}

}
