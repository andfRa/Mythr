package org.andfRa.mythr.config;

import java.io.IOException;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.inout.Directory;
import org.andfRa.mythr.inout.FileIO;
import org.andfRa.mythr.player.Skill;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParseException;

public class SkillConfiguration {

	/** Instance of the configuration. */
	private static SkillConfiguration config;
	
	
	/** Skills. */
	private Skill[] skills;
	

	// CONSTRUCTION:
	/** Fixes all missing fields. */
	public void complete()
	 {
		if(skills == null){
			MythrLogger.nullField(getClass(), "skills");
			skills = new Skill[0];
		}
		
		
		
	 }
	
	
	// VALUES:
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
	public static int getAttrCount()
	 {
		return config.skills.length;
	 }
	
	/**
	 * Gets skill names.
	 * 
	 * @return skill names
	 */
	public static String[] getAttrNames()
	 {
		String[] result = new String[config.skills.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = config.skills[i].getName();
		}
		return result;
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
			
			MythrLogger.severe(SkillConfiguration.class, "Failed to read configuration." + e.getClass().getSimpleName());
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
