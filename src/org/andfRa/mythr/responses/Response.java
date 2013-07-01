package org.andfRa.mythr.responses;

import java.util.HashMap;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.player.DerivedStats;
import org.andfRa.mythr.player.MythrPlayer;
import org.andfRa.mythr.util.LinearFunction;
import org.bukkit.entity.Creature;

public class Response {

	/** Response name. */
	private String name;

	/** Parameters values. */
	private HashMap<String, String> parameters;

	/** Linear function values. */
	private HashMap<String, LinearFunction> linVals;

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
		
		if(parameters == null) parameters = new HashMap<String, String>();
		if(linVals == null) linVals = new HashMap<String, LinearFunction>();
		
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
	 * Response effect key.
	 * 
	 * @return effect key
	 */
	public String getEffectKey()
	 { return effectKey; }
	
	/**
	 * Returns the integer value for the requested key.
	 * 
	 * @param key key
	 * @return integer value
	 * @throws NumberFormatException if the parameter value can't be parsed (configuration should be assumed correct)
	 */
	public Integer getInt(String key)
	 {
		String val = parameters.get(key);
		if(val == null){
			MythrLogger.warning(getClass(), "Response " + name + " received request for an undefined key " + key + ".");
			val = "0";
		}
		
		return Integer.parseInt(val);
	 }

	/**
	 * Returns the double value for the requested key.
	 * 
	 * @param key key
	 * @return double value
	 * @throws NumberFormatException if the parameter value can't be parsed (configuration should be assumed correct)
	 */
	public Double getDouble(String key)
	 {
		String val = parameters.get(key);
		if(val == null){
			MythrLogger.warning(getClass(), "Response " + name + " received request for an undefined key " + key + ".");
			val = "0";
		}
		
		return Double.parseDouble(val);
	 }

	/**
	 * Returns the string value for the requested key.
	 * 
	 * @param key key
	 * @return string value
	 */
	public String getString(String key)
	 {
		String val = parameters.get(key);
		if(val == null){
			MythrLogger.warning(getClass(), "Response " + name + " received request for an undefined key " + key + ".");
			return "";
		}
		
		return val;
	 }
	
	/**
	 * Returns the linear function value for the requested key.
	 * 
	 * @param key key
	 * @return linear function value
	 */
	public LinearFunction getFunction(String key)
	 {
		LinearFunction val = linVals.get(key);
		if(val == null){
			MythrLogger.warning(getClass(), "Response " + name + " received request for an undefined key " + key + ".");
			return new LinearFunction(0.0);
		}
		
		return val;
	 }

	
	// TRIGGERS:
	/**
	 * Triggers the response effect.
	 * 
	 * @param mplayer Mythr player
	 * @param dsstats derived stats
	 * @return true if successful
	 */
	public boolean castTrigger(MythrPlayer mplayer, DerivedStats dsstats)
	 {
		if(effect != null) return effect.castTrigger(this, mplayer, dsstats);
		return false;
	 }
	
	/**
	 * Called on interact.
	 * 
	 * @param mplayer Mythr player
	 * @param dsstats derived stats
	 * @return true if successful
	 */
	public boolean interactTrigger(MythrPlayer mplayer, DerivedStats dsstats)
	 {
		if(effect != null) return effect.interactTrigger(this, mplayer, dsstats);
		return false;
	 }

	/**
	 * Called on PvP.
	 * 
	 * @param mattacker Mythrl player attacker
	 * @param mdefender Mythrl player defender
	 * @param dsattacker attackers derived stats
	 * @param dsdefender defenders derived stats
	 * @return true if successful
	 */
	public boolean attackTrigger(MythrPlayer mattacker, MythrPlayer mdefender, DerivedStats dsattacker, DerivedStats dsdefender)
	 {
		if(effect != null) return effect.attackTrigger(this, mattacker, mdefender, dsattacker, dsdefender);
		return false;
	 }

	/**
	 * Called on PvC.
	 * 
	 * @param mattacker Mythrl player attacker
	 * @param mdefender creature defender
	 * @param dsattacker attackers derived stats
	 * @param dsdefender defenders derived stats
	 * @return true if successful
	 */
	public boolean attackTrigger(MythrPlayer mattacker, Creature cdefender, DerivedStats dsattacker, DerivedStats dsdefender)
	 {
		if(effect != null) return effect.attackTrigger(this, mattacker, cdefender, dsattacker, dsdefender);
		return false;
	 }

	/**
	 * Called on CvP.
	 * 
	 * @param cattacker creature attacker
	 * @param mdefender Mythr player defender
	 * @param dsattacker attackers derived stats
	 * @param dsdefender defenders derived stats
	 * @return true if successful
	 */
	public boolean attackTrigger(Creature cattacker, MythrPlayer mdefender, DerivedStats dsattacker, DerivedStats dsdefender)
	 {
		if(effect != null) return effect.attackTrigger(this, cattacker, mdefender, dsattacker, dsdefender);
		return false;
	 }

	/**
	 * Called on CvC.
	 * 
	 * @param cattacker creature attacker
	 * @param mdefender Creature defender
	 * @param dsattacker attackers derived stats
	 * @param dsdefender defenders derived stats
	 * @return true if successful
	 */
	public boolean attackTrigger(Creature cattacker, Creature cdefender, DerivedStats dsattacker, DerivedStats dsdefender)
	 {
		if(effect != null) return effect.attackTrigger(this, cattacker, cdefender, dsattacker, dsdefender);
		return false;
	 }
	
}
