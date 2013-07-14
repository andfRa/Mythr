package org.andfRa.mythr.config;

import java.io.IOException;
import java.util.HashMap;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.inout.Directory;
import org.andfRa.mythr.inout.FileIO;
import org.andfRa.mythr.items.MythrDrop;
import org.andfRa.mythr.items.MythrItem;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParseException;

public class ItemConfiguration {

	/** Instance of the configuration. */
	private static ItemConfiguration config;
	
	/** No item. */
	public final static String NO_ITEM = "NONE";
	
	
	/** Item map for quick access. */
	transient private HashMap<String, MythrDrop> itemMap;
	
	/** All defined items. */
	private MythrDrop[] items;

	
	// CONSTRUCTION:
	/** Fixes all missing fields. */
	public void complete()
	 {
		if(items == null){
			MythrLogger.nullField(getClass(), "items");
			items = new MythrDrop[0];
		}
		for (int i = 0; i < items.length; i++) {
			items[i].complete();
		}
		
		// Transient:
		itemMap = new HashMap<String, MythrDrop>();
		for (int i = 0; i < items.length; i++) {
			if(itemMap.put(items[i].getName(), items[i]) != null) MythrLogger.warning(getClass(), "Found item " + items[i] + " with a duplicate name.");
		}
		
	 }
	
	
	// VALUES:
	/**
	 * Gets an item with the given name.
	 * 
	 * @param name item name
	 * @return Mythr item, null if none
	 */
	public static MythrItem getItem(String name)
	 {
		MythrDrop mdrop = config.itemMap.get(name);
		if(mdrop == null) return null;
		return mdrop.generate();
	 }
	
	/**
	 * Matches and item.
	 * 
	 * @param name name to match to
	 * @return Mythrl item, null if none
	 */
	public static MythrItem matchItem(String name)
	 {
		// Full match:
		for (int i = 0; i < config.items.length; i++) {
			if(config.items[i].getName().equalsIgnoreCase(name)) return config.items[i].generate();
		}

		// Starts with match:
		for (int i = 0; i < config.items.length; i++) {
			if(config.items[i].getName().toLowerCase().startsWith(name.toLowerCase())) return config.items[i].generate();
		}
		
		return null;
	 }
	
	public static ChatColor getDamageColour(){
		return ChatColor.GRAY;
	}
	
	public static ChatColor getRequirementsColour(){
		return ChatColor.WHITE;
	}
	
	
	// LOAD UNLOAD:
	/** Loads the configuration. */
	public static void load()
	 {
		// Create config:
		if(!FileIO.exists(Directory.ITEM_CONFIG)){

			try {
				FileIO.unpackConfig(Directory.ITEM_CONFIG);
			}
			catch (IOException e) {
				MythrLogger.severe(ItemConfiguration.class, "Failed to unpack default configuration.");
				MythrLogger.severe(" " + e.getClass().getSimpleName() + ":" + e.getMessage());
			}
			
		}
		
		// Read config:
		ItemConfiguration config;
		try {
			
			config = FileIO.readConfig(Directory.ITEM_CONFIG, ItemConfiguration.class);
			
		} catch (IOException e) {
			
			MythrLogger.severe(ItemConfiguration.class, "Failed to read configuration.");
			MythrLogger.severe(" " + e.getClass().getSimpleName() + ":" + e.getMessage());
			config = new ItemConfiguration();
			
		} catch (JsonParseException e) {

			MythrLogger.severe(ItemConfiguration.class, "Failed to parse configuration.");
			MythrLogger.severe(" " + e.getClass().getSimpleName() + ":" + e.getMessage());
			config = new ItemConfiguration();
			
		}
		
		// Set instance:
		ItemConfiguration.config = config;
		
		// Complete:
		config.complete();
	 }
	
	/** Unloads the configuration. */
	public static void unload()
	 {
		ItemConfiguration.config = null;
	 }
	
}
