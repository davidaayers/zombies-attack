package com.wwflgames.za.item;

import com.wwflgames.za.mob.Hero;
import com.wwflgames.za.mob.Zombie;
import com.wwflgames.za.mob.attribute.Stat;
import com.wwflgames.za.ui.MessageManager;
import com.wwflgames.za.util.Dice;

public class Pistol extends RangedWeapon {

	public Pistol() {
		super("Pistol", true, false, 1 , true , 3 , 2 , Stat.PISTOL_SKILL );
	}
	
	@Override
	public void doRangedDamage(Hero hero, Zombie zombie) {
		// get the headshot chance
		int headshotChance = hero.getPlayer().getStatValue(
				Stat.PISTOL_HEADSHOT_CHANCE);
		
		if ( Dice.d(100) > 100 - headshotChance ) {
			// HEADSHOT! BOOM!
			zombie.doDamage(zombie.getCurrentHp());
			MessageManager.instance().addCenteredMessage("HEADSHOT! BOOM!");
		} else {
			super.doRangedDamage(hero, zombie);
		}
	}
	
}
