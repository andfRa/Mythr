package org.andfRa.mythr.listeners;

import java.util.List;

import org.andfRa.mythr.Mythr;
import org.andfRa.mythr.config.ResponseConfiguration;
import org.andfRa.mythr.config.VanillaConfiguration;
import org.andfRa.mythr.items.MythrItem;
import org.andfRa.mythr.player.DerivedStats;
import org.andfRa.mythr.player.MythrPlayer;
import org.andfRa.mythr.responses.Response;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener  implements Listener {


	@EventHandler(priority = EventPriority.NORMAL)
	public void onClick(InventoryClickEvent event)
	 {
		
		// Armour:
		if(event.getSlotType()  == SlotType.ARMOR){
			
			// Update derived stats:
			final MythrPlayer mplayer = Mythr.plugin().getLoadedPlayer(event.getWhoClicked().getName());
			if(mplayer == null) return;
		
			Bukkit.getServer().getScheduler().runTask(Mythr.plugin(), new Runnable() {
				@Override
				public void run() {
					mplayer.updateAll();
				}
			});
			
		}
		
		// Quickbar:
		else if(event.getSlotType() == SlotType.QUICKBAR){

			int heldSlot = VanillaConfiguration.QUICKBAR_FIRST_SLOT + event.getWhoClicked().getInventory().getHeldItemSlot();
			int numberSlot = VanillaConfiguration.QUICKBAR_FIRST_SLOT + event.getHotbarButton();
			int clickSlot = event.getRawSlot();
			// Update derived stats:
			if(clickSlot == heldSlot || ((event.getHotbarButton() != -1)  && numberSlot == heldSlot)) {
				
				final MythrPlayer mplayer = Mythr.plugin().getLoadedPlayer(event.getWhoClicked().getName());
				if(mplayer == null) return;
				
				Bukkit.getServer().getScheduler().runTask(Mythr.plugin(), new Runnable() {
					@Override
					public void run() {
						mplayer.updateHeld();
					}
				});
				
			}
			
		}
	 }

	@EventHandler(priority = EventPriority.NORMAL)
	public void onHeld(PlayerItemHeldEvent event)
	 {
		MythrPlayer mplayer = Mythr.plugin().getLoadedPlayer(event.getPlayer().getName());
		
		// Focus:
		ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
		if(item != null){
			MythrItem mitem = MythrItem.fromBukkitItem(item);
			DerivedStats dstats = mplayer.getDerived();
			
			// Responses:
			List<String> respnames = mitem.getResponses();
			for (String respname : respnames) {
				Response response = ResponseConfiguration.getResponse(respname);
				if(response != null) response.focusTrigger(mplayer, item, dstats);
			}
			
			// Effect:
			String respname = mitem.getEffect();
			Response response = ResponseConfiguration.getResponse(respname);
			if(response != null) response.focusTrigger(mplayer, item, dstats);
			
		}
	 }
	
}
