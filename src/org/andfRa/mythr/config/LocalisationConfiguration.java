package org.andfRa.mythr.config;

import org.apache.commons.lang.WordUtils;



public class LocalisationConfiguration {
	
	
	// COMMAND MESSAGES:
	/** Space character in commands. */
	public final static String SPACE = "_";

	/** Indicator for values. */
	public final static String VALUE_INDICATOR = "#";

	
	/** Command message. */
	public final static String MUST_BE_INTEGER = "Value " + VALUE_INDICATOR + " must an integer.";

	/** Command message. */
	public final static String MUST_BE_A_NUMBER = "Value " + VALUE_INDICATOR + " must a number.";


	/** Command message. */
	public final static String PLAYER_DOESNT_EXIST = "Player " + VALUE_INDICATOR + " doesn't exist.";

	/** Command message. */
	public final static String PLAYER_NOT_ONLINE = "Player " + VALUE_INDICATOR + " is not online.";

	
	/** Command message. */
	public final static String ATTRIBUTE_DOESNT_EXIST = "Attribute " + VALUE_INDICATOR + " doesn't exist.";

	/** Command message. */
	public final static String SKILL_DOESNT_EXIST = "Skill " + VALUE_INDICATOR + " doesn't exist.";
	

	/** Command message. */
	public final static String ATTRIBUTE_SET = "Attribute " + VALUE_INDICATOR + " set to " + VALUE_INDICATOR + ".";

	/** Command message. */
	public final static String ATTRIBUTE_SET_OTHER = "Players " + VALUE_INDICATOR + " attribute " + VALUE_INDICATOR + " set to " + VALUE_INDICATOR + ".";

	/** Command message. */
	public final static String SKILL_SET = "Skill " + VALUE_INDICATOR + " set to " + VALUE_INDICATOR + ".";

	/** Command message. */
	public final static String SKILL_SET_OTHER = "Players " + VALUE_INDICATOR + " skill " + VALUE_INDICATOR + " set to " + VALUE_INDICATOR + ".";

	/** Command message. */
	public final static String JOURNAL_SPAWNED_OTHER = "Spawned journal for player " + VALUE_INDICATOR + ".";
	
	
	//ITEMS:
	/** Command message. */
	public final static String ITEM_DOESNT_EXIST = "Item " + VALUE_INDICATOR + " doesn't exist.";
	
	
	public static String getString(String message)
	 {
		return message;
	 }
	
	public static String getCapitString(String message)
	 {
		return WordUtils.capitalize(message);
	 }

	public static String getString(String message, Object... values)
	 {
		message = getString(message);

		for (int i = 0; i < values.length; i++) {
			message = message.replaceFirst(VALUE_INDICATOR, values[i].toString());
		}
		
		return message;
	 }
	
	/**
	 * Converts command argument.
	 * 
	 * @param arg command argument
	 * @return readable format
	 */
	public static String handleArg(String arg)
	 {
		return arg.replace(SPACE, " ");
	 }
	
	
}
