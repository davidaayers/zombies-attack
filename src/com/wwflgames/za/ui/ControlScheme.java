package com.wwflgames.za.ui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ControlScheme {

	public enum Control {
		NORTH,
		NORTHEAST,
		EAST,
		SOUTHEAST,
		SOUTH,
		SOUTHWEST,
		WEST,
		NORTHWEST,
		CHANGE_DIR,
		WAIT,
		DOOR_STATE,
		ACTIVATE_TARGET_MODE,
		TARGET_1,
		TARGET_2,
		TARGET_3,
		TARGET_4,
		TARGET_5,
		TARGET_6,
		TARGET_7,
		TARGET_8,
		TARGET_9,
		TARGET_10,
		WEAPON_1,
		WEAPON_2,
		WEAPON_3,
		WEAPON_4,
		WEAPON_5,
		WEAPON_6,
		WEAPON_7,
		USE_BANDAGE,
		IDLE,
		PAUSE_MENU
	}
	
	public static final Control[] TARGETING_CONTROLS = {
		Control.TARGET_1,
		Control.TARGET_2,
		Control.TARGET_3,
		Control.TARGET_4,
		Control.TARGET_5,
		Control.TARGET_6,
		Control.TARGET_7,
		Control.TARGET_8,
		Control.TARGET_9,
		Control.TARGET_10	
	};
	
	protected Map<Integer,Control> controlKeyMap = 
		new HashMap<Integer,Control>();
	
	protected List<Control> targetingControls;
	
	public ControlScheme() {
		targetingControls = Arrays.asList(TARGETING_CONTROLS);
	}
	
	public Control getControl(int key)  {
		return controlKeyMap.get(key);
	}
	
	public boolean isTargetingControl(Control c) {
		return targetingControls.contains(c);
	}
	
	public Integer getKeyForControl(Control control) {
		for ( Integer key : controlKeyMap.keySet() ) {
			Control c = getControl(key);
			if ( c == control ) {
				return key;
			}
		}
		return null;
	}
	
}
