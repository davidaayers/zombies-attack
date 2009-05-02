package com.wwflgames.za.ui;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.wwflgames.za.game.GameController;
import com.wwflgames.za.game.TallyTracker;
import com.wwflgames.za.game.TallyTracker.Tally;
import com.wwflgames.za.gamestate.AbstractPopupSupportState;
import com.wwflgames.za.mob.Hero;
import com.wwflgames.za.mob.attribute.Attribute;
import com.wwflgames.za.mob.attribute.AttributeFactory;
import com.wwflgames.za.ui.SelectableList.ItemSelectedListener;

public class LevelCompletePopup extends AbstractPopup {

	public static final int WIDTH = 500;
	public static final int HEIGHT = 400;
	private SelectableList selectableList;
	
	public LevelCompletePopup(AbstractPopupSupportState state) {
		super(state, WIDTH, HEIGHT);
		
		float x = getCenterX();
		float y = state.getContainer().getHeight()/2 - popupHeight/2;

		// create a selectable list
		selectableList = new SelectableList(4, x, y+180, 240, Color.green, 
				Color.blue, Color.gray,  new ItemSelectedListener() {
			public void itemSelected(Object o) {
				Log.debug("Attribute " + o + " selected");
			}
			public void selectionConfirmed(Object o) {
				attributeSelected((Attribute)o);
			}
		});
	}

	protected void attributeSelected(Attribute attribute) {
		Log.debug("Attribute " + attribute + " confirmed");
		GameController.instance().getPlayer().addAttribute(attribute);
		state.clearPopup();
		GameController.instance().startNextLevel();
	}

	@Override
	public void keyPressed(int key, char c) {
		if ( selectableList.isVisible() ) {
			selectableList.keyPressed(key, c);
		}
		else {
			state.clearPopup();
		}
	}

	@Override
	public void doRender(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {

		g.setFont(GameController.GAME_FONT);

		// The screen is divided into three sections.  The top shows a recap
		// of the mayhem on the current level (zombies kills, turns elapsed,
		// damage taken, etc). Below that are two different lists, a list of
		// the current attributes the player has, and a list of the new
		// attributes the player can select
		
		// render the stats section first
		float y = getTopY() + 5;
		float centerX = getCenterX();
		
		g.setColor(Color.blue);
		centeredText("YOU MADE IT TO THE STAIRS!", g, y);
		y+= 20;

		float x1 = getLeftX() + 5;
		y = TallyRenderer.render(g, y, x1, centerX);
		y+= 30;
		
		// draw a line to separate this section from the previous
		g.setColor(Color.black);
		g.fillRect(getLeftX(), y, popupWidth, 4);
		
		y+= 10;
		
		g.setColor(Color.blue);
		centeredText("CHOOSE A NEW ATTRIBUTE:", g, y );

		g.setColor(Color.darkGray);
		y+=20;
		leftAlignedText("Current Attributes:", g , y );
		if ( selectableList.isVisible() ) {
			g.drawString("Available Attributes:", centerX , y );

			y+=20;
			String instr = (selectableList.isConfirming() ?
					"Enter to confirm, esc to change" : "Enter to choose" );
			g.setColor(Color.green);
			g.drawString(instr, centerX , y );
		} else {
			g.drawString("No more attributes available", centerX, y);
			y+= 40;
		}
		
		y+=10;
		
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
		
		if ( selectableList.isVisible() ) {
			selectableList.render(g);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		if ( selectableList.isVisible() ) {
			selectableList.update(delta);
		}
	}
	
	@Override
	public void aboutToShow() {
		// grab the hero, and all his current attributes
		Hero hero = GameController.instance().getHero();
		List<Attribute> availableAttr = AttributeFactory.
			getAttributesForHero(hero);
		
		// create the elements in the list
		SelectableList.SelectableItem[] items = 
			new SelectableList.SelectableItem[availableAttr.size()];
		
		if ( items.length == 0 ) {
			selectableList.hide();
			return;
		}
		
		for ( int cnt = 0 ; cnt < availableAttr.size() ; cnt ++ ) {
			Attribute sel = availableAttr.get(cnt);
			items[cnt] = selectableList.createSelectableItem(sel, 
					sel.getName() , sel.getDescription() );
		}
		
		selectableList.setList(items);
		
	}

	public void mouseClicked(int button, int x, int y, int clickCount) {
		// TODO Auto-generated method stub
		
	}
	

}
