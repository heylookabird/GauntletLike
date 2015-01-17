package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.ManipulatableObject.DIRECTION;

import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;


public class AOE extends AbstractAbility{
	
	private float endTime;
	private Vector<Float> timers;
	
	public AOE(Animation animation, ManipulatableObject parent, int damage, DIRECTION direction) {
		super(parent, parent.position.x, parent.position.y, 1, 1);
		this.animation = animation;
		endTime = animation.animationDuration;
		this.damage = damage;
		
		stunTime = .3f;
		timers = new Vector<Float>();
		
		this.knockbackSpeed = 10;
	}
	

	public AOE(TextureRegion image, ManipulatableObject parent, int damage, DIRECTION direction){
		super(parent, parent.position.x, parent.position.y, 1, 1);
		this.setImage(image);					
		this.damage = damage;
		
		timers = new Vector<Float>();
		stunTime = .3f;
		this.knockbackSpeed = 10;
	}
	
	
	@Override
	public void update(float deltaTime){
		super.update(deltaTime);
		updateTimers(deltaTime);
		spread();
		checkCollisions();
		
		
		
	}

	private void updateTimers(float deltaTime) {
		for(int i = 0; i < timers.size(); i++){
			Float temp = timers.get(i);
			if (temp > 0) {
				temp -= deltaTime;
			}else{
				this.objectsAlreadyHit.removeIndex(i);
			}
		}
	}


	private void checkCollisions() {
		
	}

@Override
protected boolean isFirstInteraction(ManipulatableObject obj){
		
		for(ManipulatableObject object : objectsAlreadyHit){
			if(object == obj)
				return false;
		}
		
		objectsAlreadyHit.add(obj);
		timers.add(.3f);
		
		
		return true;
	}

	
	@Override
	public void interact(AbstractGameObject couple) {
		
		
		if(couple instanceof ManipulatableObject){
			ManipulatableObject temp = (ManipulatableObject) couple;
			
			if(!this.isSameTeam(temp)){
				boolean newObj = isFirstInteraction(temp);
			
				if(newObj){
					temp.takeHitFor(damage, this);
					
				}
			}
		}
	}

	private void spread() {
		if(animation != null)
			bounds.setSize(animation.getKeyFrame(stateTime).getRegionWidth(), animation.getKeyFrame(stateTime).getRegionHeight());
		else{
			Vector2 temp = getCenter();
			dimension.x += .05f;
			dimension.y += .05f;
			bounds.width = dimension.x;
			bounds.height = dimension.y;
			position.set(temp.x - dimension.x/2, temp.y - dimension.y/2);
		}
		
/*		bounds.width += .2; 
		bounds.height += .2;*/
		/*position.x = position.x + origin.x - bounds.width/2;
		position.y =position.y + origin.y - bounds.height/2;*/
		

		
	}
}
