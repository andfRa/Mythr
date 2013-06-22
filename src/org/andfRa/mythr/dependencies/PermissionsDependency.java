package org.andfRa.mythr.dependencies;

import net.milkbowl.vault.permission.Permission;

import org.andfRa.mythr.Mythr;
import org.andfRa.mythr.MythrLogger;
import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.sk89q.CommandsManager;

import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * Permissions manager. Hooks up with different permission plugins.
 * 
 * @author andf
 *
 */
public class PermissionsDependency {

	/** Manager instance. */
	private static PermissionsDependency manager;
	
	
	/** Commands map. */
	private CommandsManager<Player> commandMap;
	
	
	/** Group manager. */
	private GroupManager groupManager = null;

	/** Group manager. */
	private PermissionsEx permissionsEx = null;

	/** Vault permissions. */
	private Permission vaultPermissions = null;
	
	
	
	/**
	 * Enables the manager.
	 * 
	 */
	public static void enable() {

		
		manager = new PermissionsDependency();
		
		final PluginManager pluginManager = Mythr.plugin().getServer().getPluginManager();
		Plugin plugin = null;
		 
		// Commands map:
		manager.commandMap = new CommandsManager<Player>() {

			@Override
			public boolean hasPermission(Player player, String perm) {
				return PermissionsDependency.hasPermission(player, perm);
			}
			
		};
		
		// Group manager:
		plugin = pluginManager.getPlugin("GroupManager");
		if (plugin != null && plugin.isEnabled()) {
		
			manager.groupManager = (GroupManager)plugin;
			MythrLogger.info("Using GroupManager permissions.");
			return;
			
		}

		// PermissionsEx:
		plugin = pluginManager.getPlugin("PermissionsEx");
		if (plugin != null && plugin.isEnabled()) {
			
			manager.permissionsEx = (PermissionsEx)plugin;
			MythrLogger.info("Using PermissionsEx permissions.");
			return;
			
		}
		
		// Vault:
		try {
			Class.forName("net.milkbowl.vault.permission.Permission");
			
			RegisteredServiceProvider<net.milkbowl.vault.permission.Permission> permissionProvider = Mythr.plugin().getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
			if (permissionProvider != null) {
	            manager.vaultPermissions = permissionProvider.getProvider();
	        }
			
			if(manager.vaultPermissions != null){
	        	MythrLogger.info("Using Vault permissions.");
	        	return;
	        }
			
		}
		catch (ClassNotFoundException e) {}
		
		MythrLogger.info("Using default permissions.");
		

	}
	
	/**
	 * Disables the manager.
	 * 
	 */
	public static void disable() {

		
		manager.commandMap = null;
		manager.groupManager = null;
		manager.vaultPermissions = null;
		manager.permissionsEx = null;
		
		manager = null;
		

	}
	
	
	
	/**
	 * Checks if the player has permission.
	 * 
	 * @param player player
	 * @param permission permission node
	 * @return true if has permission
	 */
	public static boolean hasPermission(Player player, String permission)
	{
		
		// GroupManager:
		if(manager.groupManager != null){
			final AnjoPermissionsHandler handler = manager.groupManager.getWorldsHolder().getWorldPermissions(player);
			if (handler == null) return false;
			return handler.has(player, permission);
		}

		// Vault:
		if(manager.vaultPermissions != null){
			String world = player.getLocation().getWorld().getName();
			return manager.vaultPermissions.has(world, player.getName(), permission);
		}

		// PermissionsEx:
		if(manager.permissionsEx != null){
			String world = player.getLocation().getWorld().getName();
			return manager.permissionsEx.has(player, permission, world);
		}
		
		// Default:
		if(permission.startsWith("saga.user")) return true;
		
		// Bukkit:
		return player.hasPermission(permission);
		
	}
	
	/**
	 * Gets the command map.
	 * 
	 * @return commands manager
	 */
	public static CommandsManager<Player> getCommandMap() {
		return manager.commandMap;
	}
	
	
}
