package com.wwflgames.za.slick;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public abstract class SlickEntity {

	protected GameContainer container;
	protected StateBasedGame game;
	
	public SlickEntity() {
	}

	/**
	 * Empty implementation, in case sub-class doesn't need to
	 * use this method
	 * @throws SlickException
	 */
	public void init(GameContainer container, StateBasedGame game) 
		throws SlickException {
		this.container = container;
		this.game = game;
	}

	/**
	 * Empty implementation, in case sub-class doesn't need to
	 * use this method
	 * @throws SlickException
	 */
	public void update(int delta) throws SlickException {
	}
	
	/**
	 * Empty implementation, in case sub-class doesn't need to
	 * use this method
	 * @throws SlickException
	 */
	public void render(Graphics g)throws SlickException {
	}
}
