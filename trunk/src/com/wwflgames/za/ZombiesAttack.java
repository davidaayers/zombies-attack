package com.wwflgames.za;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.wwflgames.za.game.GameController;
import com.wwflgames.za.gamestate.GamePlayState;
import com.wwflgames.za.gamestate.MenuState;
import com.wwflgames.za.gamestate.TitleState;

public class ZombiesAttack extends StateBasedGame {

	public static boolean DEBUG = false;
	private static AppGameContainer container = null;
	
	public ZombiesAttack() throws SlickException {
		super("When Zombies Attack!");
	}

	public static GameContainer getGameContainer() {
		return container;
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new TitleState());
		addState(new MenuState());
		addState(new GamePlayState());
	}
	
	public static void main(String[] argv) {
		try {
			container = new AppGameContainer(new ZombiesAttack());
			container.setDisplayMode(800,600,false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}