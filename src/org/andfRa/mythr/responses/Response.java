package org.andfRa.mythr.responses;

import java.util.HashMap;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.player.DerivedStats;
import org.andfRa.mythr.player.MythrPlayer;
import org.bukkit.entity.Creature;

public class Response {

	/** Response name. */
	private String name;

	/** Attribute used to calculate effectiveness. */
	private String attribute = null;

	/** Attack or defence base rating. */
	private Integer baseRating = null;

	/** Attack or defence base modifier. */
	private Integer ratingMod;

	/** Response effect key. */
	private String effectKey = null;

	
	/** Response effect. */
	transient private ResponseEffect effect = null;
	
	
	// CONSTRUCTION:
	/** Fixes all missing fields. */
	public void complete()
	 {
		if(name == null){
			MythrLogger.nullField(getClass(), "name");
			name = "????";
		}
		
		// attribute can be null
		// baseRating can be null
		if(ratingMod == null) ratingMod = 0;

		if(effectKey == null){
			MythrLogger.nullField(getClass(), "effect");
			effectKey = "????";
		}
		
	 }
	
	/**
	 * Assigns effects.
	 * 
	 * @param effects effects
	 */
	public void assign(HashMap<String, ResponseEffect> effects)
	 {
		effect = effects.get(effectKey);
	 }
	
	
	// VALUES:
	/**
	 * Gets response name.
	 * 
	 * @return response name
	 */
	public String getName()
	 { return name; }

	/**
	 * Gets the attribute to calculate rating.
	 * 
	 * @return attribute, null if none
	 */
	public String getAttribute()
	 { return attribute; }
	
	/**
	 * Gets the base rating.
	 * 
	 * @return the base rating, null if none
	 */
	public Integer getBaseRating()
	 { return baseRating; }

	/**
	 * Gets the rating modifier.
	 * 
	 * @return the rating modifier
	 */
	public Integer getRatingMod()
	 { return ratingMod; }
	
	
	// TRIGGERS:
	/**
	 * Called on interact.
	 * 
	 * @param dsstats derived stats
	 */
	public void interactTrigger(MythrPlayer mplayer, DerivedStats dsstats)
	 {
		if(effect != null) effect.interactTrigger(this, mplayer, dsstats);
	 }

	/**
	 * Called on attack.
	 * 
	 * @param mattacker Mythrl player attacker
	 * @param mdefender Mythrl player defender
	 * @param dsattacker attackers derived stats
	 * @param dsdefender defenders derived stats
	 */
	public void attackTrigger(MythrPlayer mattacker, MythrPlayer mdefender, DerivedStats dsattacker, DerivedStats dsdefender)
	 {
		if(effect != null) effect.attackTrigger(this, mattacker, mdefender, dsattacker, dsdefender);
	 }

	/**
	 * Called on attack.
	 * 
	 * @param mattacker Mythrl player attacker
	 * @param mdefender creature defender
	 * @param dsattacker attackers derived stats
	 * @param dsdefender defenders derived stats
	 */
	public void attackTrigger(MythrPlayer mattacker, Creature cdefender, DerivedStats dsattacker, DerivedStats dsdefender)
	 {
		if(effect != null) effect.attackTrigger(this, mattacker, cdefender, dsattacker, dsdefender);
	 }

	/**
	 * Called on defend.
	 * 
	 * @param mattacker Mythrl player attacker
	 * @param mdefender Mythrl player defender
	 * @param dsattacker attackers derived stats
	 * @param dsdefender defenders derived stats
	 */
	public void defendTrigger(MythrPlayer mattacker, MythrPlayer mdefender, DerivedStats dsattacker, DerivedStats dsdefender)
	 {
		if(effect != null) effect.defendTrigger(this, mattacker, mdefender, dsdefender, dsdefender);
	 }

	/**
	 * Called on defend.
	 * 
	 * @param mattacker Mythrl player attacker
	 * @param mdefender creature defender
	 * @param dsattacker attackers derived stats
	 * @param dsdefender defenders derived stats
	 */
	public void defendTrigger(MythrPlayer mattacker, Creature cdefender, DerivedStats dsattacker, DerivedStats dsdefender)
	 {
		if(effect != null) effect.defendTrigger(this, mattacker, cdefender, dsdefender, dsdefender);
	 }
	
}
