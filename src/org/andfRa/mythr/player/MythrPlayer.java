package org.andfRa.mythr.player;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.config.AttributeConfiguration;
import org.andfRa.mythr.config.SkillConfiguration;
import org.andfRa.mythr.config.VanillaConfiguration;
import org.andfRa.mythr.inout.Directory;
import org.andfRa.mythr.inout.FileIO;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParseException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class MythrPlayer {

	/** Wrapped player. */
	transient private Player player = null;

	/** Derived stats. */
	transient private DerivedStats[] derived;

	/** True if player information can be saved. */
	transient private boolean save = true;

	
	/** Player name. */
	private String name;

	/** Player level. */
	private Integer level;
	
	/** Attribute scores. */
	private HashMap<String, Integer> attribs;

	/** Skill scores. */
	private HashMap<String, Integer> skills;

	/** Perks. */
	private HashMap<Integer, ArrayList<String>> perks;
	
	
	// INITIALISE:
	/** Restrict access to no arguments constructor. */
	@SuppressWarnings("unused")
	private MythrPlayer() { }
	
	/**
	 * Creates a Mythr player.
	 * 
	 * @param name player name
	 */
	public MythrPlayer(String name)
	 {
		// Fields:
		this.name = name;
		this.level = 0;
		this.attribs = new HashMap<String, Integer>();
		this.skills = new HashMap<String, Integer>();
		this.perks = new HashMap<Integer, ArrayList<String>>();
		
		// Transient:
		derived = new DerivedStats[VanillaConfiguration.QUICKBAR_SLOT_COUNT];
		for (int i = 0; i < derived.length; i++) derived[i] = new DerivedStats();
	 }
	
	/** Fixes all missing fields. */
	private void complete()
	 {

		if(name == null){
			name = "????";
			MythrLogger.severe(getClass(), "Received a Myth player with a null name.");
		}
		if(level == null) level = 0;
		
		// Fields:
		if(attribs == null) attribs = new HashMap<String, Integer>();
		if(skills == null) attribs = new HashMap<String, Integer>();
		if(perks == null) perks = new HashMap<Integer, ArrayList<String>>();
		
		// Transient:
		derived = new DerivedStats[VanillaConfiguration.QUICKBAR_SLOT_COUNT];
		for (int i = 0; i < derived.length; i++) derived[i] = new DerivedStats();
	 }
	
	
	// PLAYER:
	/**
	 * Wraps the player.
	 * 
	 * @param player player
	 */
	public void wrapPlayer(Player player)
	 {
		this.player = player;
		updateAll();
	 }
	
	/**
	 * Unwraps the player.
	 * 
	 */
	public void unwrap() {
		this.player = null;
	}
	
	/**
	 * Gets wrapped player.
	 * 
	 * @return player, null if not wrapped
	 */
	public Player getPlayer()
	 { return player; }
	
	/**
	 * Gets players name.
	 * 
	 * @return player name
	 */
	public String getName() 
	 { return name; }
	
	
	// LEVEL:
	/**
	 * Gets Mythr player level.
	 * 
	 * @return player level
	 */
	public Integer getLevel()
	 { return level; }
	
	/**
	 * Sets Mythr player level.
	 * 
	 * @param level level to set
	 */
	public void setLevel(Integer level)
	 { this.level = level; }
	
	
	// ATTRIBUTES:
	/**
	 * Gets the attribute score.
	 * 
	 * @param name attribute name
	 */
	public Integer getAttribute(String name)
	 {
		Integer score = attribs.get(name);
		if(score == null) return 0;
		return score;
	 }
	
	/**
	 * Gets the attribute score.
	 * 
	 * @param name attribute name
	 * @param value attribute score
	 */
	public void setAttribute(String name, Integer value)
	 {
		attribs.put(name, value);
		updateAll();
	 }
	
	/**
	 * Gets players attribute scores.
	 * 
	 * @return players attribute scores
	 */
	public int[] getAttrbutes()
	 { 
		String[] names = AttributeConfiguration.getAttrNames();
		int[] scores = new int[names.length];
		for (int i = 0; i < scores.length; i++) {
			scores[i] = getAttribute(names[i]);
		}
		return scores;
	 }
	
	
	/**
	 * Gets the amount of available attribute points.
	 * 
	 * @return available attribute points
	 */
	public Integer getAvailableAttribs()
	 {
		return AttributeConfiguration.getAttribPoints(level);
	 }

	/**
	 * Gets the amount of used attribute points.
	 * 
	 * @return used attribute points
	 */
	public Integer getUsedAttribs()
	 {
		int total = 0;
		
		String[] names = AttributeConfiguration.getAttrNames();
		for (int i = 0; i < names.length; i++) {
			int score = getAttribute(names[i]);
			total+= AttributeConfiguration.calcCost(score);
		}
		
		return total;
	 }

	/**
	 * Gets the amount of remaining attribute points.
	 * 
	 * @return remaining attribute points
	 */
	public Integer getRemainingAttribs()
	 {
		return getAvailableAttribs() - getUsedAttribs();
	 }
	
	
	// SKILLS:
	/**
	 * Gets the skill score.
	 * 
	 * @param name skill name
	 */
	public Integer getSkill(String name)
	 {
		Integer score = skills.get(name);
		if(score == null)  return 0;
		return score;
	 }

	/**
	 * Gets the skill score.
	 * 
	 * @param name skill name
	 * @param value skill score
	 */
	public void setSkill(String name, Integer value)
	 {
		skills.put(name, value);
		updateAll();
	 }
	
	
	/**
	 * Gets the amount of available skill points.
	 * 
	 * @return available skill points
	 */
	public Integer getAvailableSkills()
	 {
		return SkillConfiguration.getSkillPoints(level);
	 }

	/**
	 * Gets the amount of used skill points.
	 * 
	 * @return used skill points
	 */
	public Integer getUsedSkills()
	 {
		int total = 0;
		
		String[] names = SkillConfiguration.getSkillNames();
		for (int i = 0; i < names.length; i++) {
			total+= getSkill(names[i]);
		}
		
		return total;
	 }

	/**
	 * Gets the amount of remaining skill points.
	 * 
	 * @return remaining skill points
	 */
	public Integer getRemainingSkills() {
		return getAvailableSkills() - getUsedSkills();
	}
	
	
	// PERKS:
	/**
	 * Collects all perks the player has.
	 * 
	 * @return all perks
	 */
	public ArrayList<String> collectAllPerks()
	 {
		ArrayList<String> allPerks = new ArrayList<String>();
		Collection<ArrayList<String>> values = perks.values();
		for (ArrayList<String> value : values) {
			allPerks.addAll(value);
		}
		
		return allPerks;
	 }
	
	
	// DERIVED STATS:
	/**
	 * Updates derived stats for held item.
	 * 
	 */
	public void updateHeld()
	 {
		int slot = player.getInventory().getHeldItemSlot();
		PlayerInventory inventory = player.getInventory();
		derived[slot].update(attribs, skills, collectAllPerks(), inventory.getItemInHand(), inventory.getHelmet(), inventory.getChestplate(), inventory.getLeggings(), inventory.getBoots());
		derived[slot].assignHealth(player);
	 }
	
	/**
	 * Updates all derived stats.
	 * 
	 */
	public void updateAll()
	 {
		PlayerInventory inventory = player.getInventory();
		for (int i = 0; i < VanillaConfiguration.QUICKBAR_SLOT_COUNT; i++) {
			int slot = i;
			derived[slot].update(attribs, skills, collectAllPerks(), inventory.getItem(slot), inventory.getHelmet(), inventory.getChestplate(), inventory.getLeggings(), inventory.getBoots());
		}
		derived[inventory.getHeldItemSlot()].assignHealth(player);
	 }
	
	/**
	 * Gets the derived stats.
	 * 
	 * @return derived stats
	 */
	public DerivedStats getDerived()
	 {
		int slot = player.getInventory().getHeldItemSlot();
		return derived[slot];
	 }
	

	// MESSAGES:
	/**
	 * Sends an message.
	 * 
	 * @param message message to send
	 */
	public void message(String message)
	 {
		if(player != null) player.sendMessage(message);
	 }
	
	/**
	 * Sends a positive message.
	 * 
	 * @param message message to send
	 */
	public void positive(String message)
	 {
		message(ChatColor.GREEN + message);
	 }

	/**
	 * Sends a negative message.
	 * 
	 * @param message message to send
	 */
	public void negative(String message)
	 {
		message(ChatColor.RED + message);
	 }
	
	/**
	 * Sends an error message.
	 * 
	 * @param message message
	 */
	public void error(String message)
	 {
		message(ChatColor.DARK_RED + "Error! " + message);
	 }
	
	
	// SAVING AND LOADING:
	/**
	 * Loads the Mythr player.
	 * 
	 * @param name player name
	 * @return Mythr player player
	 */
	public static MythrPlayer load(String name)
	 {
		// Try loading:
		MythrPlayer mplayer;
		
		// New players:
		if(!FileIO.exists(Directory.PLAYER_DATA, name.toLowerCase())){
		
			mplayer = new MythrPlayer(name);
			mplayer.save();
			return mplayer;
		
		}
		
		// Try loading:
		try {

			mplayer = FileIO.readData(Directory.PLAYER_DATA, name.toLowerCase(), MythrPlayer.class);
			mplayer.complete();
		
		} catch (FileNotFoundException e) {

			MythrLogger.info(MythrPlayer.class, "Player data file not found for " + name + ".");
			mplayer = new MythrPlayer(name);

		} catch (IOException e) {

			MythrLogger.severe(MythrPlayer.class, "Player data read failure for " + name + ".");
			MythrLogger.severe(MythrPlayer.class, " " + e.getClass().getSimpleName() + ":" + e.getMessage());
			MythrLogger.info("Disabling player data saving for " + name + ".");
			mplayer= new MythrPlayer(name);
			mplayer.save = false;

		} catch (JsonParseException e) {

			MythrLogger.severe(MythrPlayer.class, "Player data parse failure for " + name + ".");
			MythrLogger.severe(MythrPlayer.class, " " + e.getClass().getSimpleName() + ":" + e.getMessage());
			MythrLogger.info("Disabling player data saving for " + name + ".");
			mplayer= new MythrPlayer(name);
			mplayer.save = false;

		}

		return mplayer;

	}
	
	/**
	 * Unloads Mythr player.
	 */
	public void unload()
	 {
		save();
	 }

	/**
	 * Saves Mythr player.
	 */
	public void save()
	 {
		if(!save){
			MythrLogger.warning(MythrPlayer.class, "Denied player data save request for " + name + ".");
			return;
		}

        try {
            FileIO.writeData(Directory.PLAYER_DATA, name.toLowerCase(), this);
        } catch (Throwable e) {
            MythrLogger.severe(MythrPlayer.class, "Player data save failure for " + name + ".");
            MythrLogger.severe(MythrPlayer.class, " " + e.getClass().getSimpleName() + ":" + e.getMessage());
        }
	 }
	
}
