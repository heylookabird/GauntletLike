package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;

import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class AOE extends AbstractAbility {

	private boolean hitAll;

	public AOE(Animation animation, ManipulatableObject parent, int damage) {
		super(parent, parent.position.x, parent.position.y, 1, 1);
		this.animation = animation;
		this.damage = damage;
		this.lifeTimer = animation.animationDuration;

		knockbackTime = .3f;

		timers = new Vector<Float>();
		this.knockbackSpeed = 10;
	}

	public AOE(Animation animation, ManipulatableObject parent, int damage,
			float knockbackTime, float deletionTime, float x, float y,
			float width, float height, boolean hitAll) {
		super(parent, x, y, 1, 1);
		this.hitAll = hitAll;
		this.setAnimation(animation);
		this.damage = damage;
		this.lifeTimer = animation.animationDuration;
		this.deletionTime = deletionTime;

		timers = new Vector<Float>();
		this.knockbackTime = knockbackTime;
		this.knockbackSpeed = 10;
	}

	public AOE(TextureRegion image, ManipulatableObject parent, int damage) {
		super(parent, parent.position.x, parent.position.y, 1, 1);
		this.setImage(image);
		this.damage = damage;

		timers = new Vector<Float>();
		knockbackTime = .3f;
		this.knockbackSpeed = 10;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		spread();
		checkCollisions();
	}

	private void checkCollisions() {

	}

	@Override
	public void interact(AbstractGameObject couple) {

		if (couple instanceof ManipulatableObject) {
			ManipulatableObject temp = (ManipulatableObject) couple;
			if (hitAll) {
				boolean newObj = isFirstInteraction(temp);
				if (newObj) {
					temp.takeHitFor(damage, this);
				}
				return;
			} else if (!this.isSameTeam(temp)) {
				boolean newObj = isFirstInteraction(temp);

				if (newObj) {
					temp.takeHitFor(damage, this);
				}
			}
		}
	}

	private void spread() {
		Vector2 temp = getCenter();
		dimension.x += .05f;
		dimension.y += .05f;
		bounds.width = dimension.x;
		bounds.height = dimension.y;
		position.set(temp.x - dimension.x / 2, temp.y - dimension.y / 2);
	}

}
