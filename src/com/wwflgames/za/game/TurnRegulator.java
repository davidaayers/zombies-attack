package com.wwflgames.za.game;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.util.Log;

import com.wwflgames.za.game.TallyTracker.Tally;
import com.wwflgames.za.mob.ZombieController;

//TODO add some kind of indicator on the screen that
// it's the zombie's turn, like an iphone spinny thing
// so the player doesn't think the controls are frozen
public class TurnRegulator {

	int turn = 1;
	private boolean playerTurnInProgress = false;
	private boolean zombieTurnInProgress = false;
	private ZombieController zombieController;
	private List<TurnListener> listeners = new ArrayList<TurnListener>();
	
	public void reset() {
		playerTurnInProgress = false;
		zombieTurnInProgress = false;
	}
	
	public void startPlayerTurn(boolean incrementTurnCounter) {
		playerTurnInProgress = true;
		if ( incrementTurnCounter ) {
			turn++;
			TallyTracker.instance().addTally(Tally.TURNS_TAKEN, 1);
		}
	}

	public void endPlayerTurn(boolean getAnother) {
		
		// see if the player ended their turn in the end location
		if ( GameController.instance().isHeroOnExitLocation() ) {
			GameController.instance().currentLevelCompleted();
			return;
		}
		
		Log.debug("Player turn ended, getAnother = " + getAnother);
		playerTurnInProgress = false;

		if ( !getAnother ) {
			// player's turn has ended, including animations, now
			// let the zombies have a go
			startZombieTurn();
		}

	}
	
	public void startZombieTurn() {
		Log.debug("zombie turn started");
		zombieTurnInProgress = true;
		zombieController.takeZombieTurn(turn);
	}
	
	public void endZombieTurn() {
		Log.debug("zombie turn ended");
		// after the zombies go, notify any turn listeners
		// so they can do their stuff
		notifyTurnListeners();
		zombieTurnInProgress = false;
	}
	
	private void notifyTurnListeners() {
		for ( TurnListener listener : listeners ) {
			listener.turnHappened(turn);
		}	
	}

	public int getTurn() {
		return turn;
	}

	public boolean isPlayerTurnInProgress() {
		return playerTurnInProgress;
	}

	public boolean isZombieTurnInProgress() {
		return zombieTurnInProgress;
	}

	public ZombieController getZombieController() {
		return zombieController;
	}

	public void setZombieController(ZombieController zombieController) {
		this.zombieController = zombieController;
	}

	public void addTurnListener(TurnListener listener) {
		listeners.add(listener);
	}

}
