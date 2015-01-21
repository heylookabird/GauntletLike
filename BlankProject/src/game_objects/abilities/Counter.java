package game_objects.abilities;

import backend.LevelStage;
import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;

public class Counter extends AbstractAbility {


	public Counter(ManipulatableObject parent, float x, float y, float width,
			float height) {
		super(parent, x, y, width, height);
		lifeTimer = 4;
		parent.invulnerable = true;
		parent.stun(lifeTimer);
	
	}
	
	@Override
	public void postDeathEffects() {
		parent.invulnerable = false;
		parent.stunTimer = 0;
		super.postDeathEffects();
	}
	
	@Override
	public void update(float deltaTime) {
		for(int i = 0; i < LevelStage.interactables.size; i++){
			AbstractAbility obj = (AbstractAbility)LevelStage.interactables.get(i);
			
			if(obj != this && parent.teamObjects != obj.parent.teamObjects){
				if(obj.bounds.overlaps(bounds)){
					System.out.println("Countered!");
					lifeTimer = 0;
				}
			}
			
		}
		
		super.update(deltaTime);
	}

}
