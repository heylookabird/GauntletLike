package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;

public class BasicMelee extends AbstractAbility {

	

	public BasicMelee(ManipulatableObject parent, int damage) {
		super(parent, parent.position.x, parent.position.y, 1, 1);
		this.damage = damage;
		suicidal = true;
		System.out.println(bounds);
	}
	
	public BasicMelee(ManipulatableObject parent, int damage, DIRECTION direction) {
		
		super(parent, parent.position.x, parent.position.y, 1, 1);
		this.damage = damage;
		suicidal = true;
		
		if(direction == DIRECTION.LEFT){
			position.set(parent.position.x - dimension.x, position.y);
		}else if(direction == DIRECTION.RIGHT){
			position.set(parent.position.x + dimension.x, position.y);

		}else if(direction == DIRECTION.UP){
			position.set(parent.position.x, position.y + dimension.y);
			
		}else if(direction == DIRECTION.DOWN){
			position.set(parent.position.x, position.y - dimension.y);

		}
	}
	@Override
	public void update(float deltaTime) {
		position.x += parent.deltax;
		position.y += parent.deltay;
		super.update(deltaTime);
	}
	

	@Override
	public void interact(AbstractGameObject couple) {
		
		
		ManipulatableObject obj = (ManipulatableObject) couple;
		
		boolean newObj = isFirstInteraction(obj);
		
		if(newObj)
			obj.takeHitFor(damage);
		
	}
	

}
