package com.wwflgames.za.mob;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.wwflgames.za.map.Dir;
import com.wwflgames.za.map.FloorMap;
import com.wwflgames.za.map.MapRenderer;
import com.wwflgames.za.map.MapSquare;
import com.wwflgames.za.map.Wall;
import com.wwflgames.za.mob.animation.Animation;
import com.wwflgames.za.mob.animation.AnimationPathPoint;
import com.wwflgames.za.mob.animation.MoveAnimation;
import com.wwflgames.za.slick.SlickEntity;

public abstract class Mobile extends SlickEntity {

	public enum AnimationState { NONE , MOVE , ATTACK };
	
	protected MobSpriteSheet mobSpriteSheet = null;
	protected int mobx = 0;
	protected int moby = 0;
	
	protected float renderx;
	protected float rendery;
	
	protected Dir facing = Dir.EAST;
	
	protected AnimationState currentAnimationState;
	protected Animation currentAnimation;
	
	protected FloorMap currentMap;
	protected int maxHp;
	protected int currentHp;
	
	public Mobile() {
		currentAnimationState = AnimationState.NONE;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) 
		throws SlickException {
		super.init(container,game);
	}
	
	@Override
	public void render(Graphics g) throws SlickException {
		
		//TODO: use animations from sprite sheet for walking
		if ( currentAnimationState == AnimationState.NONE ) {
			// TODO draw a shadow under the MOB, just a gray circle
			g.drawImage(mobSpriteSheet.getCurrentFrame(), renderx, rendery);
		} else {
			g.drawImage(mobSpriteSheet.getCurrentFrame(), renderx, rendery);
		}
		
		g.setColor(Color.white);
		// draw the x,y below the mob
//		String xy = mobx + "," + moby;
//		int width = g.getFont().getWidth(xy);
//		g.drawString(xy, renderx+12-(width/2), rendery-10);
		
	}
	
	@Override
	public void update(int delta) throws SlickException {
		if ( currentAnimationState == AnimationState.NONE ) {
			renderx = getRenderX();
			rendery = getRenderY();
		} else {
			if ( currentAnimation != null ) {
				// update the sheet, so the zombie walks
				mobSpriteSheet.update(delta);
				if ( currentAnimation.shouldGetNextPoint(delta) ) {
					AnimationPathPoint app = 
						currentAnimation.getNextPathPoint();
					if ( app != null ) {
						renderx = app.getX();
						rendery = app.getY();
						if ( app.isDoSpecial() ) {
							app.doSpecial();
						}
					} else {
						animationComplete();
					}
				} 
			} 
		}
	}

	public void moveDelta(int dx, int dy) {
		
		float startx = getRenderX();
		float starty = getRenderY();
		
		int checkmobx = mobx+dx;
		int checkmoby = moby+dy;
		
		if ( canMoveTo(checkmobx,checkmoby) ) {
			moveTo(checkmobx,checkmoby);
		}
		
		float endx = getRenderX();
		float endy = getRenderY();
		
		// animate the move
		MoveAnimation ma = new MoveAnimation(startx,starty,endx,endy);
		playAnimation(AnimationState.MOVE,ma);
		
	}
	
	public boolean canMoveTo(int x , int y ) {
		
		if ( !currentMap.inBounds(x, y)) {
			return false;
		}
		
		// figure out if there's a wall in the way between our
		// map square and the map square at x,y
		MapSquare currentMS = currentMap.getMapSquare(mobx, moby);
		MapSquare checkMS = currentMap.getMapSquare(x, y);

		// NORTH and SOUTH movement isn't dependent on facing
		Wall w = currentMS.findWall(Dir.NORTH);
		if ( w != null && y < moby ) {
			if ( w.hasDoor() && w.isDoorOpen() ) {
				return true;
			} else {
				return false;
			}
		}
		w = currentMS.findWall(Dir.SOUTH);
		if ( w != null && y > moby ) {
			if ( w.hasDoor() && w.isDoorOpen() ) {
				return true;
			} else {
				return false;
			}
		}
		w = checkMS.findWall(Dir.SOUTH);
		if ( w != null && y < moby ) {
			if ( w.hasDoor() && w.isDoorOpen() ) {
				return true;
			} else {
				return false;
			}
		}
		w = checkMS.findWall(Dir.NORTH);
		if ( w != null && y > moby ) {
			if ( w.hasDoor() && w.isDoorOpen() ) {
				return true;
			} else {
				return false;
			}
		}
		
		// EAST and WEST movment is dependent on facing
		if ( facing == Dir.EAST ) {
			w = currentMS.findWall(Dir.EAST);
			if ( w != null && x > mobx) {
				if ( w.hasDoor() && w.isDoorOpen() ) {
					return true;
				} else {
					return false;
				}
			}
			w = checkMS.findWall(Dir.WEST);
			if ( w != null && x > mobx ) {
				if ( w.hasDoor() && w.isDoorOpen() ) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			w = currentMS.findWall(Dir.WEST);
			if ( w != null && x < mobx ) {
				if ( w.hasDoor() && w.isDoorOpen() ) {
					return true;
				} else {
					return false;
				}
			}
			w = checkMS.findWall(Dir.EAST);
			if ( w != null && x < mobx ) {
				if ( w.hasDoor() && w.isDoorOpen() ) {
					return true;
				} else {
					return false;
				}
			}
		}
		
		return currentMap.inBounds(x, y);
	}
	
	public void moveTo(int checkmobx, int checkmoby) {
		MapSquare oldms = currentMap.getMapSquare(mobx, moby);
		if ( oldms != null ) {
			oldms.setMobile(null);
		}
		mobx=checkmobx;
		moby=checkmoby;
		MapSquare newms = currentMap.getMapSquare(mobx, moby);
		newms.setMobile(this);
	}

	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

	public int getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(int currentHp) {
		this.currentHp = currentHp;
	}
	
	public void doDamage(int damage) {
		currentHp-=damage;
		if ( currentHp <= 0 ) {
			doDeath();
		}
	}
	
	public abstract void doDeath();
	
	public int getMobx() {
		return mobx;
	}

	public int getMoby() {
		return moby;
	}

	public float getRenderX() {
		return getRenderXForMapCoord(mobx, moby);
	}
	
	public float getRenderY() {
		return getRenderYForMapCoord(mobx, moby);
	}
	
	protected float getRenderXForMapCoord(int mapx,int mapy) {
		return mapx * MapRenderer.TILE_WIDTH + MapRenderer.START_DRAW_X + 
			MapRenderer.xoffset(mapy) + 12;
	}
	
	protected float getRenderYForMapCoord(int mapx,int mapy) {
		return mapy * MapRenderer.TILE_HEIGHT + 
			MapRenderer.START_DRAW_Y - 22;
	}
	
	public Dir getFacing() {
		return facing;
	}

	public void setFacing(Dir facing) {
		this.facing = facing;
		mobSpriteSheet.setFacing(facing);
	}
	
	protected void playAnimation(AnimationState animationState, 
			Animation animation) {
		
//		if ( animationState == AnimationState.MOVE ) {
//			mobSpriteSheet.startWalking();
//		}
		
		currentAnimation = animation;
		currentAnimationState = animationState;
	}
	
	protected void animationComplete() {
//		mobSpriteSheet.standStill();
		currentAnimation = null;
		currentAnimationState = AnimationState.NONE;
	}

}
