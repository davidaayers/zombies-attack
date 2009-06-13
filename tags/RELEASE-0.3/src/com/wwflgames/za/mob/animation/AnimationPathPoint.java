package com.wwflgames.za.mob.animation;


public class AnimationPathPoint {

	float x;
	float y;
	boolean doSpecial;
	AnimationPathPointSpecial special;
	
	
	public AnimationPathPoint(float x , float y ) {
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public boolean isDoSpecial() {
		return doSpecial;
	}

	public void setDoSpecial(boolean doSpecial) {
		this.doSpecial = doSpecial;
	}

	public AnimationPathPointSpecial getSpecial() {
		return special;
	}

	public void setSpecial(AnimationPathPointSpecial special) {
		this.special = special;
	}
	
	public void doSpecial() {
		if ( special != null ) {
			special.doSpecial();
		}
	}
	
	public interface AnimationPathPointSpecial {
		public void doSpecial();
	}
	
	public String toString() {
		return "Path point [" + x + "," + y + "]";
	}
	
}
