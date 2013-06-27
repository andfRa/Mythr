package org.andfRa.mythr.items;


public enum ItemType {

	MELEE_WEAPON('\u24D0'),
	RANGED_WEAPON('\u24D1'),
	ARCANE_SPELL('\u24D2'),
	CURSE_SPELL('\u24D3'),
	BLESSING_SPELL('\u24D4'),
	
	LIGHT_ARMOUR('\u24D5'),
	HEAVY_ARMOUR('\u24D6'),
	EXOTIC_ARMOUR('\u24D7'),
	
	TOOL('\u24D8'),
	
	SKILL_TOME('\u24D9'),
	PERK_TOME('\u24DA'),
	JOURNAL('\u24DB'),
	SCROLL('\u24DC'),
	
	OTHER('\u24DD');

	/** Type indicator. */
	private char ind;

	/** String version. */
	private String str;
	
	private ItemType(char ind)
	 {
		this.ind = ind;
		this.str = name().replace('_', ' ').toLowerCase();
	 }
	
	/**
	 * Matches the indicator character to the item type.
	 * 
	 * @param ind indicator character
	 * @return item type, {@link #OTHER} if none
	 */
	public static ItemType match(char ind)
	 {
		ItemType[] values = values();
		for (int i = 0; i < values.length; i++) {
			if(values[i].ind == ind) return values[i];
		}
		return OTHER;
	 }
	
	/**
	 * Gets the indicator character.
	 * 
	 * @return indicator
	 */
	public char indicator()
	 { return ind; }
	
	/**
	 * Gets the item type as a text.
	 * 
	 * @return item type as text
	 */
	public String text()
	 { return str; }
	
}
