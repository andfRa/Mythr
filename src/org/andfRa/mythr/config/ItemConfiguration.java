package org.andfRa.mythr.config;

import java.io.IOException;
import java.util.HashMap;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.inout.Directory;
import org.andfRa.mythr.inout.FileIO;
import org.andfRa.mythr.items.MythrItem;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParseException;

public class ItemConfiguration {

	/** Instance of the configuration. */
	private static ItemConfiguration config;
	
	
	/** Item map for quick access. */
	transient private HashMap<String, MythrItem> itemMap;
	
	/** All defined items. */
	private MythrItem[] items;

	// CONSTRUCTION:
	/** Fixes all missing fields. */
	public void complete()
	 {
		if(items == null){
			MythrLogger.nullField(getClass(), "items");
			items = new MythrItem[0];
		}
		for (int i = 0; i < items.length; i++) {
			items[i].complete();
		}
		
		// Transient:
		for (int i = 0; i < items.length; i++) {
			if(itemMap.put(items[i].getName(), items[i]) != null) MythrLogger.warning(getClass(), "Found item " + items[i] + " with a duplicate name.");
		}
		
	 }
	
	
	// VALUES:
	/**
	 * Gets an item with the given name.
	 * 
	 * @param name item name
	 * @return Mythr item
	 */
	public static MythrItem getItem(String name) {
		return config.itemMap.get(name);
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
