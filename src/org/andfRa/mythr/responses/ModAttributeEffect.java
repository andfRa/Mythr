package org.andfRa.mythr.responses;

import org.andfRa.mythr.config.AttributeConfiguration;
import org.andfRa.mythr.dependencies.EffectDependancy;
import org.andfRa.mythr.player.DerivedStats;
import org.andfRa.mythr.player.MythrPlayer;
import org.bukkit.Location;
import org.bukkit.Sound;

public class ModAttributeEffect extends ResponseEffect {

	/** Attribute key. */
	final public static String ATTRIBUTE_KEY = "ATTRIBUTE";

	/** Amount key. */
	final public static String AMOUNT_KEY = "AMOUNT";
	
	
	@Override
	public String key()
	 { return "MODIFY_ATTRIBUTE_EFFECT"; }
	
	@Override
	public boolean interactTrigger(Response response, MythrPlayer mplayer, DerivedStats dsstats)
	 {
		String attribute = response.getString(ATTRIBUTE_KEY);
		Integer amount = response.getInt(AMOUNT_KEY);
		
		// Invalid attribute:
		if(AttributeConfiguration.getAttribute(attribute) == null){
			EffectDependancy.playFail(mplayer.getPlayer());
			return false;
		}
		
		// Check cost and amount:
		int score = mplayer.getAttribute(attribute);
		if(amount > 0){
			int cost = AttributeConfiguration.calcCost(score + amount) - AttributeConfiguration.calcCost(score);
			if(mplayer.getRemainingAttribs() < cost){
				EffectDependancy.playFail(mplayer.getPlayer());
				return false;
			}
		}
		else if(amount < 0){
			if(score - amount < 0){
				EffectDependancy.playFail(mplayer.getPlayer());
				return false;
			}
		}else{
			EffectDependancy.playFail(mplayer.getPlayer());
			return false;
		}
		
		// Increase:
		mplayer.setAttribute(attribute, score + amount);

		// Effect:
		Location loc = mplayer.getPlayer().getLocation();
		loc.getWorld().playSound(loc, Sound.ORB_PICKUP, 1.0f, 1.0f);
		
		return true;
	 }
	
}
