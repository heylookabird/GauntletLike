package game_objects.abilities;

import java.util.Vector;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.Wall;
import backend.Assets;
import backend.LevelStage;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public abstract class AbstractAbility extends AbstractGameObject {

	protected Array<ManipulatableObject> objectsAlreadyHit;
	protected Array<Float> timesToBeDeleted;
	protected int damage;
	protected ManipulatableObject parent;
	public float range;

	public float stunTime, knockbackSpeed, knockbackTime, knockbackAngle;
	// booleans for controlling
	boolean projectile, melee;

	// HOW LONG BEFORE IT DELETES ITSELF.... FOREVER?
	protected float lifeTimer;
	boolean removesItself = true;
	protected Vector<Float> timers;
	protected Float deletionTime;

	public AbstractAbility() {
		super();
		objectsAlreadyHit = new Array<ManipulatableObject>();
		timers = new Vector<Float>();
		deletionTime = 2f;
	}

	public AbstractAbility(ManipulatableObject parent, float x, float y,
			float width, float height) {
		super(x, y, width, height);
		this.parent = parent;
		objectsAlreadyHit = new Array<ManipulatableObject>();
		timers = new Vector<Float>();
		deletionTime = .2f;

		initDebug();
		lifeTimer = 1;
		stunTime = .3f;
		knockbackSpeed = 6;
		knockbackTime = .3f;
		knockbackAngle = 90;
		image = Assets.instance.weapons.sword;

	}

	protected boolean isFirstInteraction(ManipulatableObject obj) {

		for (ManipulatableObject object : objectsAlreadyHit) {
			if (object == obj)
				return false;
		}

		objectsAlreadyHit.add(obj);
		timers.add(deletionTime);

		return true;
	}

	@Override
	public void update(float deltaTime) {

		super.update(deltaTime);

		manageObjectsHit(deltaTime);

		// REMOVES ITSELF
		if (removesItself) {
			lifeTimer -= deltaTime;
			if (lifeTimer < 0) {
				removeThyself();
				postDeathEffects();
			}
		}

		deltax = velocity.x;

		if (!collision(deltax, 0)) {
			position.x += deltax;
		}

		deltay = velocity.y;
		if (!collision(0, deltay)) {
			position.y += deltay;
		}

	}

	private void manageObjectsHit(float deltaTime) {
		for (int i = 0; i < timers.size(); i++) {
			Float temp = timers.get(i);
			if (temp > 0) {
				temp -= deltaTime;
				timers.set(i, temp);

			} else {
				this.objectsAlreadyHit.removeIndex(i);
				timers.remove(i);
				i--;
			}
		}
	}

	public void postDeathEffects() {

	}

	@Override
	public void interact(AbstractGameObject couple) {
		if (couple instanceof Wall) {
			lifeTimer = 0;
		}
	}

	public boolean isSameTeam(ManipulatableObject obj) {
		if (parent.teamObjects == obj.teamObjects) {
			return true;
		}

		return false;
	}

	protected void removeThyself() {
		LevelStage.interactables.removeValue(this, true);
	}

	@Override
	public void render(SpriteBatch batch) {

		super.render(batch);
	}

}
