package game_objects.abilities;

import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;
import game_objects.ManipulatableObject.STATE;

public class Slash extends AbstractAbility{
	private boolean doublehit;
	private float slashspeed = .2f;
	private DIRECTION facing;
	
	public Slash(ManipulatableObject parent, float x, float y, float width, float height,int damage, DIRECTION direction, boolean doublehit) {
		super(parent,x,y,width,height);
		this.doublehit = doublehit;
		this.damage = damage;
		this.removesItself = true;
		facing = direction;
		
		this.lifeTimer = .4f;
				
		if(direction == DIRECTION.LEFT){
			position.set(parent.position.x, position.y);
			//parent.moveLeft();
		}else if(direction == DIRECTION.RIGHT){
			position.set(parent.position.x + dimension.x, position.y);
			//parent.moveRight();

		}else if(direction == DIRECTION.UP){
			position.set(parent.position.x, position.y + dimension.y);
			//parent.moveUp();
			
		}else if(direction == DIRECTION.DOWN){
			position.set(parent.position.x, position.y - dimension.y);
			//parent.moveDown();

		}
		parent.stopMove();
		parent.state = STATE.ATTACKING;
	}
	
	public Slash(ManipulatableObject parent, DIRECTION facing, int damage, boolean doublehit){
		super(parent, 1, 1, 1, 1);//position will be set a few lines lower anyways
		
		this.facing = facing;
		
		this.damage = damage;
		this.doublehit = doublehit;
		
		switch(facing){
		
		//position to left so cross is in middle
		case UP:
			position.x = parent.position.x - bounds.width;
			position.y = parent.position.y;
			break;
		
		//position below to bring to left
		case LEFT:
			position.set(parent.position.x, parent.position.y - bounds.height);
			
			
		}
	}
	
	@Override
	public void update(float deltaTime){
		if(doublehit){
			this.bounds.x += slashspeed;
			this.bounds.y -= slashspeed;
		}else{
			this.bounds.x += slashspeed;
			this.bounds.y += slashspeed;
		}
	}
	
	@Override
	public void postDeathEffects() {
		if(doublehit){
			this.objectsAlreadyHit.clear();
			
			
		}
	}
	
	
}
