package com.wwflgames.za.util;

import java.util.Random;

public class Dice {
	private static Random random = new Random();
	
	// random number between 1 and num, inclusive of num
	public static int d(int num) {
		return random.nextInt(num)+1;
	}
	
	public static int d(int start,int end) {
		return start + random.nextInt(end-start);
	}
	
	// random number between 0 and num, exclusive of num
	public static int randomInt(int num) {
		return random.nextInt(num);
	}

}
