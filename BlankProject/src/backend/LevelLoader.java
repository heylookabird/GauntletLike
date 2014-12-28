package backend;

import game_map_classes.TransitionBoundary;
import game_objects.Mage;
import game_objects.Wall;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

public class LevelLoader {

	private Pixmap pixmap;
	private int playerNum;

	public enum BLOCK_TYPE {
		EMPTY(0, 0, 0), PLAYER_SPAWNPOINT(255, 255, 255), ENEMY_SPAWNPOINT(255,
				0, 0), EXIT_BOUNDS(255, 255, 0);

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

	// default constructor so that we can have it for use whenever. should be
	// constructed in LevelStage
	public LevelLoader() {

	}

	public LevelLoader(String fileName) {
		init(fileName, false, false);
	}

	public void startLevel(String fileName) {
		init(fileName, false, false);
	}
	
	public void loadTestRoom(boolean players) {
		if(!players){
			Mage player = new Mage(false);
			player.setButtons(19, 21, 20, 22, 62);
			Mage player2 = new Mage(false);

			LevelStage.addPlayer(player);
			LevelStage.addPlayer(player2);
		}
		
		LevelStage.exitBounds.add(new TransitionBoundary(0, 5, 1, 1, false));
		
		this.makeTestBoundaries();
	}

	// method to be used that will automatically ensure that the players are there
	public void goToNextRoom(String file, boolean cleared) {
		init(file, true, cleared);
	}

	// changed constructor for init so that if players already exist, it will
	// know that.
	private void init(String fileName, boolean players, boolean cleared) {
		// if players already exist, it will skip resetting the num of players
		// if not, it will dispose of the old pixmap before creating a new one
		// TODO don't know if pixmap.dispose will work that way..doesn't really
		// have to dispose to work
		// just to be more efficient I wanted to try to get rid of the old
		// pixmap from memory
		if (!players)
			playerNum = 0;
		else
			pixmap.dispose();

		// Load image file that represents level data
		try {
			pixmap = new Pixmap(Gdx.files.internal(fileName));
		} catch (Exception e) {

			return;
		}
		// scan pixels from top-left to bottom right
		for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {

			for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {

				// height grows bottom to top
				float baseHeight = pixmap.getHeight() - pixelY;

				// Get color of current pixel as 32-bit RGBA value
				int currentPixel = pixmap.getPixel(pixelX, pixelY);

				if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
					continue;
				}
				
				if(BLOCK_TYPE.EXIT_BOUNDS.sameColor(currentPixel)){
					
				}
				
				if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {
					if (Controllers.getControllers().size < playerNum)
						continue;

					if (itIsStartOfNewObject(pixelX, pixelY, currentPixel)) {

						// Spawn player
						/*
						 * Mage rogue = new Mage(pixelX * 1, baseHeight, .19f,
						 * .31f, Constants.ROGUE_SCALE,
						 * Controllers.getControllers().size > 0); // Track him
						 * in these arrays
						 * LevelStage.playerControlledObjects.add(rogue);
						 * InputManager.inputManager.addObject(rogue);
						 * playerNum++;
						 */
					}
				}// end of playerSpawn loop

				// wont check for enemies if map has been cleared before
				if (!cleared) {
					if (BLOCK_TYPE.ENEMY_SPAWNPOINT.sameColor(currentPixel)) {

					}
				}// end of enemy Spawn loop

			}// inner for loop
		}// outer for loop
		
		makeBoundaries(pixmap);
	}// end of method

	private void makeBoundaries(Pixmap map){
		float height = map.getHeight();
		float width = map.getWidth();
		float wallThickness = 2;
		
		LevelStage.solidObjects.add(new Wall(-1,-1, width, wallThickness));//bottom bound
		LevelStage.solidObjects.add(new Wall(-1, -1, wallThickness, height));//left bound
		LevelStage.solidObjects.add(new Wall(-1,height, width, wallThickness));//top bound
		LevelStage.solidObjects.add(new Wall(width,-1, wallThickness, height));//right bound
	}
	
	private void makeTestBoundaries(){
		float wallThickness = 2;
		LevelStage.solidObjects.add(new Wall(-4,-4, 8, wallThickness));//bottom bound
		LevelStage.solidObjects.add(new Wall(-4, -4, wallThickness, 8));//left bound
		LevelStage.solidObjects.add(new Wall(-4, 4, 10, wallThickness));//top bound
		LevelStage.solidObjects.add(new Wall(4,-4, wallThickness, 8));//right bound
	}
	private Vector2 getDimension(int pixelX, int pixelY, int currentPixel) {
		// Gets the
		Vector2 newPixelXY = extendPlatformDownRight(pixelX, pixelY,
				currentPixel);
		int lengthX = (int) (newPixelXY.x - pixelX) + 1;
		int lengthY = (int) (newPixelXY.y - pixelY) + 1;
		return newPixelXY.set(lengthX, lengthY);
	}

	private Vector2 extendPlatformDownRight(int i, int j, int color) {
		while (nextIsSameColor(i, j + 1, color)) {
			j++;

		}
		while (nextIsSameColor(i + 1, j, color)) {
			i++;
		}

		return new Vector2(i, j);
	}

	private Vector2 extendPlatformRightDown(int i, int j, int color) {
		while (nextIsSameColor(i + 1, j, color)) {
			i++;
		}
		while (nextIsSameColor(i, j + 1, color)) {
			j++;
		}

		return new Vector2(i, j);
	}

	private boolean nextIsSameColor(int i, int j, int color) {
		if (pixmap.getPixel(i, j) == color) {
			return true;
		}
		return false;
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

	private boolean isStartOfNewObject(int i, int j, int color, int color2) {
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