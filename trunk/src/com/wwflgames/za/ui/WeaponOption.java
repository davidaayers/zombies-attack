package com.wwflgames.za.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.wwflgames.za.item.Ammo;
import com.wwflgames.za.item.Weapon;
import com.wwflgames.za.mob.Hero;
import com.wwflgames.za.ui.ControlScheme.Control;

public class WeaponOption {
	
	Hero hero;
	Weapon weapon;
	Control control;
	Image icon;
	ControlScheme controlScheme;
	
	public WeaponOption(Control control, Weapon weapon, Image icon, 
			Hero hero, ControlScheme controlScheme ) {
		this.control = control;
		this.weapon = weapon;
		this.hero = hero;
		this.icon = icon;
		this.controlScheme = controlScheme;
	}

	public Control getControl() {
		return control;
	}

	public boolean optionActivatable() {
		// fists are always activatable
		if ( weapon == null ) {
			return true;
		}
		return hero.inventoryContains(weapon);
	}
	
	public void activate() {
		//NULL is ok here, it means we're equipping "fists", 
		hero.equipWeapon(weapon);
	}
	
	//TODO implement icons for weapons
	public void render(float x , float y , Graphics g) throws SlickException {
		if ( optionActivatable() ) {
			g.setColor(Color.white);
		} else {
			g.setColor(Color.gray);
		}
		
		Weapon equippedWeapon = hero.getEquippedWeapon();
		
		// change the color to yellow for the currentWeapon
		if ( equippedWeapon != null ) {
			if ( equippedWeapon.equals( weapon ) ) {
				g.setColor(Color.yellow);
			}
		} else {
			// handle fists
			if ( weapon == null ) {
				g.setColor(Color.yellow);
			}
		}
		
		int dmg;
		boolean usesAmmo = false;
		if ( weapon == null ) {
			dmg = hero.getUnequippedDmg();
		} else {
			dmg = weapon.getBaseDamage();
			usesAmmo = weapon.usesAmmo();
		}
		
		String name = weapon != null ? weapon.getName() : "Fists";
		
		
		String key = Input.getKeyName(controlScheme.getKeyForControl(control));
		//hotkey-1 because the KEY for 1 is really 2
		String str1 = key+": "+name;
		String str2 = dmg + " d";
		if ( usesAmmo ) {
			Ammo a = hero.getAmmoFor(weapon);
			int ammoQty = a != null ? a.getQuantity() : 0;
			str2+= "/" + ammoQty + " a";
		}
		g.drawString(str1,x,y);
		y+=12;
		g.drawString(str2,x,y);
	}
	
}
