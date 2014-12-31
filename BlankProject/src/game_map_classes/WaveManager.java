package game_map_classes;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.Ranger;
import backend.LevelStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class WaveManager {
	private String difficulty;
	private int type; //int that allows us to randomly change what type of wave it is
	private Pixmap pixmap;
	private float stateTime;
	private float waitTime;
	private boolean clear;
	private Array<TimingNode> spawns;
	
	private enum ENEMY_TYPE {
		EMPTY(0, 0, 0), ENEMY1(255, 255, 255), ENEMY2(255,
				0, 0), SPAWNER(255, 255, 0);

		private int color;

		private ENEMY_TYPE(int r, int g, int b) {

			color = r << 24 | g << 16 | b << 8 | 0xff;

		}

		public boolean sameColor(int color) {
			return this.color == color;
		}

		public int getColor() {
			return color;
		}
	}
	
	private class TimingNode{
		public int timing;
		public AbstractGameObject spawn;
		
		public TimingNode(AbstractGameObject spawn, int timing){
			this.spawn = spawn;
			this.timing = timing;
		}
	}
	public WaveManager(){
		difficulty = "easy";
		type = MathUtils.random(2);
		stateTime = 0;
		waitTime = 3;
		clear = true;
		spawns = new Array<TimingNode>();
	}
	
	public void setDifficulty(String diff){
		difficulty = diff;
	}

	
	public void newWave(String path){
		String fileLoc = path + "/" + difficulty + type;
		
		try {
			pixmap = new Pixmap(Gdx.files.internal(fileLoc));
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

				if (ENEMY_TYPE.EMPTY.sameColor(currentPixel)) {
					continue;
				}
				
				
				
				// wont check for enemies if map has been cleared before

			}// inner for loop
		}// outer for loop
		
		pixmap.dispose();
	}
	
	private void addEnemy(AbstractGameObject enemy){
		LevelStage.enemyControlledObjects.add(enemy);
		//System.out.println("spawned");
	}
	
	private void addToSpawn(AbstractGameObject enemy, int time){
		if(LevelStage.isAreaFree(enemy.bounds)){
			spawns.add(new TimingNode(enemy, time));
		}
	}
	
	public void testWave(){
	/*	LevelStage.enemyControlledObjects.add(new Ranger(false, 1, 1, 1, 1));
		LevelStage.enemyControlledObjects.add(new Ranger(false, -2, -2, 1, 1));
		LevelStage.enemyControlledObjects.add(new Ranger(false, -3,-3, 1, 1));*/
		spawns.add(new TimingNode(new Ranger(false, 1, 1, 1, 1), 1));
		spawns.add(new TimingNode(new Ranger(false, -2, -2, 1, 1), 2));
		spawns.add(new TimingNode(new Ranger(false, -3,-3, 1, 1), 3));

		
		stateTime = 0;
		clear = false;

	}
	
	public void destroyEnemy(){
		if(!clear){
			LevelStage.enemyControlledObjects.pop();
		}
	}
	
	public void update(float deltatime){
		stateTime += deltatime;
		if(clear){
			if(stateTime > waitTime)
				testWave();
		}else{
			checkClear();
			spawnToTiming(deltatime);
		}
	}

	private void spawnToTiming(float statetime) {
		int delay = 3;
		for(int i = 0; i < spawns.size; i++){
			if(stateTime > spawns.get(i).timing){
				if(LevelStage.isAreaFree(spawns.get(i).spawn.bounds)){
					addEnemy(spawns.get(i).spawn);
					spawns.removeIndex(i);
				}
				//can be changed but makes it so that if you are delaying an enemy spawn by being on top of it, the enemy won't spawn instantly
				//after you leave
				else{
					spawns.get(i).timing = (int) stateTime + delay;
					System.out.println("Got here, timing :" + spawns.get(i).timing + " stateTime: " + stateTime);
				}
			}
			
		}
	}

	private void checkClear() {
		if(LevelStage.enemyControlledObjects.size > 0 || spawns.size > 0){
			clear = false;
		}else{
			clear = true;
			stateTime = 0;
		}
	}
}
