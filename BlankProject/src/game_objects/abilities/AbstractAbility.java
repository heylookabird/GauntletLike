package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;

import backend.LevelStage;

import com.badlogic.gdx.utils.Array;

public abstract class AbstractAbility extends AbstractGameObject {
	
	protected Array<ManipulatableObject> objectsAlreadyHit;
	protected int damage;
	protected ManipulatableObject parent;
	
	//booleans for controlling
	boolean projectile, melee;
	
	//HOW LONG BEFORE IT DELETES ITSELF.... FOREVER?
	protected float lifeTimer; boolean suicidal; //if suicidal it will delete itself

	public AbstractAbility() {
		super();
		objectsAlreadyHit = new Array<ManipulatableObject>();

	}

	public AbstractAbility(ManipulatableObject parent, float x, float y, float width, float height) {
		super(x, y, width, height);
		this.parent = parent;
		objectsAlreadyHit = new Array<ManipulatableObject>();
		
		initDebug();
		lifeTimer = 1;
		
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
		if(suicidal){
			lifeTimer -= deltaTime;
			if(lifeTimer < 0)
				removeThyself();
			
		}
	}

	protected void removeThyself() {
		LevelStage.interactables.removeValue(this, true);
	}


}
