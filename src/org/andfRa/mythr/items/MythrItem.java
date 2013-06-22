package org.andfRa.mythr.items;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.andfRa.mythr.config.AttributeConfiguration;
import org.andfRa.mythr.config.LocalisationConfiguration;
import org.andfRa.mythr.config.VanillaConfiguration;
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
	public final static char ITEMTYPE_INDICATOR = '\u2475';

	/** Attribute requirements indicator. */
	public final static char ATTRIBUTE_LEVEL_REQUIRMENTS_INDICATOR = '\u2476';

	/** Attribute modifiers indicator. */
	public final static char ATTRIBUTE_MODIFIERS_INDICATOR = '\u2477';

	/** Piercing percent indicator. */
	public final static char PIERCING_PERCENT_INDICATOR = '\u2478';

	/** Attack rating indicator. */
	public final static char ATTACK_RATING_INDICATOR = '\u2479';

	/** Defence rating indicator. */
	public final static char DEFENCE_RATING_INDICATOR = '\u247A';

	/** Perk indicator. */
	public final static char USABLE_BY_INDICATOR = '\u247B';
	
	
	/** True if the item failed to parse. */
	boolean error = false;
	
	/** Item name. */
	private String name = null;
	
	/** Material. */
	private Material material;

	/** Data value. */
	private byte data = 0;
	
	
	/** Weapon type. */
	private ItemType type = ItemType.OTHER;
	
	/** Items description. */
	private ArrayList<String> description = new ArrayList<String>();
	
	/** Item minimum damage. */
	private int dmgMin = 0;

	/** Item maximum damage. */
	private int dmgMax = 0;

	/** Piercing percent. */
	private double piercing = 0.0;

	/** Attack rating. */
	private int attackRating = 1;

	/** Defence rating. */
	private int defenceRating = 0;

	/** Attribute requirements. */
	private int[] attrReq = new int[AttributeConfiguration.getAttrCount()];

	/** Required level. */
	private int levelReq = 0;

	/** Attribute modifiers. */
	private int[] attrMod = new int[AttributeConfiguration.getAttrCount()];
	
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
			mitem.dmgMin = baseDmg;
			mitem.dmgMax = baseDmg;
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
				
				if(matcher.find()) mitem.dmgMin = Integer.parseInt(matcher.group());
				else mitem.error = true;

				pattern = Pattern.compile("(?<=\\d+ - )\\d+");
				matcher = pattern.matcher(line);
					
				if(matcher.find()) mitem.dmgMax = Integer.parseInt(matcher.group());
				else mitem.error = true;
					
				break;

			// Item type:
			case ITEMTYPE_INDICATOR:
				
				if(line.length() >= 4 && line.charAt(2) == ChatColor.COLOR_CHAR){
					mitem.type = ItemType.match(line.charAt(3));
				}else{
					mitem.error = true;
				}
				
				break;

			// Attributes:
			case ATTRIBUTE_LEVEL_REQUIRMENTS_INDICATOR:	
				
				for (int i = 0; i < abrevs.length; i++) {
					pattern = Pattern.compile("(?<=" + abrevs[i] + " )\\d+");
					matcher = pattern.matcher(line);
					if(matcher.find()) mitem.attrReq[i] = Integer.parseInt(matcher.group());
					else mitem.error = true;
				}
				
				pattern = Pattern.compile("(?<=lvl )\\d+");
				matcher = pattern.matcher(line);
				if(matcher.find()) mitem.levelReq = Integer.parseInt(matcher.group());
				else mitem.error = true;	
				
				break;

			case ATTRIBUTE_MODIFIERS_INDICATOR:	
				
				for (int i = 0; i < abrevs.length; i++) {
					pattern = Pattern.compile("(?<=" + abrevs[i] + " )\\d+");
					matcher = pattern.matcher(line);
					if(matcher.find()) mitem.attrMod[i] = Integer.parseInt(matcher.group());
					else mitem.error = true;
				}
					
				break;

			case PIERCING_PERCENT_INDICATOR:	
				
				for (int i = 0; i < abrevs.length; i++) {
					pattern = Pattern.compile("(?<=: )\\d+(?=%)");
					matcher = pattern.matcher(line);
					if(matcher.find()) mitem.piercing = 0.01 * Integer.parseInt(matcher.group());
					else mitem.error = true;
				}
					
				break;
			
			case ATTACK_RATING_INDICATOR:	
				
				for (int i = 0; i < abrevs.length; i++) {
					pattern = Pattern.compile("(?<=: )\\d+(?=)");
					matcher = pattern.matcher(line);
					if(matcher.find()) mitem.attackRating = Integer.parseInt(matcher.group());
					else mitem.error = true;
				}
					
				break;

			case USABLE_BY_INDICATOR:	
				
				for (int i = 0; i < abrevs.length; i++) {
					pattern = Pattern.compile("(?<=: ).+");
					matcher = pattern.matcher(line);
					if(matcher.find()){
						mitem.useableBy = matcher.group().split(", ");
					}
					else mitem.error = true;
				}
					
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
		
		// Base damage:
		lore.add("" + ChatColor.COLOR_CHAR + BASE_DAMAGE_INDICATOR + statsCol + LocalisationConfiguration.getString("Damage:") + " " + dmgMin + " - " + dmgMax);

		// Type:
		lore.add("" + ChatColor.COLOR_CHAR + BASE_DAMAGE_INDICATOR + ChatColor.COLOR_CHAR + ITEMTYPE_INDICATOR + statsCol + LocalisationConfiguration.getString("Type:") + " " + LocalisationConfiguration.getString(type.text()));
		
		// Piercing:
		lore.add("" + ChatColor.COLOR_CHAR + PIERCING_PERCENT_INDICATOR + statsCol + LocalisationConfiguration.getString("Piercing:") + " " + (int)(piercing*100) + "%");

		// Chance to hit:
		lore.add("" + ChatColor.COLOR_CHAR + ATTACK_RATING_INDICATOR + statsCol + LocalisationConfiguration.getString("Attack rating:") + " " + attackRating);

		// Requires:
		StringBuffer strb = new StringBuffer();
		 String[] attrAbbrev = AttributeConfiguration.getAttrAbbreviations();
		 for (int i = 0; i < attrReq.length; i++) {
			if(attrReq[i] == 0) continue;
			if(strb.length() != 0) strb.append(", ");
			strb.append(attrAbbrev[i] + " " + attrReq[i]);
		 }
		 if(levelReq != 0){
			if(strb.length() != 0) strb.append(", ");
			strb.append("lvl" + " " + levelReq);
		 }
		 if(strb.length() == 0) strb.append('-');
		lore.add("" + ChatColor.COLOR_CHAR + ATTRIBUTE_LEVEL_REQUIRMENTS_INDICATOR + statsCol + LocalisationConfiguration.getString("Requires:") + " " + strb.toString());
		
		// Usable by:
		strb = new StringBuffer();
		 for (int i = 0; i < useableBy.length; i++) {
			if(i != 0) strb.append(", ");
			strb.append(useableBy[i]);
		 }
		 if(strb.length() == 0) strb.append('-');
		lore.add("" + ChatColor.COLOR_CHAR + USABLE_BY_INDICATOR + statsCol + LocalisationConfiguration.getString(statsCol + "Usable by:") + " " + strb.toString());
		
		bmeta.setLore(lore);
		
		bitem.setItemMeta(bmeta);
		return bitem;
	 }

	
	// VALUES:
	/**
	 * Gets item name.
	 * 
	 * @return item name
	 */
	public String getName()
	 { return name; }
	

	/**
	 * Gets item data.
	 * 
	 * @return item data
	 */
	public byte getData()
	 { return data; }
	
	/**
	 * Gets item material.
	 * 
	 * @return item material
	 */
	public Material getMaterial()
	 { return material; }

	/**
	 * Gets item type.
	 * 
	 * @return type
	 */
	public ItemType getType() 
	 { return type; }

	/**
	 * Gets item description.
	 * 
	 * @return item description
	 */
	public ArrayList<String> getDescription()
	 { return description; }

	/**
	 * Gets item minimum damage.
	 * 
	 * @return minimum damage
	 */
	public int getDmgMin()
	 { return dmgMin; }

	/**
	 * Gets item maximum damage.
	 * 
	 * @return maximum damage
	 */
	public int getDmgMax()
	 { return dmgMax; }
	
	/**
	 * Gets item piercing.
	 * 
	 * @return item piercing
	 */
	public double getPiercing() 
	 { return piercing; }
	
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
	 * Gets the attribute requirements.
	 * 
	 * @return attribute requirments
	 */
	public int[] getAttrReq() {
		return attrReq;
	}

	/**
	 * Gets the level requirement.
	 * 
	 * @return level requirement
	 */
	public int getLevelReq()
	 { return levelReq; }

	/**
	 * Gets item attribute modifiers.
	 * 
	 * @return attribute modifiers
	 */
	public int[] getAttrMod()
	 { return attrMod; }

	/**
	 * Gets the useableBy.
	 * 
	 * @return the useableBy
	 */
	public String[] getUseableBy()
	 { return useableBy; }
	
	
	/**
	 * Sets item name.
	 * 
	 * @param name name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

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
	 * Sets the type.
	 * 
	 * @param type the type to set
	 */
	public void setType(ItemType type) {
		this.type = type;
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
	 * Sets the dmgMin.
	 * 
	 * @param dmgMin the dmgMin to set
	 */
	public void setMinDamage(int dmgMin) {
		this.dmgMin = dmgMin;
	}

	/**
	 * Sets the dmgMax.
	 * 
	 * @param dmgMax the dmgMax to set
	 */
	public void setMaxDamage(int dmgMax) {
		this.dmgMax = dmgMax;
	}

	/**
	 * Sets the piercing.
	 * 
	 * @param piercing the piercing to set
	 */
	public void setPiercing(double piercing) {
		this.piercing = piercing;
	}

	/**
	 * Sets the attrReq.
	 * 
	 * @param attrReq the attrReq to set
	 */
	public void setAttrReq(int[] attrReq) {
		this.attrReq = attrReq;
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
	 * Sets the attrMod.
	 * 
	 * @param attrMod the attrMod to set
	 */
	public void setAttrMod(int[] attrMod) {
		this.attrMod = attrMod;
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
		
		result.append("damage=" + dmgMin + "-" + dmgMax);
		result.append(", ");
		
		result.append(", ");
		
		result.append("attrMod=[");
		 for (int i = 0; i < attrMod.length; i++) {
			if(i!=0) result.append(",");
			result.append(attrMod[i]);
		 }
		 result.append("]");
		 
		result.append(", ");
		
		result.append("piercing=" + piercing);
			 
		result.append(", ");

		result.append("attackRating=" + attackRating);
		
		result.append(", ");

		result.append("defenceRating=" + defenceRating);

		result.append(", ");
		
		result.append("attrReq=[");
		 for (int i = 0; i < attrReq.length; i++) {
			if(i!=0) result.append(",");
			result.append(attrReq[i]);
		 }
		 result.append("]");
		
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
