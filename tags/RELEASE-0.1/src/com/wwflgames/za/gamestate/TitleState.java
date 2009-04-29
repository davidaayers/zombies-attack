package com.wwflgames.za.gamestate;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.wwflgames.za.game.GameController;

public class TitleState extends BasicGameState{
	
	public final static int STATE_ID = 0;
	
	GameContainer container = null;
	StateBasedGame game = null;
	Image wwflLogo = null;
	int counter = 0;
	
	@Override
	public int getID() {
		return STATE_ID;
	}

	public void init(GameContainer container, StateBasedGame game) 
		throws SlickException {
		this.container = container;
		this.game = game;
		wwflLogo = new Image("wwflgames.gif");

		// set up the game controller.
		//SMELLS: I don't like this here, but it has to be in an init method
		GameController.instance().init(container,game);

	}

	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.setColor(Color.white);
		
		int y = 200;
		g.drawImage(wwflLogo, (container.getWidth() - wwflLogo.getWidth())/2,y );

	}

	private void centerText( Graphics g , String text , int y ) {
		int x = (container.getWidth() - g.getFont().getWidth(text))/2;
		g.drawString(text, x, y);
	}
	
	public void update(GameContainer container, StateBasedGame game, int delta) 
		throws SlickException {
		counter += delta;
		
		if ( counter > 2000 ) {
			nextScreen();
		}
	}
	
	private void nextScreen() {
		GameController.instance().showGameMenu();
	}

	@Override
	public void keyPressed(int key, char c) {
		nextScreen();
	}
	
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		nextScreen();
	}
	
}
