package game_objects;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractGameObject {
	public Vector2 position;
	public Vector2 dimension;
	public Vector2 origin;
	public float scale;
	public boolean debug;

	// Physics
	public Vector2 accelerationPerSecond; // Acceleration will be set to this
											// constant
	public Vector2 acceleration;
	public Vector2 velocity;
	public Vector2 terminalVelocity; // Objects max speed magnitude
	public Rectangle bounds; // objects bounding box used for collision
	protected float deltax, deltay, rotSpeed;

	protected TextureRegion image;

	public boolean looping;
	public Vector2 currentFrameDimension;
	public float rotation;

	// Animation
	public Animation animation;
	public float stateTime;
	public boolean animationBool;
	protected boolean baseMovement;

	protected boolean flipX;
	protected boolean flipY;
	public boolean onScreen;

	private float rotationalVelocity;
	protected Texture debugTex;

	public AbstractGameObject() {
		position = new Vector2();
		dimension = new Vector2(1, 1);
		origin = new Vector2();
		scale = 1;
		rotation = 0;
		rotationalVelocity = 0;

		acceleration = new Vector2();
		velocity = new Vector2();
		terminalVelocity = new Vector2(1, 1);
		bounds = new Rectangle(position.x, position.y, 1, 1);
		accelerationPerSecond = new Vector2();
	}

	public AbstractGameObject(float x, float y, float width, float height) {
		position = new Vector2(x, y);
		dimension = new Vector2(width, height);
		origin = new Vector2(width / 2, height / 2);
		scale = 1;
		rotation = 0;

		acceleration = new Vector2();
		accelerationPerSecond = new Vector2();
		velocity = new Vector2();
		terminalVelocity = new Vector2(20, 20);
		bounds = new Rectangle(x, y, width, height);
	}

	public AbstractGameObject(float x, float y, float width, float height,
			boolean flipX, boolean flipY) {
		position = new Vector2(x, y);
		dimension = new Vector2(width, height);
		origin = new Vector2();
		scale = 1;
		rotation = 0;

		acceleration = new Vector2();
		accelerationPerSecond = new Vector2();
		velocity = new Vector2();
		terminalVelocity = new Vector2(20, 20);
		currentFrameDimension = new Vector2();
		bounds = new Rectangle(x, y, width, height);
		this.flipX = flipX;
		this.flipY = flipY;
	}

	protected void initDebug() {
		debug = true;

		Pixmap temp = new Pixmap(32, 32, Format.RGB888);
		temp.setColor(0, 0, 0, 0);
		temp.fill();

		debugTex = new Texture(temp);

		temp.dispose();
	}

	public void setRotationalVelocity(float rotationalVelocity) {
		this.rotationalVelocity = rotationalVelocity;
	}

	private void updateRotation(float deltaTime) {
		rotation += rotationalVelocity * deltaTime;
	}

	public void interact(AbstractGameObject couple) {

	}

	public void setAnimation(Animation animation) {
		if (animation.getPlayMode() == Animation.NORMAL) {
			looping = false;
		} else
			looping = true;
		this.animation = animation;
		stateTime = 0;

	}

	public void animationComplete() {

	}

	public void setImage(TextureRegion image) {
		this.image = image;
	}

	public void update(float deltaTime) {

		// Update stateTime for animation to know which frame it's in
		stateTime += deltaTime;
		stateTime = stateTime > 0 ? stateTime : 0;

		// Calls animationComplete which is specified on an object basis
		if (animation != null && animation.isAnimationFinished(stateTime)) {
			animationComplete();
		}
		// onScreen = World.controller.cameraHelper.onScreen(this) ? true :
		// false;

		updateMotionX(deltaTime);
		updateMotionY(deltaTime);
		updateRotation(deltaTime);

	}

	protected void updateMotionX(float deltaTime) {

		// Apply acceleration
		velocity.x += acceleration.x * deltaTime;
		// Make sure the object's velocity does not exceed the terminal velocity
		velocity.x = MathUtils.clamp(velocity.x, -terminalVelocity.x,
				terminalVelocity.x);
		
		if(baseMovement)
			position.x += velocity.x * deltaTime;

	}

	protected void updateMotionY(float deltaTime) {

		velocity.y += acceleration.y * deltaTime;
		velocity.y = MathUtils.clamp(velocity.y, -terminalVelocity.y,
				terminalVelocity.y);
		if(baseMovement)
			position.y += velocity.y * deltaTime;

	}

	public void render(SpriteBatch batch) {
		// get correct image and draw the current proportions
		image = null;
		image = animation.getKeyFrame(stateTime, looping);
		currentFrameDimension.set(image.getRegionWidth() / 10,
				image.getRegionHeight() / 10);
		// Draw
		if (onScreen)
			batch.draw(image.getTexture(), position.x, position.y, origin.x,
					origin.y, currentFrameDimension.x, currentFrameDimension.y,
					1, 1, rotation, image.getRegionX(), image.getRegionY(),
					image.getRegionWidth(), image.getRegionHeight(), flipX,
					flipY);

	}
}
