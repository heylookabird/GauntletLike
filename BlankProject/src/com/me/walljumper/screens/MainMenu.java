package com.me.walljumper.screens;

import game.GameMain;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import backend.ActorAccessor;
import backend.DirectedGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;
import com.me.walljumper.gui.Button;
import com.me.walljumper.gui.Image;
import com.me.walljumper.gui.Scene;
import com.me.walljumper.gui.SceneAssets;
import com.me.walljumper.gui.SceneObject;

public class MainMenu extends AbstractScreen{

	Image title, bg, platform;
	Button play, tutorial, options;

	public MainMenu(DirectedGame game) {
		super(game);
	}

	private Scene scene;
	
	private TweenManager twnManager;

	@Override
	public void show() {
		//ProfileLoader.init();

		//WallJumper.profile = new Profile();
		//WallJumper.profile.setFile("data/profile.json");
		
			
		
		//LOAD ASSETS FOR UI
		Array<String> paths = new Array<String>();
		paths.add("images/ui.pack");
		SceneAssets.instance = new SceneAssets(new AssetManager(), paths);
		
		//Set up scene
		scene = new Scene(this, game);
		GameMain.currentScreen = this;
		
		rebuildStage();
		
	}
	private void rebuildStage(){
		
	}

	@Override
	public void hide() {
		scene.destroy();
		twnManager.killAll();
		twnManager = null;
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		
		
	}
	
	public void handleTouchInputDown(int screenX, int screenY, int pointer, int button){

	}
	@Override
	public InputProcessor getInputProcessor() {
		return scene;
	}



	
	@Override
	public void render(float delta) {

		//Have to do this
		Gdx.gl.glClearColor(255, 255, 255, 0); // Default background color
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		scene.update(delta);
		scene.render();
		
		
		twnManager.update(delta);
	}
	
	@Override
	public void resize(int width, int height) {
		scene.resize(width, height);
	}
	
	private void buildTween() {
		
		twnManager = new TweenManager();
		
		//CALLED AFTER TWEEN FINISHES, ClEAN UP CODE
		TweenCallback myCallBack = new TweenCallback(){
			@Override
			 public void onEvent(int type, BaseTween<?> source) {
				
				/*play.bounds.setPosition(play.afterTwnPos);
				play.setToWrite("Play", play.dimension.x / 2 - 40, play.dimension.y /2 + 5, true);
				
				tutorial.bounds.setPosition(tutorial.afterTwnPos);
				tutorial.setToWrite("Tutorial", tutorial.dimension.x / 2 - 70, tutorial.dimension.y / 2 + 5, true);
				*/
				
				//BUILD ANOTHER TWEEN EVER 15 SECONDS
				Tween.registerAccessor(SceneObject.class, new ActorAccessor());
				Timeline.createSequence()
				.delay(5)
				
				.push(Tween.set(title, ActorAccessor.ROTATION).target(0))
				.push(Tween.set(play, ActorAccessor.ROTATION).target(0))
				.push(Tween.set(tutorial, ActorAccessor.ROTATION).target(0))

				.beginSequence()
					.push(Tween.to(title, ActorAccessor.ROTATION, .5f).target(180))
					.push(Tween.to(play, ActorAccessor.ROTATION, .7f).target(180))
					.push(Tween.to(tutorial, ActorAccessor.ROTATION, .7f).target(180))
					.end()
					
				.pushPause(1)
				
				.beginSequence()
					.push(Tween.to(tutorial, ActorAccessor.ROTATION, .7f).target(360))
					.push(Tween.to(play, ActorAccessor.ROTATION, .7f).target(360))
					.push(Tween.to(title, ActorAccessor.ROTATION, .5f).target(360))

					.end()
					.repeat(1000, 5).
					start(twnManager);
			}
		};
		
		//Set up register... value manipulator during tween
		Tween.registerAccessor(SceneObject.class, new ActorAccessor());
		
		Timeline.createSequence()
		.beginParallel()
			.push(Tween.to(title, ActorAccessor.SCALE, .5f).target(1.1f, 1.1f))
			.push(Tween.to(title, ActorAccessor.ROTATION, .5f).target(360))
			.push(Tween.to(title, ActorAccessor.XY, .5f)
					.target(title.afterTwnPos.x, title.afterTwnPos.y))
		.end()
		.beginParallel()
			.push(Tween.to(title, ActorAccessor.SCALE, .2f).target(1, 1))
			
			.push(Tween.to(play, ActorAccessor.SCALE, .7f).target(1.1f, 1.1f))
			.push(Tween.to(play, ActorAccessor.ROTATION, .7f).target(360))
			.push(Tween.to(play, ActorAccessor.XY, .7f)
					.target(play.afterTwnPos.x, play.afterTwnPos.y))
		.end()	
		.beginParallel()
			.push(Tween.to(play, ActorAccessor.SCALE, .2f).target(1, 1))
		
			.push(Tween.to(tutorial, ActorAccessor.SCALE, .2f).target(1, 1))
			.push(Tween.to(tutorial, ActorAccessor.SCALE, .7f).target(1.1f, 1.1f))
			.push(Tween.to(tutorial, ActorAccessor.ROTATION, .7f).target(360))
			.push(Tween.to(tutorial, ActorAccessor.XY, .7f)
					.target(tutorial.afterTwnPos.x, tutorial.afterTwnPos.y))
		.end()
		.beginSequence()
			.push(Tween.to(tutorial, ActorAccessor.SCALE, .2f).target(1, 1))
			
		.end()

		.setCallback(myCallBack).setCallbackTriggers(TweenCallback.COMPLETE).start(twnManager);
		
	}

}

		