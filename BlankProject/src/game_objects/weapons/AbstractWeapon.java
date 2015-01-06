package game_objects.weapons;

import com.badlogic.gdx.math.Vector2;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;

public abstract class AbstractWeapon extends AbstractGameObject {

	ManipulatableObject parent;
	private Vector2 positionOffset;

	public AbstractWeapon(ManipulatableObject parent, float width, float height, Vector2 positionOffset) {
		super(parent.position.x, parent.position.y, width, height);
		this.parent = parent;
		this.positionOffset = positionOffset;
	
	}
	public void moveRight(){
		rotation = 0;
		positionOffset.set(parent.dimension.x /2 - .1f, parent.dimension.y / 3 - .2f);
		parent.primaryBehind = false;
	}
	public void moveLeft(){
		rotation = 180;
		positionOffset.set(parent.dimension.x / 2, parent.dimension.y / 2);
		parent.primaryBehind = true;


		
	}
	public void moveUp(){
		rotation = 90;
		positionOffset.set(parent.dimension.x, parent.dimension.y / 2);

	}
	public void moveDown(){
		rotation = 270;
		positionOffset.set(0, parent.dimension.y / 2);

		
	}
	@Override
	public void update(float deltaTime) {
		position.set(parent.position.x + positionOffset.x, parent.position.y + positionOffset.y);
		super.update(deltaTime);
	}
	
	
	

	

}
