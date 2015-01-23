package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;

import com.badlogic.gdx.utils.Array;

public class Taunt extends AbstractAbility {


	private Array<ManipulatableObject> taunted;
	private boolean shieldOn;
	public Taunt(ManipulatableObject parent, float x, float y, float width,
			float height, boolean shieldOn) {
		super(parent, x, y, width, height);
		lifeTimer = 5;
		taunted = new Array<ManipulatableObject>();
		this.shieldOn = shieldOn;
		
	}
	
	@Override
	public void postDeathEffects() {
		for(int i = 0; i < taunted.size; i++){
			ManipulatableObject obj = taunted.get(i);
			obj.resetTarget();
		}
		
	}
	@Override
	public void interact(AbstractGameObject couple) {
		if(couple instanceof ManipulatableObject){
			ManipulatableObject obj = (ManipulatableObject) couple;
			
		
			obj.setTarget(this);
			taunted.add(obj);

			
			
		}
	}

}
