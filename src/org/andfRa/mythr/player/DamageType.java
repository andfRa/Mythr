package org.andfRa.mythr.player;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.items.ItemType;

public enum DamageType {

	MELEE,
	RANGED,
	ARCANE,
	CURSE,
	BLESSING,
	NORMAL;

	
	/**
	 * Matches damage type with item type.
	 * 
	 * @param type damage type
	 * @return item type
	 */
	public static DamageType match(ItemType type)
	 {
		switch (type) {
		case MELEE_WEAPON:
			return MELEE;

		case RANGED_WEAPON:
			return RANGED;
			
		case ARCANE_SPELL:
			return ARCANE;

		case CURSE_SPELL:
			return CURSE;

		case BLESSING_SPELL:
			return BLESSING;
			
		default:
			break;
		}
		return NORMAL;
	 }
	
	/**
	 * Matches a damage type.
	 * 
	 * @param name damage type name
	 * @return matched damage type
	 */
	public static DamageType match(String name)
	 {
		try {
			return DamageType.valueOf(name.toUpperCase().replace(' ', '_'));
		}
		catch (IllegalArgumentException e) {
			MythrLogger.warning(DamageType.class, "Failed to find damage type for " + name + ".");
			return null;
		}
	 }
	
	/**
	 * Gets the damage type count. 
	 * 
	 * @return damage type count
	 */
	public static int count()
	 {
		return values().length;
	 }
	
	@Override
	public String toString()
	 {
		return super.toString().replace('_', ' ').toLowerCase();
	 }
	
}
