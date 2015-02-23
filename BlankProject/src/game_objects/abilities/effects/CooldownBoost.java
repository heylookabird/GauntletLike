package game_objects.abilities.effects;

import game_objects.ManipulatableObject;
import game_objects.weapons.AbstractWeapon;

public class CooldownBoost extends Effect{
	AbstractWeapon weapon;
	public CooldownBoost(ManipulatableObject parent) {
		super(parent);
		lifeTimer = 3f;
		weapon = parent.getWeapon();
		weapon.changeCoolDownSpeed(.2f);
	}
	
	@Override
	public void update(float deltaTime){
		super.update(deltaTime);
		
		System.out.println(weapon.getCooldowns());
	}
	
	@Override
	public void postDeathEffects(){
		weapon.setDefaultCooldown();
	}

}
