package com.wwflgames.za.item;

import com.wwflgames.za.mob.Hero;
import com.wwflgames.za.mob.Zombie;
import com.wwflgames.za.mob.attribute.Stat;
import com.wwflgames.za.ui.MessageManager;
import com.wwflgames.za.util.Dice;

public class Bat extends Weapon {

	public Bat() {
		super("Bat", true, false, 2 , false , Stat.BAT_SKILL );
	}
	
	@Override
	public void doDamage(Hero hero, Zombie zombie) {
		// get the headsmash chance
		int headshotChance = hero.getPlayer().getStatValue(
				Stat.BAT_HEAD_SMASH_CHANCE);
		
		if ( Dice.d(100) > 100 - headshotChance ) {
			zombie.doDamage(zombie.getCurrentHp());
			MessageManager.instance().addCenteredMessage("HEAD SMASH! KA-KOW!");
		} else {
			super.doDamage(hero, zombie);
		}
	}
}
