package com.wwflgames.za.gamestate;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.wwflgames.za.ui.Popup;

public abstract class AbstractPopupSupportState extends BasicGameState {

	protected boolean showPopup = false;
	protected Popup currentPopup = null;
	protected Color defaultOverlay = null;
	protected Color currentOverlay = null;
	protected GameContainer container;
	
	public abstract int getID();

	public final void init(GameContainer container, StateBasedGame game) throws SlickException {
		Color c = Color.black;
		defaultOverlay = new Color(c.r,c.g,c.b,.75f);
		this.container = container;
		doInit(container,game);
	}

	public abstract void doInit(GameContainer container, StateBasedGame game) throws SlickException;
	
	public final void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		
		doRender(container,game,g);
		
		// if we're showing a popup, draw a giant rectangle over the screen with 75% alpha
		// and then draw the popup
		if ( showPopup ) {
			g.setColor(currentOverlay);
			g.fillRect(0, 0, container.getWidth(), container.getHeight());
			currentPopup.render(container, game, g);
		}

		doAfterRender(container,game,g);

	}

	public void doRender(GameContainer container, StateBasedGame game, 
			Graphics g) throws SlickException {}
	public void doAfterRender(GameContainer container, StateBasedGame game, 
			Graphics g) throws SlickException {}
	
	public final void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
		beforeUpdate(container,game,delta);
		
		// if we're showing a popup, dont update anything, just show the popup
		if ( showPopup ) {
			currentPopup.update(container, game, delta);
			return;
		}		

		doUpdate(container,game,delta);

	}
	
	public void beforeUpdate(GameContainer container, StateBasedGame game, 
			int delta) throws SlickException {}
	public void doUpdate(GameContainer container, StateBasedGame game, 
			int delta) throws SlickException {}

	@Override
	public final void keyPressed(int key, char c) {
		// if we're showing a popup, pass keyPresses to the popup
		if ( showPopup ) {
			currentPopup.keyPressed(key,c);
			return;
		}
		doKeyPressed(key,c);
	}

	@Override
	public final void mouseClicked(int button, int x, int y, int clickCount) {
		// if we're showing a popup, pass mouse clicks to the popup
		if ( showPopup ) {
			currentPopup.mouseClicked(button,x,y,clickCount);
			return;
		}
		doMousePressed(button,x,y,clickCount);
	}
	
	
	/** 
	 * Empty implementation, in case the implementing class doens't want
	 * to use mouse events
	 * @param button
	 * @param x
	 * @param y
	 * @param clickCount
	 */
	public void doMousePressed(int button, int x, int y, int clickCount) {
	}

	public abstract void doKeyPressed(int key, char c);
	
	public final void showPopup ( Popup pop ) {
		showPopup(pop,defaultOverlay);
	}

	public final void showPopup ( Popup pop , Color overlayColor ) {
		pop.aboutToShow();
		currentOverlay = overlayColor;
		currentPopup = pop;
		showPopup=true;
	}

	public final void clearPopup() {
		showPopup=false;
		currentPopup = null;
	}
	
	public GameContainer getContainer() {
		return container;
	}
	
}
