package game_objects.weapons;

import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.abilities.BasicMelee;
import game_objects.abilities.AOE;
import game_objects.abilities.Charge;
import backend.Assets;
import backend.LevelStage;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class SwordAndShield extends AbstractWeapon {

	public SwordAndShield(ManipulatableObject parent, float width, float height, Vector2 positionOffset) {
		super(parent, width, height, positionOffset);
		origin.set(0, dimension.y / 2);
		image = Assets.instance.weapons.sword;
		ability1CoolDown = 3;
		moveUp();
		name = "Sword and Shield";
	}
	@Override
	protected void defaultAttackInit(DIRECTION direction) {
		defaultAttack = new AOE(Assets.instance.weapons.sword, parent, 3, direction);
		LevelStage.interactables.add(defaultAttack);

	}
	@Override
	protected void defaultAttackInit() {
		defaultAttack = new AOE(Assets.instance.weapons.sword, parent, 3, parent.facing);
		LevelStage.interactables.add(defaultAttack);
	}
	
	@Override
	public void ability1(DIRECTION direction) {
		System.out.println("sword and shield ability 1");
		Charge charge = new Charge(parent, parent.position.x - .15f, parent.position.y - .15f, parent.dimension.x + .3f, parent.dimension.y + .3f);
		LevelStage.interactables.add(charge);
		
	}

	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
	}
}
