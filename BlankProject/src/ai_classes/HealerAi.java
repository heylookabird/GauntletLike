package ai_classes;

import com.badlogic.gdx.math.Vector2;

import game_objects.ManipulatableObject;

public class HealerAi extends KiterAi{

	public HealerAi(ManipulatableObject par) {
		super(par);
		this.range = 100;
	}
	
	
	@Override
	public void update(float deltaTime){
		currTime += deltaTime;
		if(target == null){
			target = findWeakestAlly();
			System.out.println("oabstract ai");
		}
		
		if(currTime > thinkingTime){
			makeNextDecision();

		}
		//updateHealthState();
	}

}
