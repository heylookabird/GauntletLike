package backend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

public class LevelLoader {

	private Pixmap pixmap;
	private int playerNum;

	public enum BLOCK_TYPE {
		EMPTY(0, 0, 0), 
		PLAYER_SPAWNPOINT(255, 255, 255), 
		ENEMY_SPAWNPOINT(255, 0, 0);
		private int color;

		private BLOCK_TYPE(int r, int g, int b) {

			color = r << 24 | g << 16 | b << 8 | 0xff;

		}

		public boolean sameColor(int color) {
			return this.color == color;
		}

		public int getColor() {
			return color;
		}
	}

	public LevelLoader(String fileName) {
		init(fileName);
	}
	public void startLevel(String fileName){
		init(fileName);
	}
	private void init(String fileName) {
		playerNum = 0;
		// Load image file that represents level data
		try{
		pixmap = new Pixmap(Gdx.files.internal(fileName));
		}catch(Exception e){
			
			return;
		}
		// scan pixels from top-left to bottom right
		for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {

			for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {

				// height grows bottom to top
				float baseHeight = pixmap.getHeight() - pixelY;

				// Get color of current pixel as 32-bit RGBA value
				int currentPixel = pixmap.getPixel(pixelX, pixelY);
				
				if(BLOCK_TYPE.EMPTY.sameColor(currentPixel)){
					continue;
				}
					if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {
						if(Controllers.getControllers().size < playerNum)
						continue;
					
					if (itIsStartOfNewObject(pixelX, pixelY, currentPixel)) {

						// Spawn player
						/*Mage rogue = new Mage(pixelX * 1,
								baseHeight, .19f, .31f, Constants.ROGUE_SCALE, Controllers.getControllers().size > 0);
						// Track him in these arrays
						LevelStage.playerControlledObjects.add(rogue);
						InputManager.inputManager.addObject(rogue);
						playerNum++;
						*/

					}
					
					
				}
			}// inner for loop
		}// outer for loop
	}// end of method
	private Vector2 getDimension(int pixelX, int pixelY, int currentPixel){
		//Gets the
		Vector2 newPixelXY = extendPlatformDownRight(pixelX, pixelY, currentPixel);
		int lengthX = (int) (newPixelXY.x - pixelX) + 1;
		int lengthY = (int)(newPixelXY.y - pixelY) + 1;
		return newPixelXY.set(lengthX, lengthY);
	}
	private Vector2 extendPlatformDownRight(int i, int j, int color) {
		while(nextIsSameColor(i, j + 1, color)){
			j++;
			
		}
		while(nextIsSameColor(i + 1, j, color)){
			i++;
		}
		
		return new Vector2(i, j);
	}
	private Vector2 extendPlatformRightDown(int i, int j, int color){
		while(nextIsSameColor(i + 1, j, color)){
			i++;
		}
		while(nextIsSameColor(i, j + 1, color)){
			j++;
		}
		
		return new Vector2(i, j);
	}
	private boolean nextIsSameColor(int i, int j, int color){
		if(pixmap.getPixel(i, j) == color){
			return true;
		}
		return  false;
	}
	private boolean itIsStartOfNewObject(int i, int j, int color) {
		int lastPixelX = pixmap.getPixel(i - 1, j);
		int lastPixelY = pixmap.getPixel(i, j - 1);

		if (lastPixelX == color)
			return false;
		if (lastPixelY == color)
			return false;

		return true;
	}
	private boolean isStartOfNewObject(int i, int j, int color, int color2){
		int lastPixelX = pixmap.getPixel(i - 1, j);
		int lastPixelY = pixmap.getPixel(i, j - 1);

		if (lastPixelX == color || lastPixelX == color2)
			return false;
		if (lastPixelY == color || lastPixelY == color2)
			return false;

		return true;
	}

	public void destroy() {
		
	}
}