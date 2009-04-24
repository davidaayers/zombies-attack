package com.wwflgames.za.mob.attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.newdawn.slick.util.Log;

import com.wwflgames.za.game.Player;
import com.wwflgames.za.mob.Hero;

public class AttributeFactory {

	private static List<Attribute> allAttributes = new ArrayList<Attribute>();
	
	static {
		allAttributes.addAll(Arrays.asList(AllAttributes.ALL_ATTRIBUTES));
	}
	
	public static List<Attribute> getAttributesForHero( Hero hero ) {
		return getAttributesForPlayer(hero.getPlayer());
	}
	
	public static List<Attribute> getAttributesForPlayer( Player player ) {
		List<Attribute> retList = new ArrayList<Attribute>();
		List<Attribute> heroList = player.getAttributes();
		
		Log.debug("All attributes = " + allAttributes );
		
		for ( Attribute attr : allAttributes ) {
			Log.debug("attr = " + attr );
			// see if hero already has this attribute
			if ( !heroList.contains(attr) ) {
				// hero doesn't. see if he has any necessary preReq
				Attribute preReq = attr.getPreReq();
				if ( preReq == null || 
						( preReq != null && heroList.contains(preReq) ) ) {
					retList.add(attr);
				}
			}
		}
		
		return retList;
	}
	
	public static Attribute getRandomAttributeForPlayer(Player player) {
		List<Attribute> attr = AttributeFactory.getAttributesForPlayer(player);
		Collections.shuffle(attr);
		return attr.get(0);
	}
	
}
