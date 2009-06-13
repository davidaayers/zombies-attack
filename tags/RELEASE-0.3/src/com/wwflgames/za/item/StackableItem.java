package com.wwflgames.za.item;

public abstract class StackableItem extends Item {

	protected int quantity;
	
	public StackableItem(String name, boolean equippable, boolean usable) {
		super(name, equippable, usable);
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public abstract String getStackingQualifier();

}
