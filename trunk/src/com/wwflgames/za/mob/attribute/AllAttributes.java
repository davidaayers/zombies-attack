package com.wwflgames.za.mob.attribute;

public class AllAttributes {

	public static final Attribute SLUGGER = 
		new Attribute(
				"Slugger" , 
				"Increase Head Smash 10%",
				"+10% Head Smash",
				null, 
				Stat.BAT_HEAD_SMASH_CHANCE, 
				10
		);
	
	public static final Attribute SLUGGER2 = 
		new Attribute(
				"Slugger 2" , 
				"Increase Head Smash 10%",
				"+10% Head Smash",
				SLUGGER, 
				Stat.BAT_HEAD_SMASH_CHANCE, 
				10
		);
	
	
	public static final Attribute SLUGGER3 = 
		new Attribute(
				"Slugger 3" , 
				"Increase Head Smash 10%",
				"+10% Head Smash",
				SLUGGER2, 
				Stat.BAT_HEAD_SMASH_CHANCE, 
				10
		);

	public static final Attribute DUNDEE = 
		new Attribute(
				"Crocodile Dundee" , 
				"Increase Knife Damage by 2",
				"+2 Knife Dmg",
				null, 
				Stat.KNIFE_SKILL, 
				2
		);
	
	public static final Attribute DUNDEE2 = 
		new Attribute(
				"Crocodile Dundee 2" , 
				"Increase Knife Damage by 2",
				"+2 Knife Dmg",
				DUNDEE, 
				Stat.KNIFE_SKILL, 
				2
		);

	public static final Attribute DUNDEE3 = 
		new Attribute(
				"Crocodile Dundee 3" , 
				"Increase Knife Damage by 2",
				"+2 Knife Dmg",
				DUNDEE2, 
				Stat.KNIFE_SKILL, 
				2
		);

	public static final Attribute MEDIC = 
		new Attribute(
				"Medic!" , 
				"Increase Bandage Healing by 2",
				"+2 Bandage Heal",
				null, 
				Stat.BANDAGING_SKILL, 
				2
		);

	public static final Attribute SNIPER = 
		new Attribute(
				"Sniper" , 
				"Increase Rifle Headshot Chance by 10%",
				"+10% Rifle Headshot",
				null, 
				Stat.RIFLE_HEADSHOT_CHANCE, 
				10
		);

	public static final Attribute MARKSMAN = 
		new Attribute(
				"Marksman" , 
				"Increase Pistol Headshot Change by 10%",
				"+10% Pistol Headhsot",
				null, 
				Stat.PISTOL_HEADSHOT_CHANCE, 
				2
		);

	public static final Attribute FLEET_FOOTED = 
		new Attribute(
				"Fleet Footed" , 
				"An extra turn every 10 turns",
				"1 extra/10 turns",
				null, 
				Stat.SPEED, 
				1
		);
	
	public static final Attribute FLEET_FOOTED2 = 
		new Attribute(
				"Fleet Footed 2" , 
				"An extra turn every 7 turns",
				"1 extra/10 turns",
				FLEET_FOOTED, 
				Stat.SPEED, 
				1
		);

	public static final Attribute FLEET_FOOTED3 = 
		new Attribute(
				"Fleet Footed 3" , 
				"An extra turn every 5 turns",
				"1 extra/10 turns",
				FLEET_FOOTED2, 
				Stat.SPEED, 
				1
		);

	public static final Attribute HEALTHY_HORSE = 
		new Attribute(
				"Healthy as a Horse" , 
				"Max health increased by 5",
				"+5 Max Health",
				null, 
				Stat.MAX_HEALTH, 
				5
		);
	
	public static final Attribute[] ALL_ATTRIBUTES = {
		SLUGGER,
		SLUGGER2,
		SLUGGER3,
		DUNDEE,
		DUNDEE2,
		DUNDEE3,
		MEDIC,
		SNIPER,
		MARKSMAN,
		FLEET_FOOTED,
		FLEET_FOOTED2,
		FLEET_FOOTED3,
		HEALTHY_HORSE
	};
	
}
