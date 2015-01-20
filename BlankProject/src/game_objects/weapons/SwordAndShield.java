package game_objects.weapons;

import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.abilities.BasicMelee;
import game_objects.abilities.Charge;
import game_objects.abilities.ShieldThrow;
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
		ability2CoolDown = 4;
		moveUp();
		name = "Sword and Shield";
	}
	@Override
	protected void defaultAttackInit(DIRECTION direction) {
		defaultAttack = new BasicMelee(parent, 3, .5f, direction);
		LevelStage.interactables.add(defaultAttack);

	}
	@Override
	protected void defaultAttackInit() {
		defaultAttack = new BasicMelee(parent, 3,  .5f, parent.facing);
		LevelStage.interactables.add(defaultAttack);
	}
	
	@Override
	public void ability1(DIRECTION direction) {
		Charge charge = new Charge(parent, parent.position.x , parent.position.y,
				parent.dimension.x, parent.dimension.y, .25f, .25f);
		LevelStage.interactables.add(charge);
		
	}
	@Override
	public void ability2(DIRECTION direction) {
		ShieldThrow charge = new ShieldThrow(parent, parent.position.x , parent.position.y,
				parent.dimension.x, parent.dimension.y, .3f);
		LevelStage.interactables.add(charge);
		
	}
	

	@Override
	protected void defaultAttackInit(Vector2 rightJoyStick) {
		boolean xAxis = Math.abs(rightJoyStick.x) > Math.abs(rightJoyStick.y);
		
		if(xAxis){
			if(rightJoyStick.x < 0){
				defaultAttackInit(DIRECTION.LEFT);
			}else if(rightJoyStick.x > 0)
				defaultAttackInit(DIRECTION.RIGHT);
		}else{
			if(rightJoyStick.y < 0)
				defaultAttackInit(DIRECTION.UP);
			else if(rightJoyStick.y > 0)
				defaultAttackInit(DIRECTION.DOWN);
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
	}
}
