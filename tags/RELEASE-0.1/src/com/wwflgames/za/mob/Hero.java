package com.wwflgames.za.mob;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.wwflgames.za.game.GameCommand;
import com.wwflgames.za.game.GameController;
import com.wwflgames.za.game.Player;
import com.wwflgames.za.game.TallyTracker;
import com.wwflgames.za.game.TurnRegulator;
import com.wwflgames.za.game.TallyTracker.Tally;
import com.wwflgames.za.item.Ammo;
import com.wwflgames.za.item.Item;
import com.wwflgames.za.item.RangedWeapon;
import com.wwflgames.za.item.Weapon;
import com.wwflgames.za.map.FloorMap;
import com.wwflgames.za.map.MapChangeListener;
import com.wwflgames.za.mob.animation.AttackAnimation;
import com.wwflgames.za.mob.attribute.Stat;

public class Hero extends Mobile implements MapChangeListener {

	private final static boolean DEBUG_MOVEMENT = false;
	
	private TurnRegulator turnRegulator;
	private int unequippedDmg = 1;
	private Player player;
	
	public Hero() {
		maxHp = 10;
		currentHp = 10;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) 
		throws SlickException {
		
		super.init(container,game);
		
		mobSpriteSheet = new MobSpriteSheet("hero.png");
		mobSpriteSheet.standStill();
		mobx = 4;
		moby = 2;
	}
	
	public void changeFacing() {
		this.setFacing(getFacing().opposite());
	}

	@Override
	public void render(Graphics g) throws SlickException {
		
		super.render(g);
		
		if ( DEBUG_MOVEMENT ) {
			// draw the x,y on the screen
			g.setColor(Color.white);
			g.drawString(mobx+","+moby, 100,100);
		}
	}
	
	public void mapChanged(FloorMap newMap) {
		this.currentMap = newMap;
	}

	public void attack(Zombie z, int dx, int dy) {
		// tell the renderer to render the attack animation
		AttackAnimation aa = new AttackAnimation(this,z,currentMap);

		//TODO different animations for different weapons.
		//probably not going to happen
		playAnimation(AnimationState.ATTACK,aa);
		
		if ( getEquippedWeapon() == null ) {
			z.doDamage(getUnequippedDmg());
		} else {
			getEquippedWeapon().doDamage(this,z);
		}
	}
	
	@Override
	public void doDeath() {
		GameController.instance().fireGameCommand(GameCommand.PLAYER_DIED);
	}
	
	@Override
	protected void animationComplete() {
		super.animationComplete();
		
		// tell the player our animation is done, and therefore
		// our turn is done
		player.endPlayerTurn();
	}

	public int getUnequippedDmg() {
		return unequippedDmg;
	}

	public void setUnequippedDmg(int unequippedDmg) {
		this.unequippedDmg = unequippedDmg;
	}
	
	public Weapon getEquippedWeapon() {
		return player.getEquippedWeapon();
	}

	public void setEquippedWeapon(Weapon equippedWeapon) {
		player.setEquippedWeapon(equippedWeapon);
	}

	public boolean inventoryContains(Weapon weapon) {
		return player.inventoryContains(weapon);
	}
	
	public void equipWeapon(Weapon weapon) {
		player.equipWeapon(weapon);
	}

	public void addItemToInventory(Item item){
		player.addItemToInventory(item);
	}
	
	public Ammo getAmmoFor(Weapon weapon) {
		return player.getAmmoFor(weapon);
	}

	public void doRangedAttack(Zombie zombie) {
		//TODO: add animation for ranged attack. For now, just
		//do damage to the zombie and put some blood on the floor
		RangedWeapon weapon = (RangedWeapon)getEquippedWeapon();
		
		weapon.doRangedDamage(this,zombie);
		
		// remove one unit from ammo
		getAmmoFor(weapon).useAmmo();
		
		// add blood (temp) this should go in the animation
		currentMap.addBlood(zombie.getMobx(), zombie.getMoby(), 
				zombie.getRenderX()+12, zombie.getRenderY()+24, 5);
		
		// end the player turn
		player.endPlayerTurn();
	}

	@Override
	/**
	 * Override to get max health from the player, not the hero
	 */
	public int getMaxHp() {
		return player.getStatValue(Stat.MAX_HEALTH);
	}
	
	public TurnRegulator getTurnRegulator() {
		return turnRegulator;
	}

	public void setTurnRegulator(TurnRegulator turnRegulator) {
		this.turnRegulator = turnRegulator;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
		this.currentHp = player.getStatValue(Stat.MAX_HEALTH);
	}
	
	@Override
	public void doDamage(int damage) {
		super.doDamage(damage);
		TallyTracker.instance().addTally(Tally.HEALTH_LOST, damage);
	}
		
}
