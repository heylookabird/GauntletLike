package ai_classes;

import game_objects.ManipulatableObject;
import game_objects.abilities.AbstractAbility;

import com.badlogic.gdx.math.Vector2;

public class AbstractAi {
	public ManipulatableObject parent;
	public ManipulatableObject target;
	//public Attack attack1, attack2;
	public HEALTH_STATE healthstate;
	protected AbstractAbility nextAbility;
	public float range = 10, thinkingTime = .5f, currTime = 0;
	private Vector2 targetSpot;
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
		float closestDistance = 100;
		for(int i = 0; i < parent.enemyTeamObjects.size; i++){
			ManipulatableObject obj = parent.enemyTeamObjects.get(i);
			
			float distance = parent.getCenter().dst2(obj.getCenter());
			
			
			if(distance < closestDistance){
				target = obj;
				closestDistance = distance;
			}
			
		}
		
		return target;
	}
	
	public ManipulatableObject findStrongestEnemy(){
		ManipulatableObject target = parent.enemyTeamObjects.first();
		float strongestHealth = 0;
		for(int i = 0; i < parent.enemyTeamObjects.size; i++){
			ManipulatableObject enemy = parent.enemyTeamObjects.get(i);
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
		for(int i = 0; i < parent.teamObjects.size; i++){
			ManipulatableObject enemy = parent.teamObjects.get(i);
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
		for(int i = 0; i < parent.enemyTeamObjects.size; i++){
			ManipulatableObject enemy = parent.enemyTeamObjects.get(i);
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
		for(int i = 0; i < parent.teamObjects.size; i++){
			ManipulatableObject enemy = parent.teamObjects.get(i);
			float originX = parent.position.x + parent.origin.x, originY = parent.position.y + parent.origin.y, enOriginX = enemy.position.x + enemy.origin.x, enOriginY = enemy.position.y + enemy.origin.y;
			float distance = (originX - enOriginX) * (originX - enOriginX) + (originY - enOriginY)* (originY - enOriginY);
			if(distance < range){
				if(enemy.hp < lowestHealth)
					target = enemy;
					lowestHealth = enemy.hp;
			}
			
		}
		
		return target;
	}
	
	public ManipulatableObject findClosestAlly(){
		ManipulatableObject target = null;
		float closestDistance = range;
		//same code as enemy
		for(int i = 0; i < parent.teamObjects.size; i++){
			ManipulatableObject enemy = parent.teamObjects.get(i);
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

	}
	

	public void align(ManipulatableObject target, boolean x, float distance){
		if(x){
			if(parent.position.x + parent.bounds.width / 2 < target.position.x + target.dimension.x / 2 - distance){
				parent.moveRight();
			}else if(parent.position.x + parent.dimension.x / 2 > target.position.x + target.bounds.width / 2 + distance){
				parent.moveLeft();
			}else
				parent.stopMoveX();
		
		}
		else{
			if(parent.position.y + parent.bounds.height / 2 < target.position.y + target.dimension.y / 2 - distance){
				parent.moveUp();
			}else if(parent.position.y + parent.dimension.y / 2 > target.position.y + target.bounds.height / 2 + distance)
				parent.moveDown();
			else{
				parent.stopMoveY();
			}
		}
	}
	
	public void update(float deltaTime){
		currTime += deltaTime;
		if(targetSpot != null){
			moveTowardsPosition();
			return;
		}else if(target == null){
			target = findClosestEnemy();
		}
		
		if(currTime > thinkingTime){
			makeNextDecision();
			currTime = 0;
			

		}
		//updateHealthState();
	}
	
	

	private void moveTowardsPosition() {
		if(parent.position.x < targetSpot.x - 1){
			parent.moveRight();
		}else if(parent.position.x + parent.dimension.x > targetSpot.x + 1){
			parent.moveLeft();
		}else{
			parent.stopMoveX();
		}
		
		if(parent.position.y < targetSpot.y - 1){
			parent.moveUp();
		}else if(parent.position.y + parent.dimension.y > targetSpot.y + 1){
			parent.moveDown();
		}else{
			parent.stopMoveY();
		}
		
		
	}

	protected void makeNextDecision() {
		target = findClosestEnemy();
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

	public void taunt(ManipulatableObject target) {
		this.target = target;
		
	}

	public void targetPosition(Vector2 targetSpot) {
		this.targetSpot = targetSpot;
	}
	
}
