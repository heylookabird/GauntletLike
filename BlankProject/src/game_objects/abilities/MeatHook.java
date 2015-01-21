package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import backend.Assets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class MeatHook extends Arrow {
	Array<ChainNode> chains;
	int maxChains = 6, currentNumOfChains = 0;
	boolean hit = false;
	ManipulatableObject grabbed;

	public class ChainNode {
		TextureRegion img = Assets.instance.weapons.sword;
		Rectangle bounds;

		public ChainNode(float x, float y, float width, float height) {
			bounds = new Rectangle(x, y, width, height);
		}

		public Vector2 getCenter() {
			return new Vector2(bounds.x + bounds.width / 2, bounds.y
					+ bounds.height / 2);
		}

		public void render(SpriteBatch batch) {
			batch.draw(img, bounds.x, bounds.y, bounds.width, bounds.height);
		}
	}

	public MeatHook(ManipulatableObject parent, int damage, float xVelocity,
			float yVelocity) {
		super(parent, damage, xVelocity, yVelocity);
		chains = new Array<ChainNode>();
		chains.add(new ChainNode(position.x, position.y, 1, 1));
		this.damage = 0;
		this.lifeTimer = .9f;
		this.knockbackSpeed = 0;
		removesItself = false;

	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		if (!hit) {
			extend();
		}

		else {
			retract();
		}

		for (ChainNode node : chains) {
			node.bounds.x += parent.deltax;
			node.bounds.y += parent.deltay;
		}
	}

	private void retract() {
		if (stateTime > .1f) {
			if (chains.size > 1) {
				ChainNode last = chains.pop();
				grabbed.position.set(last.bounds.x, last.bounds.y);
				stateTime = 0;
			} else {
				removeThyself();
				grabbed.stun(.3f);
			}
		}
	}

	private void extend() {
		if (stateTime > .2f) {
			if (chains.size <= maxChains) {
				chains.add(new ChainNode(bounds.x, bounds.y, 1, 1));
				stateTime = 0;
			} else {
				removeThyself();
			}
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		if (!hit)
			super.render(batch);

		for (ChainNode node : chains) {
			node.render(batch);
		}
	}

	@Override
	public void interact(AbstractGameObject couple) {
		super.interact(couple);

		if (couple instanceof ManipulatableObject) {
			if (!hit) {
				ManipulatableObject obj = (ManipulatableObject) couple;
				if (!isSameTeam(obj)) {
					hit = true;
					grabbed = obj;
					stateTime = 0;
					// grabbed.stun(1f);
				}
			}
		}

	}

}
