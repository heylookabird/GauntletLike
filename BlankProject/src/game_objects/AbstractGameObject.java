package game_objects;

import backend.LevelStage;

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
	public float deltax, deltay, rotSpeed;

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
		//this.initDebug();
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
	
	public Vector2 getCenter() {
		
		return new Vector2(position.x + dimension.x / 2, position.y + dimension.y / 2);
	}

	//TO BE OVERRIDEN IN SUBCLASSES
	public void interact(AbstractGameObject couple) {}

	
	protected boolean collision(float deltaX, float deltaY) {

		// Set bounds to where this object will be after adding
		// the velocity of this frame to check and see if we are
		// going to collide with anything
		bounds.setPosition(position.x + deltaX, position.y + deltaY);
		boolean temp = false;
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
				temp = true;
				interact(platform);
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
				temp = true;
				interact(platform);
			}
		}

		// Collide with objects that have an effect on collision
		for (AbstractGameObject interactable : LevelStage.interactables) {
			if (bounds.overlaps(interactable.bounds)) {
				interact(interactable);
			}
		}
		return temp;

	}
	
	public void setAnimation(Animation animation) {
		if (animation.getPlayMode() == Animation.NORMAL) {
			looping = false;
		} else
			looping = true;
		this.animation = animation;
		//stateTime = 0;

	}
	//TO BE OVERRIDEN IN SUBCLASSES
	public void animationComplete() {
	
	}

	public void setImage(TextureRegion image) {
		this.image = image;
	}

	public void update(float deltaTime) {

		// Update stateTime for animation to know which frame it's in
		stateTimeUpdate(deltaTime);
		// onScreen = World.controller.cameraHelper.onScreen(this) ? true :
		// false;

		updateMotionX(deltaTime);
		updateMotionY(deltaTime);
		updateRotation(deltaTime);
		
		bounds.setPosition(position);

	}
	
	private void stateTimeUpdate(float deltatime){
		stateTime += deltatime;
		stateTime = stateTime > 0 ? stateTime : 0;

		// Calls animationComplete which is specified on an object basis
		if (animation != null && animation.isAnimationFinished(stateTime)) {
			animationComplete();
		}
	}

	public void updateMotionX(float deltaTime) {

		// Apply acceleration
		velocity.x += acceleration.x * deltaTime;
		// Make sure the object's velocity does not exceed the terminal velocity
		velocity.x = MathUtils.clamp(velocity.x, -terminalVelocity.x,
				terminalVelocity.x);


	}

	protected void updateMotionY(float deltaTime) {
		//Apply acceleration
		velocity.y += acceleration.y * deltaTime;
		//Clamp it baby
		velocity.y = MathUtils.clamp(velocity.y, -terminalVelocity.y,
				terminalVelocity.y);
	}

	public void render(SpriteBatch batch) {
		// get correct image and draw the current proportions
		if(animation != null)
			image = animation.getKeyFrame(stateTime);
		
				// Draw
		if(image != null){
			batch.draw(image.getTexture(), position.x, position.y, origin.x,
					origin.y, dimension.x, dimension.y,
					1, 1, rotation, image.getRegionX(), image.getRegionY(),
					image.getRegionWidth(), image.getRegionHeight(), flipX,
					flipY);
		}
			
		if(debug){
			batch.draw(debugTex, bounds.x, bounds.y, bounds.width, bounds.height);
		}

	}
	
	public void updateOrigin(){
		origin.set(position.x + dimension.x/2, position.y + dimension.y/2);
	}
}
