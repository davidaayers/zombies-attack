package com.wwflgames.za.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.Log;

import com.wwflgames.za.gamestate.GamePlayState;
import com.wwflgames.za.gamestate.GameWonState;
import com.wwflgames.za.gamestate.MenuState;
import com.wwflgames.za.item.Ammo;
import com.wwflgames.za.item.Bandage;
import com.wwflgames.za.item.RangedWeapon;
import com.wwflgames.za.item.Weapon;
import com.wwflgames.za.map.Dir;
import com.wwflgames.za.map.FloorMap;
import com.wwflgames.za.map.MapChangeListener;
import com.wwflgames.za.map.MapController;
import com.wwflgames.za.map.MapGenerator;
import com.wwflgames.za.map.MapRenderer;
import com.wwflgames.za.map.MapSquare;
import com.wwflgames.za.mob.Hero;
import com.wwflgames.za.mob.ZombieController;
import com.wwflgames.za.mob.ZombieFactory;
import com.wwflgames.za.mob.attribute.AllAttributes;
import com.wwflgames.za.mob.attribute.Attribute;
import com.wwflgames.za.mob.attribute.AttributeFactory;
import com.wwflgames.za.ui.MessageManager;
import com.wwflgames.za.ui.UI;
import com.wwflgames.za.util.Dice;

/**
 * Controls the flow of the game, moving from level to level,
 * screen to screen, etc.
 * 
 * @author davidaayers
 *
 */
public class GameController {

	public static AngelCodeFont GAME_FONT;
	
	private static GameController instance;
	
	private GameContainer container;
	private StateBasedGame game;
	
	private List<MapChangeListener> mapListeners = 
		new ArrayList<MapChangeListener>();
	private List<GameCommandListener> commandListeners = 
		new ArrayList<GameCommandListener>();
	
	private FloorMap currentMap;
	private MapController mapController;
	private TurnRegulator turnRegulator;
	private Hero hero;
	private ZombieController zombieController;
	private UI ui;
	private MapRenderer mapRenderer;
	private MapGenerator mapGenerator;
	
	private int currentFloor;
	private int maxFloors;

	private Player player;

	private ZombieFactory zombieFactory;

	// singleton
	private GameController() {}
	
	public static GameController instance() {
		if ( instance == null ) {
			instance = new GameController();
		}
		return instance;
	}
	
	// should be called once as the game is starting up
	public void init(GameContainer container, StateBasedGame game) 
		throws SlickException {
		this.container = container;
		this.game = game;
		
		// create objects
		//TODO: probably shouldn't create the hero here, wont
		// work for saved games
		hero = new Hero();
		hero.init(container,game);
		
		zombieController = new ZombieController();
		zombieController.init(container,game);
		
		ui = new UI();
		ui.init(container,game,hero);

		mapRenderer = new MapRenderer();
		mapRenderer.init(container,game);
		
		mapController = new MapController();
		
		zombieFactory = new ZombieFactory();
		zombieFactory.init(container,game);
		
		GAME_FONT = new AngelCodeFont(
				"verdana.fnt",
				"verdana.png");
	}

	public void showGameMenu() {
		game.enterState(MenuState.STATE_ID, 
				new FadeOutTransition(Color.black), 
				new FadeInTransition(Color.black)); 
	}
	
	public void initNewGame(int difficulty) {
	
		MessageManager.instance().clearAll();
		
		currentFloor = 1;
		maxFloors = difficulty;

		Log.debug("maxFloors = " + maxFloors);
		
		mapGenerator = new MapGenerator(maxFloors);
		
		turnRegulator = new TurnRegulator();

		hero.setTurnRegulator(turnRegulator);
		
		addMapChangeListener(hero);

		zombieController.setHero(hero);
		zombieController.setTurnRegulator(turnRegulator);
		addMapChangeListener(zombieController);
		
		turnRegulator.setZombieController(zombieController);
		
		ui.setTurnRegulator(turnRegulator);
		ui.setZombieController(zombieController);
		addMapChangeListener(ui);
		
		mapRenderer.setHero(hero);
		addMapChangeListener(mapRenderer);
		
		mapController.setMap(currentMap);
		mapController.setController(zombieController);
		turnRegulator.addTurnListener(mapController);
		addMapChangeListener(mapController);
		
		currentFloor = 1;

		addMapChangeListener(TallyTracker.instance());
		
		// create a new player 
		player = createPlayerForDifficulty();
		player.setTurnRegulator(turnRegulator);
		addMapChangeListener(player);
		hero.setPlayer(player);
		player.setHero(hero);
		ui.setPlayer(player);

		//startLevel();
	}

	public boolean isHeroOnExitLocation() {
		MapSquare ms = currentMap.getMapSquare(hero.getMobx(), hero.getMoby());
		return ms.isExitLocation();
	}
	
	public void currentLevelCompleted() {
		
		// if this was the last level
		// take the player to the victory screen
		if ( currentFloor == maxFloors ) {
			showGameWonScreen();
		} else {
			fireGameCommand(GameCommand.SHOW_LEVEL_COMPLETE_POPUP);
		}
	}
	
	public void startNextLevel() {
		currentFloor++;
		startLevel();
	}

	private void showGameWonScreen() {
		game.enterState(GameWonState.STATE_ID, 
				new FadeOutTransition(Color.black), 
				new FadeInTransition(Color.black)); 	
	}

	public void startLevel() {
		
		Dir floorDir;
		// figure out which way the map should go
		if ( currentFloor % 2 == 0 ) {
			floorDir = Dir.WEST;
		} else {
			floorDir = Dir.EAST;
		}
		
		// generate the map for the new level
		currentMap = mapGenerator.generateFloor(60, floorDir , currentFloor);
		
		fireMapChangeEvent(currentMap);
		hero.moveTo(currentMap.getPlayerSpawnX(), currentMap.getPlayerSpawnY());
		// turn the hero in the right direction
		hero.setFacing(currentMap.getDirection());
		// reset the turn regulator 
		turnRegulator.reset();
		
		game.enterState(GamePlayState.STATE_ID, 
				new FadeOutTransition(Color.black), 
				new FadeInTransition(Color.black)); 				
	}
	
	public void addMapChangeListener(MapChangeListener listener) {
		mapListeners.add(listener);
	}
	
	public void addGameCommandListener(GameCommandListener listener) {
		commandListeners.add(listener);
	}
	
	public void fireGameCommand(GameCommand command ) {
		for ( GameCommandListener listener : commandListeners ) {
			listener.performCommand(command);
		}
	}
	
	private void fireMapChangeEvent(FloorMap floorMap) {
		// tell everyone who cares the map has changed
		for ( MapChangeListener listener : mapListeners ) {
			listener.mapChanged(floorMap);
		}
	}

	private Player createPlayerForDifficulty() {
		int numAttribute = maxFloors/4;
		
		Player p = new Player();

		// give the player some random attributes
		for ( int cnt = 0 ; cnt < numAttribute ; cnt ++ ) {
			p.addAttribute(AttributeFactory.getRandomAttributeForPlayer(p));
		}
		
		// give the player a weapon
		Weapon[] choices = new Weapon[] {
				Weapon.BAT,
				Weapon.KNIFE,
				Weapon.PISTOL
		};
		
		int weaponChance = Dice.randomInt(4);
		// if the player just gets fists as their weapon, they get an extra
		// attribute
		if ( weaponChance == 3 ) {
			p.addAttribute(AttributeFactory.getRandomAttributeForPlayer(p));
		} else {
			Weapon w = choices[weaponChance];
			p.addItemToInventory(w);
			p.equipWeapon(w);
			// add some ammo too
			if ( w instanceof RangedWeapon ) {
				int ammoCnt = Dice.d(5, 15);
				Ammo a = Ammo.createAmmoFor(w,ammoCnt);
				p.addItemToInventory(a);
			}
		}
		
		// add some bandages
		int bandageCnt = Dice.d(1, 4);
		Bandage b = new Bandage(bandageCnt);
		p.addItemToInventory(b);
		
		return p;
	}

	
	public UI getUi() {
		return ui;
	}

	public MapRenderer getMapRenderer() {
		return mapRenderer;
	}

	public Hero getHero() {
		return hero;
	}

	public ZombieController getZombieController() {
		return zombieController;
	}

	public Player getPlayer() {
		return player;
	}
	
	public int getMaxFloors() {
		return maxFloors;
	}

	public FloorMap getCurrentMap() {
		return currentMap;
	}

	public TurnRegulator getTurnRegulator() {
		return turnRegulator;
	}

	public ZombieFactory getZombieFactory() {
		return zombieFactory;
	}

}
