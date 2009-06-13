package com.wwflgames.za.map;

import java.util.ArrayList;
import java.util.List;

import com.wwflgames.za.item.Item;
import com.wwflgames.za.map.effect.BloodTileEffect;

public class FloorMap {

	public enum TileType { FLOOR };
	
	// define the direction for this floor, i.e. in which
	// direction on the map is the elevator

	private Dir direction;
	private MapSquare[][] map;
	private List<Room> rooms = new ArrayList<Room>();
	private int playerSpawnX;
	private int playerSpawnY;
	private int exitLocationX;
	private int exitLocationY;
	private int difficulty;
	
	public FloorMap(MapSquare[][] map,Dir direction, int difficulty) {
		this.map = map;
		this.direction = direction;
		this.difficulty = difficulty;
	}
	
	public MapSquare[][] getMap() {
		return map;
	}
	
	public int getWidth() {
		return map.length - 1;
	}
	
	public int getHeight() {
		return map[0].length - 1;
	}
	
	public boolean inBounds(int x , int y ) {
		return x >= 0 && x <= getWidth() && 
			y >=0 && y <= getHeight();
	}
	
	public MapSquare getMapSquare(int x , int y ) {
		return map[x][y];
	}
	
	public void addFloorItem(Item item , int x , int y ) {
		getMapSquare(x,y).setFloorItem(new FloorItem(item,x,y));
	}
	
	public Room findRoomForMapSquare(MapSquare ms) {
		for ( Room room : rooms ) {
			if ( room.getMapSquares().contains(ms) ) {
				return room;
			}
		}
		return null;
	}
	
	public Room findRoomForXY( int x , int y ) {
		for ( Room room : rooms ) {
			if ( room.getX1()<=x && x <= room.getX2() &&
					room.getY1() <= y && y <= room.getY2() ) {
				return room;
			}
		}
		return null;
	}
	
	public void addRoom(Room room) {
		// add it to the list of rooms
		rooms.add(room);
		
		// go through all of the map squares for the room and add
		// walls
		int x1 = room.getX1();
		int y1 = room.getY1();
		
		int x2 = room.getX2();
		int y2 = room.getY2();
		
		// add the top and bottom walls
		for ( int x = x1 ; x <= x2 ; x ++ ) {
			MapSquare top = map[x][y1]; 
			room.addMapSquare(top);
			top.addWall(MapUtils.newWall(Dir.NORTH));
			MapSquare bottom = map[x][y2];
			room.addMapSquare(bottom);
			bottom.addWall(MapUtils.newWall(Dir.SOUTH));
		}
		
		// add the left and right walls
		for ( int y = y1 ; y <= y2 ; y ++ ) {
			MapSquare left = map[x1][y];
			room.addMapSquare(left);
			left.addWall(MapUtils.newWall(Dir.WEST));
			MapSquare right = map[x2][y];
			room.addMapSquare(right);
			right.addWall(MapUtils.newWall(Dir.EAST));
		}
		
		// finally, add doors
		for ( Room.Door door : room.getDoors() ) {
			MapSquare ms = map[door.doorx][door.doory];
			if ( ms.hasWall(door.direction) ) {
				Wall w = ms.findWall(door.direction);
				w.setDoor(door);
			}
		}		
	}
	
	public List<Room> getRooms() {
		return rooms;
	}
	
	public void addBlood( int mapx, int mapy, float renderx, float rendery, 
			int splats)  {
		MapSquare ms = this.getMapSquare(mapx, mapy);
		BloodTileEffect bte = null;
		// see if there's already a blood effect being rendered. If
		// so, just add more blood to it
		if ( ms.getTileEffect() != null && 
				ms.getTileEffect() instanceof BloodTileEffect ) {
			bte = (BloodTileEffect)ms.getTileEffect();
		} else {
			bte = new BloodTileEffect();
			bte.init();
			ms.setTileEffect(bte);
		}
		bte.addBlood(renderx, rendery, splats);
	}

	public int getPlayerSpawnY() {
		return playerSpawnY;
	}

	public void setPlayerSpawnY(int playerSpawnY) {
		this.playerSpawnY = playerSpawnY;
	}

	public int getPlayerSpawnX() {
		return playerSpawnX;
	}

	public void setPlayerSpawnX(int playerSpawnX) {
		this.playerSpawnX = playerSpawnX;
	}

	public int getExitLocationX() {
		return exitLocationX;
	}
	
	public int getExitLocationY() {
		return exitLocationY;
	}
	
	public void setExitLocation(int x , int y ) {
		exitLocationX = x;
		exitLocationY = y;
		map[x][y].setExitLocation(true);
	}

	public Dir getDirection() {
		return direction;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
}
