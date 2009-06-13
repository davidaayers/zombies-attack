package com.wwflgames.za;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ClasspathLocation;
import org.newdawn.slick.util.ResourceLoader;

import com.wwflgames.za.game.GameController;
import com.wwflgames.za.gamestate.GamePlayState;
import com.wwflgames.za.gamestate.GameWonState;
import com.wwflgames.za.gamestate.MenuState;
import com.wwflgames.za.gamestate.TitleState;

/**
 * Copyright (C) 2009 David A. Ayers
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *  
 * @author davidaayers
 *
 */
public class ZombiesAttack extends StateBasedGame {

	public static boolean DEBUG = false;
	private static AppGameContainer container = null;
	
	public ZombiesAttack() throws SlickException {
		super("When Zombies Attack!");
	}

	public static GameContainer getGameContainer() {
		return container;
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new TitleState());
		addState(new MenuState());
		addState(new GamePlayState());
		addState(new GameWonState());
	}
	
	public static void main(String[] argv) {
		
		ResourceLoader.removeAllResourceLocations();
		ResourceLoader.addResourceLocation(new ClasspathLocation());
		
		try {
			container = new AppGameContainer(new ZombiesAttack());
			container.setDisplayMode(800,600,false);
			container.setShowFPS(false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}