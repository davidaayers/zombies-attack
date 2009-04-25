package com.wwflgames.za.mob;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.wwflgames.za.game.GameController;
import com.wwflgames.za.game.TurnRegulator;
import com.wwflgames.za.map.FloorMap;
import com.wwflgames.za.util.Dice;

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
public class ZombieFactory {

	private Image[] spriteImages;
	
	private String[] imageNames = {
		"head-gone-zombie.png",
		"red-haired-zombie.png",
		"green-zombie.png"
	};

	private GameContainer container;
	private StateBasedGame game;
	
	
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
		
		int chance = Dice.d(100);
		
		if ( chance > 0 && chance < 33 ) {
			MobSpriteSheet sheet = new MobSpriteSheet(spriteImages[1]);
			z = new WeakZombie(container, game, controller, map, regulator, 
					hero, sheet , 1 );
		} else if ( chance >= 33 && chance < 90 ) {
			MobSpriteSheet sheet = new MobSpriteSheet(spriteImages[0]);
			z = new WeakZombie(container, game, controller, map, regulator, 
					hero, sheet , 3 );
		} else {
			MobSpriteSheet sheet = new MobSpriteSheet(spriteImages[2]);
			z = new RegenZombie(container, game, controller, map, regulator, 
					hero, sheet , 3 , 20 );
		}
		
		
		return z;
	}
	
}
