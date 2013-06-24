package org.andfRa.mythr.commands;

import org.andfRa.mythr.Mythr;
import org.andfRa.mythr.config.AttributeConfiguration;
import org.andfRa.mythr.config.LocalisationConfiguration;
import org.andfRa.mythr.config.SkillConfiguration;
import org.andfRa.mythr.dependencies.EffectDependancy;
import org.andfRa.mythr.dependencies.particles.ParticleEffect;
import org.andfRa.mythr.items.ItemType;
import org.andfRa.mythr.items.JournalSpawner;
import org.andfRa.mythr.items.MythrItem;
import org.andfRa.mythr.player.MythrPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.sk89q.Command;
import org.sk89q.CommandContext;
import org.sk89q.CommandPermissions;

public class AdminCommands {

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

		Player player = mythrPlayer.getPlayer();
		EffectDependancy.playParticle(ParticleEffect.ANGRY_VILLAGER, player.getLocation(), 2f, 0.1f, 4);
		
	}
	
	@SuppressWarnings("deprecation")
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
		
		// Update:
		mtarget.updateWeapon();
		mtarget.updateArmour();
		
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

		// Update:
		mtarget.updateWeapon();
		mtarget.updateArmour();
		
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

	@SuppressWarnings("deprecation")
	@Command(
	 aliases = {"aspawnjournal","ajournal"},
	 usage = "[player_name]",
	 flags = "",
	 desc = "Spawn a player stats journal.",
	 min = 0,
	 max = 1
	)
	@CommandPermissions({"mythr.admin.spawnjournal"})
	public static void spawnJournal(CommandContext args, MythrPlayer mplayer)
	 {
		String argTarget;
		MythrPlayer mtarget;
		
		switch (args.argsLength()) {
		case 1:
			
			// Player:
			argTarget = args.getString(0);
			mtarget = Mythr.plugin().matchPlayer(argTarget);
			if(mtarget == null){
				mplayer.negative(LocalisationConfiguration.getString(LocalisationConfiguration.PLAYER_NOT_ONLINE, argTarget));
				return;
			}
			
			break;

		default:
			
			// Player:
			mtarget = mplayer;
			
			break;
		}
		
		// Add book:
		mtarget.getPlayer().getInventory().addItem(JournalSpawner.create());
		mtarget.getPlayer().updateInventory();
		
		// Report:
		if(mplayer == mtarget){
		}else{
			mplayer.positive(LocalisationConfiguration.getString(LocalisationConfiguration.JOURNAL_SPAWNED_OTHER, mtarget.getName()));
		}
		
	 }
	
	
}
