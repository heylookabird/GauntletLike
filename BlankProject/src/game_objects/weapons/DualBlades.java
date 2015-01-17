package game_objects.weapons;

import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.abilities.BasicMelee;
import game_objects.abilities.Slash;
import backend.Assets;
import backend.LevelStage;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class DualBlades extends AbstractWeapon {
	private TextureRegion sword2;
	private boolean dualBlade = true;
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
		
		defaultAttack = new BasicMelee(parent, 3, .5f, direction);
		
		
		
		LevelStage.interactables.add(defaultAttack);
	}
	
	@Override
	public void ability1(DIRECTION direction) {
		BasicMelee attack = new BasicMelee(parent, 4, 1, direction);
		
		LevelStage.interactables.add(attack);
	}

	@Override
	public void ability2(DIRECTION direction) {
		Slash attack;
		
		if(dualBlade)
			attack = new Slash(parent, direction, 3, true);
		else
			attack = new Slash(parent, direction, 3, false);
		
		LevelStage.interactables.add(attack);
	}

	@Override
	public void ability3(DIRECTION direction) {

	}

	@Override
	public void ability4(DIRECTION direction) {

	}
	

}
