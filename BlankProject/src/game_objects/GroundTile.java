package game_objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GroundTile extends AbstractGameObject {

	public GroundTile(TextureRegion img, float x, float y, float width, float height) {
		super(x, y, width, height);
		image = img;
	}


}
