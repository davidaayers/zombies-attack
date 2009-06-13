package com.wwflgames.za.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.util.Log;

import com.wwflgames.za.item.Ammo;
import com.wwflgames.za.item.Item;
import com.wwflgames.za.item.StackableItem;
import com.wwflgames.za.item.Weapon;
import com.wwflgames.za.map.FloorMap;
import com.wwflgames.za.map.MapChangeListener;
import com.wwflgames.za.mob.Hero;
import com.wwflgames.za.mob.attribute.Attribute;
import com.wwflgames.za.mob.attribute.Stat;
import com.wwflgames.za.ui.MessageManager;

/**
 * Represents all of the stats and attributes of the player.  This has been
 * separated from the hero so that it can be easily saved.  The Hero is
 * the player's representation in the game.
 * 
 * @author davidaayers
 *
 */
public class Player implements MapChangeListener {
	
	private List<Item> inventory = new ArrayList<Item>();
	private List<Attribute> attributes = new ArrayList<Attribute>();
	private Weapon equippedWeapon;
	private int unequippedDmg = 1;
	private TurnRegulator turnRegulator;
	private int speedCounter;
	private int regenCounter;
	private Hero hero;
	
	private Map<Stat,Integer> statValues = new HashMap<Stat,Integer>();
	
	public Player() {
		// initialize statValues
		for ( Stat s : Stat.values() ) {
			statValues.put(s, s.getInitialValue());
		}
	}
	
	public Weapon getEquippedWeapon() {
		return equippedWeapon;
	}

	public void setEquippedWeapon(Weapon equippedWeapon) {
		this.equippedWeapon = equippedWeapon;
	}

	public boolean inventoryContains(Weapon weapon) {
		return inventory.contains(weapon);
	}
	
	public void equipWeapon(Weapon weapon) {
		equippedWeapon = weapon;
	}

	public int getUnequippedDmg() {
		return unequippedDmg;
	}

	public void setUnequippedDmg(int unequippedDmg) {
		this.unequippedDmg = unequippedDmg;
	}
	
	public void addItemToInventory(Item item){
		
		// if item is stackable, see if we already
		// have one in inventory and just stack it
		if ( item instanceof StackableItem ) {
			StackableItem sItem = (StackableItem)item;
			for ( Item other : inventory ) {
				if ( other instanceof StackableItem ) {
					StackableItem sOther = (StackableItem)other;
					if ( sOther.getStackingQualifier().equals(
							sItem.getStackingQualifier())) {
						sOther.setQuantity(sOther.getQuantity() + 
								sItem.getQuantity());
						return;
					}
				}
			}
		}

		// if we get here, the item either wasn't stackable, or we
		// didn't already have one in inventory
		inventory.add(item);
	}
	
	public Ammo getAmmoFor(Weapon weapon) {
		for ( Item item : inventory ) {
			if ( item instanceof Ammo ) {
				Ammo a = (Ammo)item;
				if ( a.isForWeapon(weapon) ) {
					return a;
				}
			}
		}
		return null;
	}

	public Item getItemByName(String name) {
		for ( Item item : inventory ) {
			if ( name.equals(item.getName())) {
				return item;
			}
		}
		return null;
	}
	
	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void addAttribute(Attribute attribute) {
		attributes.add(attribute);
		
		// when an attribute is added, the appropriate
		// STAT is adjusted.
		int statVal = statValues.get(attribute.getEffectedStat());
		statVal += attribute.getStatEffectValue();
		statValues.put(attribute.getEffectedStat(), statVal);

		Log.debug("Applied attribute " + attribute.getName() );
		Log.debug("Stat values is now " + statValues );
	}
	
	public int getStatValue(Stat stat) {
		return statValues.get(stat);
	}
	
	public void startPlayerTurn() {
		// increment speed counter.
		speedCounter++;
		regenCounter++;
		turnRegulator.startPlayerTurn(true);
	}
	
	public void endPlayerTurn() {

		// check speed counter to see if we
		// get an extra turn this turn
		// this is based on the speed attribute.
		// 0 speed = no extra turns
		// 1 speed = 1 extra turn every 12 rounds
		// 2 speed = 1 extra turn every 8 rounds
		// 3 speed = 1 extra turn every 4 rounds
		Log.debug("speedCounter == " + speedCounter);
		int speed = getStatValue(Stat.SPEED);
		boolean extraTurn = false;
		int[] checkSpeedArr = new int[] { 12 , 8 , 4 };
		
		if ( speed > 0 ) {
			int checkSpeed = checkSpeedArr[speed-1];
			Log.debug("Check speed = " + checkSpeed );
			if ( speedCounter >= checkSpeed ) {
				speedCounter = 0;
				extraTurn = true;
				MessageManager.instance().addCenteredMessage("Extra Turn!");
			}
		}
		
		turnRegulator.endPlayerTurn(extraTurn);
		
		// check regen counter to see if we get
		// to regen health this turn
		// based on regen attribute
		// 0 regen = no regen
		// 1 regen = 1 health every 12 rounds
		// 2 regen = 1 health every 8 rounds
		// 3 regen = 1 health every 4 rounds
		int regen = getStatValue(Stat.HEALTH_REGEN);
		int[] regenCheckArr = new int[] { 20 , 10 , 5 };
		if ( regen > 0 ) {
			int regenCheck = regenCheckArr[regen-1];
			if ( regenCounter >= regenCheck ) {
				regenCounter = 0;
				
				if ( hero.getMaxHp() != hero.getCurrentHp() ) {
					hero.setCurrentHp(hero.getCurrentHp()+1);
					MessageManager.instance().addCenteredMessage("+1 Health Regen");
				}
			}
		}
		
		
	}

	public void resetSpeedCounter() {
		speedCounter = 0;
	}
	
	public TurnRegulator getTurnRegulator() {
		return turnRegulator;
	}

	public void setTurnRegulator(TurnRegulator turnRegulator) {
		this.turnRegulator = turnRegulator;
	}

	public void mapChanged(FloorMap newMap) {
		resetSpeedCounter();
	}

	public Hero getHero() {
		return hero;
	}

	public void setHero(Hero hero) {
		this.hero = hero;
	}


	
	
}
