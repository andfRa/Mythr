package org.andfRa.mythr.util;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

public class RandomSelectionUtil {

	/** Random generator. */
	public final static Random RANDOM = new Random();
	
	
	/**
	 * Selects a random key from the map, based on weight value.
	 * 
	 * @param map map map with probability weights
	 * @return random key, null if none
	 */
	public static String selectRandom(HashMap<String, Double> map)
	 {
		double total = 0.0d;
		Set<Entry<String, Double>> entries = map.entrySet();
		
		for (Entry<String, Double> entry : entries) {
			total += entry.getValue();
		}
		
		double random = RANDOM.nextDouble() * total;
		for (Entry<String, Double> entry : entries) {
			random -= entry.getValue();
			if (random <= 0.0) {
				return entry.getKey();
			}
		}
		return null;
	 }
	
}
