package com.wwflgames.za.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.wwflgames.za.game.TallyTracker;
import com.wwflgames.za.game.TallyTracker.Tally;

public class TallyRenderer {

	/**
	 * Returns the y position after all the stats have been rendered
	 */
	public static float render(Graphics g, float y , float x1 , float x2) 
		throws SlickException {
		
		
		TallyTracker tt = TallyTracker.instance();
		
		g.setColor(Color.darkGray);
		y+= 20;
		g.drawString("Zombies Killed: " + 
				tt.getCurrentLevelTally(Tally.ZOMBIES_KILLED), x1, y );
		g.drawString("Ammo used:" +
				tt.getCurrentLevelTally(Tally.AMMO_USED), x2, y );
		y+= 20;
		g.drawString("Health Lost: " +
				tt.getCurrentLevelTally(Tally.HEALTH_LOST), x1 , y );
		g.drawString("Bandages Used: " +
				tt.getCurrentLevelTally(Tally.BANDAGES_USED), x2 , y );
		y+= 20;
		g.drawString("Turns Taken: " +
				tt.getCurrentLevelTally(Tally.TURNS_TAKEN), x1 , y );
		g.drawString("Something something: " , x2 , y );
		
		return y;
	}
	
}
