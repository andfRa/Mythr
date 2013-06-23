package org.andfRa.mythr.listeners;

import org.andfRa.mythr.Mythr;
import org.andfRa.mythr.config.VanillaConfiguration;
import org.andfRa.mythr.items.ItemType;
import org.andfRa.mythr.items.JournalSpawner;
import org.andfRa.mythr.items.MythrItem;
import org.andfRa.mythr.player.MythrPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
			
			// Update player armour:
			final MythrPlayer mplayer = Mythr.plugin().getLoadedPlayer(event.getWhoClicked().getName());
			if(mplayer == null) return;
		
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Mythr.plugin(), new Runnable() {
				@Override
				public void run() {
					mplayer.updateWeapon();
				}
			}, 1);
			
			
		}
		
		// Quickbar:
		else if(event.getSlotType() == SlotType.QUICKBAR){

			int heldSlot = VanillaConfiguration.QUICkBAR_FIRST_SLOT + event.getWhoClicked().getInventory().getHeldItemSlot();
			int clickSlot = event.getRawSlot();
			
			// Update player weapon:
			if(clickSlot == heldSlot){
				
				final MythrPlayer mplayer = Mythr.plugin().getLoadedPlayer(event.getWhoClicked().getName());
				if(mplayer == null) return;
				
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Mythr.plugin(), new Runnable() {
					@Override
					public void run() {
						mplayer.updateWeapon();
					}
				}, 1);
				
			}
			
		}
	 }

	@EventHandler(priority = EventPriority.NORMAL)
	public void onHeld(PlayerItemHeldEvent event)
	 {
		// Update player weapon:
		final MythrPlayer mplayer = Mythr.plugin().getLoadedPlayer(event.getPlayer().getName());
		if(mplayer == null) return;
		
		// Upate journal:
		ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
		if(item != null && item.getType() == Material.WRITTEN_BOOK){
			MythrItem mitem = MythrItem.fromBukkitItem(item);
			if(mitem.getType() == ItemType.JOURNAL) JournalSpawner.update(item, mplayer);
		}
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Mythr.plugin(), new Runnable() {
			@Override
			public void run() {
				mplayer.updateWeapon();
			}
		}, 1);
	 }
	
}
