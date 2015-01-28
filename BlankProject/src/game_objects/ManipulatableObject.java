package game_objects;

import game_objects.abilities.AbstractAbility;
import game_objects.abilities.Taunt;
import game_objects.abilities.effects.Effect;
import game_objects.weapons.AbstractWeapon;
import Controllers.Xbox360;
import ai_classes.AbstractAi;
import backend.Assets;
import backend.Calc;
import backend.LevelStage;
import backend.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ManipulatableObject extends AbstractGameObject {

	// MOVING, NOT-MOVING, ATTAKING, STUNNED?
	public STATE state;

	// This contains the current position of the joysticks,
	// .x is x-coordinate, .y is y-coordinate
	private Vector2 leftJoyStick, rightJoyStick;
	protected boolean controller;
	private boolean left, right, up, down; // Booleans that indicate which
											// direction you're moving
	int keyUp, keyLeft, keyRight, keyDown;
	private boolean isPlayerObject;

	protected Array<AbstractAbility> passiveAbilities;

	// Internal reference to which side this MO is on

	public Array<ManipulatableObject> teamObjects;
	public Array<ManipulatableObject> enemyTeamObjects;

	public Array<AbstractWeapon> equipableWeapons;
	int weaponEquippedIndex = 0;

	// Animations and textures for movement
	public Animation walkingLeft, walkingRight, walkingUp, walkingDown;
	public TextureRegion leftImg, rightImg, upImg, downImg, currentDirImg,
			health;
	public Vector2 walkingTerminalV;

	protected AbstractWeapon primaryWeapon, secondaryWeapon;
	public boolean twoHanded, primaryBehind;

	public float stunTimer;

	private AbstractAi Ai;

	public DIRECTION facing;

	// BASE STAT VARIABLES SPECIFIED IN SUBCLASS
	public int damage, movementSpeed, attackSpeed, resistance;

	public float hp;
	public int currentHp;// need this for Ai and health bar

	private int MAX_HEALTH;

	protected boolean stunned;
	public boolean invulnerable;
	public boolean shielding = false, counter = false;

	public int MAX_SHIELD_RATING;

	private TextureRegion shieldImg = Assets.instance.effects.iceExplosionImgs
			.peek();

	private boolean aiming;

	public enum STATE {
		NOT_MOVING, MOVING, ATTACKING, KNOCKED;

	}

	public enum DIRECTION {
		RIGHT, LEFT, UP, DOWN;
	}

	public ManipulatableObject(boolean controller) {
		// Set the Default States
		super();
		this.controller = controller;
		init();

	}

	public ManipulatableObject(boolean controller, float x, float y,
			float width, float height) {

		super(x, y, width, height);
		this.controller = controller;
		init();
	}

	private void init() {
		leftJoyStick = new Vector2();

		rightJoyStick = new Vector2();
		walkingTerminalV = new Vector2(terminalVelocity);
		baseMovement = true;
		currentFrameDimension = new Vector2();

		teamObjects = LevelStage.playerControlledObjects;
		enemyTeamObjects = LevelStage.enemyControlledObjects;

		equipableWeapons = new Array<AbstractWeapon>();
		damage = 10;
		movementSpeed = 10;
		MAX_HEALTH = 20;
		hp = MAX_HEALTH;
		facing = DIRECTION.UP;
		health = Assets.instance.mage.hp;

		passiveAbilities = new Array<AbstractAbility>();

		stunTimer = 0;
	}

	// This is the objects internal reference of who's team it's on.
	public void setTeam(Array<ManipulatableObject> teamObjects,
			Array<ManipulatableObject> enemyTeamObjects) {
		this.teamObjects = teamObjects;
		this.enemyTeamObjects = enemyTeamObjects;
	}

	public void setButtons(int up, int left, int down, int right, int fire) {
		keyUp = up;
		keyLeft = left;
		keyRight = right;
		keyDown = down;
	}

	public void togglePlayerObject() {
		if (!isPlayerObject)
			isPlayerObject = true;
		else
			isPlayerObject = false;
	}

	public void toggleWeapon() {
		this.weaponEquippedIndex++;
		if (weaponEquippedIndex >= this.equipableWeapons.size)
			weaponEquippedIndex = 0;

		this.equipWeapon(this.equipableWeapons.get(weaponEquippedIndex));
	}

	protected void removeThyself() {

		if (isPlayerObject) {
			// World.world.togglePause();
			// World.world.menu.setPlayer(this);
			hp = this.MAX_HEALTH;
		} else
			teamObjects.removeValue(this, true);

	}

	// Moves the object to the right
	public void moveRight() {

		if (acceleration.x > 0 && right)
			return;

		primaryBehind = true;

		if (left) {
			stopMoveX();
		}
		right = true;
		velocity.x = terminalVelocity.x;

		if (state == STATE.ATTACKING) {
			return;

		} else if (state == STATE.NOT_MOVING || left) {
			this.setAnimation(walkingRight);
			this.currentDirImg = rightImg;
			primaryWeapon.moveRight();
			facing = DIRECTION.RIGHT;
			state = STATE.MOVING;

		}

	}

	public void moveLeft() {

		if (acceleration.x < 0 && left)
			return;
		// Set left to true so if we were holding right previous to this,
		// when we let go of right, it will move us left

		if (right) {
			stopMoveX();
		}
		left = true;
		velocity.x = -terminalVelocity.x;

		if (state == STATE.ATTACKING) {
			return;
		} else if (state == STATE.NOT_MOVING || right) {
			this.setAnimation(walkingLeft);
			this.currentDirImg = leftImg;
			primaryWeapon.moveLeft();
			facing = DIRECTION.LEFT;
			state = STATE.MOVING;

		}

	}

	public void moveUp() {
		if (acceleration.y > 0 && up)
			return;

		if (down)
			stopMoveY();

		up = true;
		velocity.y = terminalVelocity.y;

		if (state == STATE.ATTACKING) {
			return;
		} else if (state == STATE.NOT_MOVING) {
			this.setAnimation(walkingUp);
			this.currentDirImg = upImg;
			primaryWeapon.moveUp();
			facing = DIRECTION.UP;
			state = STATE.MOVING;

		}
	}

	public void moveDown() {
		// Gets called every frame if holding down because the xbox
		// Controller needs that functionality since xbox input is
		// polled every frame
		if (acceleration.y < 0 && down)
			return;

		if (up)
			stopMoveY();

		down = true;
		velocity.y = -terminalVelocity.y;

		if (state == STATE.ATTACKING) {
			return;
		} else if (state == STATE.NOT_MOVING) {

			this.setAnimation(walkingDown);
			this.currentDirImg = downImg;
			state = STATE.MOVING;
			primaryWeapon.moveDown();
			facing = DIRECTION.DOWN;

		}
		// if this down = true line isn't run,

	}

	public void stopMove() {
		right = false;
		left = false;
		up = false;
		down = false;
		acceleration.set(0, 0);
		velocity.set(0, 0);
		state = STATE.NOT_MOVING;
	}

	public void stopMoveX() {
		// Automatically Decelerates the character
		// ToDO: Make a de-acceleration variable instead of
		// accelerationPerSecond variable
		velocity.x = 0;
		left = false;
		right = false;

		if (state == STATE.ATTACKING)
			return;

		// Continue upwards
		if (up) {
			setAnimation(this.walkingUp);
			currentDirImg = upImg;

			facing = DIRECTION.UP;
			// Adjust weapon correctly
			if (primaryWeapon != null)
				primaryWeapon.moveUp();

			// Continue downwards
			// Adjust weapon correctlys
			if (primaryWeapon != null)
				primaryWeapon.moveUp();
		} else if (down) {
			setAnimation(walkingDown);
			currentDirImg = downImg;
			facing = DIRECTION.DOWN;

			// Adjust weapon correctlys
			if (primaryWeapon != null)
				primaryWeapon.moveDown();
		} else {
			state = STATE.NOT_MOVING;
		}

	}

	public void stopMoveY() {
		up = false;
		down = false;
		velocity.y = 0;

		if (state == STATE.ATTACKING)
			return;

		// These if-else if block is for when you stop moving
		// up and down, if you were holding left or right,
		// the animation will correct itself.
		if (right) {
			setAnimation(this.walkingRight);
			currentDirImg = rightImg;
			facing = DIRECTION.RIGHT;
			// Adjust weapon correctly
			// Adjust weapon correctlys
			if (primaryWeapon != null)
				primaryWeapon.moveRight();

		} else if (left) {
			setAnimation(walkingLeft);
			currentDirImg = leftImg;
			facing = DIRECTION.LEFT;

			if (primaryWeapon != null)
				primaryWeapon.moveLeft();
		} else {
			state = STATE.NOT_MOVING;
		}
	}

	private void stopMoveUp() {
		up = false;
	}

	private void stopMoveDown() {
		down = false;
	}

	public void moveX(float deltaTime) {

		// change in X axis this frame
		deltax = velocity.x * deltaTime;

		// Collision Check this object once for x only
		if (!collision(deltax, 0)) {

			if(!aiming)
				position.x += deltax;
			
		}

	}

	public void activateAI(AbstractAi ai) {
		this.Ai = ai;
	}

	public void moveY(float deltaTime) {
		// change in y this frame
		deltay = velocity.y * deltaTime;

		// If you didn't collide in y axis,
		// add deltaY to the position.y
		if (!collision(0, deltay)) {
			if(!aiming)
				position.y += deltay;
		}

	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		if (!stunned) {
			if (Ai != null) {
				Ai.update(deltaTime);
			} else {
				handleAllPollingInput();
			}

			if (state != STATE.ATTACKING)
				checkStopMove();
		} else
			handleStunUpdate(deltaTime);

		if (primaryWeapon != null) {
			primaryWeapon.update(deltaTime);
		}

		moveX(deltaTime);
		moveY(deltaTime);

		for (int i = 0; i < passiveAbilities.size; i++) {
			AbstractAbility passive = passiveAbilities.get(i);
			passive.update(deltaTime);

		}

		primaryWeapon.setPosition();

	}

	private void handleStunUpdate(float deltaTime) {
		stunTimer -= deltaTime;

		if (stunned && stunTimer < 0) {
			stunned = false;
			velocity.set(0, 0);
			terminalVelocity.set(walkingTerminalV);
		}

	}

	public void addPassive(AbstractAbility ability) {
		passiveAbilities.add(ability);
		System.out.println("ManObj added");
	}

	// inefficient as fuck with so many conditions but it was buggy
	// otherwise..someone help me
	// tell it that it is standing when it isnt moving
	public void checkStopMove() {

		if (!left && !right && !up && !down) {
			state = STATE.NOT_MOVING;
		} else {
			state = STATE.MOVING;
		}

	}

	@Override
	protected boolean collision(float deltaX, float deltaY) {

		// Set bounds to where this object will be after adding
		// the velocity of this frame to check and see if we are
		// going to collide with anything
		bounds.setPosition(position.x + deltaX, position.y + deltaY);

		// Iterate through platforms
		for (AbstractGameObject platform : LevelStage.backObjects) {

			// If collision
			if (bounds.overlaps(platform.bounds)) {
				if (deltaX != 0) {

					deltax = 0;
				}
				if (deltaY != 0) {

					deltay = 0;
				}
			}
		}

		if (!isPlayerObject) {
			// Iterate through platforms
			for (AbstractGameObject obj : teamObjects) {

				// If collision
				if (this != obj && bounds.overlaps(obj.bounds)) {
					if (deltaX != 0) {

						deltax = 0;
					}
					if (deltaY != 0) {

						deltay = 0;
					}
				}
			}
		}

		// Iterate through platforms
		for (AbstractGameObject enemyObjs : enemyTeamObjects) {

			// If collision
			if (this != enemyObjs && bounds.overlaps(enemyObjs.bounds)) {
				if (deltaX != 0) {

					deltax = 0;
				}
				if (deltaY != 0) {

					deltay = 0;
				}
			}
		}

		// Iterate through collidable Objects
		for (AbstractGameObject platform : LevelStage.solidObjects) {

			// If collision
			if (bounds.overlaps(platform.bounds)) {

				if (deltaX != 0) {
					deltax = 0;
				}
				if (deltaY != 0) {
					deltay = 0;
				}

			}
		}

		// Collide with objects that have an effect on collision
		for (int i = 0; i < LevelStage.interactables.size; i++) {
			AbstractGameObject interactable = LevelStage.interactables.get(i);
			if (bounds.overlaps(interactable.bounds)) {
				interactable.interact(this);
			}
		}
		return false;

	}

	@Override
	public void render(SpriteBatch batch) {

		// get correct image and draw the current proportions
		if (state == STATE.MOVING) {
			image = animation.getKeyFrame(stateTime, true);
		} else {
			image = currentDirImg;
		}

		if (primaryWeapon != null && primaryBehind)
			primaryWeapon.render(batch);

		// Draw
		batch.draw(image.getTexture(), position.x, position.y, origin.x,
				origin.y, dimension.x, dimension.y, 1, 1, rotation,
				image.getRegionX(), image.getRegionY(), image.getRegionWidth(),
				image.getRegionHeight(), flipX, flipY);

		if (primaryWeapon != null && !primaryBehind)
			primaryWeapon.render(batch);

		if (secondaryWeapon != null)
			secondaryWeapon.render(batch);

		renderHp(batch);

		for (int i = 0; i < passiveAbilities.size; i++) {
			AbstractAbility passive = passiveAbilities.get(i);
			passive.render(batch);
		}

		if (shielding)
			renderShield(batch);

		if (debug)
			batch.draw(debugTex, bounds.x, bounds.y, bounds.width,
					bounds.height);
	}

	private void renderShield(SpriteBatch batch) {
		float shieldpercent = primaryWeapon.getShieldRatio();
		Vector2 center = getCenter();
		float width = bounds.width * shieldpercent;
		float height = bounds.height * shieldpercent;
		batch.draw(shieldImg, center.x - width / 2f, center.y - height / 2f,
				width, height);
	}

	private void renderHp(SpriteBatch batch) {
		float hpPercent = (float) (hp) / (float) (MAX_HEALTH);
		batch.draw(health, position.x - .2f, position.y - 1, 1.4f * hpPercent,
				.2f);
	}

	public void equipWeapon(AbstractWeapon weapon) {
		this.primaryWeapon = weapon;
	}

	private void pollKeyInput() {
		if (!isPlayerObject)
			return;
		

		if (!(right && left)) {

			// Move Left or right
			if (Gdx.input.isKeyPressed(keyLeft)) {
				moveLeft();
			} else if (Gdx.input.isKeyPressed(keyRight)) {
				moveRight();
			} else {
				if (right || left)
					stopMoveX();
			}

			// debug purposes only
		} else {
			System.out.println("ManipulatableObject, pollKeyInput: " + right
					+ " " + left);
		}

		// Move up or down
		if (Gdx.input.isKeyPressed(keyUp)) {
			moveUp();
		} else if (Gdx.input.isKeyPressed(keyDown)) {
			moveDown();

		} else {
			if (up || down)
				stopMoveY();
		}

		// dodging = Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT);

	}

	private void handleAllPollingInput() {

		// Keyboard Input
		if (!controller) {
			pollKeyInput();
			return;
		}

		// Controller movement
		if (leftJoyStick.y > .35f) {
			moveDown();
		} else if (leftJoyStick.y < -.35f) {
			moveUp();
		} else {
			stopMoveY();
		}

		if (leftJoyStick.x > .35f) {

			moveRight();
		} else if (leftJoyStick.x < -.35f) {

			moveLeft();
		} else {
			stopMoveX();
		}

		// SEND
		if (Math.abs(rightJoyStick.x) > .35 || Math.abs(rightJoyStick.y) > .35) {
			primaryWeapon.defaultAttackCheck(rightJoyStick);
		}

	}

	public void activatePlayerAbility(int ability) {
		switch (ability) {
		case 1:
			this.primaryWeapon.activateAbility1(Calc.directionToDegrees(facing));
			break;

		case 2:
			this.primaryWeapon.activateAbility2(Calc.directionToDegrees(facing));
			break;

		case 3:
			this.primaryWeapon.activateAbility3(Calc.directionToDegrees(facing));
			break;

		case 4:
			this.primaryWeapon.activateAbility4(Calc.directionToDegrees(facing));
			break;
		}

	}

	public void actOnInputKeyDown(int keycode) {
		if (stunned)
			return;

		// Movement, same among all characters
		switch (keycode) {

		// ABILITIES
		// ARROW KEYS
		case Keys.LEFT:
			primaryWeapon.defaultAttackCheck(DIRECTION.LEFT);
			break;
		case Keys.RIGHT:
			primaryWeapon.defaultAttackCheck(DIRECTION.RIGHT);
			break;
		case Keys.UP:
			primaryWeapon.defaultAttackCheck(DIRECTION.UP);
			break;
		case Keys.DOWN:
			primaryWeapon.defaultAttackCheck(DIRECTION.DOWN);
			break;

		case Keys.NUM_1:
			primaryWeapon.activateAbility1(Calc.directionToDegrees(facing));
			break;

		case Keys.NUM_2:
			primaryWeapon.activateAbility2(Calc.directionToDegrees(facing));
			break;

		case Keys.NUM_3:
			primaryWeapon.activateAbility3(Calc.directionToDegrees(facing));
			break;

		case Keys.NUM_4:
			primaryWeapon.activateAbility4(Calc.directionToDegrees(facing));
			break;

		case Keys.SPACE:
			shielding = true;
			stun(100);
			stopMove();
			break;

		case Keys.P:
			World.world.togglePause();
			World.world.menu.setPlayer(this);
			break;
		}

	}// END OF METHOD

	public void actOnInputKeyUp(int keycode) {
		// Movement, same among all characters
		switch (keycode) {
		/*
		 * // LEFT case Keys.LEFT:
		 * 
		 * stopMoveLeft(); break; // RIGHT case Keys.RIGHT:
		 * 
		 * stopMoveRight(); break;
		 */
		// DOWN

		case Keys.A:

			break;
		case Keys.S:

			break;
		case Keys.D:

			break;
		case Keys.F:

			break;

		case Keys.SPACE:
			shielding = false;
			this.stunTimer = -1;
			break;

		}

	}// End of actOnInput methods

	// CONTROLLER FUNCTIONALITY
	public void buttonDown(int buttonIndex) {
		Vector2 leftJoyStick = new Vector2(this.leftJoyStick);
		leftJoyStick.y *= -1;
		switch (buttonIndex) {

		case Xbox360.BUTTON_A:
			primaryWeapon.activateAbility1(Calc.atan2(leftJoyStick));
			break;

		case Xbox360.BUTTON_B:
			primaryWeapon.activateAbility2(Calc.atan2(leftJoyStick));
			break;

		case Xbox360.BUTTON_X:
			primaryWeapon.activateAbility3(Calc.atan2(leftJoyStick));
			
			break;

		case Xbox360.BUTTON_Y:
			primaryWeapon.activateAbility4(Calc.atan2(leftJoyStick));
			break;
		case Xbox360.AXIS_RIGHT_TRIGGER:
			aiming = true;
			break;

		case Xbox360.BUTTON_START:
			World.world.togglePause();
			World.world.menu.setPlayer(this);
			break;

		case Xbox360.BUTTON_RB:
			shielding = true;
			break;

		}
	}

	public void buttonUp(int buttonIndex) {
		System.out.println(buttonIndex);
		switch (buttonIndex) {

		case Xbox360.BUTTON_A:

			break;

		case Xbox360.BUTTON_B:

			break;
		case Xbox360.BUTTON_LB:
			aiming = false;
			break;

		case Xbox360.BUTTON_RB:
			shielding = false;
			break;

		}
	}

	public void joyStick(Vector2 leftJoyStick, Vector2 rightJoyStick) {

		this.leftJoyStick = leftJoyStick;
		this.rightJoyStick = rightJoyStick;

	}

	public void clampInRectangle(Rectangle rect) {
		this.position.x = MathUtils.clamp(position.x, rect.x, rect.x
				+ rect.width - dimension.x);
		this.position.y = MathUtils.clamp(position.y, rect.y, rect.y
				+ rect.height - dimension.y);
		bounds.setPosition(position);

	}

	public void takeKnockback(float velocity, float knockbackAngle,
			float knockbackTime) {
		System.out.println("Knocked back  by " + velocity * knockbackTime
				+ " units");

		if (knockbackTime <= 0)
			return;

		state = STATE.KNOCKED;
		stun(knockbackTime);

		this.velocity.set(
				(float) (velocity * Math.cos(knockbackAngle * Math.PI / 180)),
				(float) (velocity * Math.sin(knockbackAngle * Math.PI / 180)));

	}

	public void takeHitFor(float damage, AbstractAbility attack) {
		if (invulnerable)
			return;
		if (shielding) {
			this.shieldDamage(damage);
			//counter = true;
			primaryWeapon.openCounterWindow();
			return;
		}

		this.hp -= damage;

		if (attack != null) {

			takeKnockback(attack.knockbackSpeed, attack.knockbackAngle,
					attack.knockbackTime);

			/*
			 * Vector2 attCenter = attack.getCenter(); Vector2 thisCenter =
			 * getCenter(); double angle = MathUtils.atan2(attCenter.y -
			 * thisCenter.y, attCenter.x - thisCenter.x); angle =
			 * Math.toDegrees(angle);
			 * 
			 * velocity.set((float) Math.cos(angle) * attack.knockBackSpeed,
			 * (float) Math.sin(angle) * attack.knockBackSpeed);
			 */
		}

		if (hp <= 0)
			removeThyself();
	}

	private void shieldDamage(float damage) {
		primaryWeapon.shield -= damage;

		if (primaryWeapon.shield < 0) {
			this.stun(5);
			System.out.println("ManObj shield break");
			shielding = false;
		}
	}

	public void attack() {
		primaryWeapon.defaultAttackCheck(facing);
	}

	public void stun(float lifeTimer) {
		stunned = true;
		stunTimer = lifeTimer;
	}

	public String getCurrentWeaponName() {

		return primaryWeapon.name;
	}

	public void heal(int heal) {
		hp += heal;
		hp = MathUtils.clamp(hp, 0, MAX_HEALTH);
	}

	public void removePassive(AbstractAbility ability) {
		this.passiveAbilities.removeValue(ability, false);
	}

	public boolean checkPassive(Effect effect) {
		boolean exists = false;

		for (int i = 0; i < passiveAbilities.size; i++) {
			if (passiveAbilities.get(i).getClass() == effect.getClass()) {
				exists = true;
			}
		}

		return exists;
	}

	public void setTarget(ManipulatableObject target) {
		if (Ai != null) {
			Ai.taunt(target);

		}
	}

	public void setTarget(AbstractAbility taunt) {
		if (Ai != null) {
			Ai.targetPosition(taunt.getCenter());
		}
	}

	public void resetTarget() {
		if (Ai != null){
			Ai.target = null;
			Ai.targetSpot = null;
		}
	}

}
