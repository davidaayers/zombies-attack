package com.wwflgames.za.item;

public class Item {

	protected boolean equippable;
	protected boolean usable;
	protected String name;
	
	public Item(String name, boolean equippable,boolean usable) {
		this.name = name;
		this.equippable = equippable;
		this.usable = usable;
	}

	public boolean isEquippable() {
		return equippable;
	}

	public void setEquippable(boolean equippable) {
		this.equippable = equippable;
	}

	public boolean isUsable() {
		return usable;
	}

	public void setUsable(boolean usable) {
		this.usable = usable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Use toString() to build up a "display string" that will
	 * be shown in the UI
	 */
	public String toString() {
		return name;
	}
	
}
