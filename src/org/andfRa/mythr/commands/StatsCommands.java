package org.andfRa.mythr.commands;

import java.util.ArrayList;
import java.util.List;

import org.andfRa.mythr.config.AttributeConfiguration;
import org.andfRa.mythr.config.SkillConfiguration;
import org.andfRa.mythr.items.MythrItem;
import org.andfRa.mythr.player.Attribute;
import org.andfRa.mythr.player.MythrPlayer;
import org.andfRa.mythr.player.Skill;
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
		
		// Attributes:
		Attribute[] attributes = AttributeConfiguration.getAttributes();
		for (int i = 0; i < attributes.length; i++) {
			if(i != 0) message.append("\n");
			message.append(attributes[i].getAbbrev() + " " + mythrPlayer.getAttribute(attributes[i].getName()));
		}
		
		message.append("\n");
		message.append("\n");
		
		// Skills:
		Skill[] skills = SkillConfiguration.getSkills();
		for (int i = 0; i < skills.length; i++) {
			if(i != 0) message.append("\n");
			message.append(skills[i].getName() + " " + mythrPlayer.getSkill(skills[i].getName()));
		}
		
		mythrPlayer.message(message.toString());
	}
	
	
}
