package game_objects.abilities;

import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;

import backend.LevelStage;

import com.badlogic.gdx.utils.Array;

public class ComboStrike  {
	//CD2 is you waited too long to continue the combo
	//CD1 is how long you have to wait to do the next one.
	Array<Float> attackCds1, attackCds2;
	float cdTimer;
	ManipulatableObject parent;
	Array<AbstractAbility> attacksInCombo;
	int cdIndex;
	
	boolean coolingDown;
	float comboCd = 5; //So you can't start another combostrike for certain amount of time
	public ComboStrike() {
		attackCds1 = new Array<Float>();
		init();
	
	}

	private void init() {
		cdTimer = 0;
		cdIndex = 0;
	}

	public ComboStrike(ManipulatableObject parent, Array<AbstractAbility> attacksInCombo, Array<Float> attackCds1, Array<Float> attackCds2) {
		this.attackCds1 = attackCds1;
		this.attackCds2 = attackCds2;
		this.attacksInCombo = attacksInCombo;
		this.parent = parent;
		init();
		

	
	}
	
	public void update(float deltaTime) {
		if(cdIndex > 0 || coolingDown)
			cdTimer += deltaTime;

		//Does the cool down between combos
		if(coolingDown){
			if(cdTimer > comboCd){
				coolingDown = false;
				cdTimer = 0;
				System.out.println("reade");
			}
			return;
		}
		
		if((cdTimer > attackCds2.get(cdIndex).floatValue())){
			
			startCoolDown();
			
		}
		
	}

	private void startCoolDown() {
		cdIndex = 0;
		cdTimer = 0;
		coolingDown = true;
		
		

	}

	public void trigger(DIRECTION direction) {

		if(coolingDown || (cdTimer < attackCds1.get(cdIndex).floatValue())){
			System.out.println(coolingDown + "  cdtimer: " + cdTimer  + " attackCds1: " + attackCds1.get(cdIndex).floatValue());
			return;
		}
		AbstractAbility temp = attacksInCombo.get(cdIndex);
		temp.reposition(direction);
		temp.lifeTimer = temp.constantLifeTimer;

		LevelStage.interactables.add(temp);
		
		
		//Increment cdIndex
		cdIndex++;
		System.out.println(cdIndex);
		cdTimer = 0;
		if(cdIndex >= attackCds1.size){
			startCoolDown();
			
		}
		
		
		
	}

}
