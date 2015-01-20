package bsmenus;

import game_objects.ManipulatableObject;
import Controllers.Xbox360;
import backend.Constants;
import backend.LevelStage;
import backend.World;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class AbstractBSMenu {
	public Array<StringInstructions> strings;
	ManipulatableObject player;
	StringInstructions currentWeapon;

	public class StringInstructions{
		float x,y;
		String message;
		
		public StringInstructions(String string, float x, float y){
			this.x = x;
			this.y = y;
			message = string;
		}
	}
	public AbstractBSMenu(){
		strings = new Array<StringInstructions>();
		
		
		currentWeapon = new StringInstructions("Current Weapon: ", Constants.bgViewportWidth/4, Constants.bgViewportHeight/4);
		strings.add(new StringInstructions("Press A to toggle weapons", Constants.bgViewportWidth/4, Constants.bgViewportHeight * .75f));
		strings.add(new StringInstructions("Press R to restart, [back] on Xbox remote", Constants.bgViewportWidth/4, Constants.bgViewportHeight/2));
		strings.add(currentWeapon);
		
	}
	
	public void render(SpriteBatch batch, BitmapFont font){
		for(StringInstructions string: strings){
			font.draw(batch, string.message, string.x, string.y);
		}
	}
	
	public void setPlayer(ManipulatableObject player){
		this.player = player;
	}
	

	public void updateString(){
		currentWeapon.message = "Current Weapon: " + player.getCurrentWeaponName();
	}
	
	public void handleMenuInput(int keycode){
		if(keycode == Keys.A){
			player.toggleWeapon();
		}else if(keycode == Keys.R){
			LevelStage.reset();
		}else if(keycode == Keys.P){
			World.world.togglePause();
		}
	}
	
	public void handleControllerMenuInput(int keycode){
		if(keycode == Xbox360.BUTTON_A){
			player.toggleWeapon();
		}else if(keycode == Xbox360.BUTTON_BACK){
			LevelStage.reset();
		}else if(keycode == Xbox360.BUTTON_START){
			World.world.togglePause();
		}
	}
}
