package com.me.walljumper.screens;

import game.GameMain;
import backend.DirectedGame;
import backend.InputManager;
import backend.World;
import backend.WorldRenderer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class GameScreen extends AbstractScreen {

	public GameScreen(DirectedGame game) {
		super(game);
	}

	@Override
	public void render(float delta) {
		World.world.render(delta);

	}

	@Override
	public void resize(int width, int height) {
		WorldRenderer.renderer.resize(width, height);
	}

	@Override
	public void show() {
		World.world.show();
		GameMain.currentScreen = this;
		
		

	}

	@Override
	public void handleDrag(int screenX, int screenY, int pointer) {
		World.world.handleDrag(screenX, screenY, pointer);
	}

	@Override
	public void hide() {
		World.world.hide();

	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {

	}

	@Override
	public void handleTouchInputDown(int screenX, int screenY, int pointer,
			int button) {
		World.world.handleTouchInput(screenX, screenY, pointer, button);

	}

	@Override
	public void handleTouchInputUp(int screenX, int screenY, int pointer,
			int button) {

		World.world.handleTouchUp(screenX, screenY, pointer, button);

	}

	public boolean handleKeyInput(int keycode) {

		return World.world.handleKeyInput(keycode);
	}

	public void changeScreen(AbstractScreen screen) {
		((Game) Gdx.app.getApplicationListener()).setScreen(screen);
	}
	@Override
	public void controllerButtonDown(int controllerNumber, int buttonIndex) {
		World.world.controllerButtonDown(controllerNumber, buttonIndex);
	}

	@Override
	public void controllerButtonUp(int controllerNumber, int buttonIndex) {
		World.world.controllerButtonUp(controllerNumber, buttonIndex);
		
	} 
	@Override
	public void joyStick(int controllerNumber, Vector2 leftJoyStick,
			Vector2 rightJoyStick) {
		System.out.println("aoethuasnht");

		World.world.joyStick(controllerNumber, leftJoyStick, rightJoyStick);
	}

	@Override
	public InputProcessor getInputProcessor() {
		return InputManager.inputManager;
	}

}
