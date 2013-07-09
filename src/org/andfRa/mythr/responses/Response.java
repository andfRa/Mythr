package org.andfRa.mythr.responses;

import java.util.HashMap;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.player.DerivedStats;
import org.andfRa.mythr.player.MythrPlayer;
import org.andfRa.mythr.util.LinearFunction;
import org.bukkit.entity.LivingEntity;

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
		ResponseEffect eff = effects.get(effectKey);
		if(eff == null) MythrLogger.warning(getClass(), "Failed to assign response effect for " + effectKey + " key.");
		effect = eff;
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
	 * Checks if the response has a parameter with the given key.
	 * 
	 * @param key parameter key
	 * @return true if has parameter
	 */
	public boolean hasParameter(String key)
	 {
		return parameters.containsKey(key);
	 }
	
	/**
	 * Returns the boolean value for the requested key.
	 * 
	 * @param key key
	 * @return boolean value
	 */
	public boolean getBoolean(String key)
	 {
		String val = parameters.get(key);
		if(val == null){
			MythrLogger.warning(getClass(), "Response " + name + " received request for an undefined key " + key + ".");
			val = "false";
		}
		return val.equalsIgnoreCase("true");
	 }
	
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
	 * Triggers the passive modifications.
	 * 
	 * @param mplayer Mythr player
	 * @param dsstats derived stats
	 * @return true if successful
	 */
	public boolean passiveTrigger(DerivedStats dsstats)
	 {
		if(effect != null) return effect.passiveTrigger(this, dsstats);
		return false;
	 }
	
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
	 * Called on attack.
	 * 
	 * @param mattacker living attacker
	 * @param mdefender living defender
	 * @param dsattacker attackers derived stats
	 * @param dsdefender defenders derived stats
	 * @return true if successful
	 */
	public boolean attackTrigger(LivingEntity lattacker, LivingEntity ldefender, DerivedStats dsattacker, DerivedStats dsdefender)
	 {
		if(effect != null) return effect.attackTrigger(this, lattacker, ldefender, dsattacker, dsdefender);
		return false;
	 }

}
