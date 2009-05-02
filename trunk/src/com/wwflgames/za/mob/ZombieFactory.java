package com.wwflgames.za.mob;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.wwflgames.za.game.GameController;
import com.wwflgames.za.game.TurnRegulator;
import com.wwflgames.za.map.FloorMap;
import com.wwflgames.za.map.MapChangeListener;
import com.wwflgames.za.util.Dice;
import com.wwflgames.za.util.GShuffleBag;

//TODO: Implement this class
//
// It should:
// * spawn random different types of zombies
// * pre-load all of the images in the init, so that spawning a new one
//   doesn't have to throw a slick exception.
// 
// Maybe it should
// * Be aware of different difficulties of zombies and be able to spawn
//   them based on the level the player is on
public class ZombieFactory implements MapChangeListener {

	private Image[] spriteImages;
	
	private String[] imageNames = {
		"red-haired-zombie.png",
		"head-gone-zombie.png",
		"green-zombie.png",
		"bloody-zombie.png",
		"naked-cowboy-zombie.png"
	};

	private GameContainer container;
	private StateBasedGame game;
	
	private GShuffleBag<Integer> zombieTypeBag;
	private FloorMap currentMap;
	
	public void init(GameContainer container, StateBasedGame game) 
		throws SlickException  {
		
		this.container = container;
		this.game = game;
		
		// load all of the zombie Images
		spriteImages = new Image[imageNames.length];
		
		for ( int idx = 0 ; idx < imageNames.length ; idx ++ ) {
			spriteImages[idx] = new Image(imageNames[idx],
					MobSpriteSheet.TRANSPARENCY_COLOR);
		}
	}

	public Zombie createZombie() {
		
		GameController gc = GameController.instance();
		ZombieController controller = gc.getZombieController();
		FloorMap map = gc.getCurrentMap();
		TurnRegulator regulator = gc.getTurnRegulator();
		Hero hero = gc.getHero();
		int difficulty = map.getDifficulty();
		
		Zombie z = null;
		
		int[] baseHpByDifficulty = { 1 , 1 , 2 , 3 };
		int baseZombieHp = baseHpByDifficulty[difficulty-1];
		int[] attackPowerByDifficulty = { 1 , 1 , 2 , 3 };
		int baseZombieAttackPower = attackPowerByDifficulty[difficulty-1];
		
		int type = zombieTypeBag.next();
		
		MobSpriteSheet sheet = new MobSpriteSheet(spriteImages[type]);

		int zhp = 0;
		int zatk = 0;
		
		switch ( type ) {
		case 0:
			// cannon fodder zombie
			z = new BasicZombie(container, game, controller, map, regulator, 
					hero, sheet , baseZombieHp , baseZombieAttackPower );
			break;
		case 1:
			// harder cannon fodder
			zhp = baseZombieHp + 2;
			zatk = baseZombieAttackPower + 2;
			z = new BasicZombie(container, game, controller, map, regulator, 
					hero, sheet , zhp , zatk);
			break;
		case 2:
			// zombie that regens health
			int[] regenChanceByDifficulty = { 10 , 20 , 30 , 40 };
			int regen = regenChanceByDifficulty[difficulty-1];
			zhp = baseZombieHp + 3;
			zatk = baseZombieAttackPower + 2;
			z = new RegenZombie(container, game, controller, map, regulator, 
					hero, sheet , zhp , zatk , regen );
			break;
		case 3:
			// easy to kill, hits like a truck
			zhp = baseZombieHp;
			int[] truckArr = { 3 , 5 , 7 , 9 };
			zatk = truckArr[difficulty-1];
			z = new BasicZombie(container, game, controller, map, regulator, 
					hero, sheet , zhp , zatk);
			break;
		case 4:
			// hard to kill, hits like a baby
			zhp = baseZombieHp * 3;
			zatk = 1;
			z = new BasicZombie(container, game, controller, map, regulator, 
					hero, sheet , zhp , zatk);
			break;
		}
		
		return z;
	}

	public void mapChanged(FloorMap newMap) {
		this.currentMap = newMap;
		createShuffleBag();
	}

	private void createShuffleBag() {
		zombieTypeBag = new GShuffleBag<Integer>();
		
		switch(currentMap.getDifficulty() ) {
		case 1:
			zombieTypeBag.add(0,50);
			zombieTypeBag.add(1,25);
			zombieTypeBag.add(2,25);
			break;
		case 2:
			zombieTypeBag.add(0,25);
			zombieTypeBag.add(1,25);
			zombieTypeBag.add(2,25);
			zombieTypeBag.add(3,25);
			
			break;
		case 3:
			zombieTypeBag.add(0,25);
			zombieTypeBag.add(1,25);
			zombieTypeBag.add(2,25);
			zombieTypeBag.add(3,25);
			zombieTypeBag.add(4,25);
			
			break;
		case 4:
			zombieTypeBag.add(0,25);
			zombieTypeBag.add(1,25);
			zombieTypeBag.add(2,50);
			zombieTypeBag.add(3,50);
			zombieTypeBag.add(4,25);
			
			break;
		}
		
	}


	
}
