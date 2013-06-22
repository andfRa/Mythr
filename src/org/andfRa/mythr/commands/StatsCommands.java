package org.andfRa.mythr.commands;

import java.util.ArrayList;
import java.util.List;

import org.andfRa.mythr.config.AttributeConfiguration;
import org.andfRa.mythr.items.MythrItem;
import org.andfRa.mythr.player.MythrPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.sk89q.Command;
import org.sk89q.CommandContext;
import org.sk89q.CommandPermissions;

public class StatsCommands {

	
	@Command(
			aliases = {"stats"},
			usage = "",
			flags = "",
			desc = "Shows your player stats.",
			min = 0,
			max = 0
	)
	@CommandPermissions({"mythr.player.stats"})
	public static void stats(CommandContext args, MythrPlayer mythrPlayer)
	 {
		StringBuffer message = new StringBuffer();
		
		String[] attrbs = AttributeConfiguration.getAttrNames();
		String[] abbrevs = AttributeConfiguration.getAttrNames();
		
		for (int i = 0; i < attrbs.length; i++) {
			if(i != 0) message.append("\n");
			message.append(abbrevs[i] + " " + mythrPlayer.getAttribute(attrbs[i]));
		}
		
		mythrPlayer.message(message.toString());
	}
	
	
}
