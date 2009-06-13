package com.wwflgames.za.mob.attribute;

public enum Stat {
	MAX_HEALTH(20),
	BANDAGING_SKILL(1),
	KNIFE_SKILL(0),
	BAT_SKILL(0),
	BAT_HEAD_SMASH_CHANCE(10),
	PISTOL_SKILL(0),
	PISTOL_HEADSHOT_CHANCE(20),
	RIFLE_SKILL(0),
	RIFLE_HEADSHOT_CHANCE(20),
	SHOTGUN_SKILL(0),
	FLAMETHROWER_SKILL(0),
	SPEED(0), 
	HEALTH_REGEN(0);
	;
	
	private int initialValue;
	Stat(int initialValue) {
		this.initialValue = initialValue;
	}
	
	public int getInitialValue() {
		return initialValue;
	}
}