package org.andfRa.mythr.commands;

import java.util.ArrayList;
import java.util.List;

import org.andfRa.mythr.items.ItemType;
import org.andfRa.mythr.items.MythrItem;
import org.andfRa.mythr.player.MythrPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.sk89q.Command;
import org.sk89q.CommandContext;
import org.sk89q.CommandPermissions;

public class AdminCommands {

	
	@Command(
			aliases = {"aitem"},
			usage = "",
			flags = "",
			desc = "Create a test item.",
			min = 0,
			max = 0
	)
	@CommandPermissions({"mythr.admin.test"})
	public static void testItem(CommandContext args, MythrPlayer mythrPlayer) {

		Player player = mythrPlayer.getPlayer();
		
		MythrItem mitem = new MythrItem(Material.IRON_SWORD);
		mitem.setMinDamage(7);
		mitem.setMaxDamage(10);
		mitem.setType(ItemType.MELEE_WEAPON);
		
		ItemStack bitem = mitem.toBukkitItem();
		player.getInventory().addItem(bitem);
		
		player.updateInventory();
		
	}
	
	
}
