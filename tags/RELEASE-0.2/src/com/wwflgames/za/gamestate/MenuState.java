package com.wwflgames.za.gamestate;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.wwflgames.za.game.GameController;
import com.wwflgames.za.ui.PlayerCreatedPopup;

public class MenuState extends AbstractPopupSupportState {
	
	public final static int STATE_ID = 1;
	
	GameContainer container = null;
	StateBasedGame game = null;
	
	MouseOverArea easy;
	MouseOverArea med;
	MouseOverArea hard;
	MouseOverArea exit;
	
	MouseOverArea areas[];
	
	Image title;
	
	@Override
	public int getID() {
		return STATE_ID;
	}

	@Override
	public void doInit(GameContainer container, StateBasedGame game) throws SlickException {
		this.container = container;
		this.game = game;
		
		title = new Image("wza_title.png");
		
		// load all of the images and create the mouse over areas
		Image easyImg = new Image("easy_game_menu.png");
		Image medImg = new Image("med_game_menu.png");
		Image hardImg = new Image("hard_game_menu.png");
		Image exitImg = new Image("exit_game_menu.png");
		Image easyImgOver = new Image("easy_game_menu_over.png");
		Image medImgOver = new Image("med_game_menu_over.png");
		Image hardImgOver = new Image("hard_game_menu_over.png");
		Image exitImgOver = new Image("exit_game_menu_over.png");
		
		ComponentListener listener = new ComponentListener() {
			public void componentActivated(AbstractComponent source) {
				menuActivated(source);
			}
		};
		
		int y = 200;
		easy = new MouseOverArea(container, easyImg, 
				getCenteredX(easyImg), y, listener);
		easy.setMouseOverImage(easyImgOver);
		setMouseOverDefaults(easy);

		y+= 70;
		med = new MouseOverArea(container, medImg, 
				getCenteredX(medImg), y, listener);
		med.setMouseOverImage(medImgOver);
		setMouseOverDefaults(med);

		y+= 70;
		hard = new MouseOverArea(container, hardImg, 
				getCenteredX(hardImg), y, listener);
		hard.setMouseOverImage(hardImgOver);
		setMouseOverDefaults(hard);

		y+= 70;
		exit = new MouseOverArea(container, exitImg, 
				getCenteredX(exitImg), y, listener);
		exit.setMouseOverImage(exitImgOver);
		setMouseOverDefaults(exit);

		areas = new MouseOverArea[] {
				easy,med,hard,exit
		};
		
	}
	
	private void setMouseOverDefaults(MouseOverArea area) {
		area.setMouseOverColor(Color.yellow);
		area.setMouseDownColor(Color.red);
	}

	protected void menuActivated(AbstractComponent source) {
		if ( source == exit ) {
			System.exit(0);
		}
		
		if ( source == easy ) {
			initNewGame(4);
		} else if ( source == med ) {
			initNewGame(8);
		} else if ( source == hard ) {
			initNewGame(12);
		}
	}

	private void initNewGame(int floors) {
		
		// turn off all of the mouse over areas
		acceptingInputForMenu(false);
		
		GameController.instance().initNewGame(floors);
		showPopup(new PlayerCreatedPopup(this));
	}
	
	public void startGame() {
		// reset our state
		acceptingInputForMenu(true);
		
		GameController.instance().startLevel();
	}
	
	private void acceptingInputForMenu(boolean flag) {
		// turn off all of the mouse over areas
		for ( MouseOverArea area : areas ) {
			area.setAcceptingInput(flag);
		}
	}
	
	public void doRender(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		
		g.drawImage(title, getCenteredX(title),100);
		
		easy.render(container, g);
		med.render(container, g);
		hard.render(container, g);
		exit.render(container, g);
	}

	private int getCenteredX(Image img) {
		return (container.getWidth() - img.getWidth()) /2;
	}
	
	private void centerText( Graphics g , String text , int y ) {
		int x = (container.getWidth() - g.getFont().getWidth(text))/2;
		g.drawString(text, x, y);
	}
	
	public void doUpdate(GameContainer container, StateBasedGame game, int delta) 
		throws SlickException {

	}

	@Override
	public void doKeyPressed(int key, char c) {
	}
}
