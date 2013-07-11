package org.andfRa.mythr.config;

import java.io.IOException;
import java.util.HashMap;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.creatures.MythrCreature;
import org.andfRa.mythr.inout.Directory;
import org.andfRa.mythr.inout.FileIO;
import org.andfRa.mythr.player.DerivedStats;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParseException;
import org.bukkit.entity.Creature;

public class CreatureConfiguration {
	
	/** Instance of the configuration. */
	private static CreatureConfiguration config;
	

	/** Default attributes. */
	private Integer defaultAttribScore;

	/** Default skills. */
	private Integer defaultSkillScore;
	

	/** Creature map for quick access. */
	transient private HashMap<String, MythrCreature> creatureMap;
	
	/** All defined creatures. */
	private MythrCreature[] creatures;
	
	
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
		
		if(creatures == null){
			MythrLogger.nullField(getClass(), "creatures");
			creatures = new MythrCreature[0];
		}
		for (int i = 0; i < creatures.length; i++) {
			creatures[i].complete();
		}
		
		// Transient:
		creatureMap = new HashMap<String, MythrCreature>();
		for (int i = 0; i < creatures.length; i++) {
			if(creatureMap.put(creatures[i].getName(), creatures[i]) != null) MythrLogger.warning(getClass(), "Found creature " + creatures[i] + " with a duplicate name.");
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
	
	
	/**
	 * Gets derived stats for the creature.
	 * 
	 * @param creature creature
	 * @return derived stats, default stats if none
	 */
	public static DerivedStats calcDerived(Creature creature)
	 {
		String name = creature.getCustomName();
		MythrCreature mcreature = config.creatureMap.get(name);
		
		// Default creature:
		if(mcreature == null){
			return MythrCreature.calcDefaultDerived(creature.getEquipment());
		}
		
		// Custom creature:
		return mcreature.calcDerived(creature.getEquipment());
	 }
	
	/**
	 * Matches a creatures.
	 * 
	 * @param name name to match to
	 * @return Mythrl creature, null if none
	 */
	public static MythrCreature matchCreature(String name)
	 {
		// Full match:
		for (int i = 0; i < config.creatures.length; i++) {
			if(config.creatures[i].getName().equalsIgnoreCase(name)) return config.creatures[i];
		}

		// Starts with match:
		for (int i = 0; i < config.creatures.length; i++) {
			if(config.creatures[i].getName().toLowerCase().startsWith(name.toLowerCase())) return config.creatures[i];
		}
		
		return null;
	 }
	
	
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
