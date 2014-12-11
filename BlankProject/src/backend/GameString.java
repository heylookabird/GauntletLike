package backend;

import com.badlogic.gdx.math.Vector2;

public class GameString {
	private String string;
	private Vector2 position;
	
	public GameString(String string, float x, float y){
		this.setString(string);
		setPosition(new Vector2(x, y));
	}
	public Vector2 getPosition() {
		return position;
	}
	public void setPosition(Vector2 position) {
		this.position = position;
	}
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}

}
