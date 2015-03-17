package game_objects.abilities;

import game_map_classes.CornerConcave;
import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.Wall;
import backend.Assets;
import backend.Calc;
import backend.LevelStage;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Hook extends AbstractAbility {
	private Array<Link> links;
	private int numLinks;
	private float timeBetweenChains, maxTime = .02f, deChainTime = .1f;
	public static float hookCD;
	public boolean chaining, deChaining;
	boolean hit;
	public static float angle;
	public boolean swungThisHook;
	Vector2 target, currentVector;

	private AbstractGameObject pulled, puller;
	private int maxChains;

	public Hook(ManipulatableObject parent, float joyStick, int maxchains) {
		super();
		hit = false;
		chaining = false;
		this.parent = parent;
		swungThisHook = false;
		deChaining = false;
		links = new Array<Link>();
		timeBetweenChains = 0;
		target = new Vector2();
		hookPull = new Vector2();
		numLinks = 0;
		hookCD = 0;
		removesItself = false;

		this.maxChains = maxchains;
		startHook(joyStick);
		// initDebug();
	}

	public Hook() {
		// TODO Auto-generated constructor stub
	}

	public void startHook(float direction) {
		angle = direction;

		if (hookCD <= 0) {
			chaining = true;

			// target.set(rock.position.x, rock.position.y + rock.bounds.height
			// / 2);
			timeBetweenChains = 0;
		}

	}

	public void stopHook() {
		chaining = false;
		deChaining = true;
		timeBetweenChains = 0;

	}

	public void reverse() {
		// ManipulatableObject temp = puller;
		puller = pulled;
		pulled = puller;
	}

	public void stopHookAndPull(AbstractGameObject puller,
			ManipulatableObject pulled) {
		chaining = false;
		deChaining = true;
		timeBetweenChains = 0;
		this.pulled = pulled;
		this.puller = puller;

		float time = (links.size - 1) * deChainTime;
		float angle = Calc.atan2(pulled.getCenter(), puller.getCenter());
		float distance = puller.getCenter().dst(pulled.getCenter());

		pulled.velocity.set(distance * Calc.cos(angle) / time,
				distance * Calc.sin(angle) / time);
		pulled.terminalVelocity.set(100, 100);

	}

	public void stopHookAndPull(ManipulatableObject puller,
			ManipulatableObject pulled) {
		chaining = false;
		deChaining = true;
		timeBetweenChains = 0;

		this.pulled = pulled;
		this.puller = puller;
		float time = (links.size - 1) * deChainTime;
		float angle = Calc.atan2(pulled.getCenter(), puller.getCenter());
		float distance = puller.getCenter().dst(pulled.getCenter());

		pulled.velocity.set(distance * Calc.cos(angle) / time,
				distance * Calc.sin(angle) / time);
		pulled.terminalVelocity.set(100, 100);
		System.out.println(distance);

	}

	public Vector2 getRockVector() {
		return target;
	}

	public Link getLastLink() {
		if (links.size > 0)
			return links.peek();
		return null;
	}

	public int getNumLinks() {
		return numLinks;
	}

	public void hit() {
		/*
		 * Vector2 current = new Vector2(rock.position.x + rock.bounds.width / 2
		 * - bunny.bounds.width / 2, rock.position.y + rock.bounds.height / 2);
		 * hookPull.set(current.sub(bunny.position)); hookPull.set(hookPull.x /
		 * numLinks, hookPull.y / numLinks);
		 */
		hit = true;
	}

	@Override
	protected boolean collision(float deltaX, float deltaY) {
		if (!chaining)
			return false;
		// bounds.setPosition(position.x + deltaX, position.y + deltaY);

		if (links.size < 2)
			return false;

		Link link = links.get(links.size - 1);
		AbstractGameObject temp = null;
		for (int i = 0; i < parent.teamObjects.size; i++) {
			temp = parent.teamObjects.get(i);
			if (link.bounds.overlaps(temp.bounds)) {
				interact(temp);
				links.pop();
				return false;
			}

		}
		for (int i = 0; i < parent.enemyTeamObjects.size; i++) {
			temp = parent.enemyTeamObjects.get(i);
			if (link.bounds.overlaps(temp.bounds)) {
				interact(temp);
			}
		}
		for (int i = 0; i < LevelStage.solidObjects.size; i++) {
			temp = LevelStage.solidObjects.get(i);
			if (link.bounds.overlaps(temp.bounds)) {
				interact(temp);
			}
		}

		return true;

	}

	private void makeNewLink(float angle) {
		links.add(new Link(parent, links.size, angle));
		numLinks++;
		if (numLinks > maxChains)
			stopHook();

	}

	private void removeLastLink() {
		if (links.size > 2) {
			links.pop();
			numLinks--;

		} else {
			// NEEDS TO PULL IN

			hookCD = 4;

			if (puller != null) {
				if (puller instanceof ManipulatableObject) {
					ManipulatableObject pull = (ManipulatableObject) puller;
					pull.stunTimer = 0;
					pull.invulnerable = false;
					pull.stopMove();
				} else if (puller instanceof Wall
						|| puller instanceof CornerConcave) {

				}

			}
			if (pulled != null) {

				if (pulled instanceof ManipulatableObject) {
					ManipulatableObject pull = (ManipulatableObject) pulled;
					pull.stunTimer = 0;
					pull.invulnerable = false;
					pull.stopMove();
					pull.terminalVelocity.set(pull.walkingTerminalV);
				} else if (pulled instanceof Wall
						|| pulled instanceof CornerConcave) {

				}
			}
			parent.stunTimer = 0;
			// Reset variables for the next grapple
			deChaining = false;
			hit = false;
			links.clear();
			// System.out.println("Done with last link");
		}
	}

	@Override
	public void interact(AbstractGameObject couple) {

		if (couple instanceof ManipulatableObject) {
			ManipulatableObject obj = (ManipulatableObject) couple;
			if (!this.isSameTeam(obj)) {
				stopHookAndPull(parent, obj);
			} else if (obj != parent) {
				stopHookAndPull(obj, parent);
			}

			parent.stun(100);
			parent.invulnerable = true;

			obj.stun(100);
			obj.invulnerable = true;
		}

		if (couple instanceof Wall || couple instanceof CornerConcave) {
			stopHookAndPull(couple, parent);

			parent.stun(100);
			parent.invulnerable = true;
		}

		if (couple instanceof AbstractAbility) {
			AbstractAbility ability = (AbstractAbility) couple;
			if (!ability.isSameTeam(parent)) {
				if (ability.priority > this.priority) {
					lifeTimer = 0;
					cancelled = true;
				} else if (ability.priority < priority) {
					ability.lifeTimer = 0;
					ability.cancelled = true;
				} else {
					lifeTimer = 0;
					cancelled = true;
				}
			}
		}
	}

	@Override
	public void update(float deltaTime) {
		// System.out.println(parent.stunTimer);
		super.update(deltaTime);
		if (chaining) {
			timeBetweenChains += deltaTime;
			while (timeBetweenChains > maxTime) {
				makeNewLink(angle);
				timeBetweenChains -= maxTime;
			}
			for (int i = 0; i < links.size; i++) {
				links.get(i).update(deltaTime);
			}

		} else if (deChaining) {

			timeBetweenChains += deltaTime;
			while (timeBetweenChains > deChainTime) {
				removeLastLink();
				timeBetweenChains -= deChainTime;
			}
			for (int i = 0; i < links.size; i++) {
				links.get(i).update(deltaTime);
			}
		}

		if (hookCD > 2) {
			hookCD -= deltaTime;
		}

	}

	@Override
	public void render(SpriteBatch batch) {
		for (int i = 0; i < links.size; i++) {
			links.get(i).render(batch);
		}
	}

	public class Link extends AbstractGameObject {
		private int spotNumber;
		private static final float width = 1;
		private static final float height = .25f;

		public Link() {
			super();
			image = Assets.instance.weapons.sword;
			dimension.set(width, height);
		}

		public Link(ManipulatableObject parent, int spotNumber, float angle) {
			super(parent.getCenter().x / 2 - width / 2, parent.getCenter().y
					/ 2 - height / 2, width, height);
			image = Assets.instance.weapons.sword;
			origin.set(0, height / 2);
			bounds.setSize(width / 2, width / 2);

			this.spotNumber = spotNumber;
			rotation = angle;
		}

		@Override
		public void update(float deltaTime) {
			super.update(deltaTime);

			float x = Calc.cos(rotation);
			float y = Calc.sin(rotation);
			Vector2 center = parent.getCenter();

			this.position.set(center.x + spotNumber * x * dimension.x, center.y
					+ spotNumber * y * dimension.x);
			bounds.setPosition(position.x + dimension.x * x, position.y
					+ dimension.x * y);
		}

	}

}
