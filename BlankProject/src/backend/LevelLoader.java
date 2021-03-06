package backend;

import game_map_classes.CornerConcave;
import game_objects.GroundTile;
import game_objects.Rogue;
import game_objects.Wall;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class LevelLoader {

	private Pixmap pixmap;
	private int playerNum;

	public enum BLOCK_TYPE {
		EMPTY(0, 0, 0), PLAYER_SPAWNPOINT(255, 255, 255), ENEMY_SPAWNPOINT(255,
				0, 0), EXIT_BOUNDS(255, 255, 0), 
				CORNER_TOP_RIGHT(50, 0, 50), CORNER_TOP_LEFT(50, 50, 0), 
				CORNER_BOT_RIGHT(75, 75, 0), CORNER_BOT_LEFT(75, 0, 75),
				CORNER2_TOP_RIGHT(40, 0, 0), CORNER2_TOP_LEFT(0, 40, 0), 
				CORNER2_BOT_RIGHT(100, 0, 0), CORNER2_BOT_LEFT(0, 10, 0),
				PILLAR_UP(45, 0, 0), PILLAR_RIGHT(85, 0, 0),
				PILLAR_LEFT(125, 0, 0), PILLAR_DOWN(165, 0, 0),
				TILE(0, 125, 0);

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

	public void loadTestRoom(boolean players) {
		if(!players){
			int x = 0;
			do{
				Rogue player = new Rogue(Controllers.getControllers().size > 0);
				player.position.set(Constants.viewportWidth / 2 + x * 2, Constants.viewportHeight /2);
				
				
				player.setButtons(51, 29, 47, 32, 62);
				
	
				LevelStage.addPlayer(player);
				x++;
			}while(x < Controllers.getControllers().size);
		}
				
		init("levels/level1/stage.png", false, false);
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
			e.printStackTrace();
			return;
		}
		// scan pixels from top-left to bottom right
		for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {

			for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {

				// height grows bottom to top
				float baseHeight = pixmap.getHeight() - pixelY;

				// Get color of current pixel as 32-bit RGBA value
				int currentPixel = pixmap.getPixel(pixelX, pixelY);

				
				//START LOOKING FOR SPECIFIC COLORS THAT MAP TO THE OBJECTS		
				if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
					continue;
				}else if(BLOCK_TYPE.CORNER_TOP_RIGHT.sameColor(currentPixel)){
					CornerConcave corner = new CornerConcave(Assets.instance.background.wallCorner1, pixelX, baseHeight, 2, 2, false, false);
					LevelStage.solidObjects.add(corner);
				}else if(BLOCK_TYPE.CORNER_TOP_LEFT.sameColor(currentPixel)){
					CornerConcave corner = new CornerConcave(Assets.instance.background.wallCorner1, pixelX, baseHeight, 2, 2, true, false);
					LevelStage.solidObjects.add(corner);
				}
				else if(BLOCK_TYPE.CORNER_BOT_LEFT.sameColor(currentPixel)){
					CornerConcave corner = new CornerConcave(Assets.instance.background.wallCorner1, pixelX, baseHeight, 2, 2, false, true);
					LevelStage.solidObjects.add(corner);

				}else if(BLOCK_TYPE.CORNER_BOT_RIGHT.sameColor(currentPixel)){
					CornerConcave corner = new CornerConcave(Assets.instance.background.wallCorner1, pixelX, baseHeight, 2, 2, true, true);
					LevelStage.solidObjects.add(corner);

				}else if(BLOCK_TYPE.CORNER2_TOP_RIGHT.sameColor(currentPixel)){
					CornerConcave corner = new CornerConcave(Assets.instance.background.wallCorner2, pixelX, baseHeight, 2, 2, true, true);
					LevelStage.solidObjects.add(corner);
				}else if(BLOCK_TYPE.CORNER2_TOP_LEFT.sameColor(currentPixel)){
					CornerConcave corner = new CornerConcave(Assets.instance.background.wallCorner2, pixelX, baseHeight, 2, 2, false, true);
					LevelStage.solidObjects.add(corner);
				}
				else if(BLOCK_TYPE.CORNER2_BOT_LEFT.sameColor(currentPixel)){
					CornerConcave corner = new CornerConcave(Assets.instance.background.wallCorner2, pixelX, baseHeight, 2, 2, true, false);
					LevelStage.solidObjects.add(corner);

				}else if(BLOCK_TYPE.CORNER2_BOT_RIGHT.sameColor(currentPixel)){
					CornerConcave corner = new CornerConcave(Assets.instance.background.wallCorner2, pixelX, baseHeight, 2, 2, false, false);
					LevelStage.solidObjects.add(corner);

				}else if(BLOCK_TYPE.TILE.sameColor(currentPixel) && itIsStartOfNewObject(pixelX, pixelY, currentPixel)){
					Vector2 dimension = extendPlatformDownRight(pixelX, pixelY, currentPixel);
					System.out.println(dimension);
					LevelStage.uncollidableObjects.add(new GroundTile(Assets.instance.background.floor, pixelX, baseHeight, dimension.x - pixelX, dimension.y - pixelY));
				}else if(BLOCK_TYPE.PILLAR_LEFT.sameColor(currentPixel)){
					CornerConcave corner = new CornerConcave(Assets.instance.background.wallPillar, pixelX, baseHeight, 2, 2, 90);
					LevelStage.solidObjects.add(corner);
				
				}else if(BLOCK_TYPE.PILLAR_UP.sameColor(currentPixel)){
					CornerConcave corner = new CornerConcave(Assets.instance.background.wallPillar, pixelX, baseHeight, 2, 2, 180);
					LevelStage.solidObjects.add(corner);
				
				}else if(BLOCK_TYPE.PILLAR_RIGHT.sameColor(currentPixel)){
					CornerConcave corner = new CornerConcave(Assets.instance.background.wallPillar, pixelX, baseHeight, 2, 2, 270);
					LevelStage.solidObjects.add(corner);
				
				}else if(BLOCK_TYPE.PILLAR_DOWN.sameColor(currentPixel)){
					CornerConcave corner = new CornerConcave(Assets.instance.background.wallPillar, pixelX, baseHeight, 2, 2, 0);
					LevelStage.solidObjects.add(corner);
				}
				
				
				else if(BLOCK_TYPE.EXIT_BOUNDS.sameColor(currentPixel)){
					
				}
				
				// wont check for enemies if map has been cleared before
				if (!cleared) {
					if (BLOCK_TYPE.ENEMY_SPAWNPOINT.sameColor(currentPixel)) {

					}
				}// end of enemy Spawn loop

			}// inner for loop
		}// outer for loop
		
		//makeBoundaries(pixmap);
	}// end of method

	private void makeBoundaries(Pixmap map){
		float height = map.getHeight();
		float width = map.getWidth();
		float wallThickness = 2;
		
		//SET UP THE BACKGROUND FLOOR TEXTURES TO COVER THE LEVEL
		Array <AtlasRegion> temp = Assets.instance.background.grass;
		for(int i = (int) (-Constants.viewportWidth / 2); i < width + (int) (Constants.viewportWidth / 2); i += 2){
			for(int j = (int) (-Constants.viewportHeight / 2); j < height + (int) (Constants.viewportHeight / 2); j += 2){
				LevelStage.uncollidableObjects.add(new GroundTile(temp.get((int)(temp.size * Math.random())), i, j, 2, 2));
			}
		}
		
		
		LevelStage.solidObjects.add(new Wall(wallThickness, 0, width, wallThickness));//bottom bound
		LevelStage.solidObjects.add(new Wall(0, 0, wallThickness, height));//left bound
		LevelStage.solidObjects.add(new Wall(0, height, width, wallThickness));//top bound
		LevelStage.solidObjects.add(new Wall(width, wallThickness, wallThickness, height));//right bound
	}
	
	private void makeTestBoundaries(){
		
		
		
	}
	private Vector2 getDimension(int pixelX, int pixelY, int currentPixel) {

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


	public void destroy() {

	}


}