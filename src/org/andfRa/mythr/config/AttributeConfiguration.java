package org.andfRa.mythr.config;

import java.io.IOException;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.inout.Directory;
import org.andfRa.mythr.inout.FileIO;
import org.andfRa.mythr.player.Attribute;
import org.andfRa.mythr.util.LinearFunction;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParseException;

public class AttributeConfiguration {
	
	/** Instance of the configuration. */
	private static AttributeConfiguration config;
	
	
	/** Number of available attribute points based on level. */
	private LinearFunction attribPoints;
	
	/** Range for which the cost remains constant. */
	private Integer costCostRange;
	
	/** Attributes. */
	private Attribute[] attributes;
	

	// CONSTRUCTION:
	/** Fixes all missing fields. */
	public void complete()
	 {
		if(attribPoints == null){
			MythrLogger.nullField(getClass(), "attribPoints");
			attribPoints = new LinearFunction(0.0);
		}
		attribPoints.complete();
		
		if(costCostRange == null){
			MythrLogger.nullField(getClass(), "costCostRange");
			costCostRange = 6;
		}
		
		if(attributes == null){
			MythrLogger.nullField(getClass(), "attributes");
			attributes = new Attribute[0];
		}

	 }
	
	
	// VALUES:
	/**
	 * Gets the range at which the attribute point cost remains constant.
	 * 
	 * @return constant cost range
	 */
	public static Integer getCostCostRange() {
		return config.costCostRange;
	}
	
	/**
	 * Gets the number of available attribute points.
	 * 
	 * @param level player level
	 * @return attribute points
	 */
	public static int getAttribPoints(Integer level) {
		return config.attribPoints.yIntFloor(level);
	}
	
	/**
	 * Gets attributes.
	 * 
	 * @return attributes
	 */
	public static Attribute[] getAttributes()
	 { return config.attributes; }
	
	/**
	 * Gets the attribute count.
	 * 
	 * @return attribute count
	 */
	public static int getAttrCount()
	 {
		return config.attributes.length;
	 }
	
	/**
	 * Gets attribute names.
	 * 
	 * @return attribute names
	 */
	public static String[] getAttrNames()
	 {
		String[] result = new String[config.attributes.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = config.attributes[i].getName();
		}
		return result;
	 }
	
	/**
	 * Gets attribute abbreviations.
	 * 
	 * @return attribute abbreviations
	 */
	public static String[] getAttrAbbreviations()
	 {
		String[] result = new String[config.attributes.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = config.attributes[i].getAbbrev();
		}
		return result;
	 }
	
	
	/**
	 * Checks if the attribute exists.
	 * 
	 * @param name attribute name
	 * @return true if exists
	 */
	public static boolean checkAttribute(String name)
	 {
		for (int i = 0; i < config.attributes.length; i++) {
			if(config.attributes[i].getName().equalsIgnoreCase(name)) return true;
		}
		return false;
	 }
	
	
	// LOAD UNLOAD:
	/** Loads the configuration. */
	public static void load(){


		// Create config:
		if(!FileIO.exists(Directory.ATTRIBUTE_CONFIG)){

			try {
				FileIO.unpackConfig(Directory.ATTRIBUTE_CONFIG);
			}
			catch (IOException e) {
				MythrLogger.severe(AttributeConfiguration.class, "Failed to unpack default configuration.");
				MythrLogger.severe(" " + e.getClass().getSimpleName() + ":" + e.getMessage());
			}
			
		}
		
		// Read config:
		AttributeConfiguration config;
		try {
			
			config = FileIO.readConfig(Directory.ATTRIBUTE_CONFIG, AttributeConfiguration.class);
			
		} catch (IOException e) {
			
			MythrLogger.severe(AttributeConfiguration.class, "Failed to read configuration." + e.getClass().getSimpleName());
			MythrLogger.severe(" " + e.getClass().getSimpleName() + ":" + e.getMessage());
			config = new AttributeConfiguration();
			
		} catch (JsonParseException e) {

			MythrLogger.severe(AttributeConfiguration.class, "Failed to parse configuration.");
			MythrLogger.severe(" " + e.getClass().getSimpleName() + ":" + e.getMessage());
			config = new AttributeConfiguration();
			
		}
		
		// Set instance:
		AttributeConfiguration.config = config;
		
		// Complete:
		config.complete();
		
	}
	
	/** Unloads the configuration. */
	public static void unload()
	 {
		AttributeConfiguration.config = null;
	 }
	
}
