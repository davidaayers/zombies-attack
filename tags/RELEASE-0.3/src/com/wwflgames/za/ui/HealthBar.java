package com.wwflgames.za.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class HealthBar {

	private float x;
	private float y;
	private int drawWidth;
	private int drawHeight;
	private Color outlineColor;
	private Color barColor;
	private double pct;
	private int xoffset;
	private int yoffset;

	public HealthBar(float x, float y, int drawWidth, int drawHeight,
			Color outlineColor, Color barColor , int xoffset, int yoffset) {
		this.drawHeight = drawHeight;
		this.drawWidth = drawWidth;
		this.outlineColor = outlineColor;
		this.barColor = barColor;
		this.xoffset = xoffset;
		this.yoffset = yoffset;
		setLocation(x,y);
	}

	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setPctComplete(double pct) {
		this.pct = pct;
	}

	public void render(Graphics g) throws SlickException {
		float drawx = x+xoffset;
		float drawy = y+yoffset;
		g.setColor(Color.red);
		g.fillRect(drawx, drawy, drawWidth, drawHeight);
		g.setColor(outlineColor);
		g.drawRect(drawx, drawy, drawWidth, drawHeight);
		double drawPct = pct;
		if ( drawPct < 0 ) {
			drawPct = 0;
		}
		int width = (int) ((double) (drawWidth - 1) * pct);
		g.setColor(barColor);
		g.fillRect(drawx + 1, drawy + 1, width, drawHeight - 1);
	}

	public void update(int delta) throws SlickException {
	}
}