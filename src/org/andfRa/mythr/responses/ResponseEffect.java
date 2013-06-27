package org.andfRa.mythr.responses;

import org.andfRa.mythr.player.DerivedStats;
import org.andfRa.mythr.player.MythrPlayer;
import org.bukkit.entity.Creature;

public abstract class ResponseEffect {

	/** Cast ticks key. */
	final public static String CAST_TICKS_KEY = "CAST_TICKS";
	
	
	/**
	 * Response key.
	 * 
	 * @return response key
	 */
	public abstract String key();
	
	
	/**
	 * Triggers the response effect.
	 * 
	 * @param response response
	 * @param dsstats derived stats
	 */
	public void castTrigger(Response response, MythrPlayer mplayer, DerivedStats dsstats)
	 { }
	
	/**
	 * Called on interact.
	 * 
	 * @param response response
	 * @param dsstats derived stats
	 */
	public void interactTrigger(Response response, MythrPlayer mplayer, DerivedStats dsstats)
	 { }

	/**
	 * Called on attack.
	 * 
	 * @param response response
	 * @param mattacker Mythrl player attacker
	 * @param mdefender Mythrl player defender
	 * @param dsattacker attackers derived stats
	 * @param dsdefender defenders derived stats
	 */
	public void attackTrigger(Response response, MythrPlayer mattacker, MythrPlayer mdefender, DerivedStats dsattacker, DerivedStats dsdefender)
	 { }

	/**
	 * Called on attack.
	 * 
	 * @param response response
	 * @param mattacker Mythrl player attacker
	 * @param mdefender creature defender
	 * @param dsattacker attackers derived stats
	 * @param dsdefender defenders derived stats
	 */
	public void attackTrigger(Response response, MythrPlayer mattacker, Creature cdefender, DerivedStats dsattacker, DerivedStats dsdefender)
	 { }

	/**
	 * Called on defend.
	 * 
	 * @param response response
	 * @param mattacker Mythrl player attacker
	 * @param mdefender Mythrl player defender
	 * @param dsattacker attackers derived stats
	 * @param dsdefender defenders derived stats
	 */
	public void defendTrigger(Response response, MythrPlayer mattacker, MythrPlayer mdefender, DerivedStats dsattacker, DerivedStats dsdefender)
	 { }

	/**
	 * Called on defend.
	 * 
	 * @param response response
	 * @param mattacker Mythrl player attacker
	 * @param mdefender creature defender
	 * @param dsattacker attackers derived stats
	 * @param dsdefender defenders derived stats
	 */
	public void defendTrigger(Response response, MythrPlayer mattacker, Creature cdefender, DerivedStats dsattacker, DerivedStats dsdefender)
	 { }
	
}
