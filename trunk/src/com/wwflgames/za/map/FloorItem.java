package com.wwflgames.za.map;

import com.wwflgames.za.item.Item;

/**
 * A wrapper for an item that exists on the floor for the
 * player to pick up. 
 * 
 * @author davida
 */
public class FloorItem {

	private int x;
	private int y;
	private Item item;
	
	public FloorItem(Item item, int x , int y ) {
		this.x = x;
		this.y = y;
		this.item = item;
	}

	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public Item getItem() {
		return item;
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
	
}
