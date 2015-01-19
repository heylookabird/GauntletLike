package game_objects.enemies;

import game_objects.ManipulatableObject;
import game_objects.weapons.SwordAndShield;
import ai_classes.RusherAi;
import backend.Assets;
import backend.LevelStage;

import com.badlogic.gdx.math.Vector2;

public class MeleeEnemy extends ManipulatableObject {


	public MeleeEnemy(boolean controller, float x, float y, float width,
			float height) {
		super(controller, x, y, width, height);
		
		upImg = Assets.instance.bat.walkingNorthAni.random();
		downImg = Assets.instance.bat.walkingSouthAni.random();
		leftImg = Assets.instance.bat.walkingWestAni.random();
		rightImg = Assets.instance.bat.walkingEastAni.random();
		
		walkingDown = Assets.instance.bat.south;
		walkingUp = Assets.instance.bat.north;
		walkingRight = Assets.instance.bat.east;
		walkingLeft = Assets.instance.bat.west;
		
		terminalVelocity.set(3f, 3f);
		walkingTerminalV.set(terminalVelocity);

		primaryWeapon = new SwordAndShield(this, 1, .25f, new Vector2(dimension.x / 2, dimension.y / 2));
		this.equipableWeapons.add(primaryWeapon);

		
		setTeam(LevelStage.enemyControlledObjects, LevelStage.playerControlledObjects);
		activateAI(new RusherAi(this));
		hp = 5;
		
		this.currentDirImg = upImg;
		this.setAnimation(walkingUp);
	}
	

}
