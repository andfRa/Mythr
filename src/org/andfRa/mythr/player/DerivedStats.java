package org.andfRa.mythr.player;

import java.util.Random;

import org.andfRa.mythr.config.AttributeConfiguration;
import org.andfRa.mythr.config.CreatureConfiguration;
import org.andfRa.mythr.config.SkillConfiguration;
import org.andfRa.mythr.config.VanillaConfiguration;
import org.andfRa.mythr.items.ItemType;
import org.andfRa.mythr.items.MythrItem;
import org.andfRa.mythr.util.LinearFunction;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;

public abstract class DerivedStats {


	/** Random number generator. */
	public static Random RANDOM = new Random();
	
	/** Derived stats for default creatures. */
	public static DerivedStats DEFAULT_CREATURE_STATS = new DerivedStats() {
		
		@Override
		protected int getAttributeScore(String attribName) {
			// TODO Auto-generated method stub
			return CreatureConfiguration.getDefaultAttribScore();
		}

		@Override
		protected int getSkillScore(String skillName) {
			return CreatureConfiguration.getDefaultSkillScore();
		}
		
	};
	
	
	/** Minimum base damage. */
	private int minBaseDmg = 0;

	/** Maximum base damage. */
	private int maxBaseDmg = 0;


	/** Melee damage modifier. */
	private int meleeDmgMod = 0;

	/** Ranged damage modifier. */
	private int rangedDmgMod = 0;

	/** Magic damage modifier. */
	private int magicDmgMod = 0;

	/** Curse damage modifier. */
	private int curseDmgMod = 0;

	/** Blessing damage modifier. */
	private int blessingDmgMod = 0;

	
	/** Melee attack rating. */
	private int meleeAR = 1;

	/** Ranged attack rating. */
	private int rangedAR = 1;

	/** Magic attack rating. */
	private int magicAR = 1;

	/** Curse attack rating. */
	private int curseAR = 1;

	/** Blessing attack rating. */
	private int blessingAR = 1;


	/** Armour induced damage multiplier. */
	private double armour = 1.0;

	
	/** Leather armour defence rating. */
	private int leatherDR = 0;

	/** Gold armour defence rating. */
	private int goldDR = 0;

	/** Chainmail armour defence rating. */
	private int chainDR = 0;

	/** Iron armour defence rating. */
	private int ironDR = 0;

	/** Diamond armour defence rating. */
	private int diamondDR = 0;

	
	// CONSTRUCTION:
	/**
	 * Calculates the derived stats.
	 * 
	 * @param living living entity
	 */
	public DerivedStats() {
		
	}

	/**
	 * Update all weapon stats.
	 * 
	 * @param living attacker living entity
	 */
	public void updateWeapon(LivingEntity living)
	 {
		resetWeapon();
		
		MythrItem mitem;
		EntityEquipment inventory = living.getEquipment();
		
		// Weapon item:
		if(inventory.getItemInHand() != null){
			
			mitem = MythrItem.fromBukkitItem(inventory.getItemInHand());
			
			switch (mitem.getType()) {
			case MELEE_WEAPON:
				meleeAR+= mitem.getAttackRating();
				break;
			
			case RANGED_WEAPON:
				rangedAR+= mitem.getAttackRating();
				break;
			
			case MAGIC_WEAPON:
				magicAR+= mitem.getAttackRating();
				break;
			
			case CURSE_WEAPON:
				curseAR+= mitem.getAttackRating();
				break;
				
			case BLESSING_WEAPON:
				blessingAR+= mitem.getAttackRating();
				break;

			default:
				break;
			}
			
			minBaseDmg = mitem.getDmgMin();
			maxBaseDmg = mitem.getDmgMax();
			
		}
		// No weapon item:
		else{
			minBaseDmg = VanillaConfiguration.DEFAULT_DAMAGE;
			maxBaseDmg = VanillaConfiguration.DEFAULT_DAMAGE;
		}

		// Skills:
		Skill[] skills = SkillConfiguration.getSkills();
		for (int i = 0; i < skills.length; i++) {
			int score = getSkillScore(skills[i].getName());
			
			meleeAR+= skills[i].getSpecifier(Specifier.MELEE_ATTACK_RATING_MODIFIER, score);
			rangedAR+= skills[i].getSpecifier(Specifier.RANGED_ATTACK_RATING_MODIFIER, score);
			magicAR+= skills[i].getSpecifier(Specifier.MAGIC_ATTACK_RATING_MODIFIER, score);
			curseAR+= skills[i].getSpecifier(Specifier.CURSE_ATTACK_RATING_MODIFIER, score);
			blessingAR+= skills[i].getSpecifier(Specifier.BLESSING_ATTACK_RATING_MODIFIER, score);
		}

		// Skills:
		Attribute[] attributes = AttributeConfiguration.getAttributes();
		for (int i = 0; i < attributes.length; i++) {
			int score = getAttributeScore(attributes[i].getName());
			
			meleeDmgMod+= attributes[i].getSpecifier(Specifier.MELEE_ATTACK_DAMAGE_MODIFIER, score);
			rangedDmgMod+= attributes[i].getSpecifier(Specifier.RANGED_ATTACK_DAMAGE_MODIFIER, score);
			magicDmgMod+= attributes[i].getSpecifier(Specifier.MAGIC_ATTACK_DAMAGE_MODIFIER, score);
			curseDmgMod+= attributes[i].getSpecifier(Specifier.CURSE_ATTACK_DAMAGE_MODIFIER, score);
			blessingDmgMod+= attributes[i].getSpecifier(Specifier.BLESSING_ATTACK_DAMAGE_MODIFIER, score);
		}
		
		
	 }

	/** Resets weapon stats. */
	public void resetWeapon()
	 {
		minBaseDmg = 0;
		maxBaseDmg = 0;
		
		meleeAR = 1;
		rangedAR = 1;
		magicAR = 1;
		curseAR = 1;
		blessingAR = 1;
	 }

	/**
	 * Updates all armour stats.
	 * 
	 * @param living living entity
	 */
	public void updateArmour(LivingEntity living)
	 {
		resetArmour();

		Material material;
		MythrItem mitem;
		EntityEquipment inventory = living.getEquipment();
		
		double leather = 0.0;
		double gold = 0.0;
		double chain = 0.0;
		double iron = 0.0;
		double diamond = 0.0;
		
		// Helmet:
		if(inventory.getHelmet() != null){
			mitem = MythrItem.fromBukkitItem(inventory.getHelmet());
			material = mitem.getMaterial();
			
			switch (material) {
			case LEATHER_HELMET:
				leatherDR+= mitem.getDefenceRating();
				armour+= VanillaConfiguration.LEATHER_HELMET_MULTIPLIER;
				leather+= VanillaConfiguration.HELMET_RATIO;
				break;
			
			case GOLD_HELMET:
				goldDR+= mitem.getDefenceRating();
				armour+= VanillaConfiguration.GOLD_HELMET_MULTIPLIER;
				gold+= VanillaConfiguration.HELMET_RATIO;
				break;

			case CHAINMAIL_HELMET:
				chainDR+= mitem.getDefenceRating();
				armour+= VanillaConfiguration.CHAINMAIL_HELMET_MULTIPLIER;
				chain+= VanillaConfiguration.HELMET_RATIO;
				break;

			case IRON_HELMET:
				ironDR+= mitem.getDefenceRating();
				armour+= VanillaConfiguration.IRON_HELMET_MULTIPLIER;
				iron+= VanillaConfiguration.HELMET_RATIO;
				break;
			
			case DIAMOND_HELMET:
				diamondDR+= mitem.getDefenceRating();
				armour+= VanillaConfiguration.DIAMOND_HELMET_MULTIPLIER;
				diamond+= VanillaConfiguration.HELMET_RATIO;
				break;
				
			default:
				break;
			}
		}
		
		// Chestplate:
		if(inventory.getChestplate() != null){
			mitem = MythrItem.fromBukkitItem(inventory.getChestplate());
			material = mitem.getMaterial();
			
			switch (material) {
			case LEATHER_CHESTPLATE:
				leatherDR+= mitem.getDefenceRating();
				armour+= VanillaConfiguration.LEATHER_CHESTPLATE_MULTIPLIER;
				leather+= VanillaConfiguration.CHESTPLATE_RATIO;
				break;
			
			case GOLD_CHESTPLATE:
				goldDR+= mitem.getDefenceRating();
				armour+= VanillaConfiguration.GOLD_CHESTPLATE_MULTIPLIER;
				gold+= VanillaConfiguration.CHESTPLATE_RATIO;
				break;

			case CHAINMAIL_CHESTPLATE:
				chainDR+= mitem.getDefenceRating();
				armour+= VanillaConfiguration.CHAINMAIL_CHESTPLATE_MULTIPLIER;
				chain+= VanillaConfiguration.CHESTPLATE_RATIO;
				break;

			case IRON_CHESTPLATE:
				ironDR+= mitem.getDefenceRating();
				armour+= VanillaConfiguration.IRON_CHESTPLATE_MULTIPLIER;
				iron+= VanillaConfiguration.CHESTPLATE_RATIO;
				break;
			
			case DIAMOND_CHESTPLATE:
				diamondDR+= mitem.getDefenceRating();
				armour+= VanillaConfiguration.DIAMOND_CHESTPLATE_MULTIPLIER;
				diamond+= VanillaConfiguration.CHESTPLATE_RATIO;
				break;
				
			default:
				break;
			}
		}
		
		
		// Leggings:
		if(inventory.getLeggings() != null){
			mitem = MythrItem.fromBukkitItem(inventory.getLeggings());
			material = mitem.getMaterial();
			
			switch (material) {
			case LEATHER_LEGGINGS:
				leatherDR+= mitem.getDefenceRating();
				armour+= VanillaConfiguration.LEATHER_LEGGINGS_MULTIPLIER;
				leather+= VanillaConfiguration.LEGGINGS_RATIO;
				break;
			
			case GOLD_LEGGINGS:
				goldDR+= mitem.getDefenceRating();
				armour+= VanillaConfiguration.GOLD_LEGGINGS_MULTIPLIER;
				gold+= VanillaConfiguration.LEGGINGS_RATIO;
				break;

			case CHAINMAIL_LEGGINGS:
				chainDR+= mitem.getDefenceRating();
				armour+= VanillaConfiguration.CHAINMAIL_LEGGINGS_MULTIPLIER;
				chain+= VanillaConfiguration.LEGGINGS_RATIO;
				break;

			case IRON_LEGGINGS:
				ironDR+= mitem.getDefenceRating();
				armour+= VanillaConfiguration.IRON_LEGGINGS_MULTIPLIER;
				iron+= VanillaConfiguration.LEGGINGS_RATIO;
				break;
			
			case DIAMOND_LEGGINGS:
				diamondDR+= mitem.getDefenceRating();
				armour+= VanillaConfiguration.DIAMOND_LEGGINGS_MULTIPLIER;
				diamond+= VanillaConfiguration.LEGGINGS_RATIO;
				break;
				
			default:
				break;
			}
		}
		
		// Boots:
		if(inventory.getBoots() != null){
			mitem = MythrItem.fromBukkitItem(inventory.getBoots());
			material = mitem.getMaterial();
			
			switch (material) {
			case LEATHER_BOOTS:
				leatherDR+= mitem.getDefenceRating();
				armour+= VanillaConfiguration.LEATHER_BOOTS_MULTIPLIER;
				leather+= VanillaConfiguration.BOOTS_RATIO;
				break;
			
			case GOLD_BOOTS:
				goldDR+= mitem.getDefenceRating();
				armour+= VanillaConfiguration.GOLD_BOOTS_MULTIPLIER;
				gold+= VanillaConfiguration.BOOTS_RATIO;
				break;

			case CHAINMAIL_BOOTS:
				chainDR+= mitem.getDefenceRating();
				armour+= VanillaConfiguration.CHAINMAIL_BOOTS_MULTIPLIER;
				chain+= VanillaConfiguration.BOOTS_RATIO;
				break;

			case IRON_BOOTS:
				ironDR+= mitem.getDefenceRating();
				armour+= VanillaConfiguration.IRON_BOOTS_MULTIPLIER;
				iron+= VanillaConfiguration.BOOTS_RATIO;
				break;
			
			case DIAMOND_BOOTS:
				diamondDR+= mitem.getDefenceRating();
				armour+= VanillaConfiguration.DIAMOND_BOOTS_MULTIPLIER;
				diamond+= VanillaConfiguration.BOOTS_RATIO;
				break;
				
			default:
				break;
			}
		}

		// Attributes:
		Attribute[] attributes = AttributeConfiguration.getAttributes();
		for (int i = 0; i < attributes.length; i++) {
			int score = getSkillScore(attributes[i].getName());
			if(leather > 0.0) leatherDR+= leather * attributes[i].getSpecifier(Specifier.LEATHER_ARMOUR_DEFENCE_RATING_MODIFIER, score);
			if(gold > 0.0) goldDR+= gold * attributes[i].getSpecifier(Specifier.GOLD_ARMOUR_DEFENCE_RATING_MODIFIER, score);
			if(chain > 0.0) chainDR+= chain * attributes[i].getSpecifier(Specifier.CHAIN_ARMOUR_DEFENCE_RATING_MODIFIER, score);
			if(iron > 0.0) ironDR+= iron * attributes[i].getSpecifier(Specifier.IRON_ARMOUR_DEFENCE_RATING_MODIFIER, score);
			if(diamond > 0.0) diamondDR+= diamond * attributes[i].getSpecifier(Specifier.DIAMOND_ARMOUR_DEFENCE_RATING_MODIFIER, score);
		}
		
		// Skills:
		Skill[] skills = SkillConfiguration.getSkills();
		for (int i = 0; i < skills.length; i++) {
			int score = getSkillScore(skills[i].getName());
			if(leather > 0.0) leatherDR+= leather * skills[i].getSpecifier(Specifier.LEATHER_ARMOUR_DEFENCE_RATING_MODIFIER, score);
			if(gold > 0.0) goldDR+= gold * skills[i].getSpecifier(Specifier.GOLD_ARMOUR_DEFENCE_RATING_MODIFIER, score);
			if(chain > 0.0) chainDR+= chain * skills[i].getSpecifier(Specifier.CHAIN_ARMOUR_DEFENCE_RATING_MODIFIER, score);
			if(iron > 0.0) ironDR+= iron * skills[i].getSpecifier(Specifier.IRON_ARMOUR_DEFENCE_RATING_MODIFIER, score);
			if(diamond > 0.0) diamondDR+= diamond * skills[i].getSpecifier(Specifier.DIAMOND_ARMOUR_DEFENCE_RATING_MODIFIER, score);
		}
	 }

	/** Resets armour stats. */
	public void resetArmour()
	 {
		armour = 1.0;

		leatherDR = 0;
		goldDR = 0;
		chainDR = 0;
		ironDR = 0;
		diamondDR = 0;
	 }
	
	
	// SCORES:
	/**
	 * Gets the skill score.
	 * 
	 * @param skillName skill name
	 * @return score
	 */
	abstract protected int getSkillScore(String skillName);

	/**
	 * Gets the attribute score.
	 * 
	 * @param attribName attribute name
	 * @return score
	 */
	abstract protected int getAttributeScore(String attribName);
	
	
	// DAMAGE:
	/**
	 * 
	 * 
	 * @param type
	 * @param attacker
	 * @return
	 */
	public int defend(ItemType type, DerivedStats attacker)
	 {
		// Damage and defence:
		int damage = random(attacker.minBaseDmg, attacker.maxBaseDmg);
		double defenceRating = leatherDR + goldDR + chainDR + ironDR + diamondDR;
		double armour = this.armour;
		
		// Attack:
		double attackRating = 0;
		
		switch (type) {
		case MELEE_WEAPON:
			attackRating+= attacker.meleeAR;
			damage+= attacker.meleeDmgMod;
			break;
			
		case RANGED_WEAPON:
			attackRating+= attacker.rangedAR;
			damage+= attacker.rangedDmgMod;
			break;
			
		case MAGIC_WEAPON:
			attackRating+= attacker.magicAR;
			damage+= attacker.magicDmgMod;
			break;

		case CURSE_WEAPON:
			attackRating+= attacker.curseAR;
			damage+= attacker.curseDmgMod;
			break;

		case BLESSING_WEAPON:
			attackRating+= attacker.blessingAR;
			damage+= attacker.blessingDmgMod;
			break;
			
		default:
			break;
		}
		
		// Hit chance:
		double tohit = attackRating / (attackRating + defenceRating);
		boolean hit = tohit >= RANDOM.nextDouble();
		
		// Half armour on hit:
		if(hit){
			armour = armour / 2;
		}else{
			// Apply perks:
		}
		
		// Calculate damage:
		return LinearFunction.roundRand(damage*armour);
	 }
	
	
	// UTIL:
	/**
	 * Generates a random number between the given values.
	 * 
	 * @param min minimum value
	 * @param max maximum value
	 * @return random number
	 */
	public static int random(int min, int max) {
		return min + (int)(RANDOM.nextDouble() * ((max - min) + 1));
	}
	
}
