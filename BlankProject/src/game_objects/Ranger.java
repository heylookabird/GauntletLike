package game_objects;

import backend.Assets;

public class Ranger extends ManipulatableObject {

	public Ranger(boolean controller) {
		super(controller);
		upImg = Assets.instance.planes.bluePlaneImgs.first();
		downImg =  Assets.instance.planes.bluePlaneImgs.random();
		leftImg =  Assets.instance.planes.bluePlaneImgs.random();
		rightImg =  Assets.instance.planes.bluePlaneImgs.random();
		
		walkingDown =  Assets.instance.planes.bluePlane;
		walkingUp = Assets.instance.planes.greenPlane;
		walkingRight = Assets.instance.planes.whitePlane;
		walkingLeft = Assets.instance.planes.yellowPlane;
		
		this.currentImg = upImg;
		
	}

	public Ranger(boolean controller, float x, float y, float width, float height){
		super(controller, x, y, width, height);
		upImg = Assets.instance.planes.bluePlaneImgs.first();
		downImg =  Assets.instance.planes.bluePlaneImgs.random();
		leftImg =  Assets.instance.planes.bluePlaneImgs.random();
		rightImg =  Assets.instance.planes.bluePlaneImgs.random();
		
		walkingDown =  Assets.instance.planes.bluePlane;
		walkingUp = Assets.instance.planes.greenPlane;
		walkingRight = Assets.instance.planes.whitePlane;
		walkingLeft = Assets.instance.planes.yellowPlane;
		
		this.currentImg = upImg;
	}
}
