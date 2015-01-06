package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;

public class BasicMelee extends AbstractAbility {

	

	public BasicMelee(ManipulatableObject parent, int damage) {
		super(parent, parent.position.x, parent.position.y, 1, 1);
		this.damage = damage;
		suicidal = true;
		System.out.println(bounds);
	}
	
	@Override
	public void interact(AbstractGameObject couple) {
		
		
		ManipulatableObject obj = (ManipulatableObject) couple;
		
		boolean newObj = isFirstInteraction(obj);
		
		if(newObj)
			obj.takeHitFor(damage);
		
	}
	

}
