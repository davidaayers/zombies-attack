package com.wwflgames.za.map;

import java.util.ArrayList;
import java.util.List;

public class Path {

	List<Step> steps = new ArrayList<Step>();
	
	public Path() {
	}
	
	public void addStep(int x , int y ) {
		steps.add(new Step(x,y));
	}
	
	public List<Step> getSteps() {
		return steps;
	}
	
	public class Step {
		public int x;
		public int y;
		public Step(int x, int y) {
			this.x = x;
			this.y = y;
		}

	}
}
