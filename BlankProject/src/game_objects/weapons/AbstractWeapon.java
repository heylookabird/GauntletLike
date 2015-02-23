package game_objects.weapons;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.abilities.AbstractAbility;
import backend.Calc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class AbstractWeapon extends AbstractGameObject {

	ManipulatableObject parent;
	private Vector2 positionOffset;

	// protected Array<AbstractAbility> abilities;
	protected float[] abilityCooldowns;

	// ATTACKS
	protected float /* defaultAttackTimer, */defaultAttackCooldown;
	protected float shieldMax = 20, shieldRecharge = 5, shieldDrain = .2f,
			counterwindow = .5f, countertime = 0;
	public float shield = 20;
	protected AbstractAbility defaultAttack;
	protected int ability1CoolDown;
	protected int ability2CoolDown;
	protected int ability3CoolDown;
	protected int ability4CoolDown;
	public int swing = 0;
	public String name;
	private boolean countering = false;
	private float coolDownSpeed = .05f;

	public AbstractWeapon(ManipulatableObject parent, float width,
			float height, Vector2 positionOffset) {
		super(parent.position.x, parent.position.y, width, height);
		this.parent = parent;
		this.positionOffset = positionOffset;
		defaultAttackCooldown = 2;
		abilityCooldowns = new float[5];

		// set terminalVelocity to be dodgeSpeed in children, attacks dont use
		// weapon terminalVelocity anyways
		// this.terminalVelocity.set(dodgeSpeed, dodgeSpeed);

	}

	public float getShieldRatio() {
		return shield / shieldMax;
	}

	private boolean checkAttack(int index) {
		if (abilityCooldowns[index] < 0)
			return true;

		return false;
	}

	// override methods
	public void ability1(float direction) {

	}

	public void ability2(float direction) {

	}

	public void ability3(float direction) {

	}

	public void ability4(float direction) {

	}

	public void activateAbility1(float direction) {
		if (checkAttack(1)) {
			ability1(direction);
			abilityCooldowns[1] = ability1CoolDown;

		}
	}

	public void activateAbility2(float direction) {
		if (checkAttack(2)) {
			ability2(direction);
			abilityCooldowns[2] = ability2CoolDown;
		}

	}

	public void activateAbility3(float direction) {
		if (checkAttack(2)) {
			ability3(direction);
			abilityCooldowns[2] = ability3CoolDown;
		}

	}

	public void activateAbility4(float direction) {
		if (checkAttack(3)) {
			ability4(direction);
			abilityCooldowns[3] = ability4CoolDown;

		}
	}

	public void activateDodge(Vector2 rightJoyStick) {

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

		if (!parent.shielding)
			shield += shieldRecharge;
		else
			shield -= shieldDrain;

		shield = MathUtils.clamp(shield, -2, shieldMax);

		for (int i = 0; i < this.abilityCooldowns.length; i++) {
			abilityCooldowns[i] -= coolDownSpeed;
		}

		handleCounter(deltaTime);

	}

	public void changeCoolDownSpeed(float speed) {
		this.coolDownSpeed = speed;
	}

	public void setDefaultCooldown() {
		this.coolDownSpeed = .05f;
	}

	public String getCooldowns() {
		return "default " + abilityCooldowns[0] + ", ab1 "
				+ abilityCooldowns[1] + ", ab2 " + abilityCooldowns[2]
				+ ", ab3 " + abilityCooldowns[3] + ", ab4 "
				+ abilityCooldowns[4];
	}

	public void setPosition() {
		position.set(parent.position.x + positionOffset.x, parent.position.y
				+ positionOffset.y);

	}

	// CHECK IF POSSIBLE TO DO THE DEFAULT ATTACK
	public void defaultAttackCheck() {
		if (checkAttack(0) || countering) {
			defaultAttackInit();
			countering = false;
			this.abilityCooldowns[0] = defaultAttackCooldown;
		}
	}

	// OVERLOADING THE DEFAULT CHECK
	public void defaultAttackCheck(DIRECTION direction) {
		if (checkAttack(0) || countering) {
			defaultAttackInit(direction);
			countering = false;
			this.abilityCooldowns[0] = defaultAttackCooldown;
		}
	}

	// OVERRIDEN IN SUBCLASSES
	protected void defaultAttackInit() {

	}

	// OVERRIDEN IN SUBCLASSES
	protected void defaultAttackInit(DIRECTION direction) {

	}

	public void defaultAttackCheck(Vector2 rightJoyStick) {
		if (checkAttack(0) || countering) {
			defaultAttackInit(rightJoyStick);
			this.abilityCooldowns[0] = defaultAttackCooldown;
		}
	}

	protected void defaultAttackInit(Vector2 rightJoyStick) {

	}

	public void handleCounter(float deltaTime) {
		this.countertime -= deltaTime;

		if (countertime < 0) {
			countering = false;
			swing = 0;
		}
	}

	public void openCounterWindow() {
		countertime = this.counterwindow;
		countering = true;
	}

}
