package org.andfRa.mythr.listeners;


import org.andfRa.mythr.Mythr;
import org.andfRa.mythr.player.MythrPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {


	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event)
	 {
		Player player = event.getPlayer();

		MythrPlayer mythrPlayer = Mythr.plugin().loadMythrPlayer(player.getName());
		
		// Set player:
		mythrPlayer.wrapPlayer(player);
	 }

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event)
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
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
	{
		
		String[] split = event.getMessage().split(" ");
		
		if (Mythr.handleCommand(event.getPlayer(), split, event.getMessage())) event.setCancelled(true);
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerCraft(PrepareItemCraftEvent event)
	{
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent event)
	{
		
		if(event.getPlayer().isSprinting()){
			
			event.getPlayer().setWalkSpeed(0.6f);
			
		}else{
			
			event.getPlayer().setWalkSpeed(0.2f);
			
		}
		
	}
	
	
}
