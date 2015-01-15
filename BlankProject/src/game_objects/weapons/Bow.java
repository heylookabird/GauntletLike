package game_objects.weapons;

import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.abilities.Arrow;
import backend.Assets;
import backend.LevelStage;

import com.badlogic.gdx.math.Vector2;

public class Bow extends AbstractWeapon{
	
	private float arrowSpeed;
	private int arrowDamage;
	public Bow(ManipulatableObject parent, float width, float height, Vector2 positionOffset) {
		super(parent, width, height, positionOffset);
		origin.set(0, dimension.y / 2);
		defaultAttackCooldown = .3f;
		image = Assets.instance.weapons.sword;
		moveUp();
		arrowSpeed = .5f;
		arrowDamage = 3;
	}
	
	@Override
	protected void defaultAttackInit(DIRECTION direction) {
		
		if(direction == DIRECTION.LEFT)
		defaultAttack = new Arrow(parent, arrowDamage, -arrowSpeed, 0);

		else if(direction == DIRECTION.RIGHT)
		defaultAttack = new Arrow(parent, arrowDamage, arrowSpeed, 0);
		
		else if(direction == DIRECTION.DOWN)
			defaultAttack = new Arrow(parent, arrowDamage, 0, -arrowSpeed);
		else if(direction == DIRECTION.UP)
			defaultAttack = new Arrow(parent, arrowDamage, 0, arrowSpeed);
		
		
		
		LevelStage.interactables.add(defaultAttack);
	}
	
	@Override
	protected void defaultAttackInit() {
		//Vector's filled in with proper velocity speed
		Vector2 temp = new Vector2();
		
		//CHECK IF MOVING VERTICALLY
		if(parent.velocity.y < 0){
			temp.y = -arrowSpeed;
		}else if(parent.velocity.y > 0){
			temp.y = arrowSpeed;
		}
		
		//CHECK IF MOVING SIDEWAYS
		if(parent.velocity.x < 0){
			temp.x = -arrowSpeed;
			//System.out.println("FACING LEFT");
		}else if(parent.velocity.x > 0){
			temp.x = arrowSpeed;
		}
		
		//IF NOT MOVING CHECK BY DIRECTION
		if(velocity.isZero()){
			//CHECK IF MOVING VERTICALLY
			if(parent.facing == DIRECTION.DOWN){
				temp.y = -arrowSpeed;
			}else if(parent.facing == DIRECTION.UP){
				temp.y = arrowSpeed;
			}

			//CHECK IF MOVING SIDEWAYS
			if(parent.facing == DIRECTION.LEFT){
				temp.x = -arrowSpeed;
				//System.out.println("FACING LEFT");
			}else if(parent.facing == DIRECTION.RIGHT){
				temp.x = arrowSpeed;
			}
		}
		
		//Initialize Attack
		defaultAttack = new Arrow(parent, arrowDamage, temp.x, temp.y);
		LevelStage.interactables.add(defaultAttack);
		System.out.println("aoetuaoetu Bow ");
	}
}
