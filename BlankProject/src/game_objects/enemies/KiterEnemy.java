package game_objects.enemies;

import game_objects.ManipulatableObject;
import game_objects.weapons.Bow;
import ai_classes.KiterAi;
import backend.Assets;
import backend.LevelStage;

import com.badlogic.gdx.math.Vector2;

public class KiterEnemy extends ManipulatableObject{
	
	public KiterEnemy(boolean controller, float x, float y, float width,
			float height) {
		super(controller, x, y, width, height);
		
		upImg = Assets.instance.mage.facingNorth;
		downImg = Assets.instance.mage.facingSouth;
		leftImg = Assets.instance.mage.facingWest;
		rightImg = Assets.instance.mage.facingEast;
		
		walkingDown = Assets.instance.mage.south;
		walkingUp = Assets.instance.mage.north;
		walkingRight = Assets.instance.mage.east;
		walkingLeft = Assets.instance.mage.west;
		
		terminalVelocity.set(3f, 3f);
		

		primaryWeapon = new Bow(this, 1, .25f, new Vector2(dimension.x / 2, dimension.y / 2));
		
		setTeam(LevelStage.enemyControlledObjects, LevelStage.playerControlledObjects);
		activateAI(new KiterAi(this));
		hp = 5;
		
		this.currentDirImg = upImg;
		this.setAnimation(walkingUp);
	}
}
