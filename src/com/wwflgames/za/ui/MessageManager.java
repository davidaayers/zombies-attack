package com.wwflgames.za.ui;

import java.util.ArrayList;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import com.wwflgames.za.game.GameController;
import com.wwflgames.za.map.MapRenderer;

public class MessageManager {

	ArrayList<Message> messages = new ArrayList<Message>();
	private static MessageManager instance = null;
	private Font impactFont = null;
	
	private MessageManager() {
		try {
			impactFont = new AngelCodeFont("impact.fnt","impact.png");
			Log.debug ( "Font loaded" );
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public static MessageManager instance() {
		if ( instance == null ) {
			instance = new MessageManager();
		}
		return instance;
	}

	private static final int Z_INDEX = 10;
	public int getZIndex() {
		return Z_INDEX;
	}
	
	public void addFloatingMessage( String message , float x , float y ) {
		messages.add(new FloatingMessage(message,x,y));
	}

	public void addFloatingMessage( String message , float x , float y , Color c ) {
		messages.add(new FloatingMessage(message,x,y,c));
	}

	public void addCenteredMessage ( String message ) {
		messages.add(new CenteredFadingMessage(message));
	}
	
	public void render(Graphics g) throws SlickException {
		for ( Message msg : messages ) {
			msg.render(g);
		}
	}

	public void update(int delta) throws SlickException {

		ArrayList<Message> removeMe = new ArrayList<Message>();
		for (int i=0;i<messages.size();i++) {
			Message message = (Message) messages.get(i);
			if (message.update(delta)) {
				removeMe.add(message);
			}
		}
		messages.removeAll(removeMe);
	}

	private interface Message {
		public boolean update(int delta);
		public void render(Graphics g);
	}
	
	private class CenteredFadingMessage implements Message {
		public String message;
		public float x;
		public float y;
		public Color c;
		public float a;
		
		public CenteredFadingMessage(String pMessage) {
			message = pMessage;
			a = 1f;
			x = 400 - impactFont.getWidth(message)/2;
			y = 150 - impactFont.getHeight(message)/2;
			c = new Color(1.0f,1.0f,1.0f,1.0f);
		}

		public boolean update(int delta) {
			a -= delta * 0.0005f;
			if (a < 0) {
				return true;
			}
			return false;
		}

		public void render(Graphics g) {
			impactFont.drawString((int) x, (int) y, message ,new Color(c.r,c.g,c.b,a));
		}
	}
	
	private class FloatingMessage implements Message {
		public String message;
		public float x;
		public float y;
		public float a;
		public Color c;
		
		public FloatingMessage(String message, float x, float y) {
			this(message,x,y,new Color(1.0f,1.0f,1.0f,1.0f));
		}
		
		public FloatingMessage(String message, float x, float y , Color color ) {
			a = 1;
			this.x = x;
			this.y = y;
			this.message = message;
			c = color;
		}
		
		public boolean update(int delta) {
			y -= delta * 0.08f;
			a -= delta * 0.001f;
		
			if (a < 0) {
				return true;
			}
			
			return false;
		}
		
		public void render(Graphics g) {
			int heromapx = GameController.instance().getHero().getMobx();
			float translateX = (heromapx/20) * MapRenderer.TILE_WIDTH * 20 * -1;
			// translate
			g.translate(translateX, 0);
			
			g.setColor(new Color(c.r,c.g,c.b,a));
			g.resetFont();
			float newx = x - (g.getFont().getWidth(message) / 2);
			g.drawString(message, (int) newx, (int) y);
			
			// untranslate
			g.translate(0,0);
			g.resetTransform();
		}
	}

	public void clearAll() {
		messages.clear();
	}
}
