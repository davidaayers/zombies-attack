package com.wwflgames.za.ui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public interface Popup {

	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException;
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException;
	public void keyPressed(int key, char c);
	public void aboutToShow();
	public void mouseClicked(int button, int x, int y, int clickCount);
	
}
