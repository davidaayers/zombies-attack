package com.wwflgames.za.item;

import com.wwflgames.za.mob.Hero;
import com.wwflgames.za.mob.Zombie;
import com.wwflgames.za.mob.attribute.Stat;

public class RangedWeapon extends Weapon {

	int range;
	int rangedDamage;
	
	public RangedWeapon(String name, boolean equippable, boolean usable,
			int baseDamage, boolean usesAmmo , int range , int rangedDamage ,
			Stat baseDmgModifierStat ) {
		super(name, equippable, usable, baseDamage, usesAmmo , 
				baseDmgModifierStat);
		this.range = range;
		this.rangedDamage = rangedDamage;
	}
	
	public int getRange() {
		return range;
	}
	
	public int getRangedDamage() {
		return rangedDamage;
	}
	
	public void doRangedDamage( Hero hero , Zombie zombie ) {
		zombie.doDamage(rangedDamage);
	}
	
}
