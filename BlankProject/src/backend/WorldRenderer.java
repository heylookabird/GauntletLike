package backend;

import game_objects.AbstractGameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.me.walljumper.gui.SceneObject;

public class WorldRenderer {

	private SpriteBatch batch;
	public static WorldRenderer renderer = new WorldRenderer();
	public OrthographicCamera camera;
	public OrthographicCamera background_camera;
	public OrthographicCamera guiCamera;
	public TextureRegion background_image;
	public BitmapFont whiteFont;
	public boolean weatherBool;
	private Array<SceneObject> sceneObjects;
	private Array<FontManager> fontManagers;

	private WorldRenderer() {

	}

	public void init() {

		batch = new SpriteBatch();
		fontManagers = new Array<FontManager>();
		sceneObjects = new Array<SceneObject>();
		fontManagers.add(new FontManager(batch));

		// Initialize main camera
		camera = new OrthographicCamera(Constants.viewportWidth,
				Constants.viewportHeight);
		camera.position.set(0, 0, 0);
		camera.setToOrtho(false);
		camera.update();

		background_camera = new OrthographicCamera(Constants.viewportWidth,
				Constants.viewportHeight);
		background_camera.position.set(0, 0, 0);
		background_camera.setToOrtho(false);
		background_camera.update();

		guiCamera = new OrthographicCamera(Constants.bgViewportWidth,
				Constants.bgViewportHeight);
		guiCamera.position.set(0, 0, 0);
		guiCamera.setToOrtho(false);
		guiCamera.update();

		background_image = Assets.instance.background.bg;
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
	}

	public void onScreen(AbstractGameObject obj) {

	}

	public void makeButtons() {
		SceneObject.setCamera(guiCamera);
		
	}

	public void writeToWorld(String string, float x, float y) {

	}

	public void updateScene(float deltaTime) {
		for (SceneObject objs : sceneObjects) {
			objs.update(deltaTime);
		}
	}

	public void clearScene() {
		unDisplayToWorld(1);
		sceneObjects.clear();
	}

	private void renderWorld() {
		// apply changes to camera, render level
		// World.controller.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		World.world.cameraHelper.render(batch);
		LevelStage.render(batch);
		
		
		batch.end();

	}

	private void renderBackground() {
		
		batch.setProjectionMatrix(background_camera.combined);
		batch.begin();

	/*	batch.draw(background_image.getTexture(), 0, 0,
				background_camera.viewportWidth,
				background_camera.viewportHeight,
				background_image.getRegionX(), background_image.getRegionY(),
				background_image.getRegionWidth(),
				background_image.getRegionHeight(), false, false);*/
		batch.end();
	}

	private void renderGUI() {
		batch.setProjectionMatrix(guiCamera.combined);
		batch.begin();

		otherRenders();

		batch.end();
	}

	private void otherRenders() {

		// Render menu screen
		for (SceneObject objects : getSceneObjects()) {

			objects.render(batch);
		}
		
		for(FontManager fontManager : fontManagers){
			fontManager.render();
		}
/*
		if (displayingText) {
			WorldRenderer.renderer.writeToWorld(displayText, textPos.x,
					textPos.y);

		}*/
	}

	public void displayToWorld(int index, String text, Vector2 textPos) {
		
		while(index > fontManagers.size - 1)
			fontManagers.add(new FontManager(batch));
		
		
		fontManagers.get(index).add(text, textPos.x, textPos.y);
		
	}

	public void unDisplayToWorld(int index) {
		if(index < fontManagers.size)
		fontManagers.get(index).clear();
	}

	public void resize(int width, int height) {

		// Sets our units to be in relation to screen size
		camera.viewportHeight = (float) Constants.viewportHeight;
		camera.viewportWidth = (Constants.viewportHeight / (float) height)
				* (float) width;
		camera.update();

		background_camera.viewportHeight = Constants.viewportHeight;
		background_camera.viewportWidth = (Constants.viewportHeight / (float) height)
				* (float) width;
		background_camera.position.set(background_camera.viewportWidth / 2,
				background_camera.viewportHeight / 2, 100);
		background_camera.update();

		guiCamera.viewportHeight = Constants.bgViewportHeight;
		guiCamera.viewportWidth = (Constants.bgViewportHeight / (float) height)
				* (float) width;
		guiCamera.position.set(guiCamera.viewportWidth / 2,
				guiCamera.viewportHeight / 2, 100);
		guiCamera.update();

	}

	public void render() {

		renderBackground();
		renderWorld();
		renderGUI();
	}


	public void destroy() {
		guiCamera = null;
		camera = null;
		background_image = null;
		background_camera = null;
		unDisplayToWorld(1);
		clearScene();
	}

	public void pauseMenu() {

	}

	public void levelCompleteMenu() {

	}

	public Array<SceneObject> getSceneObjects() {
		// TODO Auto-generated method stub
		return sceneObjects;
	}

	

}
