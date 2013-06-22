package org.andfRa.mythr;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class SecondListener implements Listener{

	Integer limit = 0;
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onTNTPrime(ExplosionPrimeEvent event) {

//		if(event.isCancelled()) return;
//		
//		if(event.getEntity() instanceof TNTPrimed){
//			
//			Location loc = event.getEntity().getLocation();
//			
//			Second.warning("TNT primed at " + "(" + loc.getWorld().getName() + ": " + (int)loc.getX() + ", " + (int)loc.getY() + ", " + (int)loc.getZ() + ").");
//			
//			List<Entity> nearby = event.getEntity().getNearbyEntities(50, 50, 50);
//			for (Entity entity : nearby) {
//				
//				Location eloc = entity.getLocation();
//				
//				if(entity instanceof Player){
//					Second.warning(((Player) entity).getName() + " found at " + "(" + eloc.getWorld().getName() + ": " + (int)eloc.getX() + ", " + (int)eloc.getY() + ", " + (int)eloc.getZ() + ").");
//				}
//				
//			}
//			
//		}
		
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent event) {

		
//		if(event.isCancelled()) return;
//		
//		if(event.getBlock().getType() == Material.TNT){
//			
//			Location loc = event.getBlock().getLocation();
//			
//			Second.warning("TNT placed by " + event.getPlayer().getName() + " at " + "(" + loc.getWorld().getName() + ": " + (int)loc.getX() + ", " + (int)loc.getY() + ", " + (int)loc.getZ() + ").");
//			
//			Player player1 = Second.plugin().getServer().getPlayer("andf54");
//			Player player2 = Second.plugin().getServer().getPlayer("Matt_Assassin22");
//			Player player3 = Second.plugin().getServer().getPlayer("Olyol95");
//			
//			if(player1 != null) player1.sendMessage(ChatColor.RED + event.getPlayer().getName() + " placed TNT!");
//			if(player2 != null) player2.sendMessage(ChatColor.RED + event.getPlayer().getName() + " placed TNT!");
//			if(player3 != null) player3.sendMessage(ChatColor.RED + event.getPlayer().getName() + " placed TNT!");
//			
//		}

		
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {


		
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerChat(PlayerChatEvent event) {

	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {

		
		// Creative permission:
		if(event.getPlayer().hasPermission("second.gamemode")) return;
		
		Player player = event.getPlayer();
		if(player.getGameMode() == GameMode.CREATIVE){
			player.setGameMode(GameMode.SURVIVAL);
			player.getInventory().clear();
			MythrLogger.warning("Warning, " + player.getName() + " doesn't have creative permission!");
			Mythr.plugin().getServer().broadcastMessage(ChatColor.RED + player.getName() + " doesn't have creative permission!");
		}


	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onDispence(BlockDispenseEvent event) {

		if(event.getItem() == null) return;

		
		// Dupe exploit:
		Material material = event.getItem().getType();
		
		if(material == Material.EGG || material == Material.ARROW || material == Material.BOAT || material.isEdible() || material == Material.WATER_BUCKET || material == Material.STATIONARY_WATER || material == Material.WATER) return;
		
		event.setCancelled(true);
		
		
	}
	
	
	
}
