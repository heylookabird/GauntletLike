package game_map_classes;

import com.badlogic.gdx.utils.Array;

public class GameMap {
	public Array<MapNode> cleared;
	
	public GameMap(){
		cleared = new Array<MapNode>();
	}
	
	public void clear() {
		cleared.clear();
	}
	
	public void cleared(int x, int y){
		System.out.println("added to clear array");
		cleared.add(new MapNode(x,y));
	}
	
	public boolean isCleared(int x, int y){
		System.out.println("Map cleared");
		for(MapNode node: cleared){
			if(node.x == x && node.y == y)
				return true;
		}
		
		return false;
	}
	
	
	private class MapNode{
		public int x, y;
		
		public MapNode(int x, int y){
			this.x = x;
			this.y = y;
		}
	}


	
}

