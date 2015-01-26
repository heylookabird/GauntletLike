package game_objects.weapons;

import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.abilities.BasicMelee;
import game_objects.abilities.Charge;
import game_objects.abilities.Counter;
import game_objects.abilities.ShieldThrow;
import game_objects.abilities.Taunt;
import backend.Assets;
import backend.LevelStage;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class SwordAndShield extends AbstractWeapon {

	public boolean shieldOn;
	
	private ShieldThrow shield;
	private int hitCount;
	public SwordAndShield(ManipulatableObject parent, float width, float height, Vector2 positionOffset) {
		super(parent, width, height, positionOffset);
		origin.set(0, dimension.y / 2);
		image = Assets.instance.weapons.sword;
		defaultAttackCooldown = 1;
		ability1CoolDown = 3;
		ability2CoolDown = 2;
		ability3CoolDown = 5;
		ability4CoolDown = 10;
		shieldOn = true;
		moveUp();
		hitCount = 0;
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
	public void ability1(DIRECTION direction) {
		Charge charge = new Charge(parent, parent.position.x , parent.position.y,
				parent.dimension.x, parent.dimension.y, .25f, .25f);
		LevelStage.interactables.add(charge);
		
	}
	@Override
	public void ability2(DIRECTION direction) {
		shield = new ShieldThrow(parent, parent.position.x , parent.position.y,
				parent.dimension.x, parent.dimension.y, .3f, 7, 7, this);
		shieldOn = false;
		LevelStage.interactables.add(shield);
		
	}
	
	@Override
	public void ability3(DIRECTION direction) {
		Counter counter = new Counter(parent, parent.position.x , parent.position.y,
				parent.dimension.x, parent.dimension.y);
		
		LevelStage.interactables.add(counter);
		
	}
	
	@Override
	public void ability4(DIRECTION direction) {
		System.out.println(shieldOn + " sword and sheild");
		Taunt taunt = new Taunt(parent, shieldOn ? parent.getCenter().x - 4 : shield.getCenter().x - 4, shieldOn ? parent.getCenter().y - 4 : shield.getCenter().y - 4, 8, 8, shieldOn);
		LevelStage.interactables.add(taunt);
		if(!shieldOn){
			LevelStage.interactables.removeValue(shield, true);
			shield = null;
			shieldOn = true;
		}
	}
	

	

	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
	}
}
