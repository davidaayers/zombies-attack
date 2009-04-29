package com.wwflgames.za.game;

import java.util.HashMap;
import java.util.Map;

import com.wwflgames.za.map.FloorMap;
import com.wwflgames.za.map.MapChangeListener;

public class TallyTracker implements MapChangeListener {
	
	public enum Tally {

		ZOMBIES_KILLED("Zombies Killed"),
		AMMO_USED("Ammo Used"),
		HEALTH_LOST("Health Lost"),
		BANDAGES_USED("Bandages Used"),
		TURNS_TAKEN("Turns Taken")
		;
		
		private String desc;
		Tally(String desc) {
			this.desc = desc;
		}
		
		public String getDescription() {
			return desc;
		}
	}
	
	private Map<Tally,Integer> currentLevelTally = new HashMap<Tally,Integer>();
	private Map<Tally,Integer> totalTally = new HashMap<Tally,Integer>();
	
	private static TallyTracker instance;
	
	public static TallyTracker instance() {
		if ( instance == null ) {
			instance = new TallyTracker();
		}
		return instance;
	}

	public void mapChanged(FloorMap newMap) {
		currentLevelTally.clear();
	}
	
	public void addTally(Tally tally ,int value) {
		addTallyToMap(currentLevelTally,tally,value);
		addTallyToMap(totalTally,tally,value);
	}

	private void addTallyToMap(Map<Tally,Integer> map , Tally tally ,int value ) {
		Integer oldValue = map.get(tally);
		if ( oldValue == null ) {
			map.put(tally,value);
		} else {
			map.put(tally,(oldValue.intValue()+value));
		}
	}
	
	public int getTally(Tally tally, boolean isCurrentLevel ){
		if ( isCurrentLevel ) {
			return getCurrentLevelTally(tally);
		} else {
			return getOverallTally(tally);
		}
	}
	
	public int getCurrentLevelTally(Tally tally) {
		Integer value = currentLevelTally.get(tally);
		if ( value == null ) {
			return 0; 
		} else { 
			return value.intValue();
		}
	}
	
	public int getOverallTally(Tally tally) {
		Integer value = totalTally.get(tally);
		if ( value == null ) { 
			return 0; 
		} else { 
			return value.intValue();
		}
	}
	
	
}
