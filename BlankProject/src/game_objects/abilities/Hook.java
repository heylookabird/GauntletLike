package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import backend.Assets;
import backend.Calc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Hook extends AbstractAbility {
	private ManipulatableObject parent;
	private Array<Link> links;
	private int numLinks;
	private float timeBetweenChains, maxTime = .02f, deChainTime = .025f;
	public static float hookCD;
	public boolean chaining, deChaining;
	boolean hit;
	public static float angle;
	public boolean swungThisHook;
	private Vector2 hookPull;
	Vector2 target, currentVector;
	public Hook(ManipulatableObject parent, float joyStick){
		super();
		hit= false;
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
		
		startHook(joyStick);
		initDebug();
	}
	public Hook() {
		// TODO Auto-generated constructor stub
	}
	public void startHook(float direction){
		angle = direction;
		
		if(hookCD <= 0){
			chaining = true;
			
			//target.set(rock.position.x, rock.position.y + rock.bounds.height / 2);
			timeBetweenChains = 0;
		}
		
	}
	public void stopHook(){
		chaining = false;
		deChaining = true;
		timeBetweenChains = 0;
		
	}
	public Vector2 getRockVector(){
		return target;
	}
	public Link getLastLink(){
		if(links.size > 0)
			return links.peek();
		return null;
	}
	public int getNumLinks(){
		return numLinks;
	}
	
	public void hit(){
		/*Vector2 current = new Vector2(rock.position.x + rock.bounds.width / 2 - bunny.bounds.width / 2, rock.position.y + rock.bounds.height / 2);
		hookPull.set(current.sub(bunny.position));
		hookPull.set(hookPull.x / numLinks, hookPull.y / numLinks);*/
		hit = true;
	}
	
	private void makeNewLink(float angle){
		links.add(new Link(parent, links.size, angle));
		numLinks++;
		if(numLinks > 10)
			stopHook();
		
	}
	private void removeLastLink(){
		
		if(links.size > 0){
			links.pop();
			numLinks--;
			
			//If hook hit a ledge, reel the player in
			if(hit){
				//Should pull in last link, I think. lazy thought
		
			}
			
		}else{
			if(hit){
				//NEEDS TO PULL IN
				
				hookCD = 4;

			}
			//Reset variables for the next grapple
			deChaining = false;
			hit = false;
		}
		
	}
	@Override
	public void update(float deltaTime){
		if(chaining){
			
			//angle = (float) Math.atan2((double)(target.y - (bunny.position.y + bunny.bounds.height / 2)), (double)(target.x - (bunny.position.x + bunny.bounds.width)));
			timeBetweenChains += deltaTime;
			while(timeBetweenChains  > maxTime){
				makeNewLink(angle);
				timeBetweenChains -= maxTime;
			}
			for(int i = 0; i < links.size; i++){
				links.get(i).update(deltaTime);
			}
			
		}else if(deChaining){

			//angle = (float) Math.atan2((double)(target.y - (bunny.position.y + bunny.bounds.height / 2)), (double)(target.x - (bunny.position.x + bunny.bounds.width)));

			timeBetweenChains += deltaTime;
			while(timeBetweenChains > deChainTime){
				removeLastLink();
				timeBetweenChains -= deChainTime;
			}
			for(int i = 0; i < links.size; i++){
				links.get(i).update(deltaTime);
			}
		}
		
		if(hookCD > 0){
			hookCD -= deltaTime;
		}
		
	}
	
	@Override
	public void render(SpriteBatch batch) {
		for(int  i = 0; i < links.size; i++){
			links.get(i).render(batch);
		}
	}
	
	public class Link extends AbstractGameObject{
		private int spotNumber;
		private static final float width = 1;
		private static final float height = .25f;
		public Link(){
			super();
			image = Assets.instance.weapons.sword;
			dimension.set(width, height);
		}
		public Link(ManipulatableObject parent, int spotNumber, float angle){
			super(parent.getCenter().x / 2 - width / 2, parent.getCenter().y / 2 - height / 2, width, height);
			image = Assets.instance.weapons.sword;
			origin.set(0, height / 2);

			this.spotNumber = spotNumber;
			rotation = angle;
		}
		
		@Override
		public void update(float deltaTime){
			super.update(deltaTime);
			
			float x = Calc.cos(rotation);
			float y = Calc.sin(rotation);
			Vector2 center = parent.getCenter();
			
			this.position.set(center.x + spotNumber * x * bounds.width,
					center.y + spotNumber * y * bounds.width);
		}
		
	}
	

}
