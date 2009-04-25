package com.wwflgames.za.mob;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.wwflgames.za.map.Dir;

public class MobSpriteSheet {

	protected int duration = 400;
	public static final Color TRANSPARENCY_COLOR = new Color(32,156,0);
	private SpriteSheet spriteSheet = null;
	protected Animation[] animations = null;
	private Dir facing = Dir.EAST;
	private Animation currentAnimation = null;

	public MobSpriteSheet() {}
	
	public MobSpriteSheet(String ref) throws SlickException {
		Image i = new Image(ref,TRANSPARENCY_COLOR);
		prepare(i);
	}
	
	public MobSpriteSheet(Image spriteImage) {
		prepare(spriteImage);
	}
	
	private void prepare(Image spriteImage) {
		spriteSheet = new SpriteSheet(spriteImage,24,32);
		
		// set up the four animations, one for each direction
		animations = new Animation[4];
		animations[Dir.NORTH.getIdx()] = new Animation(new Image[]{
				spriteSheet.getSprite(0, 0),
				spriteSheet.getSprite(1, 0),
				spriteSheet.getSprite(2, 0),
				spriteSheet.getSprite(1, 0)
			},duration);
		animations[Dir.EAST.getIdx()] = new Animation(new Image[]{
				spriteSheet.getSprite(0, 1),
				spriteSheet.getSprite(1, 1),
				spriteSheet.getSprite(2, 1),
				spriteSheet.getSprite(1, 1)
			},duration);
		animations[Dir.SOUTH.getIdx()] = new Animation(new Image[]{
				spriteSheet.getSprite(0, 2),
				spriteSheet.getSprite(1, 2),
				spriteSheet.getSprite(2, 2),
				spriteSheet.getSprite(1, 2)
			},duration);
		animations[Dir.WEST.getIdx()] = new Animation(new Image[]{
				spriteSheet.getSprite(0, 3),
				spriteSheet.getSprite(1, 3),
				spriteSheet.getSprite(2, 3),
				spriteSheet.getSprite(1, 3)
			},duration);
		
		// set the default facing to EAST
		setFacing(Dir.EAST);
	}
	
	public void setFacing(Dir direction) {
		facing = direction;
		currentAnimation = animations[facing.getIdx()];
	}
	
	public Dir getFacing() {
		return facing;
	}
	
	public void setDuration( int pDuration ) {
		duration = pDuration;
		// set the new duration on all of the animations
		for ( int cnt = 0 ; cnt < animations.length ; cnt ++ ) {
			Animation a = animations[cnt];
			for ( int cnt2 = 0 ; cnt2 < a.getFrameCount() ; cnt2 ++ ) {
				animations[cnt].setDuration(cnt2, duration);
			}
		}
	}
	
	public void update(int delta) {
		// update the current animation? should we update all of them?
		currentAnimation.update(delta);
	}
	
	public void renderAt(float x , float y ) {
		currentAnimation.draw(x, y);
	}
	
	public void standStill() {
		currentAnimation.stop();
		currentAnimation.setCurrentFrame(1);
	}
	
	public Image getCurrentFrame() {
		return currentAnimation.getCurrentFrame();
	}
	
	public void startWalking() {
		currentAnimation.start();
	}
	
	public int getWidth() {
		return currentAnimation.getWidth();
	}

	public int getHeight() {
		return currentAnimation.getHeight();
	}
	
}
