package com.wwflgames.za.mob;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.util.Log;

import com.wwflgames.za.game.GameController;
import com.wwflgames.za.game.TurnRegulator;
import com.wwflgames.za.map.FloorMap;
import com.wwflgames.za.map.MapChangeListener;
import com.wwflgames.za.slick.SlickEntity;

public class ZombieController extends SlickEntity 
	implements MapChangeListener {

	List<Zombie> zombies = new ArrayList<Zombie>();
	List<Zombie> zombiesToRemove = new ArrayList<Zombie>();
	List<Zombie> zombiesToAdd = new ArrayList<Zombie>();
	
	private TurnRegulator turnRegulator;
	private FloorMap currentMap;
	List<Zombie> zombiesTakingTheirTurn = new ArrayList<Zombie>();
	private Hero hero;
	
	public ZombieController() {
	}
	
	@Override
	public void render(Graphics g) throws SlickException {
		for ( Zombie zombie : zombies ) {
			zombie.render(g);
		}
	}
	
	@Override
	public void update(int delta) throws SlickException {
		
		// remove any zombies that need to be removed
		zombies.removeAll(zombiesToRemove);
		zombiesToRemove.clear();
		
		zombies.addAll(zombiesToAdd);
		zombiesToAdd.clear();
		
		for ( Zombie zombie : zombies ) {
			zombie.update(delta);
		}
	}
	
	public void addZombieAt(int x , int y ) throws SlickException {
		Zombie z = GameController.instance().getZombieFactory().createZombie();
		z.moveTo(x, y);
		zombiesToAdd.add(z);
	}
	
	public void removeZombie(Zombie zombie) {
		//zombiesToRemove.add(zombie);
		zombies.remove(zombie);
		currentMap.getMapSquare(zombie.getMobx(), zombie.getMoby()).
			setMobile(null);
	}
	
	public Zombie getZombieAt( int x , int y ) {
		for ( Zombie zombie : zombies ) {
			if ( zombie.getMobx() == x && zombie.getMoby() == y ) {
				return zombie;
			}
		}
		return null;
	}
	
	public List<Zombie> intersectingZombies(Shape shape) {
		ArrayList<Zombie> intersecting = new ArrayList<Zombie>();
		for ( Zombie zombie : zombies ) {			
			if ( shape.contains(zombie.getRenderX(), zombie.getRenderY())) {
				intersecting.add(zombie);
			} else if ( shape.contains(zombie.getRenderX() + 24, zombie.getRenderY() + 32)) {
				intersecting.add(zombie);
			}
		}
		return intersecting;
	}
	
	public void takeZombieTurn(int turn) {
		
		// if the player has killed all the zombies, end the zombie turn;
		if ( zombies.isEmpty() ) {
			turnRegulator.endZombieTurn();
		}
		
		zombiesTakingTheirTurn.addAll(zombies);
		Log.debug("zombies taking their turn = " + zombiesTakingTheirTurn);
		for ( Zombie zombie : zombies ) {
			zombie.takeTurn(turn);
		}
	}
	
	public void zombieDoneWithTurn(Zombie z) {
		
		zombiesTakingTheirTurn.remove(z);
		
		if ( zombiesTakingTheirTurn.isEmpty() ) {
			turnRegulator.endZombieTurn();
		}
	}

	public void mapChanged(FloorMap newMap) {
		
		// clear out all the old zombies
		zombies.clear();
		zombiesToAdd.clear();
		zombiesToRemove.clear();
		zombiesTakingTheirTurn.clear();
		
		currentMap = newMap;
		// go through the current map and spawn
		// zombies wherever it says
		for (int y = 0; y <= currentMap.getHeight(); y++) {
			for (int x = currentMap.getWidth(); x >= 0; x--) {
				if ( currentMap.getMapSquare(x, y).isSpawnZombieHere() ) {
					//TODO: fix this to get rid of slick exception
					try {
						addZombieAt(x, y);
					} catch (SlickException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public TurnRegulator getTurnRegulator() {
		return turnRegulator;
	}

	public void setTurnRegulator(TurnRegulator turnRegulator) {
		this.turnRegulator = turnRegulator;
	}

	public Hero getHero() {
		return hero;
	}

	public void setHero(Hero hero) {
		this.hero = hero;
	}
}
