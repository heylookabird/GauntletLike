package game_objects.abilities;

import backend.Assets;
import backend.LevelStage;
import game_objects.AbstractGameObject;
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
		
		this.setImage(Assets.instance.weapons.sword);
		
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
		super(parent, 10, 10, 1, 1);//position will be set a few lines lower anyways
		
		this.facing = facing;
		lifeTimer = 1f;
		this.damage = damage;
		this.doublehit = doublehit;
		
		this.setImage(Assets.instance.weapons.sword);
		
		
		switch(facing){
		
		//position to left so cross is in middle
		case UP:
			position.x = parent.position.x - bounds.width;
			position.y = parent.position.y;
			break;
		
		//position below to bring to left
		case LEFT:
			position.set(parent.position.x, parent.position.y - bounds.height);
			break;
			
		//position above to bring to right	
		case RIGHT:
			position.set(parent.position.x, parent.position.y + bounds.height);
			break;
			
		case DOWN:
			position.set(parent.position.x + bounds.width, parent.position.y);
			break;
		}
			
			
	}
	
	@Override
	public void update(float deltaTime){
		
		switch(facing){
		
		//started from left so slash diag up
		case UP:
			if(doublehit)
				velocity.set(slashspeed, slashspeed);
			else //started from right
				velocity.set(-slashspeed, slashspeed);
			
			break;
		//started from above
		case RIGHT:
			if(doublehit)
				velocity.set(slashspeed, -slashspeed);
			else//started from below
				velocity.set(slashspeed, slashspeed);
			break;
			
		//started from below
		case LEFT:
			if(doublehit)
				velocity.set(-slashspeed, slashspeed);
			else
				velocity.set(-slashspeed, -slashspeed);
				
			break;
		//started from right	
		case DOWN:
			if(doublehit)
				velocity.set(-slashspeed, -slashspeed);
			else
				velocity.set(slashspeed, -slashspeed);
			break;
		}
		
		super.update(deltaTime);
		
		System.out.println(position);
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
	
	@Override
	public void postDeathEffects() {
		//clears, repositions, and resets lifetimer
		if(doublehit){
			this.objectsAlreadyHit.clear();
			lifeTimer = 1f;
			doublehit = false;
			
			switch(facing){
			
			//reposition to right
			case UP:
				position.set(parent.position.x + parent.bounds.width, parent.position.y);
				break;
				
			//reposition to bottom	
			case RIGHT:
				position.set(parent.position.x, parent.position.y - bounds.height);
				break;
				
			case LEFT:
				position.set(parent.position.x, parent.position.y + parent.bounds.height);
				break;
				
			case DOWN:
				position.set(parent.position.x - bounds.width, parent.position.y);
				break;
			}
			
			LevelStage.interactables.add(this);
			
		}
	}
	
	
}
