package org.andfRa.mythr.config;

import java.io.IOException;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.inout.Directory;
import org.andfRa.mythr.inout.FileIO;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParseException;

public class CreatureConfiguration {
	
	/** Instance of the configuration. */
	private static CreatureConfiguration config;
	

	/** Default attributes. */
	private Integer defaultAttribScore;

	/** Default skills. */
	private Integer defaultSkillScore;
	

	// CONSTRUCTION:
	/** Fixes all missing fields. */
	public void complete()
	 {
		if(defaultAttribScore == null){
			MythrLogger.nullField(getClass(), "defaultAttribScore");
			defaultAttribScore = 0;
		}
		
		if(defaultSkillScore == null){
			MythrLogger.nullField(getClass(), "defaultSkillScore");
			defaultSkillScore = 0;
		}
	 }
	
	
	// VALUES:
	/**
	 * Gets the default attribute score.
	 * 
	 * @return default attribute score
	 */
	public static int getDefaultAttribScore()
	 { return config.defaultAttribScore; }
	
	/**
	 * Gets the default skill score.
	 * 
	 * @return default skill score
	 */
	public static int getDefaultSkillScore()
	 { return config.defaultSkillScore; }
	
	
	// LOAD UNLOAD:
	/** Loads the configuration. */
	public static void load(){


		// Create config:
		if(!FileIO.exists(Directory.CREATURE_CONFIG)){

			try {
				FileIO.unpackConfig(Directory.CREATURE_CONFIG);
			}
			catch (IOException e) {
				MythrLogger.severe(CreatureConfiguration.class, "Failed to unpack default configuration.");
				MythrLogger.severe(" " + e.getClass().getSimpleName() + ":" + e.getMessage());
			}
			
		}
		
		// Read config:
		CreatureConfiguration config;
		try {
			
			config = FileIO.readConfig(Directory.CREATURE_CONFIG, CreatureConfiguration.class);
			
		} catch (IOException e) {
			
			MythrLogger.severe(CreatureConfiguration.class, "Failed to read configuration.");
			MythrLogger.severe(" " + e.getClass().getSimpleName() + ":" + e.getMessage());
			config = new CreatureConfiguration();
			
		} catch (JsonParseException e) {

			MythrLogger.severe(CreatureConfiguration.class, "Failed to parse configuration.");
			MythrLogger.severe(" " + e.getClass().getSimpleName() + ":" + e.getMessage());
			config = new CreatureConfiguration();
			
		}
		
		// Set instance:
		CreatureConfiguration.config = config;
		
		// Complete:
		config.complete();
		
	}
	
	/** Unloads the configuration. */
	public static void unload()
	 {
		CreatureConfiguration.config = null;
	 }
	
}
