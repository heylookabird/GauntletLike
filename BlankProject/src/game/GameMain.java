package game;

import backend.DirectedGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.walljumper.screens.AbstractScreen;
import com.me.walljumper.screens.GameScreen;

public class GameMain extends DirectedGame {

	public static AbstractScreen currentScreen;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
			
		((DirectedGame) Gdx.app.getApplicationListener()).setScreen(new GameScreen(this));

		
		
		
	}

	@Override
	public void dispose() {
	
		currentScreen.dispose();
	}


	@Override
	public void resize(int width, int height) {
		currentScreen.resize(width, height);
	}

	@Override
	public void pause() {
		currentScreen.pause();
	}

	@Override
	public void resume() {
		currentScreen.resume();
	}
}
