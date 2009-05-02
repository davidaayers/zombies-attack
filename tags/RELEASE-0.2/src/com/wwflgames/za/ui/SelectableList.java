package com.wwflgames.za.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

public class SelectableList {
	
	private static final int ENTRY_HEIGHT = 40;
	
	// used to keep track of state -- if the user has pressed enter
	// once they are in confirming choice mode
	private boolean confirmingChoice = false;
	
	// this listener is notified when the item is chosen
	private ItemSelectedListener listener = null;
	
	// the list of selectableItems that we allow the user
	// to scroll through
	private SelectableItem[] list = null;
	
	// other state information
	private Color boxColor = null;
	private Color textColor = null;
	private Color textColor2 = null;
	private int selIndex = 0;
	private int selOffset = 0;
	private float alpha = .5f;
	private float alphaAdj = -.0035f;
	private int maxAttrShown = 8;
	private int max = 0;
	private float x = 0;
	private float y = 0;
	private int width = 0;

	private boolean visible = true;
	
	public SelectableList(int pMaxShown,float px, float py,int pWidth,
			Color pBoxColor, Color pTextColor, Color pTextColor2 , 
			ItemSelectedListener pListener) {
		listener = pListener;
		max = pMaxShown;
		maxAttrShown = pMaxShown;
		x = px;
		y = py;
		width = pWidth;
		// color of the things 
		textColor = new Color(pTextColor.r,pTextColor.g,pTextColor.b,
				pTextColor.a);
		textColor2 = new Color(pTextColor2.r,pTextColor2.g,pTextColor2.b,
				pTextColor2.a);
		boxColor = new Color(pBoxColor.r,pBoxColor.g,pBoxColor.b,alpha);
	}	
	
	public void setList( SelectableItem[] pList ){
		list = pList;
		// select the first element in the list
		if ( list != null && list.length != 0 ) {
			list[0].selected = true;
		}
		maxAttrShown = list.length - 1 < max ? list.length-1 : max; 
	}
	
	public void hide() {
		visible = false;
	}
	
	public void show() {
		visible = true;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public boolean isConfirming() {
		return confirmingChoice;
	}
	
	public void clearConfirmating() {
		confirmingChoice = false;
	}
	
	public void render(Graphics g ) throws SlickException {
		
		if ( !visible ) {
			return;
		}
		
		float ay = y;
		Color tmpTextColor = textColor;
		Color tmpTextColor2 = textColor2;
		for ( int cnt = 0 ; cnt < maxAttrShown+1 ; cnt ++ ) {
			SelectableItem sel = list[cnt+selOffset];
			// if an item is selected, draw all of the other items 50% faded
			if ( confirmingChoice ) {
				if ( cnt+selOffset == selIndex ) {
					// full brightness
					tmpTextColor.a = 1f;
					tmpTextColor2.a = 1f;
				} else {
					// 1/2 brightness
					tmpTextColor.a = .5f;
					tmpTextColor2.a = .5f;
				}
			} else {
				// full brightness
				tmpTextColor.a = 1f;
				tmpTextColor2.a = 1f;
			}
			g.setColor(tmpTextColor);
			g.drawString(sel.displayStr, x+5, ay );
			g.setColor(tmpTextColor2);
			if ( sel.displayStr2 != null ) {
				g.drawString(sel.displayStr2, x+5, ay + 20 );
			}
			if ( sel.selected ) {
				boxColor.a = alpha;
				g.setColor(boxColor);
				g.drawRect(x, ay, width, ENTRY_HEIGHT - 2 );
			}
			ay+=ENTRY_HEIGHT;
		}	
	}
	
	public void update(int delta) throws SlickException {
		
		if ( !visible ) {
			return;
		}
		
		// adjust the alpha, which makes the box around
		// the highlighted item "glow"
		alpha += ( alphaAdj * delta );

		if ( alpha > 1 ) {
			alpha = 1f;
			alphaAdj *= -1;
		}
		else if ( alpha < .25f ) {
			alpha = .25f;
			alphaAdj *= -1;
		}		
	}
	
	public void keyPressed(int key, char c) {
		
		if ( !visible ) {
			return;
		}
		
		if ( !confirmingChoice ) {
			if ( key == Input.KEY_DOWN ) {
				selMoveDown();
			}
			if ( key == Input.KEY_UP ) {
				selMoveUp();
			}
			if ( key == Input.KEY_ENTER ) {
				confirmingChoice = true;
				listener.itemSelected(list[selIndex].item);
			}
		} else {
			// enter now means we're choosing that attribute
			if ( key == Input.KEY_ENTER ) {
				confirmSelection();
			}
			if ( key == Input.KEY_ESCAPE ) {
				confirmingChoice = false;
			}
		}		
	}
	
	private void confirmSelection() {
		listener.selectionConfirmed(list[selIndex].item);
		
		// reset some stuff
		confirmingChoice = false;
		selIndex = 0;
		selOffset = 0;
		
		// clear all the selections
		for ( int cnt = 0 ; cnt < list.length ; cnt ++ ) {
			list[cnt].selected = false;
		}
		
		// select the first item in the list
		list[0].selected = true;
		
	}
	
	
	public void selMoveDown() {
		if ( selIndex == list.length -1  ) {
			return;
		}
		
		list[selIndex].selected=false;
		selIndex++;
		list[selIndex].selected=true;
		
		// the max value for selOffset is ListSize - maxshown - 1
		// so, a list of 8, the max should be 3
		int max = list.length - maxAttrShown - 1;
		
		if ( selIndex > maxAttrShown ) {
			selOffset++;
			if ( selOffset > max ) {
				selOffset = max;
			}
		}
	}
	
	public void selMoveUp() {
		if ( selIndex == 0 ) {
			return;
		}
		list[selIndex].selected=false;
		selIndex--;
		list[selIndex].selected=true;
		
		if ( selIndex < selOffset ) {
			selOffset --;
			if ( selOffset < 0 ) {
				selOffset = 0;
			}
		}
	}
	
	public SelectableItem createSelectableItem(Object pObject , 
			String pDisplayStr){
		return new SelectableItem(pObject,pDisplayStr);
	}
	
	public SelectableItem createSelectableItem(Object pObject , 
			String pDisplayStr , String pDisplayStr2 ){
		return new SelectableItem(pObject,pDisplayStr, pDisplayStr2);
	}
	
	public class SelectableItem {
		public boolean selected = false;
		public Object item = null;
		public String displayStr = null;
		public String displayStr2 = null;
		
		public SelectableItem(Object pItem,String pDisplayStr) {
			this(pItem,pDisplayStr,null);
		}
		
		public SelectableItem(Object pItem,String pDisplayStr, String pDisplayStr2) {
			item = pItem;
			displayStr = pDisplayStr;
			displayStr2 = pDisplayStr2;
		}
		
		public String toString() {
			return displayStr;
		}
	}
	
	public interface ItemSelectedListener {
		public void itemSelected(Object o);
		public void selectionConfirmed(Object o);
	}
}
