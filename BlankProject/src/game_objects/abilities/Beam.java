package game_objects.abilities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import backend.Calc;
import backend.LevelStage;
import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;

public class Beam extends AbstractAbility {

	private DIRECTION direction;
	private float endTime, currentTime, length, angle;
	private int numBounds;
	private boolean alsoDamagesEnemies;
	private float percent;
	private int numCubes;

	public Beam(ManipulatableObject parent, DIRECTION direction, int dmg,
			boolean alsoDamagesEnemies, float width, float height,
			float length, float time) {
		super(parent, parent.position.x + parent.dimension.x / 2 - width / 2,
				parent.position.y + parent.dimension.y / 2 - height / 2, width,
				height);
		this.angle = Calc.directionToDegrees(direction);

		init(dmg, alsoDamagesEnemies, time, length);

	}

	public Beam(ManipulatableObject parent, float angle, int dmg,
			boolean alsoDamagesEnemies, int width, int height, float length,
			float time) {
		super(parent, parent.position.x + parent.dimension.x / 2 - width / 2,
				parent.position.y + parent.dimension.y / 2 - height / 2, width,
				height);
		this.angle = angle;
		init(dmg, alsoDamagesEnemies, time, length);

	}
	
	private void init(int dmg, boolean alsoDamagesEnemies, float time,
			float length) {
		this.willHandleCollision = false;
		this.length = length;
		this.damage = dmg;
		currentTime = 0;
		lifeTimer = time;
		percent = 0;
		this.alsoDamagesEnemies = alsoDamagesEnemies;

		initDebug();
		this.endTime = time;
		this.deletionTime = .25f;
		priority = 3;
		parent.stopMove();
		parent.stun(time);
		knockbackAngle = angle;
		

	}
	
	@Override
	public void render(SpriteBatch batch) {

		Vector2 origin = parent.getCenter();

		for(int i = 0; i < numCubes; i++){
			if(debug){
				bounds.setPosition(origin.x + Calc.cos(angle) * i,
						origin.y + Calc.sin(angle) * i);
				batch.draw(debugTex, bounds.x, bounds.y, bounds.width, bounds.height);
			}
		}
		super.render(batch);
	}

	@Override
	protected void handleCollision() {
		Vector2 origin = parent.getCenter();

		// Set bounds to where this object will be after adding
		// the velocity of this frame to check and see if we are
		// going to collide with anything
		numCubes = (int) (length * percent);
		System.out.println(numCubes);
		for (int i = 0; i < numCubes; i++) {

			bounds.setPosition(origin.x + Calc.cos(angle) * i,
					origin.y + Calc.sin(angle) * i);
			// Iterate through platforms

			// Collide with objects that have an effect on collision
			for (AbstractGameObject interactable : parent.enemyTeamObjects) {
				if (bounds.overlaps(interactable.bounds)) {
					interact(interactable);
				}
			}
			// Collide with objects that have an effect on collision
			for (AbstractGameObject interactable : parent.enemyTeamObjects) {
				if (bounds.overlaps(interactable.bounds)) {
					interact(interactable);
				}
			}
		}
	}

	@Override
	public void update(float deltaTime) {
		currentTime += deltaTime;
		percent = currentTime / endTime;
		bounds.setSize(1, 1);
		
		if (percent > 1)
			percent = 1;

		/*
		 * if (direction == DIRECTION.RIGHT) { dimension.x = length * percent;
		 * bounds.width = length * percent; knockbackAngle = 0; } else if
		 * (direction == DIRECTION.LEFT) { dimension.x = length * percent;
		 * position.x = parent.getCenter().x - dimension.x; bounds.x =
		 * position.x; bounds.width = dimension.x; knockbackAngle = 180; } else
		 * if (direction == DIRECTION.UP) { dimension.y = length * percent;
		 * bounds.height = length * percent; knockbackAngle = 90; } else if
		 * (direction == DIRECTION.DOWN) { dimension.y = length * percent;
		 * position.y = parent.getCenter().y - dimension.y; bounds.y =
		 * position.y; bounds.height = dimension.y; knockbackAngle = 270; }
		 */
		super.update(deltaTime);
	}

	@Override
	public void interact(AbstractGameObject couple) {
		super.interact(couple);

		if (couple instanceof ManipulatableObject) {
			ManipulatableObject obj = (ManipulatableObject) couple;
			if (this.isSameTeam(obj)) {
				boolean newObj = isFirstInteraction(obj);

				if (newObj)
					obj.heal(damage);
			} else if (alsoDamagesEnemies) {
				boolean newObj = isFirstInteraction(obj);

				if (newObj)
					obj.takeHitFor(damage, this);
			}
		}
	}

}
