package com.wwflgames.za.map.effect;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;


public class BloodTileEffect extends TileEffect {

	Image splat;
	ParticleEmitter fadeEmitter;

	@Override
	public void init() {
		try {
			splat = new Image("splat.tga");
			Image particle = new Image("particle.tga");

			system = new ParticleSystem(particle, 1000);
			system.setBlendingMode(ParticleSystem.BLEND_COMBINE);
			fadeEmitter = new ParticleEmitter() {
				public boolean isEnabled() {
					return true;
				}

				public void setEnabled(boolean enabled) {
				}

				public void update(ParticleSystem system, int delta) {
				}

				public void updateParticle(Particle particle, int delta) {
					particle.adjustColor(0, 0, 0, -delta * 0.0001f);
					particle.adjustSize(delta * -0.0001f);
				}

				public boolean completed() {
					return false;
				}

				public Image getImage() {
					return null;
				}

				public boolean isOriented() {
					return false;
				}

				public boolean useAdditive() {
					return false;
				}

				public boolean usePoints(ParticleSystem system) {
					return false;
				}

				public void resetState() {
				}

				public void wrapUp() {
				}

			};
			system.addEmitter(fadeEmitter);
		} catch (SlickException ex) {
			System.err.println("Error init() BloodTileEffect " + ex);
		}
	}

	// x , y should be the center of the map square
	public void addBlood(float x, float y, int max) {
		// add 10 blood particles about
		for (int cnt = 0; cnt < max; cnt++) {
			int ofs = 0;
			Particle particle = system.getNewParticle(fadeEmitter,
					(int) (1500 + (Math.random() * 1000)));
			particle.setImage(splat);
			particle.setPosition((x - 5) + ((int) (Math.random() * 29)), y
					+ ofs + ((int) (Math.random() * 24)));
			particle.setSize(15 + ((int) (Math.random() * 15)));
			particle.setColor((float) (Math.random() + 0.5f), 0, 0,
					(float) ((Math.random() * 0.5f) + 0.1f));
		}
	}

}
