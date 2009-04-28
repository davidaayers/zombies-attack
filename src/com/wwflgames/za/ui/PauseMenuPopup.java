package com.wwflgames.za.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.wwflgames.za.gamestate.AbstractPopupSupportState;
import com.wwflgames.za.gamestate.GamePlayState;
import com.wwflgames.za.mob.attribute.Attribute;
import com.wwflgames.za.ui.SelectableList.ItemSelectedListener;

public class PauseMenuPopup extends AbstractPopup {

	private enum MenuChoices {
		RETURN_TO_GAME,
		MAIN_MENU,
		EXIT_GAME
	}
	
	private SelectableList selectableList;
	
	GamePlayState gamePlayState;
	
	public PauseMenuPopup(GamePlayState state) {
		super(state, 400, 300);
		
		gamePlayState = state;
		
		float x = getCenterX() - 120;
		float y = getTopY() + 5;
		
		// create a selectable list
		selectableList = new SelectableList(4, x, y, 240, Color.green, 
				Color.blue, Color.gray,  new ItemSelectedListener() {
			public void itemSelected(Object o) {
				selectableList.clearConfirmating();
				menuChosen((MenuChoices)o);
			}
			public void selectionConfirmed(Object o) {
				menuChosen((MenuChoices)o);
			}
		});
		
		// create the elements in the list
		SelectableList.SelectableItem[] items = 
			new SelectableList.SelectableItem[3];
		
		if ( items.length == 0 ) {
			selectableList.hide();
			return;
		}
		
		items[0] = selectableList.createSelectableItem( 
				MenuChoices.RETURN_TO_GAME , "Return to Game" );
		items[1] = selectableList.createSelectableItem( 
				MenuChoices.MAIN_MENU , "Main Menu" );
		items[2] = selectableList.createSelectableItem( 
				MenuChoices.EXIT_GAME, "Exit Game" );
		
		selectableList.setList(items);
	}

	protected void menuChosen(MenuChoices choice) {
		if ( choice == MenuChoices.RETURN_TO_GAME ) {
			state.clearPopup();
		}
		else if ( choice == MenuChoices.MAIN_MENU ) {
			gamePlayState.returnToGameMenu();
		}
		else if ( choice == MenuChoices.EXIT_GAME ) {
			System.exit(0);
		}
	}

	@Override
	public void doRender(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {

		selectableList.render(g);
	}
	
	@Override
	public void keyPressed(int key, char c) {
		selectableList.keyPressed(key, c);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		selectableList.update(delta);
	}

	
}
