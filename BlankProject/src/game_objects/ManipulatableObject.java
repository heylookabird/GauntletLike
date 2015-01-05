package game_objects;

import game_objects.weapons.AbstractWeapon;
import Controllers.Xbox360;
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
	protected STATE state;

	// This contains the current position of the joysticks,
	//.x is x-coordinate, .y is y-coordinate
	private Vector2 leftJoyStick, rightJoyStick;
	protected boolean controller;
	private boolean left, right, up, down; 	//Booleans that indicate which direction you're moving
	int keyUp, keyLeft, keyRight, keyDown;
	private boolean isPlayerObject;
	
	//Internal reference to which side this MO is on
	private Array<ManipulatableObject> teamObjects;
	private Array<ManipulatableObject> enemyTeamObjects;
	
	//Animations and textures for movement
	public Animation walkingLeft, walkingRight, walkingUp, walkingDown;
	public TextureRegion leftImg, rightImg, upImg, downImg, currentDirImg;
	
	protected AbstractWeapon primaryWeapon, secondaryWeapon;
	public boolean twoHanded, primaryBehind;
	
	
	//BASE STAT VARIABLES SPECIFIED IN SUBCLASS
	protected int hp, damage, movementSpeed, attackSpeed, resistance;

	public enum STATE {
		NOT_MOVING, MOVING

	}

	public ManipulatableObject(boolean controller) {
		// Set the Default States
		super();
		this.controller = controller;
		leftJoyStick = new Vector2();
		rightJoyStick = new Vector2();
		baseMovement = true;
		isPlayerObject = true;
		accelerationPerSecond = new Vector2(10, 10);
		currentFrameDimension = new Vector2();
		
		//
		teamObjects = LevelStage.playerControlledObjects;
		enemyTeamObjects = LevelStage.enemyControlledObjects;
		
		hp = 10;
		damage = 10;
		movementSpeed = 10;
	}

	public ManipulatableObject(boolean controller, float x, float y,
			float width, float height) {

		super(x, y, width, height);
		this.controller = controller;
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
		System.out.println("he's dead jim");
	}

	//Moves the object to the right
	public void moveRight() {
		

		if (acceleration.x > 0 && right)
			return;
		
		primaryBehind = true;
		
		if(left){
			stopMoveX();
		}
		
		if (state == STATE.NOT_MOVING || left) {
			this.setAnimation(walkingRight);
			this.currentDirImg = rightImg;
			primaryWeapon.moveRight();
		}
		right = true;
		state = STATE.MOVING;
		velocity.x = terminalVelocity.x;

	}

	public void moveLeft() {
		
		
		if (acceleration.x < 0 && left)
			return;
		// Set left to true so if we were holding right previous to this,
		// when we let go of right, it will move us left

		if(right){
			stopMoveX();
		}
		
		if (state == STATE.NOT_MOVING || right) {
			this.setAnimation(walkingLeft);
			this.currentDirImg = leftImg;
			primaryWeapon.moveLeft();

		}
		left = true;
		
		state = STATE.MOVING;
		velocity.x = -terminalVelocity.x;

	}

	protected void moveUp() {
		if (acceleration.y > 0 && up)
			return;

		if(down)
			stopMoveY();
		
		if (state == STATE.NOT_MOVING) {
			this.setAnimation(walkingUp);
			this.currentDirImg = upImg;
			primaryWeapon.moveUp();


		}
		up = true;
		

		state = STATE.MOVING;
		velocity.y = terminalVelocity.y;
	}

	protected void moveDown() {
		//Gets called every frame if holding down because the xbox
		//Controller needs that functionality since xbox input is
		//polled every frame
		if (acceleration.y < 0 && down)
			return;
		
		if(up)
			stopMoveY();

		//From walking to running.
		if (state == STATE.NOT_MOVING) {
			this.setAnimation(walkingDown);
			this.currentDirImg = downImg;
			primaryWeapon.moveDown();

		}
		//if this down = true line isn't run,
		down = true;
		state = STATE.MOVING;
		velocity.y = -terminalVelocity.y;
		
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

		left = false; right = false;
		velocity.x = 0;
		
		
		//Continue upwards 
		if (up) {
			setAnimation(this.walkingUp);
			currentDirImg = upImg;
			
			//Adjust weapon correctly
			if(primaryWeapon != null)
				primaryWeapon.moveUp();
			
		//Continue downwards
		} else if (down) {
			setAnimation(walkingDown);
			currentDirImg = downImg;
			
			//Adjust weapon correctlys
			if(primaryWeapon != null)
				primaryWeapon.moveDown();
		}else{
			state = STATE.NOT_MOVING;
		}
		
	}

	protected void stopMoveY() {
		up = false;
		down = false;
		velocity.y = 0;
		// These if-else if block is for when you stop moving
		// up and down, if you were holding left or right,
		// the animation will correct itself.
		if (right) {
			setAnimation(this.walkingRight);
			currentDirImg = rightImg;
			
			//Adjust weapon correctly
			if(primaryWeapon != null)
				primaryWeapon.moveRight();
			
		} else if (left) {
			setAnimation(walkingLeft);
			currentDirImg = leftImg;
			
			//Adjust weapon correctly
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
		handleAllPollingInput();

		moveX(deltaTime);
		moveY(deltaTime);

		if(primaryWeapon != null){
			primaryWeapon.update(deltaTime);
		}
		checkStopMove();

	}

	// inefficient as fuck with so many conditions but it was buggy
	// otherwise..someone help me
	// tell it that it is standing when it isnt moving
	private void checkStopMove() {
		if (!left && !right && !up && !down) {
			state = STATE.NOT_MOVING;
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
		for (AbstractGameObject obj : LevelStage.playerControlledObjects) {

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
		for (AbstractGameObject enemyObjs : LevelStage.enemyControlledObjects) {

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

		if (debug)
			batch.draw(debugTex, bounds.x, bounds.y, bounds.width,
					bounds.height);
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

		// Controller
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
	}

	public void actOnInputKeyDown(int keycode) {

		// Movement, same among all characters
		switch (keycode) {

		// ABILITIES
		// A BUTTON
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

		if (Math.abs(leftJoyStick.x - this.leftJoyStick.x) < .15f
				&& Math.abs(leftJoyStick.y - this.leftJoyStick.y) < .15f) {
			return;
		}

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

	public void takeHitFor(int damage) {
		this.hp -= damage;
		System.out.println("DIS NIGGA JUST GOT HIT FOR " + damage + " DAMAGE ");
		
		//if(hp < 0)
			//removeThyself();
	}
	

}
