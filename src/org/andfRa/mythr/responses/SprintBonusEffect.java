package org.andfRa.mythr.responses;

import org.andfRa.mythr.config.VanillaConfiguration;
import org.andfRa.mythr.player.DerivedStats;
import org.bukkit.entity.Player;

public class SprintBonusEffect extends ResponseEffect {

	/** Modification amount key. */
	final public static String MOD_AMOUNT_KEY = "MOD_AMOUNT";

	/** Modification amount if burdened key. */
	final public static String MOD_AMOUNT_BURDENED_KEY = "MOD_AMOUNT_BURDENED";
	
	
	@Override
	public String key()
	 { return "SPRINT_BONUS_EFFECT"; }
	
	@Override
	public boolean sprintTrigger(Response response, Player player, DerivedStats dstats, boolean sprinting)
	 {
		float mod = 0;
		if(dstats.isBurdened()) mod = response.getFloat(MOD_AMOUNT_BURDENED_KEY);
		else mod = response.getFloat(MOD_AMOUNT_KEY);
		
		if (sprinting){
			player.setWalkSpeed(VanillaConfiguration.DEFAULT_WALK_SPEED + mod);
			return true;
		} else {
			player.setWalkSpeed(VanillaConfiguration.DEFAULT_WALK_SPEED);
			return true;
		}
	 }
	
}
