package org.andfRa.mythr.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.andfRa.mythr.Mythr;
import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.config.AttributeConfiguration;
import org.andfRa.mythr.config.ResponseConfiguration;
import org.andfRa.mythr.config.SkillConfiguration;
import org.andfRa.mythr.config.VanillaConfiguration;
import org.andfRa.mythr.creatures.MythrCreature;
import org.andfRa.mythr.items.MythrItem;
import org.andfRa.mythr.responses.Response;
import org.andfRa.mythr.util.LinearFunction;
import org.andfRa.mythr.util.MetadataUtil;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DerivedStats {


	/** Random number generator. */
	public static Random RANDOM = new Random();
	
	/** Derived stats for default creatures. */
	public static DerivedStats DEFAULT_CREATURE_STATS = new DerivedStats(); // TODO: This must be private.
	
	
	/** Minimum base damages. */
	private int[] minBaseDmg = new int[DamageType.count()];

	/** Maximum base damages. */
	private int[] maxBaseDmg = new int[DamageType.count()];
	
	/** Attack ratings. */
	private int[] attackRatings = new int[DamageType.count()];

	
	/** Armour induced damage multiplier. */
	private double armour = 1.0;
	
	/** Armour defence rating. */
	private int armourDR = 0;

	
	/** Health. */
	private double health = VanillaConfiguration.BASE_HEALTH;

	/** Players raw attributes. */
	private int[] attributes = new int[AttributeConfiguration.getAttribCount()];

	/** Players raw attributes. */
	private int[] skills = new int[SkillConfiguration.getSkillCount()];

	
	/** Responses. */
	private String[] responses = new String[0];
	
	
	// CONSTRUCTION:
	/**
	 * Calculates the derived stats.
	 * 
	 * @param living living entity
	 */
	public DerivedStats()
	 { }


	/**
	 * Updates derived stats.
	 * 
	 * @param attribScores attribute scores
	 * @param skillScores skill scores
	 * @param perks perks
	 * @param weapon weapon, null if none
	 * @param helmet helmet, null if none
	 * @param chestplate chestplate, null if none
	 * @param leggings leggings, null if none
	 * @param boots boots, null if none
	 */
	public void update(Map<String, Integer> attribScores, Map<String, Integer> skillScores, List<String> perks, ItemStack weapon, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots)
	 {

		// Equipment:
		MythrItem mweapon = null;
		MythrItem mhelmet = null;
		MythrItem mchestplate = null;
		MythrItem mleggings = null;
		MythrItem mboots = null;
		
		if(weapon != null) mweapon = MythrItem.fromBukkitItem(weapon);
		if(helmet != null) mhelmet = MythrItem.fromBukkitItem(helmet);
		if(chestplate != null) mchestplate = MythrItem.fromBukkitItem(chestplate);
		if(leggings != null) mleggings = MythrItem.fromBukkitItem(leggings);
		if(boots != null) mboots = MythrItem.fromBukkitItem(boots);
		
		// Update:
		update(attribScores, skillScores, perks, mweapon, mhelmet, mchestplate, mleggings, mboots);
	 }
	
	/**
	 * Updates derived stats.
	 * 
	 * @param attribScores attribute scores
	 * @param skillScores skill scores
	 * @param perks perks
	 * @param mweapon Mythr weapon, null if none
	 * @param mhelmet Mythr helmet, null if none
	 * @param mchestplate Mythr chestplate, null if none
	 * @param mleggings Mythr leggings, null if none
	 * @param mboots Mythr boots, null if none
	 */
	public void update(Map<String, Integer> attribScores, Map<String, Integer> skillScores, List<String> perks, MythrItem mweapon, MythrItem mhelmet, MythrItem mchestplate, MythrItem mleggings, MythrItem mboots)
	 {
		// Reset:
		reset();
		
		// Update:
		updateStats(attribScores, skillScores, mweapon, mhelmet, mchestplate, mleggings, mboots);
		updateReponses(perks, mweapon, mhelmet, mchestplate, mleggings, mboots);
		updateWeapon(mweapon);
		updateArmour(mhelmet, mchestplate, mleggings, mboots);
		updateHealth();
	 }
	
	/**
	 * Resets derived stats.
	 * 
	 */
	private void reset()
	 {
		// Stats:
		attributes = new int[AttributeConfiguration.getAttribCount()];
		skills = new int[SkillConfiguration.getSkillCount()];
		responses = new String[0];
		
		// Weapon:
		minBaseDmg = new int[DamageType.count()];
		maxBaseDmg = new int[DamageType.count()];
		
		attackRatings = new int[DamageType.count()];
		
		// Armour:
		armour = 1.0;
		armourDR = 0;
		
		// Health:
		health = VanillaConfiguration.BASE_HEALTH;
	 }
	

	/**
	 * Updates player stats.
	 * 
	 * @param attribScores attribute scores
	 * @param skillScores skill scores
	 * @param mweapon weapon Mythr item
	 * @param mhelmet helmet Mythr item, null if none
	 * @param mchestplate chestplate Mythr item, null if none
	 * @param mleggings leggings Mythr item, null if none
	 * @param mboots boots Mythr item, null if none
	 */
	private void updateStats(Map<String, Integer> attribScores, Map<String, Integer> skillScores, MythrItem mweapon, MythrItem mhelmet, MythrItem mchestplate, MythrItem mleggings, MythrItem mboots)
	 {
		// Attributes:
		String[] attribNames = AttributeConfiguration.getAttrNames();
		for (int i = 0; i < attribNames.length; i++) {
			Integer score = attribScores.get(attribNames[i]);
			if(score == null) continue;
			attributes[i] = score;
		}

		// Skills:
		String[] skillNames = SkillConfiguration.getSkillNames();
		for (int i = 0; i < skillNames.length; i++) {
			Integer score = skillScores.get(skillNames[i]);
			if(score == null) continue;
			skills[i] = score;
		}
	 }
	
	/**
	 * Updates player reponses.
	 * 
	 * @param perks perks
	 * @param mweapon weapon Mythr item
	 * @param mhelmet helmet Mythr item, null if none
	 * @param mchestplate chestplate Mythr item, null if none
	 * @param mleggings leggings Mythr item, null if none
	 * @param mboots boots Mythr item, null if none
	 */
	private void updateReponses(List<String> perks, MythrItem mweapon, MythrItem mhelmet, MythrItem mchestplate, MythrItem mleggings, MythrItem mboots)
	 {
		
		// Gather responses:
		ArrayList<String> responses = new ArrayList<String>();
		for (String perk : perks) {
			responses.add(perk);
		}
		if(mweapon != null) responses.addAll(mweapon.getResponses());
		if(mhelmet != null) responses.addAll(mhelmet.getResponses());
		if(mchestplate != null) responses.addAll(mchestplate.getResponses());
		if(mleggings != null) responses.addAll(mleggings.getResponses());
		if(mboots != null) responses.addAll(mboots.getResponses());
		
		// Trigger passives:
		Response response = null;
		for (String respName : responses) {
			response = ResponseConfiguration.getResponse(respName);
			if(response == null) continue;
			response.passiveTrigger(this);
		}
		
		this.responses = responses.toArray(new String[responses.size()]);
	 }

	/**
	 * Update all weapon stats.
	 * 
	 * @param mweapon weapon Mythr item, null if none
	 */
	private void updateWeapon(MythrItem mweapon)
	 {
		// Weapon item:
		if(mweapon != null){
			
			switch (mweapon.getType()) {
			case MELEE_WEAPON:
				minBaseDmg[DamageType.MELEE.ordinal()] = mweapon.getDamageMin();
				maxBaseDmg[DamageType.MELEE.ordinal()] = mweapon.getDamageMax();
				attackRatings[DamageType.MELEE.ordinal()] = mweapon.getAttackRating();
				break;
			
			case RANGED_WEAPON:
				minBaseDmg[DamageType.RANGED.ordinal()] = mweapon.getDamageMin();
				maxBaseDmg[DamageType.RANGED.ordinal()] = mweapon.getDamageMax();
				attackRatings[DamageType.RANGED.ordinal()] = mweapon.getAttackRating();
				break;
			
			case ARCANE_SPELL:
				minBaseDmg[DamageType.ARCANE.ordinal()] = mweapon.getDamageMin();
				maxBaseDmg[DamageType.ARCANE.ordinal()] = mweapon.getDamageMax();
				attackRatings[DamageType.ARCANE.ordinal()] = mweapon.getAttackRating();
				break;
			
			case CURSE_SPELL:
				minBaseDmg[DamageType.CURSE.ordinal()] = mweapon.getDamageMin();
				maxBaseDmg[DamageType.CURSE.ordinal()] = mweapon.getDamageMax();
				attackRatings[DamageType.CURSE.ordinal()] = mweapon.getAttackRating();
				break;
				
			case BLESSING_SPELL:
				minBaseDmg[DamageType.BLESSING.ordinal()] = mweapon.getDamageMin();
				maxBaseDmg[DamageType.BLESSING.ordinal()] = mweapon.getDamageMax();
				attackRatings[DamageType.BLESSING.ordinal()] = mweapon.getAttackRating();
				break;

			default:
				break;
			}
			
		}
		// No weapon item:
		else{
			for (int i = 0; i < DamageType.count(); i++) minBaseDmg[i] = VanillaConfiguration.DEFAULT_DAMAGE;
			for (int i = 0; i < DamageType.count(); i++) maxBaseDmg[i] = VanillaConfiguration.DEFAULT_DAMAGE;
		}

		// Attributes bonus:
		Attribute[] attributes = AttributeConfiguration.getAttributes();
		for (int i = 0; i < attributes.length; i++) {
			int score = getAttribScore(i);
			int dmg = 0;

			dmg = attributes[i].getSpecifier(Specifier.MELEE_ATTACK_DAMAGE_MODIFIER, score);
			minBaseDmg[DamageType.MELEE.ordinal()]+= dmg;
			maxBaseDmg[DamageType.MELEE.ordinal()]+= dmg;

			dmg = attributes[i].getSpecifier(Specifier.RANGED_ATTACK_DAMAGE_MODIFIER, score);
			minBaseDmg[DamageType.RANGED.ordinal()]+= dmg;
			maxBaseDmg[DamageType.RANGED.ordinal()]+= dmg;
			
			dmg = attributes[i].getSpecifier(Specifier.ARCANE_ATTACK_DAMAGE_MODIFIER, score);
			minBaseDmg[DamageType.ARCANE.ordinal()]+= dmg;
			maxBaseDmg[DamageType.ARCANE.ordinal()]+= dmg;

			dmg = attributes[i].getSpecifier(Specifier.CURSE_ATTACK_DAMAGE_MODIFIER, score);
			minBaseDmg[DamageType.CURSE.ordinal()]+= dmg;
			maxBaseDmg[DamageType.CURSE.ordinal()]+= dmg;

			dmg = attributes[i].getSpecifier(Specifier.BLESSING_ATTACK_DAMAGE_MODIFIER, score);
			minBaseDmg[DamageType.BLESSING.ordinal()]+= dmg;
			maxBaseDmg[DamageType.BLESSING.ordinal()]+= dmg;
		}
		
		// Skill bonus:
		Skill[] skills = SkillConfiguration.getSkills();
		for (int i = 0; i < skills.length; i++) {
			int score = getSkillScore(i);
			
			attackRatings[DamageType.MELEE.ordinal()]+= skills[i].getSpecifier(Specifier.MELEE_ATTACK_RATING_MODIFIER, score);
			attackRatings[DamageType.RANGED.ordinal()]+= skills[i].getSpecifier(Specifier.RANGED_ATTACK_RATING_MODIFIER, score);
			attackRatings[DamageType.ARCANE.ordinal()]+= skills[i].getSpecifier(Specifier.ARCANE_ATTACK_RATING_MODIFIER, score);
			attackRatings[DamageType.CURSE.ordinal()]+= skills[i].getSpecifier(Specifier.CURSE_ATTACK_RATING_MODIFIER, score);
			attackRatings[DamageType.BLESSING.ordinal()]+= skills[i].getSpecifier(Specifier.BLESSING_ATTACK_RATING_MODIFIER, score);
		}
	 }

	/**
	 * Updates all armour stats.
	 * 
	 * @param mhelmet helmet Mythr item, null if none
	 * @param mchestplate chestplate Mythr item, null if none
	 * @param mleggings leggings Mythr item, null if none
	 * @param mboots boots Mythr item, null if none
	 */
	private void updateArmour(MythrItem mhelmet, MythrItem mchestplate, MythrItem mleggings, MythrItem mboots)
	 {
		Material material;
		
		int lightDR = 0;
		int heavyDR = 0;
		int exoticDR = 0;
		
		double light = 0.0;
		double heavy = 0.0;
		double exotic = 0.0;
		
		// Helmet:
		if(mhelmet != null){
			material = mhelmet.getMaterial();
			
			switch (mhelmet.getType()) {
			case LIGHT_ARMOUR:
				lightDR+= mhelmet.getDefenceRating();
				light+= VanillaConfiguration.HELMET_RATIO;
				break;
			
			case HEAVY_ARMOUR:
				heavyDR+= mhelmet.getDefenceRating();
				heavy+= VanillaConfiguration.HELMET_RATIO;
				break;

			case EXOTIC_ARMOUR:
				exoticDR+= mhelmet.getDefenceRating();
				exotic+= VanillaConfiguration.HELMET_RATIO;
				break;
				
			default:
				break;
			}
			
			switch (material) {
			case LEATHER_HELMET:
				armour+= VanillaConfiguration.LEATHER_HELMET_MULTIPLIER;
				break;
			
			case GOLD_HELMET:
				armour+= VanillaConfiguration.GOLD_HELMET_MULTIPLIER;
				break;

			case CHAINMAIL_HELMET:
				armour+= VanillaConfiguration.CHAINMAIL_HELMET_MULTIPLIER;
				break;

			case IRON_HELMET:
				armour+= VanillaConfiguration.IRON_HELMET_MULTIPLIER;
				break;
			
			case DIAMOND_HELMET:
				armour+= VanillaConfiguration.DIAMOND_HELMET_MULTIPLIER;
				break;
				
			default:
				break;
			}
		}
		
		// Chestplate:
		if(mchestplate != null){
			material = mchestplate.getMaterial();

			switch (mchestplate.getType()) {
			case LIGHT_ARMOUR:
				lightDR+= mchestplate.getDefenceRating();
				light+= VanillaConfiguration.HELMET_RATIO;
				break;
			
			case HEAVY_ARMOUR:
				heavyDR+= mchestplate.getDefenceRating();
				heavy+= VanillaConfiguration.HELMET_RATIO;
				break;

			case EXOTIC_ARMOUR:
				exoticDR+= mchestplate.getDefenceRating();
				exotic+= VanillaConfiguration.HELMET_RATIO;
				break;
				
			default:
				break;
			}
			
			switch (material) {
			case LEATHER_CHESTPLATE:
				armour+= VanillaConfiguration.LEATHER_CHESTPLATE_MULTIPLIER;
				break;
			
			case GOLD_CHESTPLATE:
				armour+= VanillaConfiguration.GOLD_CHESTPLATE_MULTIPLIER;
				break;

			case CHAINMAIL_CHESTPLATE:
				armour+= VanillaConfiguration.CHAINMAIL_CHESTPLATE_MULTIPLIER;
				break;

			case IRON_CHESTPLATE:
				armour+= VanillaConfiguration.IRON_CHESTPLATE_MULTIPLIER;
				break;
			
			case DIAMOND_CHESTPLATE:
				armour+= VanillaConfiguration.DIAMOND_CHESTPLATE_MULTIPLIER;
				break;
				
			default:
				break;
			}
		}
		
		// Leggings:
		if(mleggings != null){
			material = mleggings.getMaterial();

			switch (mleggings.getType()) {
			case LIGHT_ARMOUR:
				lightDR+= mleggings.getDefenceRating();
				light+= VanillaConfiguration.HELMET_RATIO;
				break;
			
			case HEAVY_ARMOUR:
				heavyDR+= mleggings.getDefenceRating();
				heavy+= VanillaConfiguration.HELMET_RATIO;
				break;

			case EXOTIC_ARMOUR:
				exoticDR+= mleggings.getDefenceRating();
				exotic+= VanillaConfiguration.HELMET_RATIO;
				break;
				
			default:
				break;
			}
			
			switch (material) {
			case LEATHER_LEGGINGS:
				armour+= VanillaConfiguration.LEATHER_LEGGINGS_MULTIPLIER;
				break;
			
			case GOLD_LEGGINGS:
				armour+= VanillaConfiguration.GOLD_LEGGINGS_MULTIPLIER;
				break;

			case CHAINMAIL_LEGGINGS:
				armour+= VanillaConfiguration.CHAINMAIL_LEGGINGS_MULTIPLIER;
				break;

			case IRON_LEGGINGS:
				armour+= VanillaConfiguration.IRON_LEGGINGS_MULTIPLIER;
				break;
			
			case DIAMOND_LEGGINGS:
				armour+= VanillaConfiguration.DIAMOND_LEGGINGS_MULTIPLIER;
				break;
				
			default:
				break;
			}
		}
		
		// Boots:
		if(mboots != null){
			material = mboots.getMaterial();

			switch (mboots.getType()) {
			case LIGHT_ARMOUR:
				lightDR+= mboots.getDefenceRating();
				light+= VanillaConfiguration.HELMET_RATIO;
				break;
			
			case HEAVY_ARMOUR:
				heavyDR+= mboots.getDefenceRating();
				heavy+= VanillaConfiguration.HELMET_RATIO;
				break;

			case EXOTIC_ARMOUR:
				exoticDR+= mboots.getDefenceRating();
				exotic+= VanillaConfiguration.HELMET_RATIO;
				break;
				
			default:
				break;
			}
			
			switch (material) {
			case LEATHER_BOOTS:
				armour+= VanillaConfiguration.LEATHER_BOOTS_MULTIPLIER;
				break;
			
			case GOLD_BOOTS:
				armour+= VanillaConfiguration.GOLD_BOOTS_MULTIPLIER;
				break;

			case CHAINMAIL_BOOTS:
				armour+= VanillaConfiguration.CHAINMAIL_BOOTS_MULTIPLIER;
				break;

			case IRON_BOOTS:
				armour+= VanillaConfiguration.IRON_BOOTS_MULTIPLIER;
				break;
			
			case DIAMOND_BOOTS:
				armour+= VanillaConfiguration.DIAMOND_BOOTS_MULTIPLIER;
				break;
				
			default:
				break;
			}
		}

		// Attributes:
		Attribute[] attributes = AttributeConfiguration.getAttributes();
		for (int i = 0; i < attributes.length; i++) {
			int score = getAttribScore(i);
			if(light > 0.0) lightDR+= light * attributes[i].getSpecifier(Specifier.LIGHT_ARMOUR_DEFENCE_RATING_MODIFIER, score);
			if(heavy > 0.0) heavyDR+= heavy * attributes[i].getSpecifier(Specifier.HEAVY_ARMOUR_DEFENCE_RATING_MODIFIER, score);
			if(exotic > 0.0) exoticDR+= exotic * attributes[i].getSpecifier(Specifier.EXOTIC_ARMOUR_DEFENCE_RATING_MODIFIER, score);
		}
		
		// Skills:
		Skill[] skills = SkillConfiguration.getSkills();
		for (int i = 0; i < skills.length; i++) {
			int score = getSkillScore(i);
			if(heavy > 0.0) lightDR+= heavy * skills[i].getSpecifier(Specifier.LIGHT_ARMOUR_DEFENCE_RATING_MODIFIER, score);
			if(heavy > 0.0) heavyDR+= heavy * skills[i].getSpecifier(Specifier.HEAVY_ARMOUR_DEFENCE_RATING_MODIFIER, score);
			if(exotic > 0.0) exoticDR+= exotic * skills[i].getSpecifier(Specifier.EXOTIC_ARMOUR_DEFENCE_RATING_MODIFIER, score);
		}
		
		// Armour defence rating:
		armourDR = lightDR + heavyDR + exoticDR;
		
	 }

	/**
	 * Updates health.
	 * 
	 */
	private void updateHealth()
	 {
		// Attributes:
		Attribute[] attributes = AttributeConfiguration.getAttributes();
		for (int i = 0; i < attributes.length; i++) {
			int score = getAttribScore(i);
			health+= attributes[i].getSpecifier(Specifier.HEALTH_MODIFIER, score);
		}
	 }

	
	// SCORES:
	/**
	 * Gets the attribute score.
	 * 
	 * @param index attribute index
	 * @return attribute score
	 * @throws IndexOutOfBoundsException when the index is out of bounds
	 */
	public int getAttribScore(int index)
	 {
		return attributes[index];
	 }
	
	/**
	 * Gets the attribute score.
	 * 
	 * @param attributeName attribute name
	 * @return attribute score
	 */
	public int getAttribScore(String atribName)
	 {
		int i = AttributeConfiguration.getAttribIndex(atribName);
		if(i == -1) return 0;
		
		return getAttribScore(i);
	 }
	
	/**
	 * Modifies attribute score.
	 * 
	 * @param atribName attribute name
	 * @param amount amount
	 */
	public void modAttribScore(String atribName, int amount)
	 {
		int i = AttributeConfiguration.getAttribIndex(atribName);
		if(i == -1) return;
		
		attributes[i]+= amount;
	 }
	
	/**
	 * Gets the skill score.
	 * 
	 * @param index skill index
	 * @return skill score
	 * @throws IndexOutOfBoundsException when the index is out of bounds
	 */
	public int getSkillScore(int index)
	 {
		return skills[index];
	 }
	
	/**
	 * Gets the skill score.
	 * 
	 * @param skillName skill name
	 * @return skill score
	 */
	public int getSkillScore(String skillName)
	 {
		int i = SkillConfiguration.getSkillIndex(skillName);
		if(i == -1) return 0;
		
		return getSkillScore(i);
	 }

	/**
	 * Modifies skill score.
	 * 
	 * @param skillName skill name
	 * @param amount amount
	 */
	public void modSkillScore(String skillName, int amount)
	 {
		int i = SkillConfiguration.getSkillIndex(skillName);
		if(i == -1) return;
		
		skills[i]+= amount;
	 }
	
	
	// RESPONSES:
	/**
	 * Gets all responses.
	 * 
	 * @return responses
	 */
	public Collection<Response> getResponses()
	 {
		ArrayList<Response> responses = new ArrayList<Response>();
		
		for (int i = 0; i < this.responses.length; i++) {
			Response response = ResponseConfiguration.getResponse(this.responses[i]);
			if(response == null) continue;
			responses.add(response);
		}
		return responses;
	 }
	
	
	// ASSIGNMENT:
	/**
	 * Assigns stats to the living entity.
	 * 
	 * @param lentity living entity
	 */
	public void assign(LivingEntity lentity)
	 {
		assignHealth(lentity);
	 }
	
	/**
	 * Assigns health to the living entity.
	 * 
	 * @param lentity living entity
	 */
	public void assignHealth(LivingEntity lentity)
	 {
		lentity.setMaxHealth(health);
	 }
	
	
	// DAMAGE:
	/**
	 * Calculates damage.
	 * 
	 * @param type damage type
	 * @param dsattacker attacker derived stats
	 * @return damage damage
	 */
	public int defend(DamageType type, DerivedStats dsattacker)
	 {
		// Defence:
		double armourDR = this.armourDR;
		double armour = this.armour;
		
		// Attack:
		int damage = VanillaConfiguration.DEFAULT_DAMAGE;
		double attackRating = 0;
		
		switch (type) {
		case MELEE:
			attackRating+= attackRatings[DamageType.MELEE.ordinal()];
			damage = random(dsattacker.minBaseDmg[DamageType.MELEE.ordinal()], dsattacker.maxBaseDmg[DamageType.MELEE.ordinal()]);
			break;
			
		case RANGED:
			attackRating+= attackRatings[DamageType.RANGED.ordinal()];
			damage = random(dsattacker.minBaseDmg[DamageType.RANGED.ordinal()], dsattacker.maxBaseDmg[DamageType.RANGED.ordinal()]);
			break;
			
		case ARCANE:
			attackRating+= attackRatings[DamageType.ARCANE.ordinal()];
			damage = random(dsattacker.minBaseDmg[DamageType.ARCANE.ordinal()], dsattacker.maxBaseDmg[DamageType.ARCANE.ordinal()]);
			break;

		case CURSE:
			attackRating+= attackRatings[DamageType.CURSE.ordinal()];
			damage = random(dsattacker.minBaseDmg[DamageType.CURSE.ordinal()], dsattacker.maxBaseDmg[DamageType.CURSE.ordinal()]);
			break;

		case BLESSING:
			attackRating+= attackRatings[DamageType.BLESSING.ordinal()];
			damage = random(dsattacker.minBaseDmg[DamageType.BLESSING.ordinal()], dsattacker.maxBaseDmg[DamageType.BLESSING.ordinal()]);
			break;
			
		default:
			break;
		}
		
		// Hit chance:
		double tohit = attackRating / (attackRating + armourDR);
		boolean hit = tohit >= RANDOM.nextDouble();
		
		// Half armour on hit:
		if(hit){
			armour = armour + 0.70 * (1 - armour);
		}else{
			// Apply perks:
		}
		
		// Calculate damage:
		return LinearFunction.roundRand(damage*armour);
	 }
	
	
	// VALUES:
	/**
	 * Gets the minimum base damage for damage type.
	 * 
	 * @param type damage type
	 * @return minimum damage
	 */
	public int getMinBaseDamage(DamageType type)
	 { return minBaseDmg[type.ordinal()]; }
	
	/**
	 * Gets the minimum base damage for damage type.
	 * 
	 * @param type damage type
	 * @return maximum damage
	 */
	public int getMaxBaseDamage(DamageType type)
	 { return maxBaseDmg[type.ordinal()]; }
	
	/**
	 * Gets the attack rating.
	 * 
	 * @param type damage type
	 * @return attack rating
	 */
	public int getAttackRatings(DamageType type)
	 { return attackRatings[type.ordinal()]; }
	
	
	/**
	 * Gets armour.
	 * 
	 * @return armour
	 */
	public double getArmour()
	 { return armour; }
	
	/**
	 * Gets armour defence rating.
	 * 
	 * @return defence rating
	 */
	public int getArmourDR()
	 { return armourDR; }
	
	/**
	 * Gets health.
	 * 
	 * @return health
	 */
	public double getHealth()
	 { return health; }
	
	
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
	
	
	// OTHER:
	/* 
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public DerivedStats clone()
	 {
		DerivedStats dstats = new DerivedStats();
		
		dstats.minBaseDmg = new int[DamageType.count()];
		dstats.maxBaseDmg = new int[DamageType.count()];
		dstats.attackRatings = new int[DamageType.count()];
		for (int i = 0; i < DamageType.count(); i++) {
			dstats.minBaseDmg[i] = minBaseDmg[i];
			dstats.maxBaseDmg[i] = maxBaseDmg[i];
			dstats.attackRatings[i] = attackRatings[i];
		}

		dstats.armour = armour;
		dstats.armourDR = armourDR;

		dstats.attributes = attributes;
		dstats.skills = skills;
		
		return dstats;
	}
	
	/**
	 * Finds living entity derived stats.
	 * 
	 * @param lentity living entity
	 * @return derived stats
	 */
	public static DerivedStats findDerived(LivingEntity lentity)
	 {
		// Attached:
		DerivedStats dstats = MetadataUtil.retrieveDerivedStats(lentity);
		if(dstats != null) return dstats;
		
		// Player:
		if(lentity instanceof Player){
			
			MythrPlayer mplayer = Mythr.plugin().getLoadedPlayer(((Player) lentity).getName());
			if(mplayer == null){
				MythrLogger.severe(DerivedStats.class, "Failed to retrieve Mythr player for " + ((Player) lentity).getName() + ".");
				return DEFAULT_CREATURE_STATS;
			}
			
			return mplayer.getDerived();
			
		}
		
		// Creature:
		else if(lentity instanceof Creature){
			
			dstats = MythrCreature.createDerived((Creature) lentity);
			MetadataUtil.attachDerivedStats(lentity, dstats);
			dstats.assign(lentity);
			return dstats;
			
		}
		
		// Strange living entity:
		MythrLogger.severe(DerivedStats.class, "LivingEntity " + lentity + " not a player nor a creature.");
		return DEFAULT_CREATURE_STATS;
	 }
	
}
