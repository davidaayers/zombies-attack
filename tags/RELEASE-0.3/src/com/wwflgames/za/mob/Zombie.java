package com.wwflgames.za.mob;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.wwflgames.za.game.TallyTracker;
import com.wwflgames.za.game.TurnRegulator;
import com.wwflgames.za.game.TallyTracker.Tally;
import com.wwflgames.za.map.Dir;
import com.wwflgames.za.map.FloorMap;
import com.wwflgames.za.map.MapSquare;
import com.wwflgames.za.map.MapUtils;
import com.wwflgames.za.map.Path;
import com.wwflgames.za.mob.animation.AttackAnimation;
import com.wwflgames.za.ui.HealthBar;
import com.wwflgames.za.util.Dice;
import com.wwflgames.za.util.GShuffleBag;

public abstract class Zombie extends Mobile {

	HealthBar healthBar;
	private ZombieController controller;
	private TurnRegulator turnRegulator;
	private Hero hero;
	private boolean canSeePlayer;
	
	// zombie attributes. different zombies might have
	// different attributes
	protected int visionRange = 5;
	protected int attackPower = 1;
	
	static Dir[] eastCheck = { Dir.NORTH, Dir.NORTHEAST, Dir.EAST,
			Dir.SOUTHEAST, Dir.SOUTH };
	static Dir[] westCheck = { Dir.NORTH, Dir.NORTHWEST, Dir.WEST,
			Dir.SOUTHWEST, Dir.SOUTH };

	static Map<Dir, Dir[]> dirCheck = new HashMap<Dir, Dir[]>();

	static {
		dirCheck.put(Dir.EAST, eastCheck);
		dirCheck.put(Dir.WEST, westCheck);
	}

	public Zombie(GameContainer container, StateBasedGame game, 
			ZombieController controller, FloorMap currentMap,
			TurnRegulator turnRegulator, Hero hero, MobSpriteSheet spriteSheet,
			int maxHealth, int attackPower) {
		this.controller = controller;
		this.currentMap = currentMap;
		this.turnRegulator = turnRegulator;
		this.hero = hero;
		this.facing = currentMap.getDirection();
		this.mobSpriteSheet = spriteSheet;
		this.attackPower = attackPower;
		
		this.maxHp = maxHealth;
		this.currentHp = maxHealth;
		
		mobSpriteSheet.standStill();
		
		if ( Dice.randomInt(100) > 50 ) {
			this.setFacing(Dir.WEST);
		} else {
			this.setFacing(Dir.EAST);		
		}
		// create the health bar
		healthBar = new HealthBar(getRenderX(), getRenderY(), 16, 6,
				Color.black, Color.green, 4, 0);
		healthBar.setPctComplete((double) currentHp / (double) maxHp);
	}
	
	public void takeTurn(int turn) {

		// assume we can't see player
		canSeePlayer = false;

		// based on facing, see if the player is adjacent. If so,
		// attack the player
		Dir[] check = dirCheck.get(getFacing());
		for (Dir checkDir : check) {
			int checkx = mobx + checkDir.getMapDelta().x;
			int checky = moby + checkDir.getMapDelta().y;
			// if it's in bounds, and we could move there, see if
			// the player is there
			if (currentMap.inBounds(checkx, checky)
					&& canMoveTo(checkx, checky)) {
				MapSquare ms = currentMap.getMapSquare(checkx, checky);
				Mobile checkMob = ms.getMobile();
				if (checkMob != null && checkMob instanceof Hero) {
					AttackAnimation aa = new AttackAnimation(this, checkMob,
							currentMap);
					playAnimation(AnimationState.ATTACK, aa);
					doAttack((Hero) checkMob, checkx, checky);
					// turn is over
					return;
				}
			}
		}

		// first check if the player is even in the field of vision (i.e.
		// the zombie isn't facing away from the player
		int distToPlayer = this.getMobx() - hero.getMobx();

		if ((distToPlayer >= 0 && facing == Dir.WEST)
				|| (distToPlayer <= 0 && facing == Dir.EAST)) {
			
			// facing the player -- see if we have LOS
			Path p = MapUtils.findLOSPath(mobx, moby, hero.getMobx(), hero
					.getMoby(), currentMap);
			
			// make sure the player is within vision range
			if (p != null && p.getSteps().size() <= visionRange ) {
				canSeePlayer = true;
				// we have LOS, move in the next step toward the player
				Path.Step step = p.getSteps().get(1);
				int deltax = step.x - mobx;
				int deltay = step.y - moby;
				
				MapSquare ms = currentMap.getMapSquare(step.x, step.y);
				// if there's not already something there, step there
				if ( ms.getMobile() == null ) {
					moveDelta(deltax, deltay);
				} 
				// we couldn't move, just end the turn
				else {
					controller.zombieDoneWithTurn(this);
				}
				// turn over
				return;
			}
		}

		// chance we'll just change facing
		if (Dice.d(0, 10) > 8) {
			this.setFacing(this.getFacing().opposite());
			controller.zombieDoneWithTurn(this);
		}
		// move to one of the possible squares based on facing
		else {

			// new move stuff.
			// add all possible moves to a shuffle bag
			GShuffleBag<Dir> dirBag = new GShuffleBag<Dir>();
			for ( Dir d : check ) {
				dirBag.add(d);
			}
			
			int tryCnt = 0;
			boolean didMove = false;
			
			// try to move in all possible directions
			while ( tryCnt < check.length ) {
				tryCnt++;
				Dir tryDir = dirBag.next();
				int deltax = tryDir.getMapDelta().x;
				int deltay = tryDir.getMapDelta().y;
				int checkx = mobx + deltax;
				int checky = moby + deltay;
				// is it in bounds?
				if (currentMap.inBounds(checkx, checky)) {
					// is there zombie already there?
					MapSquare ms = currentMap.getMapSquare(checkx, checky);
					Mobile checkMob = ms.getMobile();
					if ( checkMob == null ) {
						moveDelta(deltax, deltay);
						didMove = true;
						break;
					} 
				} 
			}
			
			// if they didn't move, then change their direction,
			if ( !didMove ) {
				this.setFacing(this.getFacing().opposite());
				// end the turn
				controller.zombieDoneWithTurn(this);
			}
		}
	}

	@Override
	protected void animationComplete() {
		super.animationComplete();
		controller.zombieDoneWithTurn(this);
	}

	private void doAttack(Hero hero, int checkx, int checky) {
		hero.doDamage(attackPower);
	}

	@Override
	public void update(int delta) throws SlickException {
		super.update(delta);

		// update the health bar location and health
		healthBar.setLocation(renderx, rendery);
		healthBar.setPctComplete((double) currentHp / (double) maxHp);

	}

	@Override
	public void render(Graphics g) throws SlickException {
		super.render(g);

		// render the healthBar
		healthBar.render(g);

		// render something to indicate the player can be seen
		if (canSeePlayer) {
			g.setColor(Color.red);
			g.drawString("!", getRenderX()+12, getRenderY() - 15);
		}
	}

	public void doDeath() {
		TallyTracker.instance().addTally(Tally.ZOMBIES_KILLED, 1);
		// add a big splash of blood on death
		currentMap.addBlood(mobx, moby, renderx + 12, rendery + 24, 10);
		System.out
				.println("Zombie " + this + " died, removing from controller");
		controller.removeZombie(this);
	}

}
