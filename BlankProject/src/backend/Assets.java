package backend;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Assets implements AssetErrorListener {
	public static Assets instance = new Assets();
	private AssetManager assetManager;
	
	public Background background;
	public Planes planes;
	public Mage mage;
	private Assets(){
		
	}
	
	//so we have all character art initialized
	public void characterInit(AssetManager assetManager){
		
		this.assetManager = assetManager;
		assetManager.setErrorListener(this);
		assetManager.load("images/mage.pack", TextureAtlas.class);
		assetManager.finishLoading();
		
		TextureAtlas atlas = assetManager.get("images/mage.pack");
		
		mage = new Mage(atlas);
		//planes = new Planes(atlas);

	}
	
	//making the assets a little more flexible than the backend.  You can pass in which type of map
	//so you can load specific types of maps with the same code.  Strings will be adjusted in menus before
	//World 
	public void loadMapObjects(String path){
		//assetManager = new AssetManager();
		assetManager.setErrorListener(this);
		assetManager.load(path, TextureAtlas.class);
		assetManager.finishLoading();
		
		TextureAtlas atlas = assetManager.get(path);
		
		background = new Background(atlas);
		
		//add all types of objects for the stage here. eg platform, walls, traps, etc
		
		planes = new Planes(atlas);//just BS art to use for tests
	}
	
	
	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		
		
	}
	public class Background {
		public final AtlasRegion bg;

		public Background(TextureAtlas atlas) {
			bg = atlas.findRegion("water");
		}
	}
	public class Planes{
		public final Array<AtlasRegion> bluePlaneImgs;
		public final Array<AtlasRegion> greenPlaneImgs;
		public final Array<AtlasRegion> yellowPlaneImgs;
		public final Array<AtlasRegion> whitePlaneImgs;

		public final Animation bluePlane;
		public final Animation greenPlane;
		public final Animation yellowPlane;
		public final Animation whitePlane;
		
		public Planes(TextureAtlas atlas){
			bluePlaneImgs = atlas.findRegions("enemy4");
			whitePlaneImgs = atlas.findRegions("enemy3");
			yellowPlaneImgs = atlas.findRegions("enemy2");
			greenPlaneImgs = atlas.findRegions("enemy1");
			
			bluePlane = new Animation(.1f, bluePlaneImgs);
			whitePlane = new Animation(.1f, whitePlaneImgs);
			yellowPlane = new Animation(.1f, yellowPlaneImgs);
			greenPlane = new Animation(.1f, greenPlaneImgs);
		}
	}
	
	public class Mage{
		public final Array<AtlasRegion> walkingEastAni;
		public final Array<AtlasRegion> walkingWestAni;
		public final Array<AtlasRegion> walkingNorthAni;
		public final Array<AtlasRegion> walkingSouthAni;
		
		public final Animation west, east, north, south;
		public final AtlasRegion facingWest, facingEast, facingNorth, facingSouth;
		
		public Mage(TextureAtlas atlas){
			walkingEastAni = atlas.findRegions("mage_walking_east");
			walkingWestAni = atlas.findRegions("mage_walking_west");
			walkingNorthAni = atlas.findRegions("mage_walking_north");
			walkingSouthAni = atlas.findRegions("mage_walking_south");
			
			west = new Animation(.1f, walkingWestAni, Animation.LOOP_PINGPONG);
			south = new Animation(.1f, walkingSouthAni, Animation.LOOP_PINGPONG);
			north = new Animation(.1f, walkingNorthAni, Animation.LOOP_PINGPONG);
			east = new Animation(.1f, walkingEastAni, Animation.LOOP_PINGPONG);

			facingWest = new AtlasRegion(atlas.findRegion("mage_standing_west"));
			facingEast = new AtlasRegion(atlas.findRegion("mage_standing_east"));
			facingNorth = new AtlasRegion(atlas.findRegion("mage_standing_north"));
			facingSouth = new AtlasRegion(atlas.findRegion("mage_standing_south"));

		}
	}
	
	

}

