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
