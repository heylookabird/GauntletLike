package game_map_classes;

import game_objects.AbstractGameObject;
import game_objects.ManipulatableObject;
import game_objects.Ranger;
import game_objects.enemies.MeleeEnemy;
import backend.Constants;
import backend.LevelStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.sun.xml.internal.ws.api.pipe.NextAction;

public class WaveManager {
	private String difficulty;
	private int type; //int that allows us to randomly change what type of wave it is... idk about this (joe)
	private Pixmap pixmap;
	private float stateTime;
	private float waitTime; //TIME BETWEEN CLEARING WAVE AND NEXT WAVE I BELIEVE I DIDN'T CODE THIS BULLSHIT (joe)
	private boolean clear;
	private Array<TimingNode> spawns;
	private int waveNum;
	private String path;
	
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
		public ManipulatableObject spawn;
		
		public TimingNode(ManipulatableObject spawn, int timing){
			this.spawn = spawn;
			this.timing = timing;
		}
	}
	public WaveManager(String path){
		stateTime = 0;
		waitTime = 3;
		waveNum = 1;
		clear = false;
		this.path = path;
		spawns = new Array<TimingNode>();
		
		newWave();
	}
	
	public void setDifficulty(String diff){
		difficulty = diff;
	}

	
	public void newWave(){
		String fileLoc = path + "" + waveNum + ".png";
		try {
			pixmap = new Pixmap(Gdx.files.internal(fileLoc));
		} catch (Exception e) {
			waveNum = 1; 
			newWave();
			
			//LEVEL COMPLETETETETE
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
					
				}else if(ENEMY_TYPE.ENEMY1.sameColor(currentPixel)){
					
					MeleeEnemy player2 = new MeleeEnemy(false, pixelX, baseHeight, 1, 1);
					LevelStage.enemyControlledObjects.add(player2);
				}
				
			}// inner for loop
		}// outer for loop
		
		pixmap.dispose();
		stateTime = 0;
		waveNum++;
		
	}
	
	private void addEnemy(ManipulatableObject enemy){
		LevelStage.enemyControlledObjects.add(enemy);
	}
	
	private void addToSpawn(ManipulatableObject enemy, int time){
		if(LevelStage.isAreaFree(enemy.bounds)){
			spawns.add(new TimingNode(enemy, time));
		}
	}
	
/*	public void testWave(){

		spawns.add(new TimingNode(new Ranger(false, 1, 1, 1, 1), 1));
		spawns.add(new TimingNode(new Ranger(false, -2, -2, 1, 1), 2));
		spawns.add(new TimingNode(new Ranger(false, -3,-3, 1, 1), 3));

		
		stateTime = 0;
		clear = false;

	}*/
	
	public void destroyEnemy(){
		if(!clear){
			LevelStage.enemyControlledObjects.pop();
		}
	}
	
	public void update(float deltatime){
		checkClear();

		if(clear){
			stateTime += deltatime;

			if(stateTime > waitTime)
				newWave();

			
		}else{
			spawnToTiming(deltatime);
		}
	}

	private void spawnToTiming(float statetime) {
		int delay = 3;
		for(int i = 0; i < spawns.size; i++){
			
			//WE S'PPOSED TO BE SPAWNING SOME MAH-FUCKAS
			if(stateTime > spawns.get(i).timing){
				
				//NO MAH-FUCKAS IN THE WAY SO WE SPAWNING SOME SHIT
				if(LevelStage.isAreaFree(spawns.get(i).spawn.bounds)){
					addEnemy(spawns.get(i).spawn);
					spawns.removeIndex(i);
					i--;
				//can be changed but makes it so that if you are delaying an enemy spawn by being on top of it, the enemy won't spawn instantly
				//after you leave (I'm harjit and i leave boring ass comments.. I <3 Pizza)
				}else{
					spawns.get(i).timing = (int) stateTime + delay;
					System.out.println("Got here, timing :" + spawns.get(i).timing + " stateTime: " + stateTime);
				}
			}
			
		}
	}

	private void checkClear() {
		if(LevelStage.enemyControlledObjects.size > 0){
			clear = false;
		}else{
			clear = true;
		}
	}
}
