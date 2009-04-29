package com.wwflgames.za.mob;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.wwflgames.za.game.TurnRegulator;
import com.wwflgames.za.map.FloorMap;

public class WeakZombie extends Zombie {

	public WeakZombie(GameContainer container, StateBasedGame game,
			ZombieController controller, FloorMap currentMap,
			TurnRegulator turnRegulator, Hero hero, 
			MobSpriteSheet spriteSheet , int maxHp) {
		super(container, game, controller, currentMap, turnRegulator, hero,
				spriteSheet, maxHp);
	}

}
