package com.wwflgames.za.mob.animation;

import com.wwflgames.za.map.MapUtils;
import com.wwflgames.za.map.ScreenDelta;

public class MoveAnimation extends Animation {

	public MoveAnimation(float startx,float starty, float endx, float endy) {
		
		ScreenDelta d = MapUtils.calculateDelta(startx, starty, endx, endy);

		boolean reachedEnd = false;
		
		float movex = startx;
		float movey = starty;
		int cnt = 0;
		while ( !reachedEnd ) {
			cnt++;
			// move one step in the direction
			movex+=d.x;
			movey+=d.y;
			
			if ( cnt % 8 == 0 ) {
				AnimationPathPoint p = new AnimationPathPoint(movex,movey);
				animationPath.add(p);
			}
			
			// if we're close to the end, call it good
			if ( Math.abs(endx-movex) < 3 && Math.abs(endy - movey) < 3 ) {
				reachedEnd = true;
			}
			
		}
		
		// delay for each animation frame
		delay = 40;
	}
}
