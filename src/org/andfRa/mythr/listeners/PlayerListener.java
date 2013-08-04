package org.andfRa.mythr.listeners;


import java.util.Collection;

import org.andfRa.mythr.Mythr;
import org.andfRa.mythr.config.ResponseConfiguration;
import org.andfRa.mythr.config.VanillaConfiguration;
import org.andfRa.mythr.items.MythrItem;
import org.andfRa.mythr.items.ScrollManager;
import org.andfRa.mythr.player.DerivedStats;
import org.andfRa.mythr.player.MythrPlayer;
import org.andfRa.mythr.player.SpellManager;
import org.andfRa.mythr.responses.Response;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {


	@EventHandler(priority = EventPriority.NORMAL)
	public void onJoin(PlayerJoinEvent event)
	 {
		Player player = event.getPlayer();

		MythrPlayer mythrPlayer = Mythr.plugin().loadMythrPlayer(player.getName());
		
		// Set player:
		mythrPlayer.wrapPlayer(player);
	 }

	@EventHandler(priority = EventPriority.NORMAL)
	public void onQuit(PlayerQuitEvent event)
	 {		
		Player player = event.getPlayer();
		MythrPlayer mythrPlayer = Mythr.plugin().getLoadedPlayer(player.getName());
		if(mythrPlayer == null) return;

		// Unload:
		Mythr.plugin().unloadMythrPlayer(player.getName());

		// Remove player:
		mythrPlayer.unwrap();
	 }
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onCommandPreprocess(PlayerCommandPreprocessEvent event)
	 {
		String[] split = event.getMessage().split(" ");
		if (Mythr.handleCommand(event.getPlayer(), split, event.getMessage())) event.setCancelled(true);
	 }
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onCraft(PrepareItemCraftEvent event)
	 {
		
	 }

	@EventHandler(priority = EventPriority.NORMAL)
	public void onMove(PlayerMoveEvent event)
	 {
		// Update casting:
		SpellManager.handleMoving(event.getPlayer(), event.getTo());
	 }

	@EventHandler(priority = EventPriority.NORMAL)
	public void onInteract(PlayerInteractEvent event)
	 {
		Player player = event.getPlayer();
		MythrPlayer mplayer = Mythr.plugin().getLoadedPlayer(player.getName());
		if(mplayer == null) return;

		ItemStack item = event.getItem();
		if(item != null){
			
			MythrItem mitem;

			// Spells:
			if(item.getType() == Material.BOOK){
				
				mitem = MythrItem.fromBukkitItem(item);
				
				// Right click:
				switch (event.getAction()) {
				case RIGHT_CLICK_AIR:
				case RIGHT_CLICK_BLOCK:
					
					// Effect:
					String respName = mitem.getEffect();
					if(respName == null) break;
					Response response = ResponseConfiguration.getResponse(respName);
					if(response == null) break;
					response.effectTrigger(mplayer, mplayer.getDerived());
					
					break;
					
				case LEFT_CLICK_AIR:
				case LEFT_CLICK_BLOCK:
					
					// Recharge?
					
					break;

				default:
					break;
				}
				
			}
			
			// Scrolls:
			else if(item.getType() == Material.PAPER){

				mitem = MythrItem.fromBukkitItem(item);
				
				switch (mitem.getType()) {
				case SCROLL:
					String respName = mitem.getEffect();
					if(respName == null) break;
					Response response = ResponseConfiguration.getResponse(respName);
					if(response == null) break;
					boolean remove = response.effectTrigger(mplayer, mplayer.getDerived());
					if(remove) ScrollManager.handleUseScroll(mplayer);
					
					break;

				default:
					break;
				}
				
				
				
			}
			
		}
	 }
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSprint(PlayerToggleSprintEvent event)
	 {
		if(event.isCancelled()) return;
		
		// Responses:
		DerivedStats dstats = DerivedStats.findDerived(event.getPlayer());
		Collection<Response> responses = dstats.getResponses();
		for (Response response : responses) response.sprintTrigger(event.getPlayer(), dstats, event.isSprinting());
		
		// Running speed:
		if (event.isSprinting()){
			event.getPlayer().setWalkSpeed(VanillaConfiguration.DEFAULT_WALK_SPEED * dstats.getRunMult());
		} else {
			event.getPlayer().setWalkSpeed(VanillaConfiguration.DEFAULT_WALK_SPEED);
		}
	 }
	
	
}
