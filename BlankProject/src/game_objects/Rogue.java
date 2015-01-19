package game_objects;

import game_objects.weapons.Bow;
import game_objects.weapons.DualBlades;
import game_objects.weapons.SwordAndShield;
import backend.Assets;

import com.badlogic.gdx.math.Vector2;

public class Rogue extends ManipulatableObject{
	
	public Rogue(boolean controller){
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
		walkingTerminalV.set(terminalVelocity);
		

		primaryWeapon = new DualBlades(this, 1, .25f, new Vector2(dimension.x /2, dimension.y / 2));
		this.equipableWeapons.add(primaryWeapon);
		Bow bow = new Bow(this, 1, .25f, new Vector2(dimension.x/2, dimension.y/2));
		bow.setPlayerBow();
		SwordAndShield joes = new SwordAndShield(this, 1, .25f, new Vector2(dimension.x/2, dimension.y/2));
		this.equipableWeapons.add(joes);
		this.equipableWeapons.add(bow);
		this.currentDirImg = upImg;
		this.setAnimation(walkingUp);
	}

}
