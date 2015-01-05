package game_objects.weapons;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.abilities.AbstractAbility;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class AbstractWeapon extends AbstractGameObject {

	ManipulatableObject parent;
	private Vector2 positionOffset;
	
	protected Array<AbstractAbility> abilities;
	
	
	//ATTACKS
	 protected float defaultAttackTimer, defaultAttackCooldown;
	 protected AbstractAbility defaultAttack;

	public AbstractWeapon(ManipulatableObject parent, float width, float height, Vector2 positionOffset) {
		super(parent.position.x, parent.position.y, width, height);
		this.parent = parent;
		this.positionOffset = positionOffset;
		defaultAttackCooldown = 5;
		defaultAttackTimer = 1;
		abilities = new Array<AbstractAbility>();
	
	}
	public void moveRight(){
		rotation = 0;
		positionOffset.set(parent.dimension.x /2 - .1f, parent.dimension.y / 3 - .2f);
		parent.primaryBehind = false;
	}
	public void moveLeft(){
		rotation = 180;
		positionOffset.set(parent.dimension.x / 2, parent.dimension.y / 2);
		parent.primaryBehind = true;


		
	}
	public void moveUp(){
		rotation = 90;
		positionOffset.set(parent.dimension.x, parent.dimension.y / 2);

	}
	public void moveDown(){
		rotation = 270;
		positionOffset.set(0, parent.dimension.y / 2);

		
	}
	@Override
	public void update(float deltaTime) {
		
		updateCooldowns(deltaTime);
		defaultAttackCheck();
		
		position.set(parent.position.x + positionOffset.x, parent.position.y + positionOffset.y);
		super.update(deltaTime);
	}
	private void updateCooldowns(float deltaTime) {
		defaultAttackTimer -= deltaTime;
	}
	
	//CHECK IF POSSIBLE TO DO THE DEFAULT ATTACK
	protected void defaultAttackCheck(){
		if(defaultAttackTimer < 0){
			defaultAttackInit();
			defaultAttackTimer = defaultAttackCooldown;
		}
	}
	
	//OVERRIDEN IN SUBCLASSES
	protected void defaultAttackInit() {
		
	}
	
	
	

	

}