package com.wwflgames.za.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.wwflgames.za.gamestate.AbstractPopupSupportState;

/**
 * An implementation of the popup that provides basic functionality -- basically
 * it will render the overlay rectangle on the screen for you.
 * 
 * @author David
 *
 */
public abstract class AbstractPopup implements Popup {

	protected AbstractPopupSupportState state = null;
	protected int popupWidth = 0;
	protected int popupHeight = 0;
	protected Color bgColor = null;
	protected int topx = 0;
	protected int topy = 0;
	protected int bottomx = 0;
	protected int bottomy = 0;
	
	public AbstractPopup( AbstractPopupSupportState pState , int pWidth , 
			int pHeight ) {
		this ( pState, pWidth , pHeight , Color.white );
	}
	
	public AbstractPopup( AbstractPopupSupportState pState , int pWidth , 
			int pHeight , Color pBgColor ) {
		state = pState;
		popupWidth = pWidth;
		popupHeight = pHeight;
		bgColor = pBgColor;
	}

	// empty implemention, in case the popup doesn't want to deal with
	// mouse events
	public void mouseClicked(int button, int x, int y, int clickCount) {
	}
	
	// empty implementation, in case the popup doesn't want to deal with
	// key presses
	public void keyPressed(int key, char c) {
	}
	
	public abstract void update(GameContainer container, StateBasedGame game, 
			int delta) throws SlickException;
	
	public void render(GameContainer container, StateBasedGame game, 
			Graphics g)
			throws SlickException {

		beforeRender(container,game,g);
		
		// create a large popup
		g.setColor(bgColor);
		topx = container.getWidth()/2 - popupWidth/2;
		topy = container.getHeight()/2 - popupHeight/2;
		g.fillRoundRect(topx, topy , popupWidth, popupHeight, 20);		
		
		bottomx = container.getWidth()/2 + popupWidth/2;
		bottomy = container.getHeight()/2 + popupHeight/2;

		doRender(container,game,g);
	}

	public void beforeRender(GameContainer container, 
			StateBasedGame game, Graphics g) throws SlickException {}
	public void doRender(GameContainer container, 
			StateBasedGame game, Graphics g) throws SlickException {}

	public void aboutToShow() {
	}
	
	// methods to write text various ways on the popup
	protected void rightAlignedText(String text , Graphics g , float y ) {
		// add a little padding so it's not right on the edge
		g.drawString(text, getRightX() + 5, y);
	}
	
	protected void leftAlignedText(String text, Graphics g , float y ) {
		g.drawString(text, getLeftX() + 5, y);		
	}
	
	protected void centeredText(String text, Graphics g , float y ) {
		int textWidth = g.getFont().getWidth(text);
		float x = (state.getContainer().getWidth() - textWidth) / 2;
		g.drawString(text, x, y);
	}
	
	protected float getTopY() {
		return state.getContainer().getHeight()/2 - popupHeight/2;
	}
	
	protected float getBottomY() {
		return state.getContainer().getHeight()/2 + popupHeight/2;
	}
	
	protected float getRightX() {
		return state.getContainer().getWidth()/2 + popupWidth/2;
	}
	
	protected float getLeftX() {
		return state.getContainer().getWidth()/2 - popupWidth/2;
	}
	
	protected float getCenterX() {
		return state.getContainer().getWidth() / 2;
	}
	
}
