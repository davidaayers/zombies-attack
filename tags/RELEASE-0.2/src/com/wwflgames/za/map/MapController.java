package com.wwflgames.za.map;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.util.Log;

import com.wwflgames.za.game.TurnListener;
import com.wwflgames.za.mob.ZombieController;
import com.wwflgames.za.mob.ZombieSpawner;

/**
 * Controls activities that occur on the map, such as zombie spawners
 * 
 * TODO: Maybe other stuff, like once zombies see the player they will beat down
 * the door or something
 * 
 * @author davidaayers
 * 
 */
public class MapController implements TurnListener, MapChangeListener {

	List<ZombieSpawner> spawners = new ArrayList<ZombieSpawner>();

	FloorMap map;
	ZombieController controller;

	int spawnerCountdown = 0;
	int[] spawnerCountdownByDifficult = new int[] { 15, 10, 5, 0 };

	public MapController() {
	}

	public void controlSpawners() {
		// give the spawners a chance to spawn zombies
		for (ZombieSpawner spawner : spawners) {
			spawner.maybeSpawnZombie();
		}
	}

	public void turnHappened(int turn) {

		// if the spawner countdown has expired, start
		// spawning zombies.
		if (spawnerCountdown != 0) {
			spawnerCountdown--;
		} else {
			controlSpawners();
		}
		
		Log.debug("SpawnerCountdown is " + spawnerCountdown);

	}

	public void mapChanged(FloorMap newMap) {
		map = newMap;

		spawners.clear();

		spawnerCountdown = spawnerCountdownByDifficult[newMap.getDifficulty()-1];

		Log.debug("spawnerCountdown = " + spawnerCountdown);
		
		// go through the map and look for any spawner locations
		for (int y = 0; y <= map.getHeight(); y++) {
			for (int x = map.getWidth(); x >= 0; x--) {
				MapSquare ms = map.getMapSquare(x, y);
				if (ms.isSpawnerLocation()) {
					spawners.add(new ZombieSpawner(x, y, controller, map, ms
							.getSpawnLocationPct()));
					Log.debug("Created spawner at " + x + "," + y);
				}
			}
		}
	}

	public FloorMap getMap() {
		return map;
	}

	public void setMap(FloorMap map) {
		this.map = map;
	}

	public ZombieController getController() {
		return controller;
	}

	public void setController(ZombieController controller) {
		this.controller = controller;
	}
}
