package game_objects.weapons;

import game_objects.ManipulatableObject;
import backend.Assets;

import com.badlogic.gdx.math.Vector2;

public class CloakAndDagger extends AbstractWeapon{

	public CloakAndDagger(ManipulatableObject parent, float width,
			float height, Vector2 positionOffset) {
		super(parent, width, height, positionOffset);
		
		origin.set(0, dimension.y / 2);
		defaultAttackCooldown = .3f;
		image = Assets.instance.weapons.sword;
		moveUp();
		name = "Cloak and Dagger";
	}

}
