package com.wwflgames.za.ui;

import org.newdawn.slick.Input;

public class NumKeyPadControlScheme extends ControlScheme {

	public NumKeyPadControlScheme() {
		controlKeyMap.put(Input.KEY_NUMPAD8, Control.NORTH);
		controlKeyMap.put(Input.KEY_I, 		 Control.NORTH);		
		controlKeyMap.put(Input.KEY_NUMPAD9, Control.NORTHEAST);
		controlKeyMap.put(Input.KEY_O,       Control.NORTHEAST);
		controlKeyMap.put(Input.KEY_NUMPAD6, Control.EAST);
		controlKeyMap.put(Input.KEY_L,       Control.EAST);
		controlKeyMap.put(Input.KEY_NUMPAD3, Control.SOUTHEAST);
		controlKeyMap.put(Input.KEY_PERIOD,  Control.SOUTHEAST);
		controlKeyMap.put(Input.KEY_NUMPAD7, Control.NORTHWEST);
		controlKeyMap.put(Input.KEY_U,       Control.NORTHWEST);
		controlKeyMap.put(Input.KEY_NUMPAD4, Control.WEST);
		controlKeyMap.put(Input.KEY_J,       Control.WEST);
		controlKeyMap.put(Input.KEY_NUMPAD1, Control.SOUTHWEST);
		controlKeyMap.put(Input.KEY_M,       Control.SOUTHWEST);
		controlKeyMap.put(Input.KEY_NUMPAD2, Control.SOUTH);
		controlKeyMap.put(Input.KEY_COMMA,   Control.SOUTH);
		controlKeyMap.put(Input.KEY_NUMPAD5, Control.CHANGE_DIR);
		controlKeyMap.put(Input.KEY_K,       Control.CHANGE_DIR);

		// alternative move keys for laptap users
		//TODO: figure out a better scheme for targeting, so that
		// we can use 9 keys on the keyboard to 9 way movement
		controlKeyMap.put(Input.KEY_UP, Control.NORTH);
		controlKeyMap.put(Input.KEY_NUMPAD9, Control.NORTHEAST);
		controlKeyMap.put(Input.KEY_RIGHT, Control.EAST);
		controlKeyMap.put(Input.KEY_NUMPAD3, Control.SOUTHEAST);
		controlKeyMap.put(Input.KEY_NUMPAD7, Control.NORTHWEST);
		controlKeyMap.put(Input.KEY_LEFT, Control.WEST);
		controlKeyMap.put(Input.KEY_NUMPAD1, Control.SOUTHWEST);
		controlKeyMap.put(Input.KEY_DOWN, Control.SOUTH);
		controlKeyMap.put(Input.KEY_NUMPAD5, Control.CHANGE_DIR);

		
		controlKeyMap.put(Input.KEY_D,       Control.DOOR_STATE);
		controlKeyMap.put(Input.KEY_F,       Control.ACTIVATE_TARGET_MODE);
		controlKeyMap.put(Input.KEY_SPACE,   Control.IDLE);
		controlKeyMap.put(Input.KEY_Q,       Control.TARGET_1);
		controlKeyMap.put(Input.KEY_W,       Control.TARGET_2);
		controlKeyMap.put(Input.KEY_E,       Control.TARGET_3);
		controlKeyMap.put(Input.KEY_R,       Control.TARGET_4);
		controlKeyMap.put(Input.KEY_T,       Control.TARGET_5);
		controlKeyMap.put(Input.KEY_1,       Control.WEAPON_1);
		controlKeyMap.put(Input.KEY_2,       Control.WEAPON_2);
		controlKeyMap.put(Input.KEY_3,       Control.WEAPON_3);
		controlKeyMap.put(Input.KEY_4,       Control.WEAPON_4);
		controlKeyMap.put(Input.KEY_5,       Control.WEAPON_5);
		controlKeyMap.put(Input.KEY_6,       Control.WEAPON_6);
		controlKeyMap.put(Input.KEY_7,       Control.WEAPON_7);
		
		controlKeyMap.put(Input.KEY_B,       Control.USE_BANDAGE);
		
		controlKeyMap.put(Input.KEY_ESCAPE,  Control.PAUSE_MENU);
	}
	
}
