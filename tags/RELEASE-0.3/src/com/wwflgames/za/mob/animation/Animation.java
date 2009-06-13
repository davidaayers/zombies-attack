package com.wwflgames.za.mob.animation;

import java.util.ArrayList;
import java.util.List;

public class Animation {

	protected List<AnimationPathPoint> animationPath = new ArrayList<AnimationPathPoint>();
	protected int delay;
	int currentCnt = 0;
	
	public Animation() {
	}

	public boolean isDone() {
		return animationPath.isEmpty();
	}
	
	public AnimationPathPoint getNextPathPoint() {
		if ( animationPath.isEmpty() ) {
			return null;
		} else {
			return animationPath.remove(0);
		}
	}
	
	public boolean shouldGetNextPoint(int delta) {
		currentCnt += delta;
		if ( currentCnt > delay ) {
			currentCnt -= delay;
			return true;
		} else {
			return false;
		}
	}

	public List<AnimationPathPoint> getAnimationPath() {
		return animationPath;
	}

	public void setAnimationPath(List<AnimationPathPoint> animationPath) {
		this.animationPath = animationPath;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	
	
	
}
