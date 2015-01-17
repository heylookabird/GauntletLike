package game_objects.weapons;

import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.abilities.BasicMelee;
import game_objects.abilities.AOE;
import backend.Assets;
import backend.LevelStage;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class SwordAndShield extends AbstractWeapon {

	public SwordAndShield(ManipulatableObject parent, float width, float height, Vector2 positionOffset) {
		super(parent, width, height, positionOffset);
		origin.set(0, dimension.y / 2);
		image = Assets.instance.weapons.sword;
		moveUp();
	}
	@Override
	protected void defaultAttackInit(DIRECTION direction) {
		defaultAttack = new BasicMelee(parent, 3, .5f, direction);
		LevelStage.interactables.add(defaultAttack);

	}
	@Override
	protected void defaultAttackInit() {
		defaultAttack = new BasicMelee(parent, 3, .5f, parent.facing);
		LevelStage.interactables.add(defaultAttack);
	}

	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
	}
}
