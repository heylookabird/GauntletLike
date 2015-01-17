package game_objects;

import game_objects.abilities.AbstractAbility;
import game_objects.weapons.AbstractWeapon;
import Controllers.Xbox360;
import ai_classes.AbstractAi;
import ai_classes.RusherAi;
import backend.Assets;
import backend.LevelStage;

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

	//MOVING, NOT-MOVING, ATTAKING, STUNNED?
	public STATE state;

	// This contains the current position of the joysticks,
	//.x is x-coordinate, .y is y-coordinate
	private Vector2 leftJoyStick, rightJoyStick;
	protected boolean controller;
	private boolean left, right, up, down; 	//Booleans that indicate which direction you're moving
	int keyUp, keyLeft, keyRight, keyDown;
	private boolean isPlayerObject;
	
	//Internal reference to which side this MO is on

	public Array<ManipulatableObject> teamObjects;
	public Array<ManipulatableObject> enemyTeamObjects;
	
	//Animations and textures for movement
	public Animation walkingLeft, walkingRight, walkingUp, walkingDown;
	public TextureRegion leftImg, rightImg, upImg, downImg, currentDirImg, health;
	
	protected AbstractWeapon primaryWeapon, secondaryWeapon;
	public boolean twoHanded, primaryBehind;
	
	public float stunTimer;
	
	

	private AbstractAi Ai;
	
	public DIRECTION facing;
	
	
	//BASE STAT VARIABLES SPECIFIED IN SUBCLASS
	public int hp, damage, movementSpeed, attackSpeed, resistance;
	
	public int currentHp;//need this for Ai and health bar

	private int MAX_HEALTH;

	public enum STATE {
		NOT_MOVING, MOVING, ATTACKING, STUNNED;

	}
	
	public enum DIRECTION{
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
	private void init(){
		leftJoyStick = new Vector2();
		
		rightJoyStick = new Vector2();
		baseMovement = true;
		isPlayerObject = true;
		accelerationPerSecond = new Vector2(10, 10);
		currentFrameDimension = new Vector2();
		
		//
		teamObjects = LevelStage.playerControlledObjects;
		enemyTeamObjects = LevelStage.enemyControlledObjects;
		
		damage = 10;
		movementSpeed = 10;
		MAX_HEALTH = 20;
		hp = MAX_HEALTH;
		facing = DIRECTION.UP;
		health = Assets.instance.mage.hp;
		
		stunTimer = 0;
	}
	
	//This is the objects internal reference of who's team it's on. 
	public void setTeam(Array<ManipulatableObject> teamObjects, Array<ManipulatableObject> enemyTeamObjects){
		this.teamObjects = teamObjects;
		this.enemyTeamObjects = enemyTeamObjects;
	}
	

	public void setButtons(int up, int left, int down, int right, int fire) {
		keyUp = up;
		keyLeft = left;
		keyRight = right;
		keyDown = down;
	}


	protected void removeThyself() {
		teamObjects.removeValue(this, true);
		
	}

	//Moves the object to the right
	public void moveRight() {
		

		if (acceleration.x > 0 && right)
			return;
		
		primaryBehind = true;
		
		if(left){
			stopMoveX();
		}
		right = true;
		velocity.x = terminalVelocity.x;
		
		if(state == STATE.ATTACKING){
			return;
		
		}else if (state == STATE.NOT_MOVING || left) {
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

		if(right){
			stopMoveX();
		}
		left = true;
		velocity.x = -terminalVelocity.x;
		
		if(state == STATE.ATTACKING){
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

		if(down)
			stopMoveY();

		up = true;
		velocity.y = terminalVelocity.y;
		
		if(state == STATE.ATTACKING){
			return;
		}else if (state == STATE.NOT_MOVING) {
			this.setAnimation(walkingUp);
			this.currentDirImg = upImg;
			primaryWeapon.moveUp();
			facing = DIRECTION.UP;
			state = STATE.MOVING;


		}
	}


	public void moveDown() {
		//Gets called every frame if holding down because the xbox
		//Controller needs that functionality since xbox input is
		//polled every frame
		if (acceleration.y < 0 && down)
			return;
		
		if(up)
			stopMoveY();

		down = true;
		velocity.y = -terminalVelocity.y;

		if(state == STATE.ATTACKING){
			return;
		} else if (state == STATE.NOT_MOVING) {

			this.setAnimation(walkingDown);
			this.currentDirImg = downImg;
			state = STATE.MOVING;
			primaryWeapon.moveDown();
			facing = DIRECTION.DOWN;

		}
		//if this down = true line isn't run,
		
		
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
		//Automatically Decelerates the character
		//ToDO: Make a de-acceleration variable instead of accelerationPerSecond variable
		velocity.x = 0;
		left = false; right = false;


		if(state == STATE.ATTACKING)
			return;
		
		
		
		//Continue upwards 
		if (up) {
			setAnimation(this.walkingUp);
			currentDirImg = upImg;
			
			facing = DIRECTION.UP;
			//Adjust weapon correctly
			if(primaryWeapon != null)
				primaryWeapon.moveUp();
			
		//Continue downwards
			//Adjust weapon correctlys
			if(primaryWeapon != null)
				primaryWeapon.moveUp();
		} else if (down) {
			setAnimation(walkingDown);
			currentDirImg = downImg;
			facing = DIRECTION.DOWN;

			
			//Adjust weapon correctlys
			if(primaryWeapon != null)
				primaryWeapon.moveDown();
		}else{
			state = STATE.NOT_MOVING;
		}
		
	}


	public void stopMoveY() {
		up = false;
		down = false;
		velocity.y = 0; 
		
		if(state == STATE.ATTACKING)
			return;
		
		
		// These if-else if block is for when you stop moving
		// up and down, if you were holding left or right,
		// the animation will correct itself.
		if (right) {
			setAnimation(this.walkingRight);
			currentDirImg = rightImg;
			facing = DIRECTION.RIGHT;
			//Adjust weapon correctly
			//Adjust weapon correctlys
			if(primaryWeapon != null)
				primaryWeapon.moveRight();
			
		} else if (left) {
			setAnimation(walkingLeft);
			currentDirImg = leftImg;
			facing = DIRECTION.LEFT;

			
			if(primaryWeapon != null)
				primaryWeapon.moveLeft();
		}else{
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
			position.y += deltay;
		}

	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if(state != STATE.STUNNED){
			if(Ai != null){
				Ai.update(deltaTime);
			}
			else{
				handleAllPollingInput();
				System.out.println(this.hp + "");
			}
			

			if(primaryWeapon != null){
				primaryWeapon.update(deltaTime);
			}
			if(state != STATE.ATTACKING)
				checkStopMove();
		}else
			handleStunUpdate(deltaTime);

		moveX(deltaTime);
		moveY(deltaTime);
		
		primaryWeapon.setPosition();

		

	}
	
	private void handleStunUpdate(float deltaTime){
		stunTimer -= deltaTime;		

		if(stunTimer < 0){
			state = STATE.NOT_MOVING;
			velocity.set(0,0);
		}
		
	}

	
	// inefficient as fuck with so many conditions but it was buggy
	// otherwise..someone help me
	// tell it that it is standing when it isnt moving
	public void checkStopMove() {
		
		if (!left && !right && !up && !down) {
			state = STATE.NOT_MOVING;
		}else{
			state = STATE.MOVING;
		}
		
	}
	



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
				return true;
			}
		}
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
				return true;
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
				return true;
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

				return true;
			}
		}

		// Collide with objects that have an effect on collision
		for (AbstractGameObject interactable : LevelStage.interactables) {
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
			image = animation.getKeyFrame(stateTime, looping);
		} else {
			image = currentDirImg;
		}

		if(primaryWeapon != null && primaryBehind)
			primaryWeapon.render(batch);
		
		// Draw
		batch.draw(image.getTexture(), position.x, position.y, origin.x,
				origin.y, dimension.x, dimension.y, 1, 1, rotation,
				image.getRegionX(), image.getRegionY(), image.getRegionWidth(),
				image.getRegionHeight(), flipX, flipY);
		
		if(primaryWeapon != null && !primaryBehind)
			primaryWeapon.render(batch);
		
		if(secondaryWeapon != null)
			secondaryWeapon.render(batch);
		
		renderHp(batch);

		if (debug)
			batch.draw(debugTex, bounds.x, bounds.y, bounds.width,
					bounds.height);
	}

	private void renderHp(SpriteBatch batch) {
		float hpPercent = (float)(hp) / (float)(MAX_HEALTH);
		batch.draw(health, position.x - .2f, position.y - 1, 1.4f * hpPercent, .2f);
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
		
		
		//SEND 
		if(Math.abs(rightJoyStick.x) > .35 || Math.abs(rightJoyStick.y) > .35){
			primaryWeapon.defaultAttackCheck(rightJoyStick);
		}
		
		
	}

	public void actOnInputKeyDown(int keycode) {

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
			primaryWeapon.activateAbility1(facing);
			break;
			
		case Keys.NUM_2:
			primaryWeapon.activateAbility2(facing);
			break;
			
		case Keys.NUM_3:
			primaryWeapon.activateAbility3(facing);
			break;
			
		case Keys.NUM_4:
			primaryWeapon.activateAbility4(facing);
			break;

		case Keys.SPACE:

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

			break;
		}

	}// End of actOnInput methods

	// CONTROLLER FUNCTIONALITY
	public void buttonDown(int buttonIndex) {
		switch (buttonIndex) {

		case Xbox360.BUTTON_A:

			break;

		case Xbox360.BUTTON_B:

			break;

		}
	}

	public void buttonUp(int buttonIndex) {
		switch (buttonIndex) {

		case Xbox360.BUTTON_A:

			break;

		case Xbox360.BUTTON_B:

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
	

	public void takeHitFor(int damage, AbstractAbility attack) {
		this.hp -= damage;
		
		System.out.println("DIS NIGGA JUST GOT HIT FOR " + damage + " DAMAGE ");
		
		if(attack != null){
			if(!attack.velocity.isZero())
				velocity.set(attack.velocity.x * attack.knockBack, attack.velocity.y * attack.knockBack);
			else{
				Vector2 attCenter = attack.getCenter();
				Vector2 thisCenter = getCenter();
				double angle = MathUtils.atan2(attCenter.y - thisCenter.y, attCenter.x - thisCenter.x);
				angle = Math.toDegrees(angle);
				
				velocity.set((float)Math.cos(angle) * 10, (float)Math.sin(angle) * 10);
				
				
			}
	
			state = STATE.STUNNED;
			stunTimer = attack.stunTime;
		}
		
		if(hp <= 0)
			removeThyself();
	}


	public void attack() {
		primaryWeapon.defaultAttackCheck(facing);
	}
	

}
