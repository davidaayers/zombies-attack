package com.wwflgames.za.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.wwflgames.za.game.GameCommand;
import com.wwflgames.za.game.GameController;
import com.wwflgames.za.game.Player;
import com.wwflgames.za.game.TallyTracker;
import com.wwflgames.za.game.TurnRegulator;
import com.wwflgames.za.game.TallyTracker.Tally;
import com.wwflgames.za.item.Ammo;
import com.wwflgames.za.item.Bandage;
import com.wwflgames.za.item.RangedWeapon;
import com.wwflgames.za.item.Weapon;
import com.wwflgames.za.map.Dir;
import com.wwflgames.za.map.FloorItem;
import com.wwflgames.za.map.FloorMap;
import com.wwflgames.za.map.MapChangeListener;
import com.wwflgames.za.map.MapDelta;
import com.wwflgames.za.map.MapRectangle;
import com.wwflgames.za.map.MapRenderer;
import com.wwflgames.za.map.MapSquare;
import com.wwflgames.za.map.MapUtils;
import com.wwflgames.za.map.Path;
import com.wwflgames.za.map.Wall;
import com.wwflgames.za.mob.Hero;
import com.wwflgames.za.mob.Mobile;
import com.wwflgames.za.mob.Zombie;
import com.wwflgames.za.mob.ZombieController;
import com.wwflgames.za.mob.attribute.Stat;
import com.wwflgames.za.slick.SlickEntity;
import com.wwflgames.za.ui.ControlScheme.Control;

/**
 * 
 * UI renders the main HUD and handles user input.
 * 
 * @author David
 *
 */
public class UI extends SlickEntity implements MapChangeListener {

	private static boolean DEBUG_UI = false;
	
	private Hero hero;
	private Player player;
	private FloorMap currentMap;
	private TurnRegulator turnRegulator;
	private ZombieController zombieController;
	private HealthBar healthBar;
	private List<WeaponOption> weaponOptions = new ArrayList<WeaponOption>();
	private boolean targetMode = false;
	private List<Target> targetedZombies;
	private Map<Control,MapDelta> controlMapDelta = 
		new HashMap<Control,MapDelta>();
	
	Shape targetingShape;
	
	//TODO make control scheme configurable
	ControlScheme controlScheme = new NumKeyPadControlScheme();

	private MapRectangle targetingRectangle;
	
	public UI () {
	}
	
	public void init(GameContainer container, StateBasedGame game, Hero hero) 
		throws SlickException {
		
		super.init(container, game);

		this.hero = hero;
		
		targetedZombies = new ArrayList<Target>();
		
		healthBar = new HealthBar(350,3,100,16,Color.darkGray,Color.green,0,0);
		healthBar.setPctComplete(1);
		
		weaponOptions.add(new WeaponOption(
				Control.WEAPON_1, null, null, hero , controlScheme) );
		weaponOptions.add(new WeaponOption(
				Control.WEAPON_2, Weapon.KNIFE, null, hero , controlScheme) );
		weaponOptions.add(new WeaponOption(
				Control.WEAPON_3, Weapon.BAT, null, hero , controlScheme) );
		weaponOptions.add(new WeaponOption(
				Control.WEAPON_4, Weapon.PISTOL, null, hero , controlScheme) );
		weaponOptions.add(new WeaponOption(
				Control.WEAPON_5, Weapon.RIFLE, null, hero , controlScheme) );
		weaponOptions.add(new WeaponOption(
				Control.WEAPON_6, Weapon.SHOTGUN, null, hero , controlScheme) );
		weaponOptions.add(new WeaponOption(
				Control.WEAPON_7, Weapon.FLAMETHROWER, null, hero , controlScheme));
		
		controlMapDelta.put ( Control.NORTH     , new MapDelta(  0 , -1 ) );
		controlMapDelta.put ( Control.NORTHEAST , new MapDelta(  1 , -1 ) );
		controlMapDelta.put ( Control.EAST      , new MapDelta(  1 ,  0 ) );
		controlMapDelta.put ( Control.SOUTHEAST , new MapDelta(  1 ,  1 ) );
		controlMapDelta.put ( Control.NORTHWEST , new MapDelta( -1 , -1 ) );
		controlMapDelta.put ( Control.WEST      , new MapDelta( -1 ,  0 ) );
		controlMapDelta.put ( Control.SOUTHWEST , new MapDelta( -1 ,  1 ) );
		controlMapDelta.put ( Control.SOUTH     , new MapDelta(  0 ,  1 ) );
		
	}
	
	@Override
	public void update(int delta)
		throws SlickException {
		double pctComplete = (double)hero.getCurrentHp()/(double)hero.getMaxHp();
		if ( pctComplete < 0 ) {
			pctComplete = 0;
		}
		healthBar.setPctComplete(pctComplete);
		MessageManager.instance().update(delta);
	}
	
	@Override
	public void render(Graphics g) throws SlickException {
		
		g.setFont(GameController.GAME_FONT);
		
		// draw the Player's health bar
		healthBar.render(g);
		g.setColor(Color.black);
		int currHp = hero.getCurrentHp();
		if ( currHp < 0 ) {
			currHp = 0;
		}
		String healthStr = "Health: " + currHp +"/"+hero.getMaxHp();
		g.drawString(healthStr, 360, 3);
		
		// draw bandage count
		Bandage bandage = (Bandage)GameController.instance().getPlayer().
			getItemByName(Bandage.ITEM_NAME);
		
		int numBandages = bandage == null ? 0 : bandage.getQuantity();
		
		g.setColor(Color.white);
		g.drawString("Bandages: " + numBandages, 200, 3);
		
		// draw the turn counter
		g.setColor(Color.white);
		g.drawString("Turn: " + turnRegulator.getTurn(), 730, 5);
		
		// draw the weapon bar
		int wbx = 40;
		int wby = 350;
		
		for ( WeaponOption wo : weaponOptions ) {
			wo.render(wbx, wby, g);
			wbx+=100;
		}
		
		// target mode needs to be translated, just
		// like the map.
		int heromapx = hero.getMobx();
		float translateX = (heromapx/20) * MapRenderer.TILE_WIDTH * 20 * -1;
		// translate
		g.translate(translateX, 0);
		
		
		if ( targetMode ) {
			
			if ( DEBUG_UI ) {
				for ( MapSquare ms : targetingRectangle.getMapSquares() ) {
					g.setColor(Color.red);
					g.draw(ms.getFloorPoly());
				}
			}
			
			// render a target above all of the zombies 
			for ( Target target : targetedZombies ) {
				Zombie z = target.zombie;
				float x = z.getRenderX();
				float y = z.getRenderY()+10;
				g.setColor(Color.red);
				g.drawOval(x, y, 20, 20);
				g.drawLine(x+10, y, x+10, y+20);
				g.drawLine(x,y+10,x+20,y+10);
				// draw the target key above the target
				String targetKey = Input.getKeyName(target.targetKey);
				int fontWidth = g.getFont().getWidth(targetKey);
				float fontx = x + 12 - (fontWidth/2);
				g.setColor(Color.white);
				g.drawString(targetKey, fontx, y-20);

				if ( DEBUG_UI ) {
					// draw the path to this target
					Path p = target.path;
					for ( Path.Step step : p.getSteps() ) {
						MapSquare ms = currentMap.getMapSquare(step.x, step.y);
						if ( target.hasLOS ) {
							g.setColor(Color.cyan);
						} else {
							g.setColor(Color.yellow);
						}
						g.draw(ms.getFloorPoly());
					}
				}				
			}
		} 
		
		// untranslate
		g.translate(0,0);
		g.resetTransform();
		
		MessageManager.instance().render(g);
	}
	
	public void mapChanged(FloorMap newMap) {
		//TODO what all needs to happen here?
		currentMap = newMap;
	}
	
	public void keyPressed(int key, char c) {

		// if animation are happening, don't respond
		// to keys
		if ( turnRegulator.isZombieTurnInProgress() || 
				turnRegulator.isPlayerTurnInProgress() ) {
			return;
		}
		
		// get the control for the movement key pressed
		Control control = controlScheme.getControl(key);
		// did we find a control for the given key?
		if ( control == null ) {
			return;
		}

		// see if we pressed a weapon change button, if
		// so, do it
		for ( WeaponOption wo : weaponOptions ) {
			if ( control == wo.getControl() ) {
				wo.activate();
				return;
			}
		}
		
		if ( control == Control.ACTIVATE_TARGET_MODE ) {
			Weapon w = hero.getEquippedWeapon();
			if ( w instanceof RangedWeapon ) {
				
				// make sure there is enough ammo
				Ammo a = hero.getAmmoFor(w);
				
				if ( a != null && a.getQuantity() > 0 ) {
					enableTargetMode();
					return;
				} 
				// NO AMMO!
				else {
					MessageManager.instance().addCenteredMessage(
					"No ammo for " + w.getName() );
				}
			} else {
				MessageManager.instance().addCenteredMessage(
						"No Ranged Weapon Equipped!");
			}
		}
		
		// if we're in target mode, look for next target and
		// fire commands
		if ( targetMode ) {			
			// see if the key pressed was a targeting control
			if ( controlScheme.isTargetingControl(control)) {
				// find the zombie that is targeted
				for ( Target t : targetedZombies ) {
					if ( t.targetKey == 
						controlScheme.getKeyForControl(control)) {
						doRangedAttack(t.zombie);
						return;
					}
				}
			} 
			// the user pressed a key that wasn't a target control
			// so just turn target mode off
			else {
				disableTargetMode();
			}
		}
		
		if ( control == Control.PAUSE_MENU ) {
			showPauseMenu();
			return;
		}
		
		if ( control == Control.CHANGE_DIR ) {
			changeDirection();
			return;
		}

		if ( control == Control.USE_BANDAGE ) {
			maybeUseBandage();
			return;
		}
		
		//TODO add door open animation?
		if ( control == Control.DOOR_STATE ) {
			player.startPlayerTurn();
			toggleAdjacentDoors();
			player.endPlayerTurn();
		}
		
		if ( control == Control.IDLE ) {
			player.startPlayerTurn();
			MessageManager.instance().addCenteredMessage("Time passes...");
			player.endPlayerTurn();
		}
		
		// see if the player is moving
		MapDelta delta = controlMapDelta.get(control);
		
		if ( delta != null ) {
			// if delta doesn't jive with the player's facing,
			// change their facing instead of moving
			if ( delta.x == 1 && hero.getFacing() == Dir.WEST ||
					delta.x == -1 && hero.getFacing() == Dir.EAST ) {
				changeDirection();
				return;
			} 
			// ok to move
			else {
				bumpMoveOrAttack(delta.x,delta.y);
			}
		}
	}

	private void showPauseMenu() {
		GameController.instance().fireGameCommand(GameCommand.SHOW_PAUSE_MENU);
	}

	private void maybeUseBandage() {
		// see if the player has any
		// bandages
		Player player = GameController.instance().getPlayer();
		Bandage bandage = (Bandage)player.getItemByName(Bandage.ITEM_NAME);
		
		if ( bandage == null || bandage.getQuantity() == 0 ) {
			MessageManager.instance().addCenteredMessage("No bandages!");
		} else {
			bandage.setQuantity(bandage.getQuantity()-1);
			// heal some health
			// bandages normally heal 4, but the player might
			// have attributes that improve that
			int healed = 4 + player.getStatValue(Stat.BANDAGING_SKILL);
			int maxHp = hero.getMaxHp();
			int newHp = hero.getCurrentHp() + healed;
			hero.setCurrentHp(newHp<maxHp?newHp:maxHp);
			MessageManager.instance().addCenteredMessage("Healed " + healed );
			TallyTracker.instance().addTally(Tally.BANDAGES_USED, 1);
		}
		
	}

	private void doRangedAttack(Zombie zombie) {
		disableTargetMode();
		player.startPlayerTurn();
		hero.doRangedAttack(zombie);
	}

	private void changeDirection() {
		player.startPlayerTurn();
		hero.changeFacing();
		player.endPlayerTurn();
	}
	
	private void disableTargetMode() {
		targetingShape = null;
		targetMode = false;
	}
	
	private void enableTargetMode() {
		// toggle target mode
		targetMode = true;
		// get the current equipped weapon
		RangedWeapon w = (RangedWeapon)hero.getEquippedWeapon();
		int range = w.getRange();
		
		Log.debug("range = " + range);
		
		// new way of targeting, based on map coordinates rather than poly
		int heromx = hero.getMobx();
		int heromy = hero.getMoby();
		int rx1,ry1,rx2,ry2;
		if ( hero.getFacing() == Dir.EAST ) {
			rx1 = heromx;
			ry1 = heromy - range;
			rx2 = heromx + range;
			ry2 = heromy + range;
		} else {
			rx1 = heromx - range;
			ry1 = heromy - range;
			rx2 = heromx;
			ry2 = heromy + range;
		}
		
		Log.debug("before bounds " + rx1+","+ry1+" "+rx2+","+ry2);
		
		// check bounds
		if ( rx1 < 0 ) rx1 = 0;
		if ( rx1 > currentMap.getWidth() ) rx1 = currentMap.getWidth();
		if ( rx2 < 0 ) rx2 = 0;
		if ( rx2 > currentMap.getWidth() ) rx2 = currentMap.getWidth();
		
		if ( ry1 < 0 ) ry1 = 0;
		if ( ry1 > currentMap.getHeight() ) ry1 = currentMap.getHeight();
		if ( ry2 < 0 ) ry2 = 0;
		if ( ry2 > currentMap.getHeight() ) ry2 = currentMap.getHeight();
		
		Log.debug("after bounds " + rx1+","+ry1+" "+rx2+","+ry2);
		
		targetingRectangle = new MapRectangle(rx1,ry1,rx2,ry2,currentMap);
		List<Zombie> zombies = targetingRectangle.getZombies();
		
		targetedZombies.clear();
		int cnt = 0;
		
		for ( Zombie z : zombies ) {
			// see if we can get line-of-sight to the given
			// zombie
			Path p = MapUtils.findLOSPath(hero.getMobx(), hero.getMoby(), 
					z.getMobx(), z.getMoby(), currentMap);

			if ( p != null ) {
				
				// walk the path and see if there are any zombies in
				// the way. This prevents targeting zombies that
				// are behind other zombies
				boolean foundZombie = false;
				List<Path.Step> steps = p.getSteps();
				
				// skip 1st and last step. 1st step is the player, last
				// step is the target
				for ( int idx = 1 ; idx < steps.size() -1 ; idx ++ ) {
					Path.Step step = steps.get(idx);
					MapSquare stepMs = currentMap.getMapSquare(step.x, step.y);
					Mobile m = stepMs.getMobile();
					if ( m != null ) {
						foundZombie = true;
						break;
					}
				}
				
				// zombie in the path, skip this one
				if ( foundZombie ) {
					continue;
				}
				
				Target t = new Target();
				t.zombie = z;
				t.path = p;
				t.hasLOS = true;
				t.targetKey = controlScheme.getKeyForControl(
						ControlScheme.TARGETING_CONTROLS[cnt]).intValue();
				targetedZombies.add(t);
				cnt++;
				
				// only target the first 5 zombies found
				//TODO: this should probably be the 5 closest.
				if ( cnt == 5 ) {
					break;
				}
				
			}
		}
		if ( cnt == 0 ) {
			MessageManager.instance().addCenteredMessage("Nothing in range!");
			targetMode = false;
		}
	
	}
	
	private void toggleAdjacentDoors() {
		
		// first, the easy bit. Check any of the walls that
		// are part of the current square and toggle the state
		// of any doors
		for ( Wall w : currentMap.getMapSquare(
				hero.getMobx(), hero.getMoby()).getWalls() ) {
			if ( w.hasDoor() ) {
				w.getDoor().open = !w.getDoor().open;
			}
		}
		
		// open/close any doors in walls in any of the 3 squares that
		// are adjacent, based on the facing of the hero
		Control[] checkControls;
		if ( hero.getFacing() == Dir.EAST ) {
			checkControls = new Control[] { Control.NORTH , Control.NORTHEAST , 
					Control.EAST , Control.SOUTHEAST , Control.SOUTH };
		} else {
			checkControls = new Control[] { Control.NORTH , Control.NORTHWEST , 
					Control.WEST , Control.SOUTHWEST , Control.SOUTH };
		}
		
		for ( Control c : checkControls ) {
			MapDelta d = controlMapDelta.get(c);
			int mx = hero.getMobx() + d.x;
			int my = hero.getMoby() + d.y;
			if ( !currentMap.inBounds(mx, my)) {
				continue;
			}
			MapSquare ms = currentMap.getMapSquare(mx, my);
			for ( Wall w : ms.getWalls() ) {
				if ( w.hasDoor() ) {
					w.getDoor().open = !w.getDoor().open;
				}
			}
		}
	}
	

	// bumps are used for combat. a bump will return
	// the player to their original position on the map
	// the rules here are simple:
	// if there is nothing in the space, move there
	// if there is a zombie, attack it
	private void bumpMoveOrAttack(int dx,int dy) {
		
		int checkx = hero.getMobx() + dx;
		int checky = hero.getMoby() + dy;
		
		Zombie z = zombieController.getZombieAt(checkx, checky);
		
		boolean canMove = hero.canMoveTo(checkx, checky);
		
		// if there's a zombie there, attack it
		if ( z != null && canMove ) {
			player.startPlayerTurn();
			hero.attack(z,dx,dy);
		}
		// otherwise, see if we can move
		else {
			if ( !canMove ) {
				MessageManager.instance().addCenteredMessage(
						"Can't move there!");
				return;
			}

			player.startPlayerTurn();
			hero.moveDelta(dx, dy);
			
			MapSquare ms = currentMap.getMapSquare(
					hero.getMobx(), hero.getMoby());
			
			// see if there is an item at the new location. If there
			// is, add it to the player's inventory and
			// remove it from the location
			FloorItem fi = ms.getFloorItem();
			if ( fi != null ) {
				ms.setFloorItem(null);
				MessageManager.instance().addCenteredMessage(
						"Picked up a " + fi.getItem().toString() );
				hero.addItemToInventory(fi.getItem());
			}
		}		
	}
	
	private class Target {
		public Zombie zombie;
		public int targetKey;
		public Path path;
		public boolean hasLOS = false;
	}

	public TurnRegulator getTurnRegulator() {
		return turnRegulator;
	}

	public void setTurnRegulator(TurnRegulator turnRegulator) {
		this.turnRegulator = turnRegulator;
	}

	public ZombieController getZombieController() {
		return zombieController;
	}

	public void setZombieController(ZombieController zombieController) {
		this.zombieController = zombieController;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
		// reset the health bar
		healthBar.setPctComplete(1);
	}
	
	public ControlScheme getControlScheme() {
		return controlScheme;
	}
	
}
