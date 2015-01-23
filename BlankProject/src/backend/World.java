package backend;

import game_objects.ManipulatableObject;
import Controllers.ControllerHandler;
import bsmenus.AbstractBSMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.walljumper.gui.SceneObject;

public class World {

	public static World world = new World();
	public CameraHelper cameraHelper;
	private int numPlayers;
	public int playerpaused = 0;
	private LevelStage levelStage;
	private boolean paused = false;
	public AbstractBSMenu menu;

	private World() {

	}

	public void show() {
		testInit();
	}

	public void testInit() {

		Assets.instance.characterInit(new AssetManager());

		levelStage = new LevelStage();
		LevelStage.setHeader("images/blank");
		LevelStage.activateWaveManager();

		WorldRenderer.renderer.init();

		InputManager.inputManager.init();
		cameraHelper = new CameraHelper();
		cameraHelper.setZoom(Constants.defaultZoom, true);

		ControllerHandler.init();

		LevelStage.loadTest(false);

		menu = new AbstractBSMenu();

	}

	public void render(float delta) {

		update(delta);
		Gdx.gl.glClearColor(0, 0, 0, 0);// Default background color
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		WorldRenderer.renderer.render(paused);

	}

	private void update(float delta) {

		if (!paused) {
			LevelStage.update(delta);
			cameraHelper.update(delta);
			cameraHelper.applyTo(WorldRenderer.renderer.camera);
		}else{
			menu.updateString();
		}
		ControllerHandler.controllerManager.update();

	}

	public void hide() {

	}

	public boolean handleTouchInput(int screenX, int screenY, int pointer,
			int button) {

		for (SceneObject objects : WorldRenderer.renderer.getSceneObjects()) {

			if (objects.touchDown(screenX, screenY, pointer, 0)) {

				return false;
			}
		}

		return false;
	}

	public void handleDrag(int screenX, int screenY, int pointer) {
		// Sends all touch down coordinates to the children
		for (SceneObject objects : WorldRenderer.renderer.getSceneObjects()) {
			if (objects.touchDown(screenX, screenY, pointer, 0)) {

				return;
			}
		}

		return;
	}

	public boolean handleTouchUp(int screenX, int screenY, int pointer,
			int button) {

		for (SceneObject objects : WorldRenderer.renderer.getSceneObjects()) {
			objects.touchUp(screenX, screenY, pointer, button);

		}

		return false;
	}

	public boolean handleKeyInput(int keycode) {
		// Passes input to the player objects
		if (!paused) {
			for (ManipulatableObject obj : LevelStage.playerControlledObjects) {
				obj.actOnInputKeyDown(keycode);
			}
		}else{
			menu.handleMenuInput(keycode);
		}

		if (keycode == Keys.X) {
			LevelStage.waveManager.destroyEnemy();
		} 

		return false;
	}
	
	public boolean handleKeyInputUp(int keycode){
		System.out.println("got here");
		if (!paused) {
			for (ManipulatableObject obj : LevelStage.playerControlledObjects) {
				obj.actOnInputKeyUp(keycode);
			}
		}else{
			menu.handleMenuInputUp(keycode);
		}
		
		return false;
	}

	public void togglePause() {
		if (paused)
			paused = false;
		else{
			paused = true;
		}
	}

	public void render(SpriteBatch batch) {
		LevelStage.render(batch);

	}

	public void controllerButtonDown(int controllerNumber, int buttonIndex) {
		if(!paused){
			InputManager.inputManager.controllableObjects.get(controllerNumber)
					.buttonDown(buttonIndex);
		}else
			menu.handleControllerMenuInput(buttonIndex);

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
