package com.wwflgames.za.item;

public class Ammo extends StackableItem {

	Weapon ammoFor;
	
	public Ammo(Weapon ammoFor) {
		super("Ammo", false, false);
		this.ammoFor = ammoFor;
	}

	public boolean isForWeapon(Weapon weapon){
		return ammoFor.equals(weapon);
	}
	
	public static Ammo createAmmoFor(Weapon weapon,int quantity) {
		Ammo a = new Ammo(weapon);
		a.setQuantity(quantity);
		return a;
	}
	
	public void useAmmo() {
		quantity --;
	}
	
	@Override
	public String toString() {
		return ammoFor.getName() + " ammo (" + getQuantity() + ")";
	}
	
	@Override
	public String getStackingQualifier() {
		return ammoFor.getName();
	}
}
