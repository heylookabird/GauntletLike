package game_map_classes;

import game_objects.AbstractGameObject;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CornerConcave extends AbstractGameObject {



	public CornerConcave(float x, float y, float width, float height) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
	}

	public CornerConcave(TextureRegion img, float x, float y, float width, float height,
			boolean flipX, boolean flipY) {
		super(x, y, width, height, flipX, flipY);
		this.image = img;

	
	}

	public CornerConcave(AtlasRegion img, float x, float y, float width, float height, float rotation) {
		super(x, y, width, height);
		this.rotation = rotation;
		this.image = img;
	}

}
