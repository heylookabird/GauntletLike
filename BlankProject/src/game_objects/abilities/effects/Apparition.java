package game_objects.abilities.effects;

import game_objects.ManipulatableObject;
import game_objects.abilities.AbstractAbility;

public class Apparition extends Effect{

	public Apparition(ManipulatableObject parent) {
		super(parent);
		this.setImage(parent.currentDirImg);
		this.lifeTimer = .5f;
	}

}
