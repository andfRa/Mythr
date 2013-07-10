package org.andfRa.mythr.config;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class VanillaConfiguration {

	
	// BOOK:
	/** Book page lines. */
	public static final int BOOK_PAGE_LINES = 13;
	
	/** Book line characters. */
	public static final int BOOK_LINE_CHARACTERS = 19;

	
	// ARMOUR RATIOS:
	/** The percent the helmet affects armour. */
	public static final double HELMET_RATIO = 0.15; 

	/** The percent the chestplate affects armour. */
	public static final double CHESTPLATE_RATIO = 0.40; 

	/** The percent the helmet affects armour. */
	public static final double LEGGINGS_RATIO = 0.30; 

	/** The percent the boots affects armour. */
	public static final double BOOTS_RATIO = 0.15; 
	

	// DAMAGE REDUCTION PERCENTS:
	/** The percent the helmet affects damage. */
	public static final double LEATHER_HELMET_MULTIPLIER = -0.04;

	/** The percent the helmet affects damage. */
	public static final double GOLD_HELMET_MULTIPLIER = -0.08;

	/** The percent the helmet affects damage. */
	public static final double CHAINMAIL_HELMET_MULTIPLIER = -0.08;

	/** The percent the helmet affects damage. */
	public static final double IRON_HELMET_MULTIPLIER = -0.08;

	/** The percent the helmet affects damage. */
	public static final double DIAMOND_HELMET_MULTIPLIER = -0.12;


	/** The percent the helmet affects damage. */
	public static final double LEATHER_CHESTPLATE_MULTIPLIER = -0.12;

	/** The percent the helmet affects damage. */
	public static final double GOLD_CHESTPLATE_MULTIPLIER = -0.20;

	/** The percent the helmet affects damage. */
	public static final double CHAINMAIL_CHESTPLATE_MULTIPLIER = -0.20;

	/** The percent the helmet affects damage. */
	public static final double IRON_CHESTPLATE_MULTIPLIER = -0.24;

	/** The percent the helmet affects damage. */
	public static final double DIAMOND_CHESTPLATE_MULTIPLIER = -0.32;


	/** The percent the helmet affects damage. */
	public static final double LEATHER_LEGGINGS_MULTIPLIER = -0.08;

	/** The percent the helmet affects damage. */
	public static final double GOLD_LEGGINGS_MULTIPLIER = -0.12;

	/** The percent the helmet affects damage. */
	public static final double CHAINMAIL_LEGGINGS_MULTIPLIER = -0.16;

	/** The percent the helmet affects damage. */
	public static final double IRON_LEGGINGS_MULTIPLIER = -0.20;

	/** The percent the helmet affects damage. */
	public static final double DIAMOND_LEGGINGS_MULTIPLIER = -0.24;
	
	
	/** The percent the helmet affects damage. */
	public static final double LEATHER_BOOTS_MULTIPLIER = -0.04;

	/** The percent the helmet affects damage. */
	public static final double GOLD_BOOTS_MULTIPLIER = -0.08;

	/** The percent the helmet affects damage. */
	public static final double CHAINMAIL_BOOTS_MULTIPLIER = -0.08;

	/** The percent the helmet affects damage. */
	public static final double IRON_BOOTS_MULTIPLIER = -0.08;

	/** The percent the helmet affects damage. */
	public static final double DIAMOND_BOOTS_MULTIPLIER = -0.12;

	
	// QUICKBAR:
	/** First quickbar slot. */
	public static final int QUICKBAR_FIRST_SLOT = 36;

	/** Quickbar slot count. */
	public static final int QUICKBAR_SLOT_COUNT = 9;
	
	
	// DAMAGE:
	/** Default damage. */
	public static Integer DEFAULT_DAMAGE = 1;
	
	/** Maps item types to damage. */
	@SuppressWarnings("serial")
	public static HashMap<Material, Integer> DAMAGE_MAP = new HashMap<Material, Integer>()
	 {
		{
			put(Material.WOOD_SWORD, 4);
			put(Material.GOLD_SWORD, 4);
			put(Material.STONE_SWORD, 5);
			put(Material.IRON_SWORD, 6);
			put(Material.DIAMOND_SWORD, 7);
			
			put(Material.WOOD_AXE, 3);
			put(Material.GOLD_AXE, 3);
			put(Material.STONE_AXE, 4);
			put(Material.IRON_AXE, 5);
			put(Material.DIAMOND_AXE, 6);
			
			put(Material.WOOD_PICKAXE, 2);
			put(Material.GOLD_PICKAXE, 2);
			put(Material.STONE_PICKAXE, 3);
			put(Material.IRON_PICKAXE, 4);
			put(Material.DIAMOND_PICKAXE, 5);
			
			put(Material.WOOD_SPADE, 1);
			put(Material.GOLD_SPADE, 1);
			put(Material.STONE_SPADE, 2);
			put(Material.IRON_SPADE, 3);
			put(Material.DIAMOND_SPADE, 4);
		}
	 };
	
	/**
	 * Checks if the item is a sword.
	 * 
	 * @param item item, null if none
	 * @return true if sword
	 */
	public static boolean isSword(ItemStack item)
	 {
		if(item == null) return false;
		return isSword(item.getType());
	 }

	/**
	 * Checks if the item is a sword.
	 * 
	 * @param mat item material
	 * @return true if sword
	 */
	public static boolean isSword(Material mat)
	 {
		if(mat == null) return false;
		if(mat == Material.WOOD_SWORD) return true;
		if(mat == Material.GOLD_SWORD) return true;
		if(mat == Material.STONE_SWORD) return true;
		if(mat == Material.IRON_SWORD) return true;
		if(mat == Material.DIAMOND_SWORD) return true;
		return false;
	 }
	
	/**
	 * Gets the base damage the item does.
	 * 
	 * @param item item
	 * @return base damage
	 */
	public static int getBaseDamage(ItemStack item)
	 {
		if(item == null) return 0;
		Integer damage = DAMAGE_MAP.get(item.getType());
		if(damage == null) return DEFAULT_DAMAGE;
		return damage.intValue();
	 }

	/**
	 * Checks is the given the entity will not get damaged.
	 * 
	 * @param lentity living entity
	 * @return true if can't be damaged
	 */
	public static boolean checkNoDamageTicks(LivingEntity lentity)
	{
		return lentity.getNoDamageTicks() > lentity.getMaximumNoDamageTicks()/2F;
	 }
	
	
	// HEALTH:
	/** Base health. */
	public static final double BASE_HEALTH = 20.0;
	
}
