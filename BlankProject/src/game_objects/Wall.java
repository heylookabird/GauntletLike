package game_objects;

import backend.Assets;

public class Wall extends AbstractGameObject{
	
	public Wall(float x, float y, float width, float height){
		super(x,y,width,height);
		this.setImage(Assets.instance.planes.whitePlaneImgs.first());
		bounds.set(x, y, width, height);
	}
	


}
