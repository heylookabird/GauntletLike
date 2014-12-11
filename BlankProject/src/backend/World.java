package backend;

import game_objects.ManipulatableObject;
import game_objects.Ranger;
import Controllers.ControllerHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.walljumper.gui.SceneObject;

public class World {

	public static World world  = new World();
	public CameraHelper cameraHelper;
	private int numPlayers;
	private World(){
		
	}
	public void show() {
		init();
	}
	public void init(){
		//
		Assets.instance.init(new AssetManager());
		
		WorldRenderer.renderer.init();
		//AudioManager.init();
		InputManager.inputManager.init();
		cameraHelper = new CameraHelper();
		cameraHelper.setZoom(Constants.defaultZoom, true);
		
		ControllerHandler.init();
		Ranger player = new Ranger(true);
		player.setButtons(19, 21, 20, 22, 62);
		Ranger player2 = new Ranger(true);

		LevelStage.addPlayer(player);
		LevelStage.addPlayer(player2);

		
		
		
		//WorldRenderer.renderer.displayToWorld(0, "Welcome to Something Bitch!", new Vector2(800, 700));	
	
	}
	
	public void render(float delta) {
		
		update(delta);
		Gdx.gl.glClearColor(0, 0, 189, 0);// Default background color
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		WorldRenderer.renderer.render();
		
		
	}
	private void update(float delta) {
		
		LevelStage.update(delta);
		cameraHelper.update(delta);
		ControllerHandler.controllerManager.update();
		cameraHelper.applyTo(WorldRenderer.renderer.camera);
	}
	public void hide() {

	}
	public boolean handleTouchInput(int screenX, int screenY, int pointer,
			int button) {
		
		for(SceneObject objects : WorldRenderer.renderer.getSceneObjects()){

			if(objects.touchDown(screenX, screenY, pointer, 0)){

				return false;
			}
		}

		return false;
	}
	public void handleDrag(int screenX, int screenY, int pointer){
		//Sends all touch down coordinates to the children
		for(SceneObject objects : WorldRenderer.renderer.getSceneObjects()){
			if(objects.touchDown(screenX, screenY, pointer, 0)){

				return;
			}
		}
				
		return;
	}
	
	public boolean handleTouchUp(int screenX, int screenY, int pointer,
			int button){
		
		for(SceneObject objects : WorldRenderer.renderer.getSceneObjects()){
			objects.touchUp(screenX, screenY, pointer, button);

		}
		
		return false;
	}
	public boolean handleKeyInput(int keycode) {
		//Passes input to the player objects
		for(ManipulatableObject obj : LevelStage.playerControlledObjects){
			obj.actOnInputKeyDown(keycode);
		}
		
		return false;
	}
	public void render(SpriteBatch batch) {
		LevelStage.render(batch);
		
	}
	public void controllerButtonDown(int controllerNumber, int buttonIndex) {

		InputManager.inputManager.controllableObjects.get(controllerNumber)
		.buttonDown(buttonIndex);
		
	}
	public void controllerButtonUp(int controllerNumber, int buttonIndex) {
		InputManager.inputManager.controllableObjects.get(controllerNumber)
		.buttonUp(buttonIndex);
				
	}
	public void joyStick(int controllerNumber, Vector2 leftJoyStick,
			Vector2 rightJoyStick) {

		InputManager.inputManager.controllableObjects.get(controllerNumber)
				.joyStick(leftJoyStick, rightJoyStick);

	}
}
