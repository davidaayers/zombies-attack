package com.wwflgames.za.ui;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.wwflgames.za.game.GameController;
import com.wwflgames.za.game.Player;
import com.wwflgames.za.gamestate.AbstractPopupSupportState;
import com.wwflgames.za.gamestate.MenuState;
import com.wwflgames.za.item.Ammo;
import com.wwflgames.za.item.Bandage;
import com.wwflgames.za.item.Weapon;
import com.wwflgames.za.mob.attribute.Attribute;

public class PlayerCreatedPopup extends AbstractPopup {

	private static final int HEIGHT = 300;
	private static final int WIDTH = 400;
	
	MenuState menuState;
	
	public PlayerCreatedPopup(MenuState state) {
		super(state, WIDTH, HEIGHT);
		menuState = state;
	}

	@Override
	public void keyPressed(int key, char c) {
		state.clearPopup();
		menuState.startGame();
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doRender(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		// grab the player
		Player player = GameController.instance().getPlayer();
		int maxFloors = GameController.instance().getMaxFloors();

		float y = getTopY() + 10;
		
		g.setColor(Color.blue);
		centeredText("Your hero has been created!", g, y);
		y+=30;
		
		g.setColor(Color.darkGray);
		leftAlignedText("Your starting attributes:", g, y);
		y+=20;
		
		// list the current attributes from the hero
		List<Attribute> attributes = 
			GameController.instance().getPlayer().getAttributes();
		
		for ( Attribute attr : attributes ) {
			float x = getLeftX() + 5;
			String attrName = attr.getName();
			int width = g.getFont().getWidth(attrName);
			g.setColor(Color.blue);
			g.drawString(attrName, x, y);
			g.setColor(Color.gray);
			g.drawString("("+attr.getShortDesc() +")", x+width+4, y);
			y+=20;
		}
		
		y+=10;
		
		// list the starting weapon
		Weapon w = player.getEquippedWeapon();
		Ammo a = player.getAmmoFor(w);
		
		g.setColor(Color.darkGray);
		leftAlignedText("Starting weapon:",g,y);
		y+=20;
		g.setColor(Color.blue);
		if ( w != null ) {
			String text = w.getName();
			if ( a != null ) {
				text += "(" + a.getQuantity() + " ammo)";
			}
			leftAlignedText(text,g,y);
		} else {
			leftAlignedText("None",g,y);
		}
		
		y+=30;
		Bandage b = (Bandage)player.getItemByName(Bandage.ITEM_NAME);
		leftAlignedText(b.getQuantity() + " bandages",g,y);
		
		g.setColor(Color.blue);
		y = getBottomY() - 30;
		centeredText("Press any key to start",g,y);
	}

}
