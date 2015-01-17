package game_objects.weapons;

import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.abilities.BasicMelee;
import game_objects.abilities.Slash;
import game_objects.abilities.Teleport;
import game_objects.abilities.ThrowSword;
import backend.Assets;
import backend.LevelStage;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class DualBlades extends AbstractWeapon {
	private TextureRegion sword2;
	private boolean dualBlade = true;
	private float throwSpeed = .5f;
	private ThrowSword secondary;
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
		attack.bounds.width = 2;
		attack.bounds.height = 2;
		
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
		if(this.dualBlade){
		
		
		int arrowDamage = 2;
		if(direction == DIRECTION.LEFT)
			secondary = new ThrowSword(parent, arrowDamage, -throwSpeed, 0);

		else if(direction == DIRECTION.RIGHT)
			secondary = new ThrowSword(parent, arrowDamage, throwSpeed, 0);
		
		else if(direction == DIRECTION.DOWN)
			secondary = new ThrowSword(parent, arrowDamage, 0, -throwSpeed);
		else if(direction == DIRECTION.UP)
			secondary = new ThrowSword(parent, arrowDamage, 0, throwSpeed);
		
		LevelStage.interactables.add(secondary);
	
		dualBlade = false;
	}else{
		Teleport attack = new Teleport(parent, secondary.position, 1){
			@Override
			public void postDeathEffects() {
				super.postDeathEffects();
				ability1(parent.facing);
			}
		};
		LevelStage.uncollidableObjects.removeValue(secondary, true);
		LevelStage.interactables.add(attack);
		dualBlade = true;
	}
	}

	@Override
	public void ability4(DIRECTION direction) {
		if(dualBlade){

			dualBlade = false;
			secondary = new ThrowSword(parent, 4, 0, 0);
			LevelStage.interactables.add(secondary);
		}else{
			
			Teleport attack = new Teleport(parent, secondary.position, 1){
				@Override
				public void postDeathEffects() {
					super.postDeathEffects();
					ability2(parent.facing);
				}
			};
			
			LevelStage.uncollidableObjects.removeValue(secondary, true);
			LevelStage.interactables.add(attack);
			dualBlade = true;
		}
	}
	

}
