package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.Wall;
import backend.LevelStage;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public abstract class AbstractAbility extends AbstractGameObject {
	
	protected Array<ManipulatableObject> objectsAlreadyHit;
	protected int damage;
	protected ManipulatableObject parent;
	public float range;
	
	public float stunTime, knockBack = .3f;
	//booleans for controlling
	boolean projectile, melee;
	
	//HOW LONG BEFORE IT DELETES ITSELF.... FOREVER?
	protected float lifeTimer; boolean removesItself = true; 

	public AbstractAbility() {
		super();
		objectsAlreadyHit = new Array<ManipulatableObject>();

	}

	public AbstractAbility(ManipulatableObject parent, float x, float y, float width, float height) {
		super(x, y, width, height);
		this.parent = parent;
		objectsAlreadyHit = new Array<ManipulatableObject>();
		
		//initDebug();
		lifeTimer = 1;
		stunTime = .3f;
		
	}
	protected boolean isFirstInteraction(ManipulatableObject obj){
		
		for(ManipulatableObject object : objectsAlreadyHit){
			if(object == obj)
				return false;
		}
		
		objectsAlreadyHit.add(obj);
		
		
		return true;
	}
	@Override
	public void update(float deltaTime) {

		super.update(deltaTime);
		
		//REMOVES ITSELF
		if(removesItself){
			lifeTimer -= deltaTime;
			if(lifeTimer < 0){
				removeThyself();
				postDeathEffects();
			}
		}
		
		deltax = velocity.x;
		
		if(!collision(deltax, 0)){
			position.x += deltax;
		}
		
		deltay = velocity.y;
		if(!collision(0, deltay)){
			position.y += deltay;
		}
		
	}

	public void postDeathEffects() {
		
	}
	
	@Override
	public void interact(AbstractGameObject couple){
		if(couple instanceof Wall){
			lifeTimer = 0;
		}
	}
	
	public boolean isSameTeam(ManipulatableObject obj){
		if(parent.teamObjects == obj.teamObjects){
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
