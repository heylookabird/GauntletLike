package backend;

import game.GameMain;
import game_objects.ManipulatableObject;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class InputManager extends InputAdapter {
	
	
	public static InputManager inputManager = new InputManager();
	public ArrayList<ManipulatableObject> controllableObjects;
	
	
	private InputManager(){
		
		
	}
	public void init(){
		//Will send input to any objects that take it
		Gdx.input.setInputProcessor(this); //Only this object receives player input
		controllableObjects = new ArrayList<ManipulatableObject>();
	}
	
	public void addObject(ManipulatableObject object){
		controllableObjects.add(object);
	}
	@Override
	public boolean keyDown(int keycode) {

		if(keycode == Keys.ESCAPE){
			Gdx.app.exit();
			return false;
		}
		
		
		GameMain.currentScreen.handleKeyInput(keycode);
		
		return false;
	}

	
	@Override
	public boolean keyUp(int keycode) {
		
		
		
		return false;
	}

	
	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		
		
		//Pass input to currentScreen
		GameMain.currentScreen.handleTouchInputDown(screenX, screenY, pointer, button);
		return false;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		
		//Pass input to currentScreen
		GameMain.currentScreen.handleTouchInputUp(screenX, screenY, pointer, button);
		
		return false;
	}


	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		GameMain.currentScreen.handleDrag(screenX, screenY, pointer);
		
		/*//Sends all the on release touch coordinates to children
		for(SceneObject objects : WorldRenderer.renderer.getSceneObjects()){
			objects.touchDragged(screenX, screenY, pointer);
		}*/
		return false;
	}

	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	
}
