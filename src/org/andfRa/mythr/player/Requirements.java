package org.andfRa.mythr.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class Requirements {

	/** Required attributes. */
	private HashMap<String, Integer> attributes;
	
	/** Required skills. */
	private HashMap<String, Integer> skills;
	
	/** Required perks. */
	private ArrayList<String> perks;
	
	
	/** Fixes all missing fields. */
	public void complete()
	 {
		if(attributes == null) attributes = new HashMap<String, Integer>();
		if(skills == null) skills = new HashMap<String, Integer>();
		if(perks == null) perks = new ArrayList<String>();
	 }
	
	
	// CHECKS:
	/**
	 * Checks requirements.
	 * 
	 * @param mplayer Mythr player
	 * @return true if requirements are met
	 */
	public boolean check(MythrPlayer mplayer)
	 {
		Set<Entry<String, Integer>> entries = attributes.entrySet();
		for (Entry<String, Integer> entry : entries) {
			if(mplayer.getAttribute(entry.getKey()) < entry.getValue()) return false;
		}
		
		entries = skills.entrySet();
		for (Entry<String, Integer> entry : entries) {
			if(mplayer.getSkill(entry.getKey()) < entry.getValue()) return false;
		}
		
		ArrayList<String> allPerks = mplayer.collectAllPerks();
		for (String perk : perks) {
			if(!allPerks.contains(perk)) return false;
		}
		
		return true;
	 }

	
	// VALUES:
	/**
	 * Gets the attributes.
	 * 
	 * @return the attributes
	 */
	public HashMap<String, Integer> getAttributes()
	 { return attributes; }

	/**
	 * Gets the skills.
	 * 
	 * @return the skills
	 */
	public HashMap<String, Integer> getSkills()
	 { return skills; }

	/**
	 * Gets the perks.
	 * 
	 * @return the perks
	 */
	public ArrayList<String> getPerks()
	 { return perks; }
	
}
