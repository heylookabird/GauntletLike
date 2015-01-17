package game_objects.weapons;

import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.abilities.BasicMelee;
import backend.Assets;
import backend.LevelStage;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class DualBlades extends AbstractWeapon {
	private TextureRegion sword2;
	private boolean dualBlade;
	public DualBlades(ManipulatableObject parent, float width, float height,
			Vector2 positionOffset) {
		super(parent, width, height, positionOffset);
		origin.set(0, dimension.y / 2);
		defaultAttackCooldown = .3f;
		image = Assets.instance.weapons.sword;
		sword2 = Assets.instance.weapons.sword;
		moveUp();
		dualBlade = true;
	}
	
	
	@Override
	protected void defaultAttackInit(DIRECTION direction) {
		
		defaultAttack = new BasicMelee(parent, 1, direction);
		
		
		
		LevelStage.interactables.add(defaultAttack);
	}
	

}
