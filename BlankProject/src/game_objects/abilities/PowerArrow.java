package game_objects.abilities;

import game_map_classes.CornerConcave;
import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.Wall;

public class PowerArrow extends Arrow{

	public PowerArrow(ManipulatableObject parent, int damage, float x, float y,
			float width, float height, float speed, float angle) {
		super(parent, damage, x, y, width, height, speed, angle);
		init();
	}
	public PowerArrow(ManipulatableObject parent, int damage, float xVelocity,
			float yVelocity) {
		super(parent,damage,xVelocity, yVelocity);
		init();
	}
	
	public PowerArrow(ManipulatableObject parent, int damage, float x, float y, float width, float height, float speed, float angle, float lifeTimer){
		super(parent, damage, x, y, width, height, speed, angle, lifeTimer);
		init();
	}
	
	private void init(){
		this.priority = 10;
		this.knockbackSpeed = 15;
		if(this.velocity.x > velocity.y){
			this.bounds.width = bounds.width * 2;
		}else
			this.bounds.height = bounds.height * 2;
	}
	
	@Override
	public void interact(AbstractGameObject couple){
		if (couple instanceof CornerConcave || couple instanceof Wall) {
			lifeTimer = -1;
			cancelled = true;
		}

		if (couple instanceof AbstractAbility) {
			AbstractAbility ability = (AbstractAbility) couple;
			if (!ability.isSameTeam(parent)) {
				if (ability.priority > this.priority) {
					lifeTimer = 0;
					cancelled = true;
				} else if (ability.priority < priority) {
					ability.lifeTimer = 0;
					ability.cancelled = true;
				} else {
					lifeTimer = 0;
					cancelled = true;
				}
			}
		}
		
		if (couple instanceof ManipulatableObject) {
			ManipulatableObject obj = (ManipulatableObject) couple;
			if (!this.isSameTeam(obj)) {
				if(isFirstInteraction(obj))
					obj.takeHitFor(damage, this);
				//lifeTimer = -1;
			}
		}
	}
	

}
