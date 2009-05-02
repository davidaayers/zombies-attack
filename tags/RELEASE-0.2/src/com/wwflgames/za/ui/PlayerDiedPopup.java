package com.wwflgames.za.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.wwflgames.za.game.GameController;
import com.wwflgames.za.gamestate.AbstractPopupSupportState;
import com.wwflgames.za.gamestate.GamePlayState;

public class PlayerDiedPopup extends AbstractPopup {

	GamePlayState gamePlayState;
	
	public PlayerDiedPopup(GamePlayState state) {
		super(state, 600, 400);
		gamePlayState = state;
	}

	@Override
	public void doRender(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		g.setFont(GameController.GAME_FONT);

		// render the stats section first
		float y = getTopY() + 5;
		float centerX = getCenterX();
		
		g.setColor(Color.red);
		centeredText("YOU DIED!", g, y);
		y+=20;
		centeredText("GAME OVER", g, y);
		y+=40;
		centeredText("Press any key to return to main menu.", g , y );
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(int key, char c) {
		gamePlayState.returnToGameMenu();
	}
}
