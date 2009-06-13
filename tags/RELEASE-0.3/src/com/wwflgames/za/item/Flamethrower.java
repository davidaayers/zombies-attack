package com.wwflgames.za.item;

import com.wwflgames.za.mob.attribute.Stat;

public class Flamethrower extends RangedWeapon {

	public Flamethrower() {
		super("Flamethrower", true, false, 2 , true , 3 , 4 , 
				Stat.FLAMETHROWER_SKILL );
	}
}
