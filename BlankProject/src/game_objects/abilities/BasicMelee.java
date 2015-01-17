package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.ManipulatableObject.STATE;

public class BasicMelee extends AbstractAbility {

	

	public BasicMelee(ManipulatableObject parent, int damage) {
		super(parent, parent.position.x, parent.position.y, 1, 1);
		this.damage = damage;
		removesItself = true;
		System.out.println(bounds);
	}
	
	public BasicMelee(ManipulatableObject parent, int damage, DIRECTION direction) {
		
		super(parent, parent.position.x, parent.position.y, 1, 1);
		this.damage = damage;
		removesItself = true;
		parent.stopMove();
		
		if(direction == DIRECTION.LEFT){
			position.set(parent.position.x - dimension.x, position.y);
			parent.moveLeft();
		}else if(direction == DIRECTION.RIGHT){
			position.set(parent.position.x + dimension.x, position.y);
			parent.moveRight();

		}else if(direction == DIRECTION.UP){
			position.set(parent.position.x, position.y + dimension.y);
			parent.moveUp();
			
		}else if(direction == DIRECTION.DOWN){
			position.set(parent.position.x, position.y - dimension.y);
			parent.moveDown();

		}
		parent.stopMove();
		parent.state = STATE.ATTACKING;
	}
	@Override
	public void update(float deltaTime) {
		position.x += parent.deltax;
		position.y += parent.deltay;
		super.update(deltaTime);
	}
	@Override
	protected void removeThyself() {
		parent.checkStopMove();
		parent.stopMove();
		super.removeThyself();
	}

	@Override
	public void interact(AbstractGameObject couple) {
		
		
		ManipulatableObject obj = (ManipulatableObject) couple;
		
		if(!isSameTeam(obj)){
			boolean newObj = isFirstInteraction(obj);
		
			if(newObj)
				obj.takeHitFor(damage, this);
		
		}
		
	}
	

}
