package com.wwflgames.za.item;

import org.newdawn.slick.util.Log;

import com.wwflgames.za.mob.Hero;
import com.wwflgames.za.mob.Zombie;
import com.wwflgames.za.mob.attribute.Attribute;
import com.wwflgames.za.mob.attribute.Stat;

public class Weapon extends Item {

	// single instances of all weapons
	public static final Weapon KNIFE = new Knife();
	public static final Weapon BAT = new Bat();
	public static final Weapon PISTOL = new Pistol();
	public static final Weapon RIFLE = new Rifle();
	public static final Weapon SHOTGUN = new Shotgun();
	public static final Weapon FLAMETHROWER = new Flamethrower();
	
	public static final Weapon[] ALL_WEAPONS = {
		KNIFE,
		BAT,
		PISTOL,
		RIFLE,
		SHOTGUN,
		FLAMETHROWER
	};
	
	public static final Weapon[] AMMOD_WEAPONS = {
		PISTOL,
		RIFLE,
		SHOTGUN,
		FLAMETHROWER
	};
	
	
	private int baseDamage;
	private boolean usesAmmo;
	private Stat baseDmgModifierStat;
	
	public Weapon(String name, boolean equippable, boolean usable ,
			int baseDamage , boolean usesAmmo , Stat baseDmgModifierStat ) {
		super(name, equippable, usable );
		this.baseDamage = baseDamage;
		this.usesAmmo = usesAmmo;
		this.baseDmgModifierStat = baseDmgModifierStat;
	}

	public int getBaseDamage() {
		return baseDamage;
	}

	public void setBaseDamage(int baseDamage) {
		this.baseDamage = baseDamage;
	}

	public boolean usesAmmo() {
		return usesAmmo;
	}

	public void doDamage( Hero hero , Zombie zombie ) {
		// get the player's skill level
		int modifier = hero.getPlayer().getStatValue(baseDmgModifierStat);
		int dmg = getBaseDamage() + modifier;
		Log.debug("dmg for " + name + " + is " + dmg );
		zombie.doDamage( dmg );
	}
	
	
}
