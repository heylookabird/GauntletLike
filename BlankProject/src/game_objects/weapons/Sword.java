package game_objects.weapons;

import game_objects.ManipulatableObject;
import game_objects.abilities.AOE;
import backend.Assets;
import backend.LevelStage;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Sword extends AbstractWeapon {

	public Sword(ManipulatableObject parent, float width, float height, Vector2 positionOffset) {
		super(parent, width, height, positionOffset);
		origin.set(0, dimension.y / 2);
		image = Assets.instance.weapons.sword;
		moveUp();
	}
	
	@Override
	protected void defaultAttackInit() {
		defaultAttack = new AOE(Assets.instance.weapons.sword, parent, 1, position.x + origin.x, origin.y + position.y, 1, 1);
		LevelStage.interactables.add(defaultAttack);
	}

	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
	}
}
