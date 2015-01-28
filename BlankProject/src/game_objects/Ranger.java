package game_objects;

import game_objects.weapons.Bow;
import game_objects.weapons.DualBlades;
import game_objects.weapons.HealingStaff;
import game_objects.weapons.SwordAndShield;

import com.badlogic.gdx.math.Vector2;

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
		
		this.terminalVelocity.set(3.5f, 3.5f);
		walkingTerminalV.set(terminalVelocity);
		
		this.currentDirImg = upImg;
		DualBlades blades = new DualBlades(this, 1, .25f, new Vector2(dimension.x /2, dimension.y / 2));
		this.equipableWeapons.add(blades);
		this.equipWeapon(blades);
		Bow bow = new Bow(this, 1, .25f, new Vector2(dimension.x/2, dimension.y/2));
		bow.setPlayerBow();
		this.equipableWeapons.add(bow);
		this.setAnimation(walkingUp);		
	}

}
