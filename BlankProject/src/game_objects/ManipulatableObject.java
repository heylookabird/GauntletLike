package game_objects;

import Controllers.Xbox360;
import backend.Assets;
import backend.Constants;
import backend.LevelStage;
import backend.World;
import backend.WorldRenderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class ManipulatableObject extends AbstractGameObject {

	public Vector2 spawnPoint;
	public VIEW_DIRECTION viewDirection;
	protected STATE state;

	// This contains the current position of the joysticks, .x is x-coordinate,
	// .y is y-coordinate
	private Vector2 leftJoyStick, rightJoyStick;
	// private MeleeEnemyAI AI;
	protected boolean controller;
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	protected boolean clampX, clampY;
	int keyUp, keyLeft, keyRight, keyDown;
	protected int fire;
	private boolean isPlayerObject;

	public enum VIEW_DIRECTION {
		left, right;
	}

	public enum STATE {
		NOT_MOVING, MOVING

	}

	public ManipulatableObject(boolean controller) {
		// Set the Default States
		super();
		spawnPoint = new Vector2();
		this.controller = controller;
		leftJoyStick = new Vector2();
		rightJoyStick = new Vector2();
		viewDirection = VIEW_DIRECTION.right;
		baseMovement = true;
		isPlayerObject = true;
		accelerationPerSecond = new Vector2(10, 10);
		setAnimation(Assets.characterimages.planes.bluePlane);
		currentFrameDimension = new Vector2();
	}

	public ManipulatableObject(boolean controller, float x, float y,
			float width, float height) {

		super(x, y, width, height);

	}

	public void setButtons(int up, int left, int down, int right, int fire) {
		keyUp = up;
		keyLeft = left;
		keyRight = right;
		keyDown = down;
		this.fire = fire;
	}

	/*
	 * @Override public void dealDamage(int dmg, AbstractGameObject dealer) { hp
	 * -= dmg; if (hp <= 0){ removeThyself(true); }
	 * 
	 * }
	 */

	protected void removeThyself(boolean explosion) {
		if (isPlayerObject) {
			LevelStage.playerControlledObjects.removeValue(this, true);
		} else
			LevelStage.enemyControlledObjects.removeValue(this, true);

	}

	public void moveRight() {

		if (acceleration.x > 0 && right)
			return;

		if (left) {
			stopMoveLeft();
		}
		right = true;

		state = STATE.MOVING;
		viewDirection = VIEW_DIRECTION.right;
		acceleration.x = accelerationPerSecond.x;

	}

	public void moveLeft() {
		if (acceleration.x < 0 && left)
			return;
		// Set left to true so if we were holding right previous to this,
		// when we let go of right, it will move us left
		if (right) {
			stopMoveRight();

		}
		left = true;
		viewDirection = VIEW_DIRECTION.left;
		state = STATE.MOVING;

		acceleration.x = -accelerationPerSecond.x;

	}

	protected void moveUp() {
		if (acceleration.y > 0 && up)
			return;

		up = true;
		state = STATE.MOVING;
		acceleration.y = accelerationPerSecond.y;
	}

	protected void moveDown() {
		if (acceleration.y < 0 && down)
			return;

		down = true;
		state = STATE.MOVING;
		acceleration.y = -accelerationPerSecond.y;
	}

	public void stopMoveRight() {

		// Set velocity to 0, check if left might be pressed
		right = false;
		/*
		 * if (state == STATE.GROUNDED) { setAnimation(aniNormal); }
		 */
		if (left) {
			moveLeft();
		}
		// Friction to slow down
		acceleration.x = velocity.x > 0 ? -accelerationPerSecond.x
				: accelerationPerSecond.x;

	}

	public void stopMoveLeft() {
		left = false;

		// Bug fix for if both buttons are down,
		// Left is released, then character should move right
		if (right) {
			moveRight();
		}

		acceleration.x = velocity.x > 0 ? -accelerationPerSecond.x
				: accelerationPerSecond.x;

	}

	public void stopMoveX() {

		acceleration.x = velocity.x > 0 ? -accelerationPerSecond.x * 2
				: accelerationPerSecond.x * 2;

		// Set velocity.x to 0, check if right might be pressed

	}

	protected void stopMoveY() {
		acceleration.y = velocity.y > 0 ? -accelerationPerSecond.y * 2
				: accelerationPerSecond.y * 2;
	}

	private void stopMoveUp() {
		up = false;
	}

	private void stopMoveDown() {
		down = false;
	}

	public void moveX(float deltaTime) {


		// If you're pressing right but moving left
		if (right && velocity.x < 0)
			right = false;
		if (left && velocity.x > 0)
			left = false;

		// Not pressing anything and you come to a stop
		if (!left && !right) {
			if (velocity.x < 1.5f && velocity.x > -1.5f) {
				acceleration.x = 0;
				velocity.x = 0;

			}

		}

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

		// If you're pressing right but moving left
		if (up && velocity.y < 0)
			up = false;
		if (down && velocity.y > 0)
			down = false;

		// Stops the plane
		if (!up && !down) {
			if (velocity.y < 1.5f && velocity.y > -1.5f) {
				acceleration.y = 0;
				velocity.y = 0;
			}
		}

		// If you didn't collide in y axis,
		// add deltaY to the position.y
		if (!collision(0, deltay)) {
			position.y += deltay;

			// if you did collide with something,
			// you did in the Y AXIS ONLY
		} else {

		}

	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		handleAllPollingInput();

		moveX(deltaTime);
		moveY(deltaTime);

	}

	// To be overridden in subclasses
	protected void ensureCorrectCollisionBounds() {

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
		for (AbstractGameObject platform : LevelStage.frontObjects) {

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

		// Collide with bullets
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
		if (animation != null) {
			image = animation.getKeyFrame(stateTime, looping);
		}

		// Draw
		batch.draw(image.getTexture(), position.x, position.y, origin.x,
				origin.y, dimension.x, dimension.y, 1, 1, rotation,
				image.getRegionX(), image.getRegionY(), image.getRegionWidth(),
				image.getRegionHeight(), flipX, flipY);

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
		this.position.x = MathUtils.clamp(position.x, rect.x, rect.x + rect.width - dimension.x);
		this.position.y = MathUtils.clamp(position.y, rect.y, rect.y + rect.height - dimension.y);
		bounds.setPosition(position);

	}

}
