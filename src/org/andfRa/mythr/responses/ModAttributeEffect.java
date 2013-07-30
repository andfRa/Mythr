package org.andfRa.mythr.responses;

import org.andfRa.mythr.config.AttributeConfiguration;
import org.andfRa.mythr.config.LevelingConfiguration;
import org.andfRa.mythr.dependencies.EffectDependancy;
import org.andfRa.mythr.player.DerivedStats;
import org.andfRa.mythr.player.MythrPlayer;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ModAttributeEffect extends ResponseEffect {

	/** Attribute key. */
	final public static String ATTRIBUTE_KEY = "ATTRIBUTE";

	/** Amount key. */
	final public static String AMOUNT_KEY = "AMOUNT";

	/** Experience cost key. (Optional) (Function: cost vs point cost) */
	final public static String EXP_COST_KEY = "EXP_COST";
	
	
	@Override
	public String key()
	 { return "PERMANENT_MODIFY_ATTRIBUTE_EFFECT"; }
	
	@Override
	public boolean effectTrigger(Response response, MythrPlayer mplayer, DerivedStats dsstats)
	 {
		Player player = mplayer.getPlayer();
		
		String attribute = response.getString(ATTRIBUTE_KEY);
		Integer amount = response.getInt(AMOUNT_KEY);
		
		// Invalid attribute:
		if(AttributeConfiguration.getAttribute(attribute) == null){
			EffectDependancy.playFail(player);
			return false;
		}
		
		// Check cost and amount:
		int score = mplayer.getAttribute(attribute);
		if(amount > 0){
			int cost = AttributeConfiguration.calcCost(score + amount) - AttributeConfiguration.calcCost(score);
			if(mplayer.getRemainingAttribs() < cost){
				EffectDependancy.playFail(player);
				return false;
			}
		}
		else if(amount < 0){
			if(score - amount < 0){
				EffectDependancy.playFail(player);
				return false;
			}
		}else{
			EffectDependancy.playFail(player);
			return false;
		}
		
		// Check exp cost:
		int ptcst = AttributeConfiguration.calcCost(score + amount) - AttributeConfiguration.calcCost(score);
		int expCost = ptcst * LevelingConfiguration.getAttribPointSpendExp();
		if(expCost > player.getLevel()){
			EffectDependancy.playFail(player);
			return false;
		}
		
		// Take exp:
		if(expCost != 0) player.setLevel(player.getLevel() - expCost);
		
		// Increase:
		mplayer.setAttribute(attribute, score + amount);

		// Effect:
		Location loc = player.getLocation();
		loc.getWorld().playSound(loc, Sound.ORB_PICKUP, 1.0f, 1.0f);
		
		return true;
	 }
	
}
