package game_objects.weapons;

import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.abilities.Arrow;
import backend.Assets;
import backend.LevelStage;

import com.badlogic.gdx.math.Vector2;

public class Bow extends AbstractWeapon{
	
	public Bow(ManipulatableObject parent, float width, float height, Vector2 positionOffset) {
		super(parent, width, height, positionOffset);
		origin.set(0, dimension.y / 2);
		image = Assets.instance.weapons.sword;
		moveUp();
		
	}
	
	@Override
	protected void defaultAttackInit() {
		if(parent.facing == DIRECTION.UP)
			defaultAttack = new Arrow(parent, 1, 0, .3f);
		else if(parent.facing == DIRECTION.DOWN)
			defaultAttack = new Arrow(parent, 1, 0, -.3f);
		else if(parent.facing == DIRECTION.LEFT)
			defaultAttack = new Arrow(parent, 1, -.3f, 0);
		else
			defaultAttack = new Arrow(parent, 1, .3f, 0);
		
		LevelStage.interactables.add(defaultAttack);
	}
}
