package game_objects.abilities.effects;

import backend.Assets;
import game_objects.ManipulatableObject;

public class Cold extends Effect{
	float slowRate;
	float speed;
	public Cold(ManipulatableObject parent, float slow, float length) {
		super(parent);
		this.lifeTimer = length;
		speed = parent.walkingTerminalV.x * slow;
		this.setImage(Assets.instance.effects.iceExplosionImgs.first());


	}
	
	@Override
	public void update(float deltaTime){
		super.update(deltaTime);
		
		this.position.set(parent.position);
		parent.terminalVelocity.set(speed, speed);


	}
	
	@Override
	public void postDeathEffects(){
		parent.terminalVelocity.set(parent.walkingTerminalV);

	}

}
