package game_objects;

import backend.Assets;

public class Mage extends ManipulatableObject {

	
	public Mage(boolean controller) {
		super(controller);
		upImg = Assets.instance.mage.facingNorth;
		downImg = Assets.instance.mage.facingSouth;
		leftImg = Assets.instance.mage.facingWest;
		rightImg = Assets.instance.mage.facingEast;
		
		walkingDown = Assets.instance.mage.south;
		walkingUp = Assets.instance.mage.north;
		walkingRight = Assets.instance.mage.east;
		walkingLeft = Assets.instance.mage.west;
		
		terminalVelocity.set(4, 4);
		accelerationPerSecond.set(20, 20);
		
		this.currentDirImg = upImg;
		
	}

}
