package com.wwflgames.za.mob;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.wwflgames.za.game.TurnRegulator;
import com.wwflgames.za.map.FloorMap;

public class BasicZombie extends Zombie {

	public BasicZombie(GameContainer container, StateBasedGame game,
			ZombieController controller, FloorMap currentMap,
			TurnRegulator turnRegulator, Hero hero, 
			MobSpriteSheet spriteSheet , int maxHp , int attackPower) {
		super(container, game, controller, currentMap, turnRegulator, hero,
				spriteSheet, maxHp , attackPower);
	}

}
