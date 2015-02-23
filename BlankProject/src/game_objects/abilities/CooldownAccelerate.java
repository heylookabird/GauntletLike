package game_objects.abilities;

import game_objects.ManipulatableObject;
import game_objects.abilities.effects.CooldownBoost;

public class CooldownAccelerate extends AbstractAbility{
	float range;
	public CooldownAccelerate(ManipulatableObject parent, float range){
		super(parent, parent.position.x, parent.position.y, parent.bounds.width, parent.bounds.height);
		this.range = range;
		
		lifeTimer = .1f;
		
		System.out.println("added");

	}
	
	public void postDeathEffects(){
		for(int i = 0; i < parent.teamObjects.size; i++){
			ManipulatableObject teammate = parent.teamObjects.get(i);

			if(teammate.position.dst(position) <= range){
				teammate.addPassive(new CooldownBoost(teammate));
			}
		}
	}
}
