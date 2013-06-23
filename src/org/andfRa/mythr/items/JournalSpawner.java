package org.andfRa.mythr.items;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import org.andfRa.mythr.config.AttributeConfiguration;
import org.andfRa.mythr.config.LocalisationConfiguration;
import org.andfRa.mythr.config.SkillConfiguration;
import org.andfRa.mythr.player.Attribute;
import org.andfRa.mythr.player.MythrPlayer;
import org.andfRa.mythr.player.Skill;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class JournalSpawner {

	/** Indicator for player skills. */
	public final static char PLAYER_KILLS_INDICATOR = '\u2440';
	
	
	/**
	 * Creates an empty journal.
	 * 
	 * @return empty journal
	 */
	public static ItemStack create()
	 {
		MythrItem mbook = new MythrItem(Material.BOOK);
		mbook.setType(ItemType.JOURNAL);
		mbook.setName(WordUtils.capitalize(LocalisationConfiguration.getString("journal")));
		ItemStack book = mbook.toBukkitItem();
		return book;
	 }

	/**
	 * Creates a claimed empty journal.
	 * 
	 * @return claimed empty journal
	 */
	public static ItemStack claim(MythrPlayer mplayer)
	 {
		MythrItem mbook = new MythrItem(Material.WRITTEN_BOOK);
		mbook.setType(ItemType.JOURNAL);
		mbook.setName(WordUtils.capitalize(LocalisationConfiguration.getString("journal")));
		
		ItemStack book = mbook.toBukkitItem();
		BookMeta bookMeta = (BookMeta) book.getItemMeta();
		bookMeta.setAuthor(mplayer.getName());
		book.setItemMeta(bookMeta);
		
		update(book, mplayer);
		
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
		
		// Set name:
		bookMeta.setDisplayName(WordUtils.capitalize(LocalisationConfiguration.getString("journal")));
		
		// Contents:
		bookMeta.setPages();
		DecimalFormat format = new DecimalFormat("00");
		StringBuffer page;
		
		// Write attributes:
		Attribute[] attributes = AttributeConfiguration.getAttributes();
		page = new StringBuffer();
		
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
		
		page.append(WordUtils.capitalize(LocalisationConfiguration.getString("cost")) +  ": ");
		int cons = AttributeConfiguration.getCostCostRange();
		int min = 1;
		int max = cons;
		int pts = 1;
		for (int i = 0; i < 3; i++) {
			if(i != 0) page.append(", ");
			page.append(min + "-" + max + " " + pts);
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
	
	/**
	 * Gets kills from the journal.
	 * 
	 * @param pages journal pages
	 * @return kills
	 */
	public static HashMap<String, Integer> findKills(List<String> pages)
	 {

		HashMap<String, Integer> kills = new HashMap<String, Integer>();
		String col = Character.toString(ChatColor.COLOR_CHAR);
		
		for (int i = 0; i < pages.size(); i++) {
			
			String page = pages.get(i);
			if(page.length() < 2 || !page.startsWith(col) || page.charAt(2) != PLAYER_KILLS_INDICATOR) continue;
			
			String[] lines = page.split("\n");
			
			for (int j = 1; j < lines.length; j++) {
				String[] line = lines[j].split(" ");
				if(line.length != 2) continue;
				try {
					kills.put(line[0], Integer.parseInt(line[1]));
				}
				catch (NumberFormatException e) { }
			}
			
		}
		
		return kills;
	 }
	
	
}
