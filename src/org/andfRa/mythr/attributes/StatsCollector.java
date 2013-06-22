package org.andfRa.mythr.attributes;

import java.util.Random;

import org.andfRa.mythr.Mythr;
import org.andfRa.mythr.config.AttributeConfiguration;
import org.andfRa.mythr.config.VanillaConfiguration;
import org.andfRa.mythr.items.ItemType;
import org.andfRa.mythr.items.MythrItem;
import org.andfRa.mythr.player.Attribute;
import org.andfRa.mythr.player.MythrPlayer;
import org.andfRa.mythr.player.Specifier;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.PlayerInventory;

public class StatsCollector {

	/** Random number generator. */
	public static Random RANDOM = new Random();
	
	
	/** Minimum damage. */
	private int minDamage = 0;

	/** Maximum damage. */
	private int maxDamage = 0;

	/** Attack rating. */
	private int attackRating = 1;
	
	/** Defence rating. */
	private int defenceRating = 1;

	/** Armour induced damage multiplier. */
	private double armour = 1.0;
	
	/** Armour composition material percents. */
	private double leather = 0.0;
	private double chain = 0.0;
	private double gold = 0.0;
	private double iron = 0.0;
	private double diamond = 0.0;

	
	// CONSTRUCTION:
	/**
	 * Collects stats from a player attack a player event.
	 * 
	 * @param type attack type
	 * @param mattacker attacker player
	 * @param mdefender defender player
	 */
	public StatsCollector(ItemType type, MythrPlayer mattacker, MythrPlayer mdefender) {

		// Attacker:
		collectAttackerBase(type, mattacker.getPlayer());
		collectAttackerStats(type, mattacker);
		
		// DEFENDER:
		collectDefenderBase(type, mdefender.getPlayer());
		collectDefenderStats(type, mdefender);
		
	}
	
	/**
	 * Collects stats from a player attack a player event.
	 * 
	 * @param type attack type
	 * @param mattacker attacker player
	 * @param defender defender creature
	 */
	public StatsCollector(ItemType type, MythrPlayer mattacker, Creature defender) {

		// Attacker:
		collectAttackerBase(type, mattacker.getPlayer());
		collectAttackerStats(type, mattacker);
		
		// DEFENDER:
		collectDefenderBase(type, defender);
		
	}
	

	/**
	 * Collects stats from a player attack a player event.
	 * 
	 * @param type attack type
	 * @param attacker attacker creature
	 * @param mdefender defender player
	 */
	public StatsCollector(ItemType type, Creature attacker, MythrPlayer mdefender) {

		// Attacker:
		collectAttackerBase(type, attacker);
		
		// DEFENDER:
		collectDefenderBase(type, mdefender.getPlayer());
		collectDefenderStats(type, mdefender);
		
	}
	
	
	/**
	 * Collects attacker player stats.
	 * 
	 * @param type attack type
	 * @param mplayer attacker player
	 */
	private void collectAttackerStats(ItemType type, MythrPlayer mplayer)
	 {
		// Attributes:
		int[] attribScores = mplayer.getAttrbScores();
		Attribute[] attributes = AttributeConfiguration.getAttributes();
		int damage = 0;
		switch (type) {
		
		// Attribute bonus:
		case MELEE_WEAPON:
			for (int i = 0; i < attributes.length; i++) {
				damage = attributes[i].getSpecifier(Specifier.MELEE_ATTACK_DAMAGE_MODIFIER, attribScores[i]);
				minDamage+= damage;
				maxDamage+= damage;
			}
			break;

		case RANGED_WEAPON:
			for (int i = 0; i < attributes.length; i++) {
				damage = attributes[i].getSpecifier(Specifier.RANGED_ATTACK_DAMAGE_MODIFIER, attribScores[i]);
				minDamage+= damage;
				maxDamage+= damage;
			}
			break;

		case MAGIC_WEAPON:
			for (int i = 0; i < attributes.length; i++) {
				damage = attributes[i].getSpecifier(Specifier.MAGIC_ATTACK_DAMAGE_MODIFIER, attribScores[i]);
				minDamage+= damage;
				maxDamage+= damage;
			}
			break;

		case CURSE_WEAPON:
			for (int i = 0; i < attributes.length; i++) {
				damage = attributes[i].getSpecifier(Specifier.CURSE_ATTACK_DAMAGE_MODIFIER, attribScores[i]);
				minDamage+= damage;
				maxDamage+= damage;
			}
			break;

		case BLESSING_WEAPON:
			for (int i = 0; i < attributes.length; i++) {
				damage = attributes[i].getSpecifier(Specifier.BLESSING_ATTACK_DAMAGE_MODIFIER, attribScores[i]);
				minDamage+= damage;
				maxDamage+= damage;
			}
			break;

		default:
			break;
		}
		
		
	 }
	
	/**
	 * Collects all defender stats.
	 * 
	 * @param type attack type
	 * @param mplayer defender
	 */
	private void collectDefenderStats(ItemType type, MythrPlayer mplayer)
	 {

		
		
	 }
	

	/**
	 * Collects attacker player stats.
	 * 
	 * @param type attack type
	 * @param mplayer attacker living entity
	 */
	private void collectAttackerBase(ItemType type, LivingEntity living)
	 {
		MythrItem mitem;
		EntityEquipment inventory = living.getEquipment();
		
		// Weapon:
		if(inventory.getItemInHand() != null){
			mitem = MythrItem.fromBukkitItem(inventory.getItemInHand());
			attackRating+= mitem.getAttackRating();
			minDamage = mitem.getDmgMin();
			maxDamage = mitem.getDmgMax();
		}
		
		// Helmet:
		if(inventory.getHelmet() != null){
			mitem = MythrItem.fromBukkitItem(inventory.getHelmet());
			attackRating+= mitem.getAttackRating();
		}

		// Chestplate:
		if(inventory.getChestplate() != null){
			mitem = MythrItem.fromBukkitItem(inventory.getChestplate());
			attackRating+= mitem.getAttackRating();
		}

		// Leggings:
		if(inventory.getLeggings() != null){
			mitem = MythrItem.fromBukkitItem(inventory.getLeggings());
			attackRating+= mitem.getAttackRating();
		}

		// Boots:
		if(inventory.getBoots() != null){
			mitem = MythrItem.fromBukkitItem(inventory.getBoots());
			attackRating+= mitem.getAttackRating();
		}
		
		System.out.println("TYPE=" + type);
		
	 }
	
	/**
	 * Collects all defender stats.
	 * 
	 * @param type attack type
	 * @param mplayer defender
	 */
	private void collectDefenderBase(ItemType type, LivingEntity living)
	 {

		Material material;
		MythrItem mitem;
		EntityEquipment inventory = living.getEquipment();
		
		// Weapon:
		if(inventory.getItemInHand() != null){
			mitem = MythrItem.fromBukkitItem(inventory.getItemInHand());
			defenceRating+= mitem.getDefenceRating();
		}
		
		// Helmet:
		if(inventory.getHelmet() != null){
			
			mitem = MythrItem.fromBukkitItem(inventory.getHelmet());
			defenceRating+= mitem.getDefenceRating();
			material = mitem.getMaterial();
			
			if(material == Material.LEATHER_HELMET){
				leather+= VanillaConfiguration.HELMET_RATIO;
				armour+= VanillaConfiguration.LEATHER_HELMET_MULTIPLIER;
			}
			if(material == Material.CHAINMAIL_HELMET){
				chain+= VanillaConfiguration.HELMET_RATIO;
				armour+= VanillaConfiguration.CHAINMAIL_HELMET_MULTIPLIER;
			}
			if(material == Material.GOLD_HELMET){
				gold+= VanillaConfiguration.HELMET_RATIO;
				armour+= VanillaConfiguration.GOLD_HELMET_MULTIPLIER;
			}
			if(material == Material.IRON_HELMET){
				iron+= VanillaConfiguration.HELMET_RATIO;
				armour+= VanillaConfiguration.IRON_HELMET_MULTIPLIER;
			}
			if(material == Material.DIAMOND_HELMET){
				diamond+= VanillaConfiguration.HELMET_RATIO;
				armour+= VanillaConfiguration.DIAMOND_HELMET_MULTIPLIER;
			}
			
		}

		// Chestplate:
		if(inventory.getChestplate() != null){
			
			mitem = MythrItem.fromBukkitItem(inventory.getChestplate());
			defenceRating+= mitem.getDefenceRating();
			material = mitem.getMaterial();
			
			if(material == Material.LEATHER_CHESTPLATE){
				leather+= VanillaConfiguration.CHESTPLATE_RATIO;
				armour+= VanillaConfiguration.LEATHER_CHESTPLATE_MULTIPLIER;
			}
			if(material == Material.CHAINMAIL_CHESTPLATE){
				chain+= VanillaConfiguration.CHESTPLATE_RATIO;
				armour+= VanillaConfiguration.CHAINMAIL_CHESTPLATE_MULTIPLIER;
			}
			if(material == Material.GOLD_CHESTPLATE){
				gold+= VanillaConfiguration.CHESTPLATE_RATIO;
				armour+= VanillaConfiguration.GOLD_CHESTPLATE_MULTIPLIER;
			}
			if(material == Material.IRON_CHESTPLATE){
				iron+= VanillaConfiguration.CHESTPLATE_RATIO;
				armour+= VanillaConfiguration.IRON_CHESTPLATE_MULTIPLIER;
			}
			if(material == Material.DIAMOND_CHESTPLATE){
				diamond+= VanillaConfiguration.CHESTPLATE_RATIO;
				armour+= VanillaConfiguration.DIAMOND_CHESTPLATE_MULTIPLIER;
			}
			
		}

		// Leggings:
		if(inventory.getLeggings() != null){
			
			mitem = MythrItem.fromBukkitItem(inventory.getLeggings());
			defenceRating+= mitem.getDefenceRating();
			material = mitem.getMaterial();
			
			if(material == Material.LEATHER_LEGGINGS){
				leather+= VanillaConfiguration.LEGGINGS_RATIO;
				armour+= VanillaConfiguration.LEATHER_LEGGINGS_MULTIPLIER;
			}
			if(material == Material.CHAINMAIL_LEGGINGS){
				chain+= VanillaConfiguration.LEGGINGS_RATIO;
				armour+= VanillaConfiguration.CHAINMAIL_LEGGINGS_MULTIPLIER;
			}
			if(material == Material.GOLD_LEGGINGS){
				gold+= VanillaConfiguration.LEGGINGS_RATIO;
				armour+= VanillaConfiguration.GOLD_LEGGINGS_MULTIPLIER;
			}
			if(material == Material.IRON_LEGGINGS){
				iron+= VanillaConfiguration.LEGGINGS_RATIO;
				armour+= VanillaConfiguration.IRON_LEGGINGS_MULTIPLIER;
			}
			if(material == Material.DIAMOND_LEGGINGS){
				diamond+= VanillaConfiguration.LEGGINGS_RATIO;
				armour+= VanillaConfiguration.DIAMOND_LEGGINGS_MULTIPLIER;
			}
			
		}

		// Boots:
		if(inventory.getBoots() != null){
			
			mitem = MythrItem.fromBukkitItem(inventory.getBoots());
			defenceRating+= mitem.getDefenceRating();
			material = mitem.getMaterial();
			
			if(material == Material.LEATHER_BOOTS){
				leather+= VanillaConfiguration.BOOTS_RATIO;
				armour+= VanillaConfiguration.LEATHER_BOOTS_MULTIPLIER;
			}
			if(material == Material.CHAINMAIL_BOOTS){
				chain+= VanillaConfiguration.BOOTS_RATIO;
				armour+= VanillaConfiguration.CHAINMAIL_BOOTS_MULTIPLIER;
			}
			if(material == Material.GOLD_BOOTS){
				gold+= VanillaConfiguration.BOOTS_RATIO;
				armour+= VanillaConfiguration.GOLD_BOOTS_MULTIPLIER;
			}
			if(material == Material.IRON_BOOTS){
				iron+= VanillaConfiguration.BOOTS_RATIO;
				armour+= VanillaConfiguration.IRON_BOOTS_MULTIPLIER;
			}
			if(material == Material.DIAMOND_BOOTS){
				diamond+= VanillaConfiguration.BOOTS_RATIO;
				armour+= VanillaConfiguration.DIAMOND_BOOTS_MULTIPLIER;
			}
			
		}

		// Attribute scores:
		//int[] attributes = mplayer.getAttrbScores();
		
		
	 }
	

	// RESULT:
	public void apply(EntityDamageByEntityEvent event) {

		double tohit = attackRating / (attackRating + defenceRating);
		boolean hit = false;
		if(tohit >= RANDOM.nextDouble()) hit = true;
		
		System.out.println("damage=" + minDamage + " - " + maxDamage);
		System.out.println("attackRating=" + attackRating);
		System.out.println("defenceRating=" + defenceRating);
		System.out.println("armour=" + armour);
		
		
	}
	
	
}
