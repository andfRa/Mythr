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
import org.bukkit.entity.Creature;
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
	
	
	/** All drops. */
	private ArrayList<HashMap<String, Double>> drops;

	/** True if drops should be added, instead of overwriting. */
	public Boolean addDrops;
	
	/** Dropped experience. */
	private Integer exp;

	/** True if exp should be added, instead of overwriting. */
	public Boolean addExp;
	

	/** Weapon drop chance. */
	private Double weaponChance;

	/** Helmet drop chance. */
	private Double helmetChance;

	/** Chestplaye drop chance. */
	private Double chestplateChance;

	/** Leggings drop chance. */
	private Double leggingsChance;

	/** Boots drop chance. */
	private Double bootsChance;
	
	
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
		
		this.drops = new ArrayList<HashMap<String,Double>>();
		this.exp = 0;
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
		
		if(drops == null) drops = new ArrayList<HashMap<String,Double>>();
		if(addDrops == null) addDrops = false;
		if(exp == null) exp = 0;
		if(addExp == null) addExp = false;
		
		if(weaponChance == null) weaponChance = 0.0;
		if(helmetChance == null) helmetChance = 0.0;
		if(chestplateChance == null) chestplateChance = 0.0;
		if(leggingsChance == null) leggingsChance = 0.0;
		if(bootsChance == null) bootsChance = 0.0;
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
	
	
	// DROPS:
	/**
	 * Selects drops.
	 * 
	 * @return drops
	 */
	public ArrayList<MythrItem> selectDrops()
	 {
		ArrayList<MythrItem> mdrops = new ArrayList<MythrItem>();
		for (HashMap<String, Double> drop : drops) {
			String strdrop = RandomSelectionUtil.selectRandom(drop);
			if(strdrop.equals(ItemConfiguration.NO_ITEM)) continue;
			mdrops.add(ItemConfiguration.getItem(strdrop));
		}
		return mdrops;
	 }
	
	/**
	 * True if drops should be added, instead of overwriting.
	 * 
	 * @return true if add
	 */
	public Boolean getAddDrops()
	 { return addDrops; }
	
	/**
	 * Gets dropped experience.
	 * 
	 * @return dropped experience
	 */
	public Integer getExp()
	 { return exp; }

	/**
	 * True if exp should be added, instead of overwriting.
	 * 
	 * @return true if add
	 */
	public Boolean getAddExp()
	 { return addExp; }
	
	
	/**
	 * Gets the weaponChance.
	 * 
	 * @return the weaponChance
	 */
	public Double getWeaponChance() {
		return weaponChance;
	}

	/**
	 * Gets the helmetChance.
	 * 
	 * @return the helmetChance
	 */
	public Double getHelmetChance() {
		return helmetChance;
	}

	/**
	 * Gets the chestplateChance.
	 * 
	 * @return the chestplateChance
	 */
	public Double getChestplateChance() {
		return chestplateChance;
	}

	/**
	 * Gets the leggingsChance.
	 * 
	 * @return the leggingsChance
	 */
	public Double getLeggingsChance() {
		return leggingsChance;
	}

	/**
	 * Gets the bootsChance.
	 * 
	 * @return the bootsChance
	 */
	public Double getBootsChance() {
		return bootsChance;
	}

	
	// DERIVED STATS:
	/**
	 * Creates the derived stats for the given creature.
	 * 
	 * @param creature creature
	 * @return calculated derived stats
	 */
	public static DerivedStats createDerived(Creature creature)
	 {
		DerivedStats dstats = null;
		HashMap<String, Integer> attribs = null;
		HashMap<String, Integer> skills = null;
		ArrayList<String> perks = null;
		
		String name = creature.getCustomName();
		MythrCreature mcreature = CreatureConfiguration.getCreature(name);
		
		// Default creature:
		if(mcreature == null){
			dstats = new DerivedStats();

			attribs = new HashMap<String, Integer>();
			 Attribute[] allAttribs = AttributeConfiguration.getAttributes();
			 for (int i = 0; i < allAttribs.length; i++) attribs.put(allAttribs[i].getName(), CreatureConfiguration.getDefaultAttribScore());

			skills = new HashMap<String, Integer>();
			 Skill[] allSkills = SkillConfiguration.getSkills();
			 for (int i = 0; i < allSkills.length; i++) skills.put(allSkills[i].getName(), CreatureConfiguration.getDefaultSkillScore());
			 
			perks = new ArrayList<String>();
		}
		
		// Custom creature:
		else{
			dstats = new DerivedStats();
			attribs = mcreature.attribs;
			skills = mcreature.skills;
			perks = mcreature.collectAllPerks();
		}
		
		// Update:
		EntityEquipment equipment = creature.getEquipment();
		dstats.update(attribs, skills, perks, equipment.getItemInHand(), equipment.getHelmet(), equipment.getChestplate(), equipment.getLeggings(), equipment.getBoots());
		
		// Assign:
		dstats.assign(creature);
		
		return dstats;
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

			// Set drop chances:
			lentity.getEquipment().setItemInHandDropChance(weaponChance.floatValue());
			lentity.getEquipment().setHelmetDropChance(helmetChance.floatValue());
			lentity.getEquipment().setChestplateDropChance(chestplateChance.floatValue());
			lentity.getEquipment().setLeggingsDropChance(leggingsChance.floatValue());
			lentity.getEquipment().setBootsDropChance(bootsChance.floatValue());
			
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
