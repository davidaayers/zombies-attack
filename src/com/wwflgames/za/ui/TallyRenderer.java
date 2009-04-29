package com.wwflgames.za.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.wwflgames.za.game.TallyTracker;
import com.wwflgames.za.game.TallyTracker.Tally;

public class TallyRenderer {

	public static float render(Graphics g, float y , float x1 , float x2, boolean currentLevel ) 
		throws SlickException {
		TallyTracker tt = TallyTracker.instance();
		
		g.setColor(Color.darkGray);
		y+= 20;
		g.drawString("Zombies Killed: " + 
				tt.getTally(Tally.ZOMBIES_KILLED,currentLevel), x1, y );
		g.drawString("Ammo used:" +
				tt.getTally(Tally.AMMO_USED,currentLevel), x2, y );
		y+= 20;
		g.drawString("Health Lost: " +
				tt.getTally(Tally.HEALTH_LOST,currentLevel), x1 , y );
		g.drawString("Bandages Used: " +
				tt.getTally(Tally.BANDAGES_USED,currentLevel), x2 , y );
		y+= 20;
		g.drawString("Turns Taken: " +
				tt.getTally(Tally.TURNS_TAKEN,currentLevel), x1 , y );
		
		return y;		
	}
	
	/**
	 * Returns the y position after all the stats have been rendered
	 */
	public static float render(Graphics g, float y , float x1 , float x2) 
		throws SlickException {
		return render(g,y,x1,x2,true);
	}
	
}
