package com.wwflgames.za.map;

import org.newdawn.slick.geom.Shape;

public class Wall {

	private Dir direction;
	private Room.Door door;
	Shape[] wallShapes;
	
	public Wall(Dir direction) {
		this.direction = direction;
	}

	public Dir direction() {
		return direction;
	}
	
	public boolean hasDoor() {
		return door != null;
	}

	public boolean isDoorOpen() {
		return door.open;
	}

	public Room.Door getDoor() {
		return door;
	}
	
	public void setDoor(Room.Door door ) {
		this.door = door;
	}

	public Shape[] getWallShapes() {
		return wallShapes;
	}

	public void setWallShapes(Shape[] wallShapes) {
		this.wallShapes = wallShapes;
	}

	public String toString() {
		return "wall " + direction + " wallshapes = " + wallShapes;
	}


}
