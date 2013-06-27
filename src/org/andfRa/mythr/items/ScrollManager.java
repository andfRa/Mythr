package org.andfRa.mythr.items;

import org.andfRa.mythr.player.MythrPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ScrollManager {

	/**
	 * Handles scroll usage.
	 * 
	 * @param mplayer Mythrl player who used the scroll
	 */
	@SuppressWarnings("deprecation")
	public static void handleUseScroll(MythrPlayer mplayer)
	 {
		Player player = mplayer.getPlayer();
		ItemStack item = mplayer.getPlayer().getInventory().getItemInHand();
		
		if(item.getAmount() > 1) item.setAmount(item.getAmount() - 1);
		else mplayer.getPlayer().getInventory().setItemInHand(null);
		player.updateInventory();
	 }
	
}
