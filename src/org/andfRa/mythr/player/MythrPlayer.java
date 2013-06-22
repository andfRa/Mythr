package org.andfRa.mythr.player;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.config.AttributeConfiguration;
import org.andfRa.mythr.inout.Directory;
import org.andfRa.mythr.inout.FileIO;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParseException;
import org.bukkit.entity.Player;

public class MythrPlayer {

	/** Wrapped player. */
	transient private Player player = null;

	/** Derived stats. */
	transient private DerivedStats derived;
	
	/** True if player information can be saved. */
	transient private boolean save = true;
	
	/** Player name. */
	private String name;
	
	/** Attribute scores. */
	private HashMap<String, Integer> attrib;

	/** Skill scores. */
	private HashMap<String, Integer> skills;

	/** Perks. */
	private HashMap<String, Integer> perks;
	
	
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
		this.attrib = new HashMap<String, Integer>();
		this.skills = new HashMap<String, Integer>();
		this.perks = new HashMap<String, Integer>();
		
		// Transient:
		derived = new DerivedStats() {
			@Override
			protected int getSkillScore(String skillName) { return getSkill(skillName); }
			
			@Override
			protected int getAttributeScore(String attribName) { return getAttribute(attribName); }
		};
	 }
	
	/** Fixes all missing fields. */
	private void complete() {

		if(name == null){
			name = "????";
			MythrLogger.severe(getClass(), "Received a Myth player with a null name.");
		}
		
		// Fields:
		if(attrib == null) attrib = new HashMap<String, Integer>();
		if(skills == null) attrib = new HashMap<String, Integer>();
		
		// Transient:
		derived = new DerivedStats() {
			@Override
			protected int getSkillScore(String skillName) { return getSkill(skillName); }
			
			@Override
			protected int getAttributeScore(String attribName) { return getAttribute(attribName); }
		};
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
		
		updateWeapon();
		updateArmour();
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
	
	// ATTRIBUTES:
	/**
	 * Gets the attribute score.
	 * 
	 * @param name attribute name
	 */
	public Integer getAttribute(String name)
	 {
		Integer score = attrib.get(name);
		if(score == null)  return 0;
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
		attrib.put(name, value);
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
	
	
	// SKILLS:
	/**
	 * Gets the attribute score.
	 * 
	 * @param name attribute name
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
	 }
	
	
	// PERKS:
	// TODO: Add perks.
	
	
	// DERIVED STATS:
	/** Updates derived statistics weapon. */
	public void updateWeapon()
	 {
		derived.updateWeapon(player);
	 }
	
	/** Updates derived statistics armour. */
	public void updateArmour()
	 {
		derived.updateArmour(player);
	 }
	
	/**
	 * Gets the derived stats.
	 * 
	 * @return derived stats
	 */
	public DerivedStats getDerived()
	 { return derived; }
	

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
            MythrLogger.severe(this, "Player data save failure for " + name + ".");
            MythrLogger.severe(MythrPlayer.class, " " + e.getClass().getSimpleName() + ":" + e.getMessage());
        }
	 }
	
}
