package game_objects.abilities;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class AOE extends AbstractAbility{
	
	private float endTime;
	public AOE(Animation animation, ManipulatableObject parent, int damage, float x, float y, float width, float height) {
		super(parent, x, y, width, height);
		this.animation = animation;
		endTime = animation.animationDuration;
		this.damage = damage;
	}
	
	public AOE(TextureRegion image, ManipulatableObject parent, int damage, float x, float y, float width, float height) {
		super(parent, x, y, width, height);
		this.setImage(image);					
		this.damage = damage;
		System.out.println(bounds);
	}	
	
	@Override
	public void update(float deltaTime){
		super.update(deltaTime);
		spread();
		
		checkCollisions();
	}

	private void checkCollisions() {
		
	}
	
	@Override
	public void interact(AbstractGameObject couple) {
		if(couple instanceof ManipulatableObject){
			ManipulatableObject temp = (ManipulatableObject) couple;
			
			if(isFirstInteraction(temp)){
				temp.takeHitFor(damage);
			}
		}
	}

	private void spread() {
		if(animation != null)
			bounds.setSize(animation.getKeyFrame(stateTime).getRegionWidth(), animation.getKeyFrame(stateTime).getRegionHeight());
		else{
			dimension.x += stateTime;
			dimension.y += stateTime;
			bounds.width = dimension.x;
			bounds.height = dimension.y;
		}
		
/*		bounds.width += .2; 
		bounds.height += .2;*/
		/*position.x = position.x + origin.x - bounds.width/2;
		position.y =position.y + origin.y - bounds.height/2;*/
		
		System.out.println(bounds);

		
	}
}
