package game_objects;

import game_objects.ManipulatableObject.DIRECTION;
import game_objects.abilities.AbstractAbility;
import game_objects.abilities.BasicMelee;
import game_objects.weapons.Bow;
import game_objects.weapons.CloakAndDagger;
import game_objects.weapons.DualBlades;
import game_objects.weapons.HealingStaff;
import game_objects.weapons.SwordAndShield;
import backend.Assets;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

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
		
		

		DualBlades blades = new DualBlades(this, 1, .25f, new Vector2(dimension.x /2, dimension.y / 2));
		this.equipableWeapons.add(blades);
		this.equipWeapon(blades);
		Bow bow = new Bow(this, 1, .25f, new Vector2(dimension.x/2, dimension.y/2));
		bow.setPlayerBow();
		SwordAndShield joes = new SwordAndShield(this, 1, .25f, new Vector2(dimension.x/2, dimension.y/2));
		this.equipableWeapons.add(joes);
		this.equipableWeapons.add(bow);
		HealingStaff staff = new HealingStaff(this, 1, .25f, new Vector2(dimension.x/2, dimension.y/2));
		equipableWeapons.add(staff);
		
		//CD 1 for 
		Array<Float> cd1 = new Array<Float>();
		cd1.add(-1f);
		cd1.add(.3f);
		cd1.add(.3f);
		
		Array<Float> cd2 = new Array<Float>();
		cd2.add(.9f);
		cd2.add(.9f);
		cd2.add(.9f);
		
		Array<AbstractAbility> attacksInCombo = new Array<AbstractAbility>();
		attacksInCombo.add(new BasicMelee(this, 2, .3f, DIRECTION.UP));
		attacksInCombo.add(new BasicMelee(this, 2, .3f, DIRECTION.UP));
		attacksInCombo.add(new BasicMelee(this, 2, .3f, DIRECTION.UP));

		
		CloakAndDagger dagger = new CloakAndDagger(this, 1, .25f,  new Vector2(dimension.x/2, dimension.y/2), attacksInCombo, cd1, cd2);
		equipableWeapons.add(dagger);
		
		this.currentDirImg = upImg;
		this.setAnimation(walkingUp);
	}

}
