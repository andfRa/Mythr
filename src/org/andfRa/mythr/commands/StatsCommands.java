package org.andfRa.mythr.commands;

import org.andfRa.mythr.config.AttributeConfiguration;
import org.andfRa.mythr.config.SkillConfiguration;
import org.andfRa.mythr.player.Attribute;
import org.andfRa.mythr.player.MythrPlayer;
import org.andfRa.mythr.player.Skill;
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
