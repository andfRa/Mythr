package org.andfRa.mythr.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.inout.Directory;
import org.andfRa.mythr.inout.FileIO;
import org.andfRa.mythr.util.LinearFunction;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParseException;

public class LevelingConfiguration {
	
	/** Instance of the configuration. */
	private static LevelingConfiguration config;
	

	/** Cost of spending an attribute point. */
	private LinearFunction levelSpendExp;
	
	/** Maximum level. */
	private Integer maxLevel;
	
	/** Cost of spending an attribute point. */
	private Integer attribPointSpendExp;

	/** Cost of spending an attribute point. */
	private Integer skillPointSpendExp;

	/** Cost of learning a perk. */
	private Integer perkSpendExp;
	
	
	/** All perks. */
	private HashMap<Integer, ArrayList<String>> perks;

	/** Perks available. */
	private HashMap<Integer, HashMap<Integer, Integer>> availPerks;
	
	/** Perk level names. */
	private HashMap<Integer, String> perkCategNames;

	
	// CONSTRUCTION:
	/** Fixes all missing fields. */
	public void complete()
	 {
		if(levelSpendExp == null){
			MythrLogger.nullField(getClass(), "levelSpendExp");
			levelSpendExp = new LinearFunction(100.0);
		}
		
		if(maxLevel == null){
			MythrLogger.nullField(getClass(), "maxLevel");
			maxLevel = 10;
		}
		
		if(attribPointSpendExp == null){
			MythrLogger.nullField(getClass(), "attribPointSpendExp");
			attribPointSpendExp = 0;
		}
		
		if(skillPointSpendExp == null){
			MythrLogger.nullField(getClass(), "skillPointSpendExp");
			skillPointSpendExp = 0;
		}
		
		if(perkSpendExp == null){
			MythrLogger.nullField(getClass(), "perkSpendExp");
			perkSpendExp = 0;
		}
		
		if(perks == null){
			MythrLogger.nullField(getClass(), "perks");
			perks = new HashMap<Integer, ArrayList<String>>();
		}
		if(perks.remove(null) != null) MythrLogger.nullField(getClass(), "perks element");
		
		if(availPerks == null){
			MythrLogger.nullField(getClass(), "availPerks");
			availPerks = new HashMap<Integer, HashMap<Integer,Integer>>();
		}
		
		if(perkCategNames == null){
			MythrLogger.nullField(getClass(), "perkCategNames");
			perkCategNames = new HashMap<Integer, String>();
		}
		
	 }
	
	
	// VALUES:
	/**
	 * Gets the exp cost of leveling up.
	 * 
	 * @param level current level
	 * @return level exp cost
	 */
	public static Integer getLevelupSpendExp(Integer level)
	 { return config.levelSpendExp.yIntFloor(level); }
	
	/**
	 * Gets the maximum level.
	 * 
	 * @return maximum level
	 */
	public static Integer getMaxLevel()
	 { return config.maxLevel; }
	
	
	/**
	 * Gets the exp cost of spending attribute point.
	 * 
	 * @return attribute point exp cost
	 */
	public static Integer getAttribPointSpendExp()
	 { return config.attribPointSpendExp; }
	
	/**
	 * Gets the exp cost of spending skill point.
	 * 
	 * @return skill point exp cost
	 */
	public static Integer getSkillPointSpendExp()
	 { return config.skillPointSpendExp; }

	/**
	 * Gets the exp cost of learning a perk.
	 * 
	 * @return skill point exp cost
	 */
	public static Integer getPerkSpendExp()
	 { return config.perkSpendExp; }

	
	/**
	 * Gets all perks.
	 * 
	 * @return all perks
	 */
	public static ArrayList<String> getAllPerks()
	 {
		ArrayList<String> perks = new ArrayList<String>();
		Set<Entry<Integer, ArrayList<String>>> entries = config.perks.entrySet();
		for (Entry<Integer, ArrayList<String>> entry : entries) {
			perks.addAll(entry.getValue());
		}
		return perks;
	 }
	
	/**
	 * Gets perks for the given level.
	 * 
	 * @param level level
	 * @return perks
	 */
	public ArrayList<String> getPerks(Integer level)
	 {
		ArrayList<String> perks = this.perks.get(level);
		if(perks == null) return new ArrayList<String>();
		return perks;
	 }
	
	/**
	 * Checks if the perk is valid.
	 * 
	 * @param perkCateg perk category
	 * @param perk perk
	 * @return true if valid
	 */
	public static boolean checkPerk(Integer perkCateg, String perk)
	 {
		ArrayList<String> perks = config.perks.get(perkCateg);
		if(perks == null) return false;
		return perks.contains(perk);
	 }
	
	/**
	 * Gets available perks at given player and perk category.
	 * 
	 * @param playerLevel player level
	 * @param perkCateg perk category
	 * @return amount of perks at given player level
	 */
	public static Integer getAvailPerks(Integer playerLevel, Integer perkCateg)
	 {
		while (playerLevel >= 0) {
			
			HashMap<Integer, Integer> avail = config.availPerks.get(playerLevel);
			if(avail != null){
				Integer amount = avail.get(perkCateg);
				if(amount == null) amount = 0;
				return amount;
			}
			
			playerLevel--;
		}
		
		return 0;
	 }
	
	/**
	 * Gets perk category.
	 * 
	 * @param perk perk
	 * @return perk category, null if none
	 */
	public static Integer getPerkCategory(String perk)
	 {
		Set<Entry<Integer, ArrayList<String>>> entries = config.perks.entrySet();
		for (Entry<Integer, ArrayList<String>> entry : entries) {
			if(entry.getValue().contains(perk)) return entry.getKey();
		}
		return null;
	 }
	
	/**
	 * Gets perk category name.
	 * 
	 * @param perkCateg perk category
	 * @return perk category name
	 */
	public static String getPerkCategName(Integer perkCateg)
	 {
		String name = config.perkCategNames.get(perkCateg);
		if(name == null){
			MythrLogger.warning(LevelingConfiguration.class, "Missing name for " + perkCateg + " perk category.");
			return "????????";
		}
		return name;
	 }
	
	/**
	 * Gets max perk category.
	 * 
	 * @return maximum perk category
	 */
	public static Integer getMaxPerkCateg()
	 {
		Set<Integer> all = config.perks.keySet();
		Integer max = 0;
		for (Integer level : all) {
			if(level > max) max = level;
		}
		return max;
	 }
	
	
	// LOAD UNLOAD:
	/** Loads the configuration. */
	public static void load(){


		// Create config:
		if(!FileIO.exists(Directory.LEVELING_CONFIG)){

			try {
				FileIO.unpackConfig(Directory.LEVELING_CONFIG);
			}
			catch (IOException e) {
				MythrLogger.severe(LevelingConfiguration.class, "Failed to unpack default configuration.");
				MythrLogger.severe(" " + e.getClass().getSimpleName() + ":" + e.getMessage());
			}
			
		}
		
		// Read config:
		LevelingConfiguration config;
		try {
			
			config = FileIO.readConfig(Directory.LEVELING_CONFIG, LevelingConfiguration.class);
			
		} catch (IOException e) {
			
			MythrLogger.severe(LevelingConfiguration.class, "Failed to read configuration.");
			MythrLogger.severe(" " + e.getClass().getSimpleName() + ":" + e.getMessage());
			config = new LevelingConfiguration();
			
		} catch (JsonParseException e) {

			MythrLogger.severe(LevelingConfiguration.class, "Failed to parse configuration.");
			MythrLogger.severe(" " + e.getClass().getSimpleName() + ":" + e.getMessage());
			config = new LevelingConfiguration();
			
		}
		
		// Set instance:
		LevelingConfiguration.config = config;
		
		// Complete:
		config.complete();
		
	}
	
	/** Unloads the configuration. */
	public static void unload()
	 {
		LevelingConfiguration.config = null;
	 }
	
}
