package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import backend.Assets;

import com.badlogic.gdx.math.Vector2;

public class Heal extends AbstractAbility{

	boolean self = false;
	public Heal(ManipulatableObject parent, int heal, float xVelocity, float yVelocity) {
		super(parent, parent.position.x, parent.position.y, 1, 1);
		this.damage = heal;
		removesItself = true;
		this.lifeTimer = 1f;
		this.setImage(Assets.instance.weapons.sword);
		this.velocity.set(xVelocity, yVelocity);
	}
	
	public Heal(ManipulatableObject parent, int heal){
		super(parent, parent.position.x, parent.position.y, 1, 1);
		this.damage = heal;
		removesItself = true;
		this.lifeTimer = 1f;
		this.setImage(Assets.instance.weapons.sword);
		this.velocity.set(0, 0);
		self = true;
	}
	
	@Override
	public void interact(AbstractGameObject couple){
		super.interact(couple);
		
		if(self){
			parent.heal(damage);
		}
		
		if(couple instanceof ManipulatableObject){
			ManipulatableObject obj = (ManipulatableObject) couple;
			if(this.isSameTeam(obj) && obj != parent){
				boolean newObj = isFirstInteraction(obj);
		
				if(newObj)
					obj.heal(damage);
			}
		}
		
		if(couple instanceof Arrow){
			if(!((AbstractAbility) couple).isSameTeam(parent)){
				lifeTimer = 0;
				((Arrow) couple).lifeTimer = 0;
			}
		}
	}
}
