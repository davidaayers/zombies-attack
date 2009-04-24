package com.wwflgames.za.map;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Rectangle;

import com.wwflgames.za.mob.Mobile;
import com.wwflgames.za.mob.Zombie;

/** 
 * Represents a rectangle on the map.
 * 
 * TODO: maybe refactor room to use this?
 * 
 * @author davidaayers
 *
 */
public class MapRectangle {

	int x1,y1;
	int x2,y2;
	
	private FloorMap map;
	
	private List<MapSquare> mapSquares;
	
	public MapRectangle( int x1, int y1 , int x2, int y2 , FloorMap map ) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.map = map;
		
		createMapSquares();
	}
	
	private void createMapSquares() {
		mapSquares = new ArrayList<MapSquare>();
		
		
		for ( int x = x1 ; x <= x2 ; x ++ ) {
			for ( int y = y1 ; y <= y2 ; y ++ ) {
				mapSquares.add( map.getMapSquare(x, y) );
			}
		}
	}

	/**
	 * Gets all of the zombies contained in this rectangle
	 * @return
	 */
	public List<Zombie> getZombies() {
		List<Zombie> zombies = new ArrayList<Zombie>();

		for ( MapSquare ms : mapSquares ) {
			Mobile m = ms.getMobile();
			if ( m instanceof Zombie ) {
				zombies.add((Zombie)m);
			}
		}
		return zombies;
	}

	public List<MapSquare> getMapSquares() {
		return mapSquares;
	}
	
}
