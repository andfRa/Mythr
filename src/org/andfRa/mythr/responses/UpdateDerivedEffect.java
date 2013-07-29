package org.andfRa.mythr.responses;

import java.text.DecimalFormat;

import org.andfRa.mythr.config.AttributeConfiguration;
import org.andfRa.mythr.config.LocalisationConfiguration;
import org.andfRa.mythr.config.SkillConfiguration;
import org.andfRa.mythr.player.Attribute;
import org.andfRa.mythr.player.DamageType;
import org.andfRa.mythr.player.DerivedStats;
import org.andfRa.mythr.player.Skill;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class UpdateDerivedEffect extends ResponseEffect {

	@Override
	public String key()
	 { return "UPDATE_DERIVED_STATS_EFFECT"; }

	@Override
	public boolean attackTrigger(Response response, LivingEntity lattacker, LivingEntity ldefender, DerivedStats dsattacker, DerivedStats dsdefender)
	 {
		ItemStack item = lattacker.getEquipment().getItemInHand();
		if(item == null) return false;
		
		// Claim:
		if(item.getType() == Material.BOOK){
			claim(item, ldefender);
			return true;
		}
		
		// Update:
		else if(item.getType() == Material.WRITTEN_BOOK){
			update(item, ldefender);
			return true;
		}
		
		return false;
	 }
	

	/**
	 * Converts an item to a stats book.
	 * 
	 * @param book book item
	 * @param lentity living entity
	 * @return claimed empty journal
	 */
	@SuppressWarnings("deprecation")
	public static ItemStack claim(ItemStack book, LivingEntity lentity)
	 {
		book.setType(Material.WRITTEN_BOOK);
		BookMeta bookMeta = (BookMeta) book.getItemMeta();
		book.setItemMeta(bookMeta);
		update(book, lentity);
		if(lentity instanceof Player) ((Player) lentity).updateInventory();
		return book;
	 }
	
	/**
	 * Updates the journal with the latest stats.
	 * 
	 * @param lentity living entity
	 * @param book book item
	 * @param mplayer Myth player
	 */
	public static void update(ItemStack book, LivingEntity lentity)
	 {
		// Book:
		if(!(book.getItemMeta() instanceof BookMeta)) return;
		BookMeta bookMeta = (BookMeta) book.getItemMeta();
		
		// Derived:
		DerivedStats dstats = DerivedStats.findDerived(lentity);
		
		// Contents:
		bookMeta.setPages();
		DecimalFormat format = new DecimalFormat("00");
		StringBuffer page;
		
		// Page:
		page = new StringBuffer();
		
		// Write attack:
		page.append(LocalisationConfiguration.getString("attack").toUpperCase());
		page.append("\n");
		page.append("\n");
		
		DamageType[] damageTypes = DamageType.values();
		for (int i = 0; i < damageTypes.length; i++) {
			DamageType type = damageTypes[i];
			
			int min = dstats.getMinBaseDamage(type);
			int max = dstats.getMaxBaseDamage(type);
			int AR = dstats.getAttackRatings(type);
			if(min == 0 && max == 0) continue;
			
			String dmg;
			if(min == max) dmg = min + "";
			else dmg = min + " - " + max;
			
			if(i != 0) page.append("\n");
			page.append(LocalisationConfiguration.getCapitString(damageTypes[i].toString()) + ": " +  dmg + " " + "(" + AR + ")");
		}
		
		page.append("\n");
		page.append("\n");
		page.append("\n");
		
		// Write defence:
		page.append(LocalisationConfiguration.getString("defence").toUpperCase());
		page.append("\n");
		page.append("\n");
		
		page.append(LocalisationConfiguration.getCapitString("armour") + ": " +  (int)(dstats.getArmour()*100) + "% " + "(" + dstats.getArmourDR() + ")");
		page.append("\n");
		page.append(LocalisationConfiguration.getCapitString("health") + ": " +  (int)dstats.getHealth());
		
		// Page:
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
			line.append(format.format(dstats.getAttribScore(i)));
			line.append("\n");
			page.append(line);
		}

		// Page:
		bookMeta.addPage(page.toString());
		page = new StringBuffer();
		
		// Write skills:
		Skill[] skills = SkillConfiguration.getSkills();
		
		page.append(LocalisationConfiguration.getString("skills").toUpperCase());
		page.append("\n");
		page.append("\n");
		
		for (int i = 0; i < skills.length; i++) {
			StringBuffer line = new StringBuffer();
			line.append(skills[i].getName());
			line.append(" ");
			line.append(dstats.getSkillScore(i));
			line.append("\n");
			page.append(line);
		}

		bookMeta.addPage(page.toString());
		
		// Update book:
		book.setItemMeta(bookMeta);
	}
	
	
}
