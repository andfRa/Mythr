package org.andfRa.mythr.config;

import java.io.IOException;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.inout.Directory;
import org.andfRa.mythr.inout.FileIO;
import org.andfRa.mythr.player.Skill;
import org.andfRa.mythr.util.LinearFunction;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParseException;

public class SkillConfiguration {

	/** Instance of the configuration. */
	private static SkillConfiguration config;
	

	/** Number of available skill points based on level. */
	private LinearFunction skillPoints;
	
	/** Skills. */
	private Skill[] skills;
	

	// CONSTRUCTION:
	/** Fixes all missing fields. */
	public void complete()
	 {
		if(skillPoints == null){
			MythrLogger.nullField(getClass(), "skillPoints");
			skillPoints = new LinearFunction(0.0);
		}
		
		if(skills == null){
			MythrLogger.nullField(getClass(), "skills");
			skills = new Skill[0];
		}
		
		
		
	 }
	
	
	// VALUES:
	/**
	 * Gets the number of available skill points.
	 * 
	 * @param level player level
	 * @return skill points
	 */
	public static int getSkillPoints(Integer level) {
		return config.skillPoints.yIntFloor(level);
	}
	
	
	/**
	 * Gets skills.
	 * 
	 * @return skills
	 */
	public static Skill[] getSkills()
	 { return config.skills; }
	
	/**
	 * Gets the skill count.
	 * 
	 * @return skill count
	 */
	public static int getSkillCount()
	 {
		return config.skills.length;
	 }
	
	/**
	 * Gets skill names.
	 * 
	 * @return skill names
	 */
	public static String[] getSkillNames()
	 {
		String[] result = new String[config.skills.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = config.skills[i].getName();
		}
		return result;
	 }
	

	/**
	 * Checks if the skill exists.
	 * 
	 * @param name skill name
	 * @return true if exists
	 */
	public static boolean checkSkill(String name)
	 {
		for (int i = 0; i < config.skills.length; i++) {
			if(config.skills[i].getName().equalsIgnoreCase(name)) return true;
		}
		return false;
	 }
	
	/**
	 * Gets the skill index.
	 * 
	 * @param name skill name
	 * @return skill index, -1 if none
	 */
	public static int getSkillIndex(String name)
	 {
		for (int i = 0; i < config.skills.length; i++) {
			if(config.skills[i].getName().equals(name)) return i;
		}
		
		return -1;
	 }
	
	
	// LOAD UNLOAD:
	/** Loads the configuration. */
	public static void load(){


		// Create config:
		if(!FileIO.exists(Directory.SKILL_CONFIG)){

			try {
				FileIO.unpackConfig(Directory.SKILL_CONFIG);
			}
			catch (IOException e) {
				MythrLogger.severe(SkillConfiguration.class, "Failed to unpack default configuration.");
				MythrLogger.severe(" " + e.getClass().getSimpleName() + ":" + e.getMessage());
			}
			
		}
		
		// Read config:
		SkillConfiguration config;
		try {
			
			config = FileIO.readConfig(Directory.SKILL_CONFIG, SkillConfiguration.class);
			
		} catch (IOException e) {
			
			MythrLogger.severe(SkillConfiguration.class, "Failed to read configuration.");
			MythrLogger.severe(" " + e.getClass().getSimpleName() + ":" + e.getMessage());
			config = new SkillConfiguration();
			
		} catch (JsonParseException e) {

			MythrLogger.severe(SkillConfiguration.class, "Failed to parse configuration.");
			MythrLogger.severe(" " + e.getClass().getSimpleName() + ":" + e.getMessage());
			config = new SkillConfiguration();
			
		}
		
		// Set instance:
		SkillConfiguration.config = config;
		
		// Complete:
		config.complete();
		
	}
	
	/** Unloads the configuration. */
	public static void unload()
	 {
		SkillConfiguration.config = null;
	 }
	
}
