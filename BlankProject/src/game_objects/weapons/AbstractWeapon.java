package game_objects.weapons;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.abilities.AbstractAbility;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Vector2;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;

public abstract class AbstractWeapon extends AbstractGameObject {

	ManipulatableObject parent;
	private Vector2 positionOffset;

	// protected Array<AbstractAbility> abilities;
	protected float[] abilityCooldowns;

	// ATTACKS
	protected float defaultAttackTimer, defaultAttackCooldown;
	protected AbstractAbility defaultAttack;
	protected int ability1CoolDown;
	protected int ability2CoolDown;
	protected int ability3CoolDown;
	protected int ability4CoolDown;

	public AbstractWeapon(ManipulatableObject parent, float width,
			float height, Vector2 positionOffset) {
		super(parent.position.x, parent.position.y, width, height);
		this.parent = parent;
		this.positionOffset = positionOffset;
		defaultAttackCooldown = 2;
		defaultAttackTimer = 1;
		abilityCooldowns = new float[4];
	}

	private boolean checkAttack(int index) {
		if (abilityCooldowns[index] < 0)
			return true;
		
		return false;
	}

	public void activateAbility1(DIRECTION direction) {
		if (checkAttack(0)) {
			ability1(direction);
			abilityCooldowns[0] = ability1CoolDown;
			
		}else{
			System.out.println(abilityCooldowns[0]);
	}
	}


	// override methods
	public void ability1(DIRECTION direction) {

	}

	public void ability2(DIRECTION direction) {

	}

	public void ability3(DIRECTION direction) {

	}

	public void ability4(DIRECTION direction) {

	}

	public void activateAbility2(DIRECTION direction) {
		if (checkAttack(1)) {
			ability2(direction);
			abilityCooldowns[1] = ability2CoolDown;
		}


	}

	public void activateAbility3(DIRECTION direction) {
		if (checkAttack(2)) {
			ability3(direction);
			abilityCooldowns[2] = ability3CoolDown;
		}

	}

	public void activateAbility4(DIRECTION direction) {
		if (checkAttack(3)) {
			ability4(direction);
			abilityCooldowns[3] = ability4CoolDown;
	}
	}

	public void moveRight() {
		rotation = 0;
		positionOffset.set(parent.dimension.x / 2 - .1f,
				parent.dimension.y / 3 - .2f);
		parent.primaryBehind = false;
	}

	public void moveLeft() {
		rotation = 180;
		positionOffset.set(parent.dimension.x / 2, parent.dimension.y / 2);
		parent.primaryBehind = true;

	}

	public void moveUp() {
		rotation = 90;
		positionOffset.set(parent.dimension.x, parent.dimension.y / 2);

	}

	public void moveDown() {
		rotation = 270;
		positionOffset.set(0, parent.dimension.y / 2);

	}

	@Override
	public void update(float deltaTime) {

		updateCooldowns(deltaTime);

		super.update(deltaTime);
	}

	private void updateCooldowns(float deltaTime) {
		defaultAttackTimer -= deltaTime;
		
		for(int i = 0; i < this.abilityCooldowns.length; i++){
			abilityCooldowns[i] -= deltaTime;
		}
	}

	public void setPosition() {
		position.set(parent.position.x + positionOffset.x, parent.position.y
				+ positionOffset.y);

	}

	// CHECK IF POSSIBLE TO DO THE DEFAULT ATTACK
	public void defaultAttackCheck() {
		if (defaultAttackTimer < 0) {
			defaultAttackInit();
			defaultAttackTimer = defaultAttackCooldown;
		}
	}

	// OVERLOADING THE DEFAULT CHECK
	public void defaultAttackCheck(DIRECTION direction) {
		if (defaultAttackTimer < 0) {
			defaultAttackInit(direction);
			defaultAttackTimer = defaultAttackCooldown;
		}
	}

	// OVERRIDEN IN SUBCLASSES
	protected void defaultAttackInit() {

	}

	// OVERRIDEN IN SUBCLASSES
	protected void defaultAttackInit(DIRECTION direction) {

	}

	public void defaultAttackCheck(Vector2 rightJoyStick) {

	}

}
