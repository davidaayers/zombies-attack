package com.wwflgames.za.map;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.util.Log;

import com.wwflgames.za.item.Ammo;
import com.wwflgames.za.item.Bandage;
import com.wwflgames.za.item.Item;
import com.wwflgames.za.item.Weapon;
import com.wwflgames.za.mob.ZombieController;
import com.wwflgames.za.util.Dice;
import com.wwflgames.za.util.GShuffleBag;

/**
 * There are 4 difficulty levels.  In an easy game, each floor increases
 * the difficulty.  In med games, every 2 floors, and hard, every 3 floors.
 * 
 * @author davidaayers
 *
 */
public class MapGenerator {

	private final static int HEIGHT = 6;

	private int maxFloors = 0;

	private Map<Integer,Integer> floorToDifficultyMap = 
		new HashMap<Integer,Integer>();

	private GShuffleBag<Item> itemBag;
	
	
	public MapGenerator(int maxFloors) {
		this.maxFloors = maxFloors;
		// create floorToDifficultyMap
		int increaseEvery = maxFloors / 4;
		
		int difficulty = 1;
		for ( int floor = 1 ; floor <= maxFloors ; floor ++ ) {
			floorToDifficultyMap.put(floor, difficulty);
			if ( floor % increaseEvery == 0 ) {
				difficulty ++;
			}
		}
	}
	
	public FloorMap generateFloor(int width,Dir direction,int floor) {

		int difficulty = floorToDifficultyMap.get(floor);
		
		initItemBag(difficulty);
		
		Log.debug("Floor = " + floor + " difficulty = " + difficulty);
		
		Log.debug("Dir = " + direction );
		
		int mapLeadingEdge = direction == Dir.EAST ? 0 : width -1;
		int mapTrailingEdge = direction == Dir.EAST ? width -1 : 0;
		
		Log.debug("Map leading edge " + mapLeadingEdge );
		Log.debug("Map trailing edge " + mapTrailingEdge );
		
		MapSquare[][] squares = new MapSquare[width][HEIGHT];

		// set up all the squares to have floor, we'll fill in more
		// stuff later
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < width; x++) {
				squares[x][y] = new MapSquare(x, y, FloorMap.TileType.FLOOR);

				// add a back wall, if y = 0
				if (y == 0) {
					squares[x][y].addWall(MapUtils.newWall(Dir.NORTH));
				}
				
				// add an east wall, if x == width -1
				if ( x == width -1 ) {
					squares[x][y].addWall(MapUtils.newWall(Dir.EAST));
				}
			}
		}

		FloorMap map = new FloorMap(squares, direction, difficulty);
		// now that the basic floor is done, add some random rooms
		// along the length of the floor. For now, we'll just add one
		// room along the Y axis (i.e. one room can't be above another room)
		// because that creates problems with the rendering

		// the rooms will be added thusly:
		// - move down the x axis in the room.
		// - After a random number of blank spaces, add a room of random size.
		// - repeat
		int xaxis = 0;
		boolean doneWithRooms = false;

		while (!doneWithRooms) {
			xaxis += Dice.d(3, 5);

			int roomWidth = Dice.d(3, 8);
			int roomHeight = Dice.d(2, 4);

			int roomy = Dice.randomInt(HEIGHT - roomHeight);

			// if we have room to add the room, add it
			// otherwise, we're done with rooms
			// subtract 4 too make sure there's some empty space
			// at the end of the floor before the stairs to the next
			// level
			if (xaxis + roomWidth < width - 4) {

				int x1 = xaxis;
				int y1 = roomy;
				int x2 = xaxis + roomWidth;
				int y2 = roomy + roomHeight;

				Room r = new Room(x1, y1, x2, y2);

				// add some doors
				int numDoors = Dice.d(1, 4);
				int doorsAdded = 0;
				while (doorsAdded == 0) {
					for (int cnt = 0; cnt < numDoors; cnt++) {
						Dir dir = Dir.values()[Dice.randomInt(4)];
						if (dir == Dir.NORTH || dir == Dir.SOUTH) {
							int xPos = xaxis + Dice.randomInt(roomWidth);
							int yPos = dir == Dir.NORTH ? roomy : roomy
									+ roomHeight;
							// make sure if the room is at the top or bottom
							// of the map that we don't add a door where
							// the player can't get to it
							if (roomy == 0 || roomy + roomHeight == HEIGHT - 1) {
								continue;
							} else {
								r.addDoor(xPos, yPos, dir);
								doorsAdded++;
							}
						} else {
							int xPos = dir == Dir.WEST ? xaxis : xaxis
									+ roomWidth;
							int yPos = roomy + Dice.randomInt(roomHeight);
							r.addDoor(xPos, yPos, dir);
							doorsAdded++;
						}
					}
				}

				// put some stuff in the room. Bigger rooms should have more
				// stuff
				int area = r.area();
				
				// stuff density is based on level -- the higher this
				// number, the less stuff there is placed in
				// the room.
				// let's try making it the inverse of difficulty -- as you
				// progress, there is less and less stuff available to the
				// player
				//TODO: tune this based on gameplay, inverse may be too hard
				// 25 area room
				// 8 density = 4 items
				// 10 density = 3 items
				// 12 density = 2 items
				// 16 density = 2 items
				int[] densityIdx = new int[] { 8 , 10 , 12 , 16 };
				int stuffDensity = densityIdx[difficulty-1];
				Log.debug("Stuff density = " + stuffDensity);
				
				int howMuchStuff = area / stuffDensity + 1;
				for (int cnt = 0; cnt < howMuchStuff; cnt++) {
					int x = x1 + Dice.randomInt(roomWidth);
					int y = y1 + Dice.randomInt(roomHeight);
					map.addFloorItem(itemBag.next(), x, y);
				}
				
				// put some zombies in the room. Bigger rooms should have
				// more zombies
				// zombie density is the opposite of rooms. early on, there
				// are less zombies in the room, but later, there are more
				int[] zombieDensityIdx = new int[] { 12 , 10 , 8 , 4 };
				int zombieDensity = zombieDensityIdx[difficulty-1];
				Log.debug("area is " + area );
				int minZombies = area/zombieDensity +1;
				int maxZombies = (int)((double)minZombies * 1.5);

				if ( maxZombies == minZombies ) {
					maxZombies = minZombies+1;
				}
				
				Log.debug("min = " + minZombies + " max = " + maxZombies );
				int howManyZombies = Dice.d(minZombies,maxZombies);
				for (int cnt = 0; cnt < howManyZombies; cnt++) {
					int x = x1 + Dice.randomInt(roomWidth);
					int y = y1 + Dice.randomInt(roomHeight);
					// if there's already a zombie there, the player
					// got lucky
					MapSquare ms = map.getMapSquare(x, y);
					ms.setSpawnZombieHere(true);
				}

				map.addRoom(r);
				xaxis += roomWidth;
			} else {
				doneWithRooms = true;
			}
		}
		
		// add some random zombies around the map
		int howManyArr[] = { 20 , 30 , 40 , 50 };
		int howManyZombies = howManyArr[difficulty-1];
		for ( int cnt = 0 ; cnt < howManyZombies ; cnt ++ ) {
			int x = Dice.d(0,width-1);
			int y = Dice.d(0,HEIGHT-1);
			MapSquare ms = map.getMapSquare(x, y);
			ms.setSpawnZombieHere(true);
		}
		
		int[] spawnChanceByDifficulty = new int[] { 10 , 20 , 30, 40 };
		int spawnChance = spawnChanceByDifficulty[difficulty-1];
		
		//add zombie spawners to the leading edge of the map
		for ( int y = 0 ; y < HEIGHT ; y ++ ) {
			MapSquare ms = map.getMapSquare(mapLeadingEdge, y);
			ms.setSpawnerLocation(true);
			//TODO: the spawn pct should scale with level
			ms.setSpawnLocationPct(spawnChance);
		}
		
		// set the player spawn location
		map.setPlayerSpawnX(mapLeadingEdge);
		map.setPlayerSpawnY(HEIGHT/2);

		// set the map exit location
		map.setExitLocation(mapTrailingEdge, HEIGHT/2);
		
		return map;
	}

	private void initItemBag(int difficulty) {
		itemBag = new GShuffleBag<Item>();
	
		switch (difficulty) {
		case 1:
			// early levels have basic weapons in abundance
			// but more advance weapons not so much. same with ammo
			itemBag.add(Weapon.BAT,5);
			itemBag.add(Weapon.KNIFE,5);
			itemBag.add(Weapon.PISTOL,5);
			// less chance of a rifle or shotgun
			itemBag.add(Weapon.RIFLE,2);
			itemBag.add(Weapon.SHOTGUN,2);
			itemBag.add(Ammo.createAmmoFor(Weapon.PISTOL, Dice.d(5, 15)),10);
			itemBag.add(Ammo.createAmmoFor(Weapon.RIFLE, Dice.d(5, 15)),5);
			itemBag.add(Ammo.createAmmoFor(Weapon.SHOTGUN, Dice.d(5, 15)),5);
			// lots of bandages early on
			itemBag.add(new Bandage(1),20);
			break;
		case 2:
			// only ranged weapons now
			itemBag.add(Weapon.PISTOL,5);
			itemBag.add(Weapon.RIFLE,10);
			itemBag.add(Weapon.SHOTGUN,10);
			itemBag.add(Weapon.FLAMETHROWER,5);
			itemBag.add(Ammo.createAmmoFor(Weapon.PISTOL, Dice.d(5, 15)),10);
			itemBag.add(Ammo.createAmmoFor(Weapon.RIFLE, Dice.d(5, 15)),10);
			itemBag.add(Ammo.createAmmoFor(Weapon.SHOTGUN, Dice.d(5, 15)),10);
			itemBag.add(Ammo.createAmmoFor(Weapon.FLAMETHROWER, Dice.d(5, 15)),5);
			// less bandages
			itemBag.add(new Bandage(15));
			break;
		case 3:
			itemBag.add(Weapon.RIFLE,10);
			itemBag.add(Weapon.SHOTGUN,10);
			itemBag.add(Weapon.FLAMETHROWER,10);
			itemBag.add(Ammo.createAmmoFor(Weapon.PISTOL, Dice.d(5, 15)),10);
			itemBag.add(Ammo.createAmmoFor(Weapon.RIFLE, Dice.d(5, 15)),10);
			itemBag.add(Ammo.createAmmoFor(Weapon.SHOTGUN, Dice.d(5, 15)),10);
			itemBag.add(Ammo.createAmmoFor(Weapon.FLAMETHROWER, Dice.d(5, 15)),5);
			// less bandages
			itemBag.add(new Bandage(10));
			break;
		case 4:
			itemBag.add(Weapon.RIFLE,10);
			itemBag.add(Weapon.SHOTGUN,10);
			itemBag.add(Weapon.FLAMETHROWER,10);
			itemBag.add(Ammo.createAmmoFor(Weapon.RIFLE, Dice.d(5, 15)),10);
			itemBag.add(Ammo.createAmmoFor(Weapon.SHOTGUN, Dice.d(5, 15)),10);
			itemBag.add(Ammo.createAmmoFor(Weapon.FLAMETHROWER, Dice.d(5, 15)),5);
			// less bandages
			itemBag.add(new Bandage(5));
			break;
		}
		
	}
}
