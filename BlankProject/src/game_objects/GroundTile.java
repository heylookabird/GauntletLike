package game_objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GroundTile extends AbstractGameObject {

	private static final float tileSize = 2;
	public GroundTile(TextureRegion img, float x, float y, float width, float height) {
		super(x, y, width, height);
		position.y -= height;
		image = img;
		System.out.println(dimension);
		//initDebug();
	}
	
	@Override
	public void render(SpriteBatch batch) {
		float xOffset = 0, yOffset = 0; 
		for(int i = 0; xOffset < dimension.x; i++){
			yOffset = 0;
			for(int j = 0; yOffset < dimension.y; j++){
				batch.draw(image, position.x + xOffset, position.y + yOffset, origin.x, origin.y, tileSize, tileSize, 1, 1, rotation);
				yOffset += tileSize;
			}
			xOffset += tileSize;
		}
	}


}
