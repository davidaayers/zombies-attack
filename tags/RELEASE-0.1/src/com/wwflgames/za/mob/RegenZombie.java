package com.wwflgames.za.mob;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.wwflgames.za.game.TurnRegulator;
import com.wwflgames.za.map.FloorMap;
import com.wwflgames.za.ui.MessageManager;
import com.wwflgames.za.util.Dice;

public class RegenZombie extends Zombie {

	int regenChance = 30;
	
	public RegenZombie(GameContainer container, StateBasedGame game,
			ZombieController controller, FloorMap currentMap,
			TurnRegulator turnRegulator, Hero hero, MobSpriteSheet spriteSheet,
			int maxHealth , int regenChance ) {
		super(container, game, controller, currentMap, turnRegulator, hero,
				spriteSheet, maxHealth);
		this.regenChance = regenChance;
	}

	@Override
	public void takeTurn(int turn) {
		super.takeTurn(turn);

		// maybe regen some health
		if ( Dice.d(100) > regenChance && currentHp < maxHp) {
			currentHp++;
			MessageManager.instance().addFloatingMessage("+1 HP", 
					this.getRenderX(), this.getRenderY(), Color.white);
		}
	
	}
	
}
