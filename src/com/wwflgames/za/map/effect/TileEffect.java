package com.wwflgames.za.map.effect;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ParticleSystem;

/**
 * A base class for tile-based effects, such as blood splats and so forth.
 * 
 * @author davidaayers
 *
 */
public abstract class TileEffect {

	protected ParticleSystem system;	
	
	public abstract void init() throws SlickException;
	
	public void render(Graphics g) throws SlickException {
		if ( system != null ) {
			system.render();
		}
	}

	public void update(int delta) throws SlickException {
		if ( system != null ) {
			system.update(delta);
		}
	}
	
	public boolean isComplete() {
		return system != null ? system.getParticleCount() == 0 : true;
	}
	
}
