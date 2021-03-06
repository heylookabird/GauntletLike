package backend;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

public class Assets implements AssetErrorListener {
	public static Assets instance = new Assets();
	private AssetManager assetManager;
	
	public Background background;
	public Planes planes;
	public Mage mage;
	public Weapons weapons;
	public Bat bat;
	public Effects effects;
	private Assets(){
		
	}
	
	//so we have all character art initialized
	public void characterInit(AssetManager assetManager){
		
		this.assetManager = assetManager;
		assetManager.setErrorListener(this);
	
		assetManager.load("images/mage.pack", TextureAtlas.class);
		assetManager.load("images/bg.pack", TextureAtlas.class);
		assetManager.load("images/sword.pack", TextureAtlas.class);
		assetManager.load("images/Bat.pack", TextureAtlas.class);
		assetManager.load("images/Effects.pack", TextureAtlas.class);
		assetManager.load("images/ice.pack", TextureAtlas.class);
		assetManager.finishLoading();
		
		
		TextureAtlas atlas = assetManager.get("images/mage.pack");
		TextureAtlas atlas2 = assetManager.get("images/bg.pack");
		TextureAtlas sword = assetManager.get("images/sword.pack");
		TextureAtlas bat = assetManager.get("images/Bat.pack");
		TextureAtlas effects = assetManager.get("images/Effects.pack");
		background = new Background(atlas2);
		mage = new Mage(atlas);
		weapons = new Weapons(sword);
		this.bat = new Bat(bat);
		this.effects = new Effects(effects);
		

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
		
		
		//add all types of objects for the stage here. eg platform, walls, traps, etc
		
		planes = new Planes(atlas);//just BS art to use for tests
	}
	
	
	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
	//	throwable.getCause();
		
	}
	
	public class Effects{
		public final Array<AtlasRegion> explosionImgs;
		public final Array<AtlasRegion> iceShardImgs;
		public final Array<AtlasRegion> iceExplosionImgs;
		
		public final Animation explosion;
		public final Animation iceShard;
		public final Animation iceExplosion;
		
		public Effects(TextureAtlas atlas){
			explosionImgs = new Array<AtlasRegion> ();
			explosionImgs.addAll(atlas.findRegions("Explosion"));
			
			iceShardImgs = new Array<AtlasRegion>();
			iceShardImgs.addAll(atlas.findRegions("iceberg"));
			
			iceExplosionImgs = new Array<AtlasRegion>();
			iceExplosionImgs.addAll(atlas.findRegions("icespear"));
			
			iceShard = new Animation(.05f, iceShardImgs);
			iceExplosion = new Animation(.2f, iceExplosionImgs);
			explosion = new Animation(.1f, explosionImgs);
		}
	}
	public class Background {
		public final Array<AtlasRegion> walls;
		public final Array<AtlasRegion> grass;
		
		public final AtlasRegion floor;
		public final AtlasRegion wallCorner1, wallCorner2, wallPillar;
		
		public Background(TextureAtlas atlas) {
			walls = new Array<AtlasRegion>();
			grass = new Array<AtlasRegion>();
			for(int i = 1; i < 7; i++)
				walls.add(atlas.findRegion("body" + i));
			
			for(int i = 0; i < 5; i++){
				grass.add(atlas.findRegion("grass" + i));
			}
			floor = atlas.findRegion("floortile");
			wallCorner1 = atlas.findRegion("wall_cornerConcave");
			wallCorner2 = atlas.findRegion("wall_cornerConvex");
			wallPillar = atlas.findRegion("wall_pillar");
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
	
	public class Bat{
		public final Array<AtlasRegion> walkingEastAni;
		public final Array<AtlasRegion> walkingWestAni;
		public final Array<AtlasRegion> walkingNorthAni;
		public final Array<AtlasRegion> walkingSouthAni;
		
		public final Animation west, east, north, south;
		
		public Bat(TextureAtlas atlas){
			walkingEastAni = atlas.findRegions("Bat_walking_east");
			walkingWestAni = atlas.findRegions("Bat_walking_west");
			walkingNorthAni = atlas.findRegions("Bat_walking_north");
			walkingSouthAni = atlas.findRegions("Bat_walking_south");
			
			west = new Animation(.1f, walkingWestAni);
			east = new Animation(.1f, walkingEastAni);
			north = new Animation(.1f, walkingNorthAni);
			south = new Animation(.1f, walkingSouthAni);
		}
		
	}
	
	public class Mage{
		public final Array<AtlasRegion> walkingEastAni;
		public final Array<AtlasRegion> walkingWestAni;
		public final Array<AtlasRegion> walkingNorthAni;
		public final Array<AtlasRegion> walkingSouthAni;
		
		public final Animation west, east, north, south;
		public final AtlasRegion facingWest, facingEast, facingNorth, facingSouth;
		
		public final AtlasRegion hp;
		
		private final float aniSpeed = .085f;
		public Mage(TextureAtlas atlas){
			walkingEastAni = atlas.findRegions("mage_walking_east");
			walkingWestAni = atlas.findRegions("mage_walking_west");
			walkingNorthAni = atlas.findRegions("mage_walking_north");
			walkingSouthAni = atlas.findRegions("mage_walking_south");
			
			west = new Animation(aniSpeed, walkingWestAni, Animation.LOOP);
			south = new Animation(aniSpeed, walkingSouthAni, Animation.LOOP);
			north = new Animation(aniSpeed, walkingNorthAni, Animation.LOOP);
			east = new Animation(aniSpeed, walkingEastAni, Animation.LOOP);
			
			hp = atlas.findRegion("health");
			

			facingWest = new AtlasRegion(atlas.findRegion("mage_standing_west"));
			facingEast = new AtlasRegion(atlas.findRegion("mage_standing_east"));
			facingNorth = new AtlasRegion(atlas.findRegion("mage_standing_north"));
			facingSouth = new AtlasRegion(atlas.findRegion("mage_standing_south"));

		}
	}
	public class Weapons{
		
		public final AtlasRegion sword;
		public Weapons(TextureAtlas atlas){
			sword = atlas.findRegion("sword");
			
		}
	}
	

}

