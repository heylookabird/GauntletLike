package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;

public class Beam extends AbstractAbility{

	private DIRECTION direction;
	private float endTime, currentTime, length;
	private boolean alsoDamagesEnemies;
	public Beam(ManipulatableObject parent, DIRECTION direction, int dmg, boolean alsoDamagesEnemies, float width, float height, float length, float time) {
		super(parent, parent.position.x + parent.dimension.x / 2, parent.position.y + parent.dimension.y / 2, width, height);
		this.direction = direction;
		this.endTime = time;
		this.damage = dmg;
		this.alsoDamagesEnemies = alsoDamagesEnemies;
		this.deletionTime = 2f;
		currentTime = 0;
		this.length = length;
		initDebug();
		
	}
	@Override
	public void update(float deltaTime) {
		currentTime += deltaTime;
		float percent = currentTime / endTime;
		if(percent > 1)
			percent = 1;
		
		if(direction == DIRECTION.RIGHT){
			dimension.x = length * percent;
			bounds.width = length * percent;
		}else if(direction == DIRECTION.LEFT){
			dimension.x = length * percent;
			position.x = parent.getCenter().x - dimension.x;
			bounds.x = position.x;
			bounds.width = dimension.x;
		}else if(direction == DIRECTION.UP){
			dimension.y = length * percent;
			bounds.height = length * percent;
		}else if(direction == DIRECTION.DOWN){
			dimension.y = length * percent;
			position.y = parent.getCenter().y - dimension.y;
			bounds.y = position.y;
			bounds.height = dimension.y;
		}
		super.update(deltaTime);
	}
	@Override
	public void interact(AbstractGameObject couple){
		super.interact(couple);
		
		
		
		if(couple instanceof ManipulatableObject){
			ManipulatableObject obj = (ManipulatableObject) couple;
			if(this.isSameTeam(obj)){
				boolean newObj = isFirstInteraction(obj);
		
				if(newObj)
					obj.heal(damage);
			}else if(alsoDamagesEnemies){
				boolean newObj = isFirstInteraction(obj);
				
				if(newObj)
					obj.takeHitFor(damage, this);
			}
		}
	}

}
