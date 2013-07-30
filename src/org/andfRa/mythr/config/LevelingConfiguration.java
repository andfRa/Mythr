package org.andfRa.mythr.config;

import java.io.IOException;

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
