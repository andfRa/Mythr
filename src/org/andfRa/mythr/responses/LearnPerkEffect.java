package org.andfRa.mythr.responses;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.config.LevelingConfiguration;
import org.andfRa.mythr.config.ResponseConfiguration;
import org.andfRa.mythr.dependencies.EffectDependancy;
import org.andfRa.mythr.player.DerivedStats;
import org.andfRa.mythr.player.MythrPlayer;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class LearnPerkEffect extends ResponseEffect {

	/** Perk name key. */
	final public static String PERK_KEY = "PERK";

	/** Experience cost key. (Optional) (Function: cost vs point cost) */
	final public static String EXP_COST_KEY = "EXP_COST";
	
	
	@Override
	public String key()
	 { return "LEARN_PERK_EFFECT"; }
	
	@Override
	public boolean effectTrigger(Response response, MythrPlayer mplayer, DerivedStats dsstats)
	 {
		Player player = mplayer.getPlayer();
		
		String perk = response.getString(PERK_KEY);
		Integer category = LevelingConfiguration.getPerkCategory(perk);
		
		// Invalid perk:
		if(category == null || ResponseConfiguration.getResponse(perk) == null){
			mplayer.error("Perk " + perk + " with category " + category + " is invalid.");
			MythrLogger.warning(getClass(), "Perk " + perk + " with category " + category + " is invalid.");
			return false;
		}
		
		// Perk slot available:
		if(mplayer.getRemainingPerkSlots(category) < 1){
			EffectDependancy.playFail(player);
			return false;
		}

		// Check exp cost:
		int expCost = LevelingConfiguration.getPerkSpendExp();
		if(expCost > player.getLevel()){
			EffectDependancy.playFail(player);
			return false;
		}
		
		// Take exp:
		if(expCost != 0) player.setLevel(player.getLevel() - expCost);
		
		// Add perk:
		mplayer.addPerk(perk, category);
		
		// Effect:
		Location loc = player.getLocation();
		loc.getWorld().playSound(loc, Sound.ORB_PICKUP, 1.0f, 1.0f);
		
		return true;
	 }
	
}
