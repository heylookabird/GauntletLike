package ai_classes;

import game_objects.ManipulatableObject;

public class AbstractAi {
	public ManipulatableObject parent;
	private ManipulatableObject target;
	//public Attack attack1, attack2;
	public HEALTH_STATE healthstate;
	public float range = 10, thinkingTime = .5f, currTime = 0;
	public enum HEALTH_STATE{
		DANGER, LOW, MEDIUM, HIGH;
	}
	
	public AbstractAi(ManipulatableObject par){
		this.parent = par;
		healthstate = HEALTH_STATE.HIGH;
	}
	
	private void setHealthState(HEALTH_STATE state){
		this.healthstate = state;
	}
	
	public ManipulatableObject findClosestEnemy(){
		ManipulatableObject target = parent.enemyTeamObjects.first();
		float closestDistance = range;
		for(int i = 0; i < parent.enemyTeamObjects.size; i++){
			float originX = parent.position.x + parent.origin.x, originY = parent.position.y + parent.origin.y, enOriginX = parent.enemyTeamObjects.get(i).position.x + parent.enemyTeamObjects.get(i).origin.x, enOriginY = parent.enemyTeamObjects.get(i).position.y + parent.enemyTeamObjects.get(i).origin.y;
			float distance = (originX - enOriginX) * (originX - enOriginX) + (originY - enOriginY)* (originY - enOriginY);
			if(distance < closestDistance){
				target = parent.enemyTeamObjects.get(i);
				closestDistance = distance;
			}
			
		}
		
		return target;
	}
	
	public ManipulatableObject findStrongestEnemy(){
		ManipulatableObject target = parent.enemyTeamObjects.first();
		float strongestHealth = 0;
		for(ManipulatableObject enemy: parent.enemyTeamObjects){
			float originX = parent.position.x + parent.origin.x, originY = parent.position.y + parent.origin.y, enOriginX = enemy.position.x + enemy.origin.x, enOriginY = enemy.position.y + enemy.origin.y;
			float distance = (originX - enOriginX) * (originX - enOriginX) + (originY - enOriginY)* (originY - enOriginY);
			if(distance < range){
				if(enemy.hp > strongestHealth)
					target = enemy;
			}
			
		}
		
		return target;
	}
	
	public ManipulatableObject findStrongestAlly(){
		ManipulatableObject target = null;
		float strongestHealth = 0;
		for(ManipulatableObject enemy: parent.teamObjects){
			float originX = parent.position.x + parent.origin.x, originY = parent.position.y + parent.origin.y, enOriginX = enemy.position.x + enemy.origin.x, enOriginY = enemy.position.y + enemy.origin.y;
			float distance = (originX - enOriginX) * (originX - enOriginX) + (originY - enOriginY)* (originY - enOriginY);
			if(distance < range){
				if(enemy.hp > strongestHealth)
					target = enemy;
			}
			
		}
		
		return target;
	}
	
	public ManipulatableObject findWeakestEnemy(){
		ManipulatableObject target = parent.enemyTeamObjects.first();
		float lowestHealth = 10000000f;
		for(ManipulatableObject enemy: parent.enemyTeamObjects){
			float originX = parent.position.x + parent.origin.x, originY = parent.position.y + parent.origin.y, enOriginX = enemy.position.x + enemy.origin.x, enOriginY = enemy.position.y + enemy.origin.y;
			float distance = (originX - enOriginX) * (originX - enOriginX) + (originY - enOriginY)* (originY - enOriginY);
			if(distance < range){
				if(enemy.hp < lowestHealth)
					target = enemy;
			}
			
		}
		
		return target;
	}
	
	public ManipulatableObject findWeakestAlly(){
		ManipulatableObject target = null;
		float lowestHealth = 10000000f;
		for(ManipulatableObject enemy: parent.teamObjects){
			float originX = parent.position.x + parent.origin.x, originY = parent.position.y + parent.origin.y, enOriginX = enemy.position.x + enemy.origin.x, enOriginY = enemy.position.y + enemy.origin.y;
			float distance = (originX - enOriginX) * (originX - enOriginX) + (originY - enOriginY)* (originY - enOriginY);
			if(distance < range){
				if(enemy.hp < lowestHealth)
					target = enemy;
			}
			
		}
		
		return target;
	}
	
	public ManipulatableObject findClosestAlly(){
		ManipulatableObject target = null;
		float closestDistance = range;
		//same code as enemy
		for(ManipulatableObject enemy: parent.teamObjects){
			float originX = parent.position.x + parent.origin.x, originY = parent.position.y + parent.origin.y, enOriginX = enemy.position.x + enemy.origin.x, enOriginY = enemy.position.y + enemy.origin.y;
			float distance = (originX - enOriginX) * (originX - enOriginX) + (originY - enOriginY)* (originY - enOriginY);
			if(distance < closestDistance){
				enemy = target;
				closestDistance = distance;
			}
			
		}
		
		return target;
	}

	public void moveToTarget(ManipulatableObject target, float distance){
		if(parent.position.x + parent.bounds.width < target.position.x - distance){
			parent.moveRight();
		}else if(parent.position.x > target.position.x + target.bounds.width + distance){
			parent.moveLeft();
		}else
			parent.stopMoveX();
		
		if(parent.position.y + parent.bounds.height < target.position.y - distance){
			parent.moveUp();
		}else if(parent.position.y > target.position.y + target.bounds.height + distance)
			parent.moveDown();
		else
			parent.stopMoveY();
		
		System.out.println(parent.velocity);

	}
	public void update(float deltaTime){
		currTime += deltaTime;
		if(target == null){
			target = findClosestEnemy();
			System.out.println("called");

		}
		
		if(currTime > thinkingTime){
			makeNextDecision();
		}
		//updateHealthState();
	}
	
	

	private void makeNextDecision() {
		moveToTarget(target, 2);
		currTime = 0;
	}

	private void updateHealthState() {
		float ratio = parent.currentHp / parent.hp;
		
		if(ratio < .2f){
			setHealthState(HEALTH_STATE.DANGER);
		}
		else if(ratio < .5f)
			setHealthState(HEALTH_STATE.LOW);
		else if(ratio < .7f)
			setHealthState(HEALTH_STATE.MEDIUM);
		else
			setHealthState(HEALTH_STATE.HIGH);



	}
	
}
