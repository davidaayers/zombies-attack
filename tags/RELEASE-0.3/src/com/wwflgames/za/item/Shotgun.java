package com.wwflgames.za.item;

import com.wwflgames.za.game.GameController;
import com.wwflgames.za.map.Dir;
import com.wwflgames.za.map.FloorMap;
import com.wwflgames.za.map.MapSquare;
import com.wwflgames.za.mob.Hero;
import com.wwflgames.za.mob.Zombie;
import com.wwflgames.za.mob.attribute.Stat;

public class Shotgun extends RangedWeapon {

	public Shotgun() {
		super("Shotgun", true, false, 2 , true , 2 , 4 , Stat.SHOTGUN_SKILL );
	}

	@Override
	public void doRangedDamage(Hero hero, Zombie zombie) {
		super.doRangedDamage(hero, zombie);

		// shotguns do spread damage, so see if there are zombies
		// to the left and right of the targeted zombie

		FloorMap map = GameController.instance().getCurrentMap();
		
		//TODO: this is gross, and not right -- this will kill zombies
		// in a cirle around the target zombie; what it really needs
		// to do is kill zombies to the right and left of the zombie,
		// oriented based on where the player is. Ah well, maybe later
		int cnt = 0;
		for ( Dir d : Dir.values() ) {
			int checkx = zombie.getMobx()+d.getMapDelta().x;
			int checky = zombie.getMoby()+d.getMapDelta().y;
			if ( map.inBounds(checkx, checky)) {
				MapSquare ms = map.getMapSquare(checkx, checky);
				// see if there's a zombie on ms
				if ( ms.getMobile() instanceof Zombie ) {
					Zombie other = (Zombie) ms.getMobile();
					super.doRangedDamage(hero, other);
					cnt ++;
					if ( cnt == 1 ) {
						break;
					}
				}
			}
		}
		
		
	}
	
}
