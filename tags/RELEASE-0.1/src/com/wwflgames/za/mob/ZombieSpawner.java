package com.wwflgames.za.mob;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import com.wwflgames.za.map.FloorMap;
import com.wwflgames.za.map.MapSquare;
import com.wwflgames.za.util.Dice;

public class ZombieSpawner {
  
	int mapx;
	int mapy;
	ZombieController controller;
	FloorMap floorMap;
	int spawnPct;
	
	public ZombieSpawner(int mapx, int mapy, ZombieController controller , 
			FloorMap floorMap , int spawnPct ) {
		this.mapx = mapx;
		this.mapy = mapy;
		this.controller = controller;
		this.floorMap = floorMap;
		this.spawnPct = spawnPct;
	}
	
	// based on our spawn % chance, decide if we're going to 
	// spawn a zombie
	public void maybeSpawnZombie() {
		// see if there is already a zombie at mapx, mapy
		MapSquare ms = floorMap.getMapSquare(mapx, mapy);
		
		if ( ms.getMobile() == null ) {
			int spawn = Dice.d(100);
			if ( spawn <= spawnPct ) {
				//TODO: refactor this into a zombie factory
				// that will not throw a slick exception
				try {
					controller.addZombieAt(mapx, mapy);
					Log.debug("Zombie spawned at " + mapx + " , " 
							+ mapy );
				} catch (SlickException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}
