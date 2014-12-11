package backend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class FontManager {
	private SpriteBatch batch;
	private Array<GameString> strings;
	private BitmapFont blackFont;
	
	public FontManager(SpriteBatch batch) {
		this.batch = batch;
		strings = new Array<GameString>();
		blackFont = new BitmapFont(Gdx.files.internal("font/font.fnt"),
				Gdx.files.internal("font/font.png"), false);
	}

	public void add(String string, float x, float y){
		strings.add(new GameString(string, x, y));
		
	}
	public void clear(){
		strings.clear();
	}
	public void render() {
		for(GameString string : strings){
			blackFont.draw(batch, string.getString(), string.getPosition().x, string.getPosition().y);
		}

	}

}
