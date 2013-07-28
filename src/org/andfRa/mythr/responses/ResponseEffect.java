package org.andfRa.mythr.responses;

import org.andfRa.mythr.player.DerivedStats;
import org.andfRa.mythr.player.MythrPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public abstract class ResponseEffect {

	/** Cast ticks key. */
	final public static String CAST_TICKS_KEY = "CAST_TICKS";

	/** Attribute used for checks key. */
	final public static String ATTRIBUTE_KEY = "ATTRIBUTE";

	/** Attack score modifier key. */
	final public static String ATTACK_SCORE_MODIFIER_KEY = "MODIFIER";

	
	/**
	 * Response key.
	 * 
	 * @return response key
	 */
	public abstract String key();
	

	/**
	 * Triggers the passive modifications.
	 * 
	 * @param response response
	 * @param mplayer Mythr player
	 * @param dsstats derived stats
	 * @return true if successful
	 */
	public boolean passiveTrigger(Response response, DerivedStats dsstats)
	 { return false; }
	
	/**
	 * Triggers the response effect.
	 * 
	 * @param response response
	 * @param mplayer Mythr player
	 * @param dsstats derived stats
	 * @return true if successful
	 */
	public boolean castTrigger(Response response, MythrPlayer mplayer, DerivedStats dsstats)
	 { return false; }
	
	/**
	 * Called on effect.
	 * 
	 * @param response response
	 * @param mplayer Mythr player
	 * @param dsstats derived stats
	 * @return true if successful
	 */
	public boolean effectTrigger(Response response, MythrPlayer mplayer, DerivedStats dsstats)
	 { return false; }

	/**
	 * Called on item focus.
	 * 
	 * @param response response
	 * @param mplayer Mythr player
	 * @param item item that gained focus
	 * @param dsstats derived stats
	 * @return true if successful
	 */
	public boolean focusTrigger(Response response, MythrPlayer mplayer, ItemStack item, DerivedStats dsstats)
	 { return false; }

	/**
	 * Called on attack.
	 * 
	 * @param response response
	 * @param mattacker living attacker
	 * @param mdefender living defender
	 * @param dsattacker attackers derived stats
	 * @param dsdefender defenders derived stats
	 * @return true if successful
	 */
	public boolean attackTrigger(Response response, LivingEntity lattacker, LivingEntity ldefender, DerivedStats dsattacker, DerivedStats dsdefender)
	 { return false; }

}
