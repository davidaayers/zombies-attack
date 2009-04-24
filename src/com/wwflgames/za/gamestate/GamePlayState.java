package com.wwflgames.za.gamestate;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.wwflgames.za.game.GameCommand;
import com.wwflgames.za.game.GameCommandListener;
import com.wwflgames.za.game.GameController;
import com.wwflgames.za.ui.LevelCompletePopup;


public class GamePlayState extends AbstractPopupSupportState 
	implements GameCommandListener {

	public final static int STATE_ID = 2;
	
	private GameContainer container = null;
	private StateBasedGame game = null;
	private LevelCompletePopup levelCompletePopup;
	
	@Override
	public int getID() {
		return STATE_ID;
	}

	@Override
	public void doInit(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.container = container;
		this.game = game;
		
		// create the level complete popup
		levelCompletePopup = new LevelCompletePopup(this);

		// add ourselves as a command listener
		GameController.instance().addGameCommandListener(this);
	}

	//TODO: I dont like this, the map renderer and UI should really
	//be part of this class, I think
	@Override
	public void doRender(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {

		// render the map, which also renders everyone on the map
		GameController.instance().getMapRenderer().renderFloor(g);
		
		// render the UI on top of everything else
		GameController.instance().getUi().render(g);
	}
	
	//TODO: I don't like this very much. Need to figure out a better
	//way
	@Override
	public void doUpdate(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {

		GameController.instance().getHero().update(delta);
		GameController.instance().getZombieController().update(delta);
		GameController.instance().getMapRenderer().update(delta);
		GameController.instance().getUi().update(delta);
	}
	
	@Override
	public void doKeyPressed(int key, char c) {
		GameController.instance().getUi().keyPressed(key, c);
	}

	public void performCommand(GameCommand command) {
		if ( GameCommand.SHOW_LEVEL_COMPLETE_POPUP == command ) {
			showPopup(levelCompletePopup);
		}
	}
}