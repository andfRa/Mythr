package org.andfRa.mythr.creatures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.config.AttributeConfiguration;
import org.andfRa.mythr.config.CreatureConfiguration;
import org.andfRa.mythr.config.ItemConfiguration;
import org.andfRa.mythr.config.SkillConfiguration;
import org.andfRa.mythr.items.MythrItem;
import org.andfRa.mythr.player.Attribute;
import org.andfRa.mythr.player.DerivedStats;
import org.andfRa.mythr.player.Skill;
import org.andfRa.mythr.util.MetadataUtil;
import org.andfRa.mythr.util.RandomSelectionUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;

public class MythrCreature {

	/** Creature name. */
	private String name;

	/** Entity type. */
	private EntityType type;
	
	
	/** Attribute scores. */
	private HashMap<String, Integer> attribs;

	/** Skill scores. */
	private HashMap<String, Integer> skills;

	/** Perks. */
	private HashMap<Integer, ArrayList<String>> perks;
	

	/** Weapons. */
	private HashMap<String, Double> weapons;

	/** Helmets. */
	private HashMap<String, Double> helmets;

	/** Chestplates. */
	private HashMap<String, Double> chestplates;

	/** Leggings. */
	private HashMap<String, Double> leggings;

	/** Boots. */
	private HashMap<String, Double> boots;
	
	
	// INITIALISE:
	/** Restrict access to no arguments constructor. */
	@SuppressWarnings("unused")
	private MythrCreature() { }
	
	/**
	 * Creates a Mythr player.
	 * 
	 * @param name player name
	 */
	public MythrCreature(String name)
	 {
		this.name = name;
		this.attribs = new HashMap<String, Integer>();
		this.skills = new HashMap<String, Integer>();
		this.perks = new HashMap<Integer, ArrayList<String>>();
		
		this.weapons = new HashMap<String, Double>();
		this.helmets = new HashMap<String, Double>();
		this.chestplates = new HashMap<String, Double>();
		this.leggings = new HashMap<String, Double>();
		this.boots = new HashMap<String, Double>();
	 }
	
	/** Fixes all missing fields. */
	public void complete()
	 {
		if(name == null){
			name = "????";
			MythrLogger.nullField(getClass(), "name");
		}
		
		if(type == null){
			type = EntityType.PIG;
			MythrLogger.nullField(getClass(), "type");
		}
		
		if(attribs == null) attribs = new HashMap<String, Integer>();
		if(skills == null) skills = new HashMap<String, Integer>();
		if(perks == null) perks = new HashMap<Integer, ArrayList<String>>();
		
		if(weapons == null) weapons = new HashMap<String, Double>();
		if(helmets == null) helmets = new HashMap<String, Double>();
		if(chestplates == null) chestplates = new HashMap<String, Double>();
		if(leggings == null) leggings = new HashMap<String, Double>();
		if(boots == null) boots = new HashMap<String, Double>();
	 }


	// STATS:
	/**
	 * Gets the attribute score.
	 * 
	 * @param name attribute name
	 */
	public Integer getAttribute(String name)
	 {
		Integer score = attribs.get(name);
		if(score == null) return 0;
		return score;
	 }
	
	/**
	 * Gets the skill score.
	 * 
	 * @param name skill name
	 */
	public Integer getSkill(String name)
	 {
		Integer score = skills.get(name);
		if(score == null)  return 0;
		return score;
	 }

	/**
	 * Collects all perks the player has.
	 * 
	 * @return all perks
	 */
	public ArrayList<String> collectAllPerks()
	 {
		ArrayList<String> allPerks = new ArrayList<String>();
		Collection<ArrayList<String>> values = perks.values();
		for (ArrayList<String> value : values) {
			allPerks.addAll(value);
		}
		
		return allPerks;
	 }
	
	
	// DERIVED STATS:
	/**
	 * Calculates derived stats.
	 * 
	 * @return derived stats for the creature
	 */
	public DerivedStats calcDerived(EntityEquipment equipment)
	 {
		DerivedStats derived = new DerivedStats();
		derived.update(attribs, skills, collectAllPerks(), equipment.getItemInHand(), equipment.getHelmet(), equipment.getChestplate(), equipment.getLeggings(), equipment.getBoots());
		return derived;
	 }
	
	/**
	 * Calculates default derived stats.
	 * 
	 * @param equipment creature equipment
	 * @return creatures derived stats
	 */
	public static DerivedStats calcDefaultDerived(EntityEquipment equipment)
	 {
		DerivedStats dscreature = new DerivedStats();
		
		HashMap<String, Integer> attribScores = new HashMap<String, Integer>();
		 Attribute[] attributes = AttributeConfiguration.getAttributes();
		 for (int i = 0; i < attributes.length; i++) attribScores.put(attributes[i].getName(), CreatureConfiguration.getDefaultAttribScore());

		HashMap<String, Integer> skillScores = new HashMap<String, Integer>();
		Skill[] skills = SkillConfiguration.getSkills();
		for (int i = 0; i < skills.length; i++) skillScores.put(skills[i].getName(), CreatureConfiguration.getDefaultSkillScore());
		
		dscreature.update(attribScores, skillScores, new ArrayList<String>(), equipment.getItemInHand(), equipment.getHelmet(), equipment.getChestplate(), equipment.getLeggings(), equipment.getBoots());
		return dscreature;
	 }
	
	
	// SPAWNING:
	/**
	 * Spawns the creature.
	 * 
	 * @param location location
	 */
	public void spawn(Location location) {

		DerivedStats dstats = new DerivedStats();
		
		// Select equipment:
		MythrItem mweapon = null;
		MythrItem mhelmet = null;
		MythrItem mchestplate = null;
		MythrItem mleggings = null;
		MythrItem mboots = null;
		
		String itemName = RandomSelectionUtil.selectRandom(weapons);
		if(itemName != null) mweapon = ItemConfiguration.getItem(itemName);

		itemName = RandomSelectionUtil.selectRandom(helmets);
		if(itemName != null) mhelmet = ItemConfiguration.getItem(itemName);

		itemName = RandomSelectionUtil.selectRandom(chestplates);
		if(itemName != null) mchestplate = ItemConfiguration.getItem(itemName);

		itemName = RandomSelectionUtil.selectRandom(leggings);
		if(itemName != null) mleggings = ItemConfiguration.getItem(itemName);

		itemName = RandomSelectionUtil.selectRandom(boots);
		if(itemName != null) mboots = ItemConfiguration.getItem(itemName);

		dstats.update(attribs, skills, collectAllPerks(), mweapon, mhelmet, mchestplate, mleggings, mboots);
		
		// Spawn creature:
		Entity entity = location.getWorld().spawnEntity(location, type);

		if(entity instanceof LivingEntity){
			
			// Set equipment:
			LivingEntity lentity = (LivingEntity) entity;
			if(mweapon != null) lentity.getEquipment().setItemInHand(mweapon.toBukkitItem());
			if(mhelmet != null) lentity.getEquipment().setHelmet(mhelmet.toBukkitItem());
			if(mchestplate != null) lentity.getEquipment().setChestplate(mchestplate.toBukkitItem());
			if(mleggings != null) lentity.getEquipment().setLeggings(mleggings.toBukkitItem());
			if(mboots != null) lentity.getEquipment().setBoots(mboots.toBukkitItem());
			
			// Set name:
			lentity.setCustomName(name);
			lentity.setCustomNameVisible(true);
			
			// Assign health:
			dstats.assignHealth(lentity);
			
			// Attach derived stats:
			MetadataUtil.attachDerivedStats(lentity, dstats);
			
		}
		
		
	}
	
	
	// VALUES:
	/**
	 * Gets creature name.
	 * 
	 * @return creature name
	 */
	public String getName()
	 { return name; }
	
}
