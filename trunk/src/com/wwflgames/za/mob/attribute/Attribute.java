package com.wwflgames.za.mob.attribute;

public class Attribute {

	protected String name;
	protected String description;
	protected String shortDesc;
	protected Attribute preReq;
	protected Stat effectedStat;
	protected int statEffectValue;
	
	public Attribute(String name , String description , String shortDesc , 
			Attribute preReq , Stat effectedStat, int statEffectValue ) {
		this.name = name;
		this.description = description;
		this.shortDesc = shortDesc;
		this.preReq = preReq;
		this.effectedStat = effectedStat;
		this.statEffectValue = statEffectValue;
	}
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	public Attribute getPreReq() {
		return preReq;
	}
	
	public String toString() {
		return name;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public Stat getEffectedStat() {
		return effectedStat;
	}

	public int getStatEffectValue() {
		return statEffectValue;
	}

}
