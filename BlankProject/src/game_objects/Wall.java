package game_objects;

import backend.Assets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Wall extends AbstractGameObject{
	
	
	private Array <TextureRegion> wallImages;
	public Wall(float x, float y, float width, float height){
		super(x,y,width,height);
		
		wallImages = new Array<TextureRegion>();
		Array<AtlasRegion> reference = Assets.instance.background.walls;
		for(int i = 0; i < dimension.x; i ++){
			wallImages.add(reference.get((int)(Math.random() * reference.size)));
		}
		
		for(int i = 0; i < dimension.y; i ++){
			wallImages.add(Assets.instance.mage.facingEast);

			wallImages.add(reference.get((int)(Math.random() * reference.size)));
		}
		
		System.out.println(wallImages.size);
	}
	
	@Override
	public void render(SpriteBatch batch) {

		for(int i = 0; i < dimension.x; i++){
			
			batch.draw(Assets.instance.mage.facingEast, position.x + i, position.y, 0, 0, 1, 1, 1, 1, rotation);
		}
		
		for(int i = 0; i < dimension.y; i++){
			batch.draw(Assets.instance.mage.facingEast, position.x, position.y + i, 0, 0, 1, 1, 1, 1, rotation);
		}
	
	}
	


}
