package com.wwflgames.za.gamestate;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.wwflgames.za.game.GameController;

public class GameWonState extends BasicGameState {

	public final static int STATE_ID = 3;
	
	Image wonScreen;
	
	@Override
	public int getID() {
		return STATE_ID;
	}

	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		wonScreen = new Image("winning-screen.png");
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.drawImage(wonScreen, 100, 100 );
	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
	}

	@Override
	public void keyPressed(int key, char c) {
		GameController.instance().showGameMenu();
	}
	
}
