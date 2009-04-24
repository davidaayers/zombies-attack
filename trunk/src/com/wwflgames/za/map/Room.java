package com.wwflgames.za.map;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Polygon;

/**
 * 
 * Rooms exist for two reasons:
 * 
 * - See if the hero is inside a room, and make the walls transparent
 * - If the hero is outside the room, nothing in the room can be seen
 *   (i.e. fog of war)
 * 
 * The default room is square. There may be subclasses for odd-shaped
 * rooms, but I'll probably never finish that feature, let alone this
 * game.
 * 
 * @author davida
 *
 */
public class Room {

	List<MapSquare> mapSquares = new ArrayList<MapSquare>();
	
	int x1;
	int y1;
	int x2;
	int y2;
	List<Door> doors = new ArrayList<Door>();
	
	public Room(int x1 , int y1 , int x2 , int y2 ) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public Room(int x1 , int y1 , int x2 , int y2 , int doorx , int doory ) {
		this(x1,y1,x2,y2);
		
	}	
	
	public boolean isInRoom(int x , int y ) {
		return x >= x1 && x <= x2 && y >=y1 && y<=y2;
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public int getY2() {
		return y2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}
	
	public void addMapSquare(MapSquare ms) {
		mapSquares.add(ms);
	}
	
	public List<MapSquare> getMapSquares() {
		return mapSquares;
	}
	
	public void addDoor(int x , int y , Dir direction ) {
		Door d = new Door();
		d.doorx = x;
		d.doory = y;
		d.direction = direction;
		doors.add(d);
	}
	
	public int area() {
		int w = x2 - x1;
		int h = y2 - y1;
		return w*h;
	}
	
	public List<Door> getDoors() {
		return doors;
	}
	
	//TODO: refactor, pull up to top level class
	public class Door {
		public int doorx;
		public int doory;
		public Dir direction;
		public boolean open = false;
		Polygon doorPoly;
	}
	
}
