package org.andfRa.mythr.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.config.AttributeConfiguration;
import org.andfRa.mythr.config.LocalisationConfiguration;
import org.andfRa.mythr.config.VanillaConfiguration;
import org.andfRa.mythr.player.Attribute;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class MythrItem {

	/** Description indicator. */
	public final static char DESCRIPTION_INDICATOR = '\u2473';

	/** Base damage indicator. */
	public final static char BASE_DAMAGE_INDICATOR = '\u2474';

	/** Base damage indicator. */
	public final static char ITEM_TYPE_INDICATOR = '\u2475';

	/** Effect response indicator. */
	public final static char EFFECT_RESPONSE_INDICATOR = '\u2476';

	/** Response indicator. */
	public final static char RESPONSE_INDICATOR = '\u2477';

	/** Attribute requirements indicator. */
	public final static char ATTRIBUTE_LEVEL_REQUIRMENTS_INDICATOR = '\u2478';

	/** Attack rating indicator. */
	public final static char ATTACK_RATING_INDICATOR = '\u2479';

	/** Defence rating indicator. */
	public final static char DEFENCE_RATING_INDICATOR = '\u247A';

	/** Perk indicator. */
	public final static char USABLE_BY_INDICATOR = '\u247B';
	
	
	/** True if the item failed to parse. */
	boolean error = false;
	
	/** Material. */
	private Material material;

	/** Data value. */
	private byte data = 0;
	

	/** Item name. */
	private String name = null;
	
	/** Items description. */
	private ArrayList<String> description = new ArrayList<String>();
	
	/** Weapon type. */
	private ItemType type = ItemType.OTHER;

	
	/** Response effect. */
	private String effect = null;

	/** Responses. */
	private List<String> responses = new ArrayList<String>();
	
	
	/** Item minimum damage. */
	private int damageMin = 0;

	/** Item maximum damage. */
	private int damageMax = 0;

	/** Attack rating. */
	private int attackRating = 1;

	/** Defence rating. */
	private int defenceRating = 0;

	/** Attribute requirements. */
	private HashMap<String, Integer> attrReq = new HashMap<String, Integer>();

	/** Required level. */
	private int levelReq = 0;
	
	/** Usable by. */
	private String[] useableBy = new String[0];
	
	
	// CONSTRUCTION:
	/** An item must always have a type. */
	private MythrItem() {};
	
	/**
	 * Creates a Mythr item.
	 * 
	 * @param mat item material
	 */
	public MythrItem(Material mat)
	 {
		this.material = mat;
	 }
	
	/** Fixes all missing fields. */
	public void complete()
	 {
		if(name == null){
			MythrLogger.nullField(getClass(), "name");
			name = "????";
		}

		if(material == null){
			MythrLogger.nullField(getClass(), "material");
			material = Material.AIR;
		}

		if(type == null){
			MythrLogger.nullField(getClass(), "type");
			type = ItemType.OTHER;
		}

		
		
	 }
	
	
	/**
	 * Converts from bukkit item.
	 * 
	 * @param bitem bukkit item
	 * @return Mythr item
	 */
	public static MythrItem fromBukkitItem(ItemStack bitem)
	 {
		MythrItem mitem = new MythrItem();
		
		// Item stack:
		mitem.material = bitem.getType();
		mitem.data = bitem.getData().getData();
		
		// Meta:
		if(!bitem.hasItemMeta()){
			int baseDmg = VanillaConfiguration.getBaseDamage(bitem);
			mitem.damageMin = baseDmg;
			mitem.damageMax = baseDmg;
			mitem.type = matchType(mitem.material);
			return mitem;
		}
		ItemMeta bmeta = bitem.getItemMeta();
		
		// Name:
		if(bmeta.hasDisplayName()) mitem.name = bmeta.getDisplayName();
		
		// Lore:
		if(!bmeta.hasLore()) return mitem;
		List<String> lore = bmeta.getLore();
		Pattern pattern = null;
		Matcher matcher = null;
		
		String[] abrevs = AttributeConfiguration.getAttrAbbreviations();
		
		for (String line : lore) {
			
			if(line.length() < 2 || line.charAt(0) != ChatColor.COLOR_CHAR) continue;
			
			switch (line.charAt(1)) {
			
			// Description:
			case DESCRIPTION_INDICATOR:
				
				if(line.length() < 4){
					mitem.error = true;
					break;
				}
				mitem.description.add(line.substring(4));
				
				break;

			// Base damage:
			case BASE_DAMAGE_INDICATOR:
				
				pattern = Pattern.compile("(?<= )\\d+(?= - \\d)");
				matcher = pattern.matcher(line);
				
				if(matcher.find()) mitem.damageMin = Integer.parseInt(matcher.group());
				else mitem.error = true;

				pattern = Pattern.compile("(?<=\\d+ - )\\d+");
				matcher = pattern.matcher(line);
					
				if(matcher.find()) mitem.damageMax = Integer.parseInt(matcher.group());
				else mitem.error = true;
					
				break;

			// Item type:
			case ITEM_TYPE_INDICATOR:
				
				if(line.length() >= 4 && line.charAt(2) == ChatColor.COLOR_CHAR){
					mitem.type = ItemType.match(line.charAt(3));
				}else{
					mitem.error = true;
				}
				
				break;

			// Effect:
			case EFFECT_RESPONSE_INDICATOR:

				pattern = Pattern.compile("(?<=: ).+");
				matcher = pattern.matcher(line);
				if(matcher.find()) mitem.effect = matcher.group();
				else mitem.error = true;
				
				break;

			// Responses:
			case RESPONSE_INDICATOR:

				if(line.length() >= 4) mitem.responses.add(line.substring(4));
				break;
					
			// Attributes:
			case ATTRIBUTE_LEVEL_REQUIRMENTS_INDICATOR:	
				
				for (int i = 0; i < abrevs.length; i++) {
					pattern = Pattern.compile("(?<=" + abrevs[i] + " )\\d+");
					matcher = pattern.matcher(line);
					if(matcher.find()) mitem.attrReq.put(abrevs[i], Integer.parseInt(matcher.group()));
					else mitem.error = true;
				}
				
				pattern = Pattern.compile("(?<=lvl )\\d+");
				matcher = pattern.matcher(line);
				if(matcher.find()) mitem.levelReq = Integer.parseInt(matcher.group());
				else mitem.error = true;	
				
				break;

			case ATTACK_RATING_INDICATOR:	
				
				pattern = Pattern.compile("(?<=: )\\d+(?=)");
				matcher = pattern.matcher(line);
				if(matcher.find()) mitem.attackRating = Integer.parseInt(matcher.group());
				else mitem.error = true;
					
				break;
				
			case DEFENCE_RATING_INDICATOR:	
				
				pattern = Pattern.compile("(?<=: )\\d+(?=)");
				matcher = pattern.matcher(line);
				if(matcher.find()) mitem.defenceRating = Integer.parseInt(matcher.group());
				else mitem.error = true;
					
				break;

			case USABLE_BY_INDICATOR:	
				
				pattern = Pattern.compile("(?<=: ).+");
				matcher = pattern.matcher(line);
				if(matcher.find()){
					mitem.useableBy = matcher.group().split(", ");
				}
				else mitem.error = true;
					
				break;
			
				
			default:
				break;
			}
			
			
		}
		
		return mitem;
	 }
	
	/**
	 * Converts to bukkit item.
	 * 
	 * @return bukkit item
	 */
	public ItemStack toBukkitItem()
	 {
		// Bukkit item:
		ItemStack bitem = new ItemStack(material, 1);
		bitem.setData(new MaterialData(material, data));
		
		// Meta:
		ItemMeta bmeta = Bukkit.getItemFactory().getItemMeta(material);
		
		// Name:
		if(name != null) bmeta.setDisplayName(name);
		
		// Lore:
		ChatColor descrCol = ChatColor.GRAY;
		ChatColor statsCol = ChatColor.GRAY;
		ArrayList<String> lore = new ArrayList<String>();

		// Description:
		for (String line : description) {
			lore.add("" + ChatColor.COLOR_CHAR + DESCRIPTION_INDICATOR + descrCol + line);
		}
		
		if(description.size() > 0) lore.add("");
		
		StringBuffer strb;
		String[] attrAbbrev;

		// Responses:
		for (String response : responses) {
			lore.add("" + ChatColor.COLOR_CHAR + RESPONSE_INDICATOR + statsCol + response);
		}
		
		switch (type) {
		case MELEE_WEAPON:
		case RANGED_WEAPON:

			// Base damage:
			lore.add("" + ChatColor.COLOR_CHAR + BASE_DAMAGE_INDICATOR + statsCol + LocalisationConfiguration.getCapitString("damage") + ": " + damageMin + " - " + damageMax);

			// Type:
			lore.add("" + ChatColor.COLOR_CHAR + ITEM_TYPE_INDICATOR + ChatColor.COLOR_CHAR + type.indicator() + statsCol + LocalisationConfiguration.getCapitString("type") + ": " + LocalisationConfiguration.getString(type.text()));

			// Effect:
			if(effect != null)
			 lore.add("" + ChatColor.COLOR_CHAR + EFFECT_RESPONSE_INDICATOR + statsCol + LocalisationConfiguration.getCapitString("effect") + ": " + effect);
			
			// Attack rating:
			lore.add("" + ChatColor.COLOR_CHAR + ATTACK_RATING_INDICATOR + statsCol + LocalisationConfiguration.getCapitString("attack rating") + ": " + attackRating);
			
			break;
		
		case ARCANE_SPELL:
		case BLESSING_SPELL:
		case CURSE_SPELL:

			// Base damage:
			lore.add("" + ChatColor.COLOR_CHAR + BASE_DAMAGE_INDICATOR + statsCol + LocalisationConfiguration.getCapitString("damage") + ": " + damageMin + " - " + damageMax);

			// Type:
			lore.add("" + ChatColor.COLOR_CHAR + ITEM_TYPE_INDICATOR + ChatColor.COLOR_CHAR + type.indicator() + statsCol + LocalisationConfiguration.getCapitString("type") + ": " + LocalisationConfiguration.getString(type.text()));

			// Effect:
			if(effect != null)
			 lore.add("" + ChatColor.COLOR_CHAR + EFFECT_RESPONSE_INDICATOR + statsCol + LocalisationConfiguration.getCapitString("effect") + ": " + effect);
			
			// Attack rating:
			lore.add("" + ChatColor.COLOR_CHAR + ATTACK_RATING_INDICATOR + statsCol + LocalisationConfiguration.getCapitString("attack rating") + ": " + attackRating);
			
			break;
			
		case LIGHT_ARMOUR:
		case HEAVY_ARMOUR:
		case EXOTIC_ARMOUR:

			// Type:
			lore.add("" + ChatColor.COLOR_CHAR + ITEM_TYPE_INDICATOR + ChatColor.COLOR_CHAR + type.indicator() + statsCol + LocalisationConfiguration.getCapitString("type") + ": " + LocalisationConfiguration.getString(type.text()));

			// Effect:
			if(effect != null)
			 lore.add("" + ChatColor.COLOR_CHAR + EFFECT_RESPONSE_INDICATOR + statsCol + LocalisationConfiguration.getCapitString("effect") + ": " + effect);
			
			// Defence rating:
			lore.add("" + ChatColor.COLOR_CHAR + DEFENCE_RATING_INDICATOR + statsCol + LocalisationConfiguration.getCapitString("defence rating") + ": " + defenceRating);
			
			break;
				
		case JOURNAL:

			// Type:
			lore.add("" + ChatColor.COLOR_CHAR + ITEM_TYPE_INDICATOR + ChatColor.COLOR_CHAR + type.indicator() + statsCol + LocalisationConfiguration.getCapitString("type") + ": " + LocalisationConfiguration.getString(type.text()));
			
			break;

		default:
			break;
		}

		// Requires:
		if(!attrReq.isEmpty()){
			
			strb = new StringBuffer();
			 attrAbbrev = AttributeConfiguration.getAttrAbbreviations();
			 for (int i = 0; i < attrAbbrev.length; i++) {
				Integer req = attrReq.get(attrAbbrev[i]);  
				if(req == null) continue;
				if(strb.length() != 0) strb.append(", ");
				strb.append(attrAbbrev[i] + " " + req);
			 }
			 if(levelReq != 0){
				if(strb.length() != 0) strb.append(", ");
				strb.append("lvl" + " " + levelReq);
			 }
			 if(strb.length() == 0) strb.append('-');
			lore.add("" + ChatColor.COLOR_CHAR + ATTRIBUTE_LEVEL_REQUIRMENTS_INDICATOR + statsCol + LocalisationConfiguration.getCapitString("requires") + ": " + strb.toString());
			
		}
		
		// Usable by:
		if(useableBy.length > 0){
			
			strb = new StringBuffer();
			 for (int i = 0; i < useableBy.length; i++) {
				if(i != 0) strb.append(", ");
				strb.append(useableBy[i]);
			 }
			 if(strb.length() == 0) strb.append('-');
			lore.add("" + ChatColor.COLOR_CHAR + USABLE_BY_INDICATOR + statsCol + LocalisationConfiguration.getCapitString("usable by") + ": " + strb.toString());
			
		}
		
		bmeta.setLore(lore);
		
		bitem.setItemMeta(bmeta);
		return bitem;
	 }

	
	// VALUES:
	/**
	 * Gets item material.
	 * 
	 * @return item material
	 */
	public Material getMaterial()
	 { return material; }

	/**
	 * Gets item data.
	 * 
	 * @return item data
	 */
	public byte getData()
	 { return data; }

	
	/**
	 * Gets item name.
	 * 
	 * @return item name
	 */
	public String getName()
	 { return name; }
	
	/**
	 * Gets item description.
	 * 
	 * @return item description
	 */
	public ArrayList<String> getDescription()
	 { return description; }

	/**
	 * Gets item type.
	 * 
	 * @return type
	 */
	public ItemType getType() 
	 { return type; }

	
	/**
	 * Gets item effect response.
	 * 
	 * @return effect
	 */
	public String getEffect()
	 { return effect; }
	
	/**
	 * Gets responses.
	 * 
	 * @return responses
	 */
	public List<String> getResponses()
	 { return responses; }

	
	/**
	 * Gets item minimum damage.
	 * 
	 * @return minimum damage
	 */
	public int getDamageMin()
	 { return damageMin; }

	/**
	 * Gets item maximum damage.
	 * 
	 * @return maximum damage
	 */
	public int getDamageMax()
	 { return damageMax; }
	
	/**
	 * Gets item attack rating.
	 * 
	 * @return attack rating
	 */
	public int getAttackRating()
	 { return attackRating; }

	/**
	 * Gets item defence rating.
	 * 
	 * @return defence rating
	 */
	public int getDefenceRating()
	 { return defenceRating; }

	/**
	 * Gets the attribute requirement.
	 * 
	 * @param attrName attribute name
	 * @return required score
	 */
	public Integer getAttrReq(String attrName)
	 {
		Attribute attribute = AttributeConfiguration.getAttribute(attrName);
		if(attribute == null) return 0;
		Integer req = attrReq.get(attribute.getName());
		if(req == null) return 0;
		return req;
	 }

	/**
	 * Gets the level requirement.
	 * 
	 * @return level requirement
	 */
	public int getLevelReq()
	 { return levelReq; }

	/**
	 * Gets the useableBy.
	 * 
	 * @return the useableBy
	 */
	public String[] getUseableBy()
	 { return useableBy; }

	
	/**
	 * Sets the material.
	 * 
	 * @param material the material to set
	 */
	public void setMaterial(Material material) {
		this.material = material;
	}

	/**
	 * Sets the data.
	 * 
	 * @param data data to set
	 */
	public void setData(byte data) {
		this.data = data;
	}

	
	/**
	 * Sets item name.
	 * 
	 * @param name name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Sets the description.
	 * 
	 * @param description the description to set
	 */
	public void setDescription(ArrayList<String> description) {
		this.description = description;
	}
	
	/**
	 * Sets the type.
	 * 
	 * @param type the type to set
	 */
	public void setType(ItemType type) {
		this.type = type;
	}

	
	/**
	 * Sets the effect response.
	 * 
	 * @param effect the effect of the item
	 */
	public void setEffect(String effect) {
		this.effect = effect;
	}
	
	/**
	 * Adds a response.
	 * 
	 * @param reponse response
	 */
	public void addResponse(String reponse)
	 { responses.add(reponse); }

	
	/**
	 * Sets the damageMin.
	 * 
	 * @param damageMin the damageMin to set
	 */
	public void setMinDamage(int damageMin) {
		this.damageMin = damageMin;
	}

	/**
	 * Sets the damageMax.
	 * 
	 * @param damageMax the damageMax to set
	 */
	public void setMaxDamage(int damageMax) {
		this.damageMax = damageMax;
	}

	/**
	 * Sets attribute score.
	 * 
	 * @param attrName attribute name
	 * @param attrScore attribute score
	 */
	public void setAttrReq(String attrName, Integer attrScore)
	 {
		String attrAbbrev = AttributeConfiguration.matchAbbreviation(attrName);
		if(attrAbbrev == null) return;
		this.attrReq.put(attrAbbrev, attrScore);
	 }

	/**
	 * Sets the levelReq.
	 * 
	 * @param levelReq the levelReq to set
	 */
	public void setLevelReq(int levelReq) {
		this.levelReq = levelReq;
	}

	/**
	 * Sets the attackRating.
	 * 
	 * @param attackRating the attackRating to set
	 */
	public void setAttackRating(int attackRating) {
		this.attackRating = attackRating;
	}

	/**
	 * Sets the defenceRating.
	 * 
	 * @param defenceRating the defenceRating to set
	 */
	public void setDefenceRating(int defenceRating) {
		this.defenceRating = defenceRating;
	}

	/**
	 * Sets the useableBy.
	 * 
	 * @param useableBy the useableBy to set
	 */
	public void setUseableBy(String[] useableBy) {
		this.useableBy = useableBy;
	}

	
	// OTHER:
	/* 
	 * Prints all values.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		StringBuffer result = new StringBuffer();

		result.append("type=" + type);
		
		result.append(", ");

		result.append("description=" + description);
		
		result.append(", ");
		
		result.append("damage=" + damageMin + "-" + damageMax);
		result.append(", ");
		
		result.append(", ");
		
		result.append("attackRating=" + attackRating);
		
		result.append(", ");

		result.append("defenceRating=" + defenceRating);

		result.append(", ");
		
		result.append("attrReq={");
		 Set<Entry<String, Integer>> reqs = attrReq.entrySet();
		 boolean first = true;
		 for (Entry<String, Integer> req : reqs) {
			if(first){
				result.append(",");
				first = false;
			}
			result.append(req.getKey() + ":" + req.getValue());
		 }
		result.append("}");
		
		result.append(", ");
		
		result.append("lvl " + levelReq);
		
		result.append(", ");
		
		result.append("usable by=[");
		 for (int i = 0; i < useableBy.length; i++) {
			if(i!=0) result.append(",");
			result.append(useableBy[i]);
		 }
		 result.append("]");
		
		return result.toString();
		
	}

	/**
	 * Matches item type based on its material.
	 * 
	 * @param meta item material
	 * @return item type, {@link ItemType#OTHER} if none
	 */
	public static ItemType matchType(Material meta)
	 {
		if(VanillaConfiguration.isSword(meta)) return ItemType.MELEE_WEAPON;
		if(meta == Material.BOW) return ItemType.RANGED_WEAPON;
		return ItemType.OTHER;
	 }

	
	public static void main(String[] args) {
		
		
	}
	
}
