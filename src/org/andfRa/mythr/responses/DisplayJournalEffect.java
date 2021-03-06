package org.andfRa.mythr.responses;

import java.text.DecimalFormat;

import org.andfRa.mythr.config.AttributeConfiguration;
import org.andfRa.mythr.config.LevelingConfiguration;
import org.andfRa.mythr.config.LocalisationConfiguration;
import org.andfRa.mythr.config.SkillConfiguration;
import org.andfRa.mythr.player.Attribute;
import org.andfRa.mythr.player.DerivedStats;
import org.andfRa.mythr.player.MythrPlayer;
import org.andfRa.mythr.player.Skill;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class DisplayJournalEffect extends ResponseEffect {

	@Override
	public String key()
	 { return "DISPLAY_JOURNAL_EFFECT"; }

	@Override
	public boolean effectTrigger(Response response, MythrPlayer mplayer, DerivedStats dsstats)
	 {
		Player player = mplayer.getPlayer();
		ItemStack item = player.getItemInHand();
		if(item == null) return false;
		
		// Claim journal:
		if(item.getType() == Material.BOOK){
			claim(mplayer, player.getItemInHand());
		}
		
		return true;
	 }
	
	@Override
	public boolean focusTrigger(Response response, MythrPlayer mplayer, ItemStack item, DerivedStats dsstats)
	 {
		update(item, mplayer);
		return true;
	 }

	/**
	 * Converts an item to a stats journal.
	 * 
	 * @param mplayer Mythr player
	 * @param book book item
	 * @return claimed empty journal
	 */
	@SuppressWarnings("deprecation")
	public static ItemStack claim(MythrPlayer mplayer, ItemStack book)
	 {
		book.setType(Material.WRITTEN_BOOK);
		BookMeta bookMeta = (BookMeta) book.getItemMeta();
		bookMeta.setAuthor(mplayer.getName());
		book.setItemMeta(bookMeta);
		update(book, mplayer);
		mplayer.getPlayer().updateInventory();
		return book;
	 }
	
	/**
	 * Updates the journal with the latest stats.
	 * 
	 * @param book book item
	 * @param mplayer Myth player
	 */
	public static void update(ItemStack book, MythrPlayer mplayer)
	 {
		// Book:
		if(!(book.getItemMeta() instanceof BookMeta)) return;
		BookMeta bookMeta = (BookMeta) book.getItemMeta();
		
		// Author check:
		if(!bookMeta.hasAuthor() || !bookMeta.getAuthor().equals(mplayer.getName())) return;
		
		// Contents:
		bookMeta.setPages();
		DecimalFormat format = new DecimalFormat("00");
		StringBuffer page;
		
		// Page:
		page = new StringBuffer();
		
		// Write level:
		page.append(LocalisationConfiguration.getString("progression").toUpperCase());
		page.append("\n");
		page.append("\n");
		
		Integer level = mplayer.getLevel();
		page.append(LocalisationConfiguration.getCapitString("level") + ": " + level + "/" + LevelingConfiguration.getMaxLevel());
		page.append("\n");
		page.append(LocalisationConfiguration.getCapitString("next") + ": " + LevelingConfiguration.getLevelupSpendExp(level) + " " + LocalisationConfiguration.getString("exp"));
		page.append("\n");
		Integer maxLevel = LevelingConfiguration.getMaxLevel();
		page.append(LocalisationConfiguration.getCapitString("attributes") + ": " + mplayer.getAvailableAttribs() + "/" + AttributeConfiguration.getAttribPoints(maxLevel));
		page.append("\n");
		page.append(LocalisationConfiguration.getCapitString("skills") + ": " + mplayer.getAvailableSkills() + "/" + SkillConfiguration.getSkillPoints(maxLevel));
		
		page.append("\n");
		
		page.append(LocalisationConfiguration.getCapitString("perks") + ":");
		Integer maxPerkCateg = LevelingConfiguration.getMaxPerkCateg();
		for (int i = 1; i <= maxPerkCateg; i++) {
			String name = LevelingConfiguration.getPerkCategName(i);
			Integer current = LevelingConfiguration.getAvailPerks(level, i);
			Integer max = LevelingConfiguration.getAvailPerks(maxLevel, i);
			
			page.append("\n");
			page.append(" " + name + " " + current + "/" + max);
		}
		
		bookMeta.addPage(page.toString());
		page = new StringBuffer();
		
		// Write attributes:
		Attribute[] attributes = AttributeConfiguration.getAttributes();
		
		page.append(LocalisationConfiguration.getString("attributes").toUpperCase());
		page.append("\n");
		page.append("\n");
		
		for (int i = 0; i < attributes.length; i++) {
			StringBuffer line = new StringBuffer();
			line.append(attributes[i].getAbbrev());
			line.append(" ");
			line.append(format.format(mplayer.getAttribute(attributes[i].getName())));
			line.append("\n");
			page.append(line);
		}

		page.append('\n');
		
		page.append(WordUtils.capitalize(LocalisationConfiguration.getString("points")) +  ": ");
		page.append(mplayer.getRemainingAttribs());
		
		page.append('\n');
		page.append('\n');
		
		page.append(WordUtils.capitalize(LocalisationConfiguration.getString("cost")) +  ": ");
		page.append('\n');
		int cons = AttributeConfiguration.getCostCostRange();
		int min = 1;
		int max = cons;
		int pts = 1;
		for (int i = 0; i < 3; i++) {
			if(i != 0) page.append("\n");
			page.append(" " + pts + " -- " + LocalisationConfiguration.getString("levels") + " " + min + " - " + max);
			min+= cons;
			max+= cons;
			pts++;
		}
		
		bookMeta.addPage(page.toString());

		// Write skills:
		Skill[] skills = SkillConfiguration.getSkills();
		page = new StringBuffer();
		
		page.append(LocalisationConfiguration.getString("skills").toUpperCase());
		page.append("\n");
		page.append("\n");
		
		for (int i = 0; i < skills.length; i++) {
			StringBuffer line = new StringBuffer();
			line.append(skills[i].getName());
			line.append(" ");
			line.append(mplayer.getSkill(skills[i].getName()));
			line.append("\n");
			page.append(line);
		}

		page.append('\n');

		page.append(WordUtils.capitalize(LocalisationConfiguration.getString("points")) +  ": ");
		page.append(mplayer.getRemainingSkills());
		
		bookMeta.addPage(page.toString());
		
		// TODO: Journal should record kills.
		// TODO: Journal should record gathered materials.
		
		// Update book:
		book.setItemMeta(bookMeta);
	}
	
}
