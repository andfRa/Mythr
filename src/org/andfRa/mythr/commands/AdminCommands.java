package org.andfRa.mythr.commands;

import java.util.ArrayList;

import org.andfRa.mythr.Mythr;
import org.andfRa.mythr.config.AttributeConfiguration;
import org.andfRa.mythr.config.ItemConfiguration;
import org.andfRa.mythr.config.LocalisationConfiguration;
import org.andfRa.mythr.config.SkillConfiguration;
import org.andfRa.mythr.items.MythrItem;
import org.andfRa.mythr.player.MythrPlayer;
import org.andfRa.mythr.util.TargetUtil;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.sk89q.Command;
import org.sk89q.CommandContext;
import org.sk89q.CommandPermissions;

public class AdminCommands {

	public static int i = 0; 
	
	@Command(
	 aliases = {"atest"},
	 usage = "",
	 flags = "",
	 desc = "Test command.",
	 min = 0,
	 max = 0
	)
	@CommandPermissions({"mythr.admin.test"})
	public static void test(CommandContext args, MythrPlayer mythrPlayer) {

		Location loc = mythrPlayer.getPlayer().getLocation();
		Location p = loc.add(10, 0, 0);
		
		TargetUtil.checkBeam(loc, 4, p);
		ArrayList<Entity> caught = TargetUtil.findBeamCollisions(mythrPlayer.getPlayer(), 2, 20);
		for (Entity entity : caught) {
			if(entity instanceof LivingEntity){
				((LivingEntity) entity).damage(1.0);
				p.getWorld().playEffect(entity.getLocation(), Effect.POTION_BREAK, 1);
				System.out.println("caught " + entity.getType());
			}
		}
		
		Player player = mythrPlayer.getPlayer();
		player.setMaxHealth(120);
		
	}
	
	@SuppressWarnings("deprecation")
	@Command(
	 aliases = {"aspawnitem","aitem"},
	 usage = "[player_name] <item_name>",
	 flags = "",
	 desc = "Spawn an item.",
	 min = 1,
	 max = 2
	)
	@CommandPermissions({"mythr.admin.items.spawn"})
	public static void spawnItem(CommandContext args, MythrPlayer mplayer)
	 {
		String argTarget;
		String argItem;
		
		MythrPlayer mtarget;
		MythrItem mitem;

		switch (args.argsLength()) {
		case 2:
			
			// Player:
			argTarget = args.getString(0);
			mtarget = Mythr.plugin().getLoadedPlayer(argTarget);
			if(mtarget == null){
				mplayer.negative(LocalisationConfiguration.getString(LocalisationConfiguration.PLAYER_NOT_ONLINE, argTarget));
				return;
			}
			
			// Item:
			argItem = LocalisationConfiguration.handleArg(args.getString(1));
			mitem = ItemConfiguration.matchItem(argItem);
			if(mitem == null){
				mplayer.negative(LocalisationConfiguration.getString(LocalisationConfiguration.ITEM_DOESNT_EXIST, argItem));
				return;
			}
			
			break;

		default:
			
			// Player:
			mtarget = mplayer;

			// Item:
			argItem = LocalisationConfiguration.handleArg(args.getString(0));
			mitem = ItemConfiguration.matchItem(argItem);
			if(mitem == null){
				mplayer.negative(LocalisationConfiguration.getString(LocalisationConfiguration.ITEM_DOESNT_EXIST, argItem));
				return;
			}
			
			break;
		}

		// Name:
		String itemName = mitem.getName();
		
		// Add item:
		Player player = mtarget.getPlayer();
		player.getInventory().addItem(mitem.toBukkitItem());
		player.updateInventory();
		
		// Report:
		if(mplayer == mtarget){
			mtarget.positive(LocalisationConfiguration.getString(LocalisationConfiguration.ITEM_SPAWNED, itemName));
		}else{
			mtarget.positive(LocalisationConfiguration.getString(LocalisationConfiguration.ITEM_SPAWNED, itemName));
			mplayer.positive(LocalisationConfiguration.getString(LocalisationConfiguration.ITEM_SPAWNED_OTHER, itemName, mtarget.getName()));
		}
	 }
	
	@Command(
	 aliases = {"asetattribute","asetattr"},
	 usage = "[player_name] <attribute_name> <new_score>",
	 flags = "",
	 desc = "Set attribute score.",
	 min = 2,
	 max = 3
	)
	@CommandPermissions({"mythr.admin.setattribute"})
	public static void setAttribute(CommandContext args, MythrPlayer mplayer)
	 {
		String argTarget;
		String argScore;
		
		MythrPlayer mtarget;
		String attribute;
		Integer score;
		
		switch (args.argsLength()) {
		case 3:
			
			// Player:
			argTarget = args.getString(0);
			mtarget = Mythr.plugin().requestPlayer(argTarget);
			if(mtarget == null){
				mplayer.negative(LocalisationConfiguration.getString(LocalisationConfiguration.PLAYER_DOESNT_EXIST, argTarget));
				return;
			}
			
			// Attribute:
			attribute = LocalisationConfiguration.handleArg(args.getString(1));
			if(!AttributeConfiguration.checkAttribute(attribute)){
				mplayer.negative(LocalisationConfiguration.getString(LocalisationConfiguration.ATTRIBUTE_DOESNT_EXIST, attribute));
				return;
			}

			// Score:
			argScore = args.getString(2);
			try {
				score = Integer.parseInt(argScore);
			}
			catch (NumberFormatException e) {
				mplayer.negative(LocalisationConfiguration.getString(LocalisationConfiguration.MUST_BE_INTEGER, argScore));
				return;
			}
			 
			break;

		default:
			
			// Player:
			mtarget = mplayer;

			// Attribute:
			attribute = args.getString(0);
			if(!AttributeConfiguration.checkAttribute(attribute)){
				mplayer.negative(LocalisationConfiguration.getString(LocalisationConfiguration.ATTRIBUTE_DOESNT_EXIST, attribute));
				return;
			}

			// Score:
			argScore = args.getString(1);
			try {
				score = Integer.parseInt(argScore);
			}
			catch (NumberFormatException e) {
				mplayer.negative(LocalisationConfiguration.getString(LocalisationConfiguration.MUST_BE_INTEGER, argScore));
				return;
			}
			
			break;
		}
		
		// Set score:
		mtarget.setAttribute(attribute, score);
		
		// Report:
		if(mplayer == mtarget){
			mtarget.positive(LocalisationConfiguration.getString(LocalisationConfiguration.ATTRIBUTE_SET, attribute, argScore));
		}else{
			mtarget.positive(LocalisationConfiguration.getString(LocalisationConfiguration.ATTRIBUTE_SET, attribute, argScore));
			mplayer.positive(LocalisationConfiguration.getString(LocalisationConfiguration.ATTRIBUTE_SET_OTHER, mtarget.getName(), attribute, argScore));
		}
		
		// Save target:
		mtarget.save();
	 }
	
	@Command(
	 aliases = {"asetskill"},
	 usage = "[player_name] <skill_name> <new_score>",
	 flags = "",
	 desc = "Set skill score.",
	 min = 2,
	 max = 3
	)
	@CommandPermissions({"mythr.admin.setskill"})
	public static void setSkill(CommandContext args, MythrPlayer mplayer)
	 {
		String argTarget;
		String argScore;
		
		MythrPlayer mtarget;
		String skill;
		Integer score;
		
		switch (args.argsLength()) {
		case 3:
			
			// Player:
			argTarget = args.getString(0);
			mtarget = Mythr.plugin().requestPlayer(argTarget);
			if(mtarget == null){
				mplayer.negative(LocalisationConfiguration.getString(LocalisationConfiguration.PLAYER_DOESNT_EXIST, argTarget));
				return;
			}
			
			// Skill:
			skill = LocalisationConfiguration.handleArg(args.getString(1));
			if(!SkillConfiguration.checkSkill(skill)){
				mplayer.negative(LocalisationConfiguration.getString(LocalisationConfiguration.SKILL_DOESNT_EXIST, skill));
				return;
			}

			// Score:
			argScore = args.getString(2);
			try {
				score = Integer.parseInt(argScore);
			}
			catch (NumberFormatException e) {
				mplayer.negative(LocalisationConfiguration.getString(LocalisationConfiguration.MUST_BE_INTEGER, argScore));
				return;
			}
			 
			break;

		default:
			
			// Player:
			mtarget = mplayer;

			// Skill:
			skill = LocalisationConfiguration.handleArg(args.getString(0));
			if(!SkillConfiguration.checkSkill(skill)){
				mplayer.negative(LocalisationConfiguration.getString(LocalisationConfiguration.SKILL_DOESNT_EXIST, skill));
				return;
			}

			// Score:
			argScore = args.getString(1);
			try {
				score = Integer.parseInt(argScore);
			}
			catch (NumberFormatException e) {
				mplayer.negative(LocalisationConfiguration.getString(LocalisationConfiguration.MUST_BE_INTEGER, argScore));
				return;
			}
			
			break;
		}
		
		// Set score:
		mtarget.setSkill(skill, score);

		// Report:
		if(mplayer == mtarget){
			mtarget.positive(LocalisationConfiguration.getString(LocalisationConfiguration.SKILL_SET, skill, argScore));
		}else{
			mtarget.positive(LocalisationConfiguration.getString(LocalisationConfiguration.SKILL_SET, skill, argScore));
			mplayer.positive(LocalisationConfiguration.getString(LocalisationConfiguration.SKILL_SET_OTHER, mtarget.getName(), skill, argScore));
		}
		
		// Save target:
		mtarget.save();
	 }
	
}
