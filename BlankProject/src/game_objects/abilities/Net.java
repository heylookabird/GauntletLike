package game_objects.abilities;

import game_objects.ManipulatableObject;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Net extends AOE {

	public Net(Animation animation, ManipulatableObject parent, int damage) {
		super(animation, parent, damage);
		init();
	}

	public Net(Animation animation, ManipulatableObject parent, int damage,
			float knockbackTime, float deletionTime, float x, float y,
			float width, float height, boolean hitAll) {
		super(animation, parent, damage, knockbackTime, deletionTime, x, y, width,
				height, hitAll);
		init();
	}
	
	public Net(TextureRegion image, ManipulatableObject parent, int damage) {
		super(image, parent, damage);
		init();
	}
	
	private void init(){
		this.knockbackSpeed = 0;
		this.knockbackTime = 10;
		this.damage = 0;
	}

	

}
