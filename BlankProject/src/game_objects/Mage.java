package game_objects;

import game_objects.weapons.Bow;
import game_objects.weapons.SwordAndShield;
import backend.Assets;

import com.badlogic.gdx.math.Vector2;

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
		

		primaryWeapon = new SwordAndShield(this, 1, .25f, new Vector2(dimension.x /2, dimension.y / 2));
		this.currentDirImg = upImg;
		this.setAnimation(walkingUp);
	}


}
