package com.wwflgames.za.map;

public enum Dir {

	NORTH(0,new MapDelta(0,-1)),
	EAST(1,new MapDelta(1,0)), 
	SOUTH(2,new MapDelta(0,1)),
	WEST(3,new MapDelta(-1,0)),
	
	NORTHEAST(4,new MapDelta(1,-1)),
	NORTHWEST(5,new MapDelta(-1,-1)),
	SOUTHEAST(6,new MapDelta(1,1)),
	SOUTHWEST(7,new MapDelta(-1,1));
	
	static {
		NORTH.opposite = SOUTH;
		SOUTH.opposite = NORTH;
		EAST.opposite = WEST;
		WEST.opposite = EAST;
		
		NORTHEAST.opposite = SOUTHWEST;
		NORTHWEST.opposite = SOUTHEAST;
		SOUTHEAST.opposite = NORTHWEST;
		SOUTHWEST.opposite = NORTHEAST;
	}
	
	private Dir opposite;
	private int idx;
	private MapDelta mapDelta;
	
	Dir(int idx,MapDelta mapDelta) {
		this.idx = idx;
		this.mapDelta = mapDelta;
	}
	
	public Dir opposite() {
		return opposite;
	}
	
	public int getIdx() {
		return idx;
	}
	
	public MapDelta getMapDelta() {
		return mapDelta;
	}
	
}
