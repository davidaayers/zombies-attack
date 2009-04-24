package com.wwflgames.za.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.geom.Shape;

import com.wwflgames.za.map.effect.TileEffect;
import com.wwflgames.za.mob.Mobile;

public class MapSquare {

	FloorMap.TileType tile;
	List<Wall> walls = new ArrayList<Wall>();
	Map<Dir,Wall> dirWallMap = new HashMap<Dir,Wall>();
	Mobile mobile;
	FloorItem floorItem;
	TileEffect tileEffect;
	Shape floorPoly;
	boolean spawnZombieHere = false;
	boolean isSpawnerLocation = false;
	boolean isExitLocation = false;
	int spawnLocationPct = 0;
	
	int x;
	int y;
	
	public MapSquare ( int x , int y , FloorMap.TileType tile ) {
		this.tile = tile;
		this.x = x;
		this.y = y;
	}
	
	public FloorMap.TileType getTile() {
		return tile;
	}
	
	public void setTile( FloorMap.TileType tile ) {
		this.tile = tile;
	}
	
	public void addWall(Wall wall) {
		walls.add(wall);
		dirWallMap.put(wall.direction(), wall);
	}
	
	public List<Wall> getWalls() {
		return walls;
	}

	public Mobile getMobile() {
		return mobile;
	}

	public void setMobile(Mobile mobile) {
		this.mobile = mobile;
	}
	
	public String toString() {
		return "x = " + x + " y = " + y;
	}

	public FloorItem getFloorItem() {
		return floorItem;
	}

	public void setFloorItem(FloorItem floorItem) {
		this.floorItem = floorItem;
	}
	
	public TileEffect getTileEffect() {
		return tileEffect;
	}

	public void setTileEffect(TileEffect tileEffect) {
		this.tileEffect = tileEffect;
	}
	
	public boolean hasWall(Dir direction) {
		return dirWallMap.get(direction) != null;
	}
	
	public Wall findWall(Dir direction) {
		return dirWallMap.get(direction);
	}

	public Shape getFloorPoly() {
		return floorPoly;
	}

	public void setFloorPoly(Shape floorPoly) {
		this.floorPoly = floorPoly;
	}

	public boolean isSpawnZombieHere() {
		return spawnZombieHere;
	}

	public void setSpawnZombieHere(boolean spawnZombieHere) {
		this.spawnZombieHere = spawnZombieHere;
	}

	public boolean isSpawnerLocation() {
		return isSpawnerLocation;
	}

	public void setSpawnerLocation(boolean isSpawnerLocation) {
		this.isSpawnerLocation = isSpawnerLocation;
	}

	public int getSpawnLocationPct() {
		return spawnLocationPct;
	}

	public void setSpawnLocationPct(int spawnLocationPct) {
		this.spawnLocationPct = spawnLocationPct;
	}

	public boolean isExitLocation() {
		return isExitLocation;
	}

	public void setExitLocation(boolean isExitLocation) {
		this.isExitLocation = isExitLocation;
	}
	
}
