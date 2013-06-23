package org.andfRa.mythr.listeners;

import org.andfRa.mythr.Mythr;
import org.andfRa.mythr.config.VanillaConfiguration;
import org.andfRa.mythr.player.MythrPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class InventoryListener  implements Listener {


	@EventHandler(priority = EventPriority.NORMAL)
	public void onClick(InventoryClickEvent event)
	 {
		
		// Armour:
		if(event.getSlotType()  == SlotType.ARMOR){
			
			// Update player armour:
			final MythrPlayer mplayer = Mythr.plugin().getLoadedPlayer(event.getWhoClicked().getName());
			if(mplayer == null) return;
		
			Bukkit.getServer().getScheduler().runTask(Mythr.plugin(), new Runnable() {
				@Override
				public void run() {
					mplayer.updateWeapon();
				}
			});
			
			
		}
		
		// Quickbar:
		else if(event.getSlotType() == SlotType.QUICKBAR){

			int heldSlot = VanillaConfiguration.QUICkBAR_FIRST_SLOT + event.getWhoClicked().getInventory().getHeldItemSlot();
			int numberSlot = VanillaConfiguration.QUICkBAR_FIRST_SLOT + event.getHotbarButton();
			int clickSlot = event.getRawSlot();
			// Update player weapon:
			if(clickSlot == heldSlot || ((event.getHotbarButton() != -1)  && numberSlot == heldSlot)) {
				
				final MythrPlayer mplayer = Mythr.plugin().getLoadedPlayer(event.getWhoClicked().getName());
				if(mplayer == null) return;
				
				Bukkit.getServer().getScheduler().runTask(Mythr.plugin(), new Runnable() {
					@Override
					public void run() {
						mplayer.updateWeapon();
					}
				});
				
			}
			
		}
	 }

	@EventHandler(priority = EventPriority.NORMAL)
	public void onHeld(PlayerItemHeldEvent event)
	 {
		// Update armour:
		final MythrPlayer mplayer = Mythr.plugin().getLoadedPlayer(event.getPlayer().getName());
		if(mplayer == null) return;
		
		Bukkit.getServer().getScheduler().runTask(Mythr.plugin(), new Runnable() {
			@Override
			public void run() {
				mplayer.updateWeapon();
			}
		});
	 }
	
}
