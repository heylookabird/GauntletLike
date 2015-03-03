package game_objects;

import backend.Assets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Wall extends AbstractGameObject {

	private Array<TextureRegion> wallImages;

	public Wall(float x, float y, float width, float height) {
		super(x, y, width, height);

		wallImages = new Array<TextureRegion>();
		Array<AtlasRegion> reference = Assets.instance.background.walls;
		for (int i = 0; i < bounds.width; i++) {
			for (int j = 0; j < bounds.height; j++) {
				wallImages.add(reference.get((int) (Math.random() * reference.size)));
			}
		}
		
	}

	@Override
	public void render(SpriteBatch batch) {
		int count = 0;
		for (int j = 0; j < bounds.height; j++) {
			for (int i = 0; i < bounds.width; i++) {

				batch.draw(wallImages.get(count), position.x + i, position.y
						+ j, bounds.width, bounds.height, 1, 1, 1, 1, rotation);
				count++;
			}
		}

		if (this.debug) {
			batch.draw(debugTex, position.x, position.y, bounds.width,
					bounds.height);
		}

	}

}
