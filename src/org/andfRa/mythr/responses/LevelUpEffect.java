package org.andfRa.mythr.responses;

import org.andfRa.mythr.config.LevelingConfiguration;
import org.andfRa.mythr.dependencies.EffectDependancy;
import org.andfRa.mythr.player.DerivedStats;
import org.andfRa.mythr.player.MythrPlayer;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class LevelUpEffect extends ResponseEffect {

	
	@Override
	public String key()
	 { return "LEVEL_UP_EFFECT"; }
	
	@Override
	public boolean effectTrigger(Response response, MythrPlayer mplayer, DerivedStats dsstats)
	 {
		Player player = mplayer.getPlayer();
		
		// Check exp cost:
		int expCost = LevelingConfiguration.getLevelupSpendExp(mplayer.getLevel());
		if(expCost > player.getLevel()){
			EffectDependancy.playFail(player);
			return false;
		}
		
		// Take exp:
		if(expCost != 0) player.setLevel(player.getLevel() - expCost);
		
		// Increase:
		mplayer.setLevel(mplayer.getLevel() + 1);

		// Effect:
		Location loc = player.getLocation();
		loc.getWorld().playSound(loc, Sound.LEVEL_UP, 1.0f, 1.0f);
		
		return true;
	 }
	
}
