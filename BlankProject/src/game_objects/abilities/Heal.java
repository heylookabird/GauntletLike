package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import backend.Assets;

import com.badlogic.gdx.math.Vector2;

public class Heal extends AbstractAbility{

	boolean self = false;
	private boolean alsoDamagesEnemies;
	public Heal(ManipulatableObject parent, int heal, float xVelocity, float yVelocity, boolean alsoDamagesEnemies) {
		super(parent, parent.position.x, parent.position.y, 1, 1);
		this.damage = heal;
		removesItself = true;
		this.lifeTimer = 1f;
		this.alsoDamagesEnemies = alsoDamagesEnemies;
		this.setImage(Assets.instance.weapons.sword);
		this.velocity.set(xVelocity, yVelocity);
	}
	
	public Heal(ManipulatableObject parent, int heal, float size, boolean alsoDamagesEnemies){
		super(parent, parent.position.x, parent.position.y, size, size);
		this.damage = heal;
		removesItself = true;
		this.lifeTimer = 1f;
		this.alsoDamagesEnemies = alsoDamagesEnemies;
		this.setImage(Assets.instance.weapons.sword);
		this.velocity.set(0, 0);
		self = true;
		
		this.position.set(parent.position.x - size/2f, parent.position.y - size/2f);
	}
	
	@Override
	public void interact(AbstractGameObject couple){
		super.interact(couple);
		
		if(self){
			parent.heal(damage);
		}
		
		if(couple instanceof ManipulatableObject){
			ManipulatableObject obj = (ManipulatableObject) couple;
			if(this.isSameTeam(obj)){
				boolean newObj = isFirstInteraction(obj);
		
				if(newObj)
					obj.heal(damage);
			}else if(alsoDamagesEnemies){
				boolean newObj = isFirstInteraction(obj);
				
				if(newObj)
					obj.takeHitFor(damage, this);
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
