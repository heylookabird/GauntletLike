package com.me.walljumper.screens;

import game.GameMain;
import backend.DirectedGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractScreen implements Screen{
	protected DirectedGame game;
	
	public AbstractScreen(DirectedGame game){
		this.game = game;
		GameMain.currentScreen = this;
	}
	@Override
	public void render(float delta) {
		
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		
	}

	@Override
	public void hide() {
		
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

	public boolean handleKeyInput(int keycode) {
		return false;
	}
	
	public boolean handleKeyInputUp(int keycode){
		return false;
	}
	public void changeScreen(AbstractScreen screen) {
		((DirectedGame) Gdx.app.getApplicationListener()).setScreen(screen);

	}

	public abstract InputProcessor getInputProcessor();
	public void backToLevelMenu(){
		
		/*ProfileLoader.profileLoader.saveProfile();
		AudioManager.instance.stopMusic();
		ScreenTransition transition = ScreenTransitionSlice.init(.6f, ScreenTransitionSlice.UP_DOWN, 10,
				Interpolation.pow2Out);
		game.setScreen(new LevelMenu(game), transition);*/
	}
	public void nextLevel() {
		
	}
	
	public void backToMainMenu() {
		
	}
	public void backToHomeMenu() {
		
	}
	public void handleTouchInputUp(int screenX, int screenY, int pointer,
			int button) {
		
	}
	public void handleDrag(int screenX, int screenY, int pointer) {
		
	}
	public void controllerButtonUp(int controllerNumber, int buttonIndex) {
		// TODO Auto-generated method stub
		
	}
	public void joyStick(int controllerNumber, Vector2 leftJoyStick,
			Vector2 rightJoyStick) {
		// TODO Auto-generated method stub
		
	}
	public void controllerButtonDown(int controllerNumber, int buttonIndex) {
		// TODO Auto-generated method stub
		
	}

}
