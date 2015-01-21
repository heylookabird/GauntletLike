package game_objects.abilities.effects;

import game_objects.ManipulatableObject;
import game_objects.abilities.AbstractAbility;

public class Effect extends AbstractAbility{
	
	public Effect(ManipulatableObject parent){
		super(parent, parent.position.x, parent.position.y, parent.bounds.width, parent.bounds.height);
		parent.addPassive(this);
	}
	
	@Override
	public void removeThyself(){
		parent.removePassive(this);
	}
}
