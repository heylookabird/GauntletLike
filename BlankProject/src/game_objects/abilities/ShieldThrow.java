package game_objects.abilities;

import backend.Calc;
import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;

public class ShieldThrow extends AbstractAbility {

	private ManipulatableObject target;
	private float time;
	public ShieldThrow(ManipulatableObject parent, float x, float y,
			float width, float height, float speed) {
		super(parent, x, y, width, height);
		time = 2f;
		lifeTimer = time;
		terminalVelocity.set(speed, speed);
		target = Calc.findClosestEnemy(this, parent.enemyTeamObjects);
		damage = 1;
		deletionTime = .03f;
	
	}
	@Override
	public void update(float deltaTime) {
		
		if(target == null){
			removeThyself();
			return;
		}
		float angle = Calc.atan2(getCenter(), target.getCenter());
		System.out.println(angle);
		angle = angle < 0 ? angle + 360 : angle;
		velocity.x = Calc.cos(angle) * terminalVelocity.x;
		velocity.y = Calc.sin(angle) * terminalVelocity.y;
		
		
		super.update(deltaTime);
	}
	@Override
	public void interact(AbstractGameObject couple) {

		if(couple instanceof ManipulatableObject){
			ManipulatableObject obj = (ManipulatableObject) couple;
			
			if(!this.isSameTeam(obj)){
				boolean newObj = isFirstInteraction(obj);
				
				target = Calc.findClosestEnemy(parent, parent.enemyTeamObjects, objectsAlreadyHit);
				lifeTimer = time;

				if(newObj)
					obj.takeHitFor(damage, this);
				
			}
		}
		super.interact(couple);
	}

}
