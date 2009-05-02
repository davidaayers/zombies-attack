package com.wwflgames.za.mob.animation;

import org.newdawn.slick.geom.Line;

import com.wwflgames.za.map.FloorMap;
import com.wwflgames.za.map.MapUtils;
import com.wwflgames.za.map.ScreenDelta;
import com.wwflgames.za.mob.Mobile;
import com.wwflgames.za.mob.animation.AnimationPathPoint.AnimationPathPointSpecial;

public class AttackAnimation extends Animation {

	public AttackAnimation(Mobile attacker, Mobile victim , FloorMap map) {
		
		// build a path between the attacker and the victim
		float startx = attacker.getRenderX();
		float starty = attacker.getRenderY();
		
		float endx = victim.getRenderX();
		float endy = victim.getRenderY();

		ScreenDelta d = MapUtils.calculateDelta(startx, starty, endx, endy);
		
		Line l = new Line(startx, starty, endx, endy);
		// use line to get the length
		
		// we're going to go 1/2 the distance to the target, and
		// then back again, along the same line, back to the starting
		// spot
		float movex = startx;
		float movey = starty;
		
		float moveLength = (float)(l.length() * .25);
		
		boolean moveAlongLine = true;
		boolean movingForward = true;
		int cnt = 0;
		
		AnimationPathPoint lastPoint = null;
		while ( moveAlongLine ) {
			cnt ++;
			// move one step in the direction
			movex+=d.x;
			movey+=d.y;
			
			// only add in every Nth point, so the animation is quick
			if ( cnt % 6 == 0 ) {
				AnimationPathPoint p = new AnimationPathPoint(movex,movey);
				animationPath.add(p);
				lastPoint = p;
			}
			
			if ( movingForward ) {
				Line checkLine = new Line(movex,movey,endx,endy);
				// have we reached the 1/2 way point? If so, reverse
				// direction
				if (checkLine.length() <= moveLength ) {
					d.x*=-1;
					d.y*=-1;
					movingForward = false;
					// add a special at the reversal point
					if ( lastPoint != null ) {
						lastPoint.setDoSpecial(true);
						lastPoint.setSpecial(new BloodSpecial(victim,map));
					}
				}
			} 
			else {
				
				// have we reached the beginning point again?
				if ( (movex >= startx - .5 && movex <= startx + .5 ) 
						&& ( movey >= starty - .5 && movey <= starty + .5 ) ) {
					moveAlongLine = false;
				}
			}
		}
		
		// delay for each animation frame
		delay = 40;
	}
	
	public class BloodSpecial implements AnimationPathPointSpecial {

		Mobile victim;
		FloorMap map;
		
		public BloodSpecial(Mobile victim , FloorMap map) {
			this.victim = victim;
			this.map = map;
		}
		
		public void doSpecial() {
			map.addBlood(victim.getMobx(), victim.getMoby(), 
					victim.getRenderX()+12, victim.getRenderY()+24, 5);
		}
	}
	
}
