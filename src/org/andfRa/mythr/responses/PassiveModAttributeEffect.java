package org.andfRa.mythr.responses;

import org.andfRa.mythr.player.DerivedStats;

public class PassiveModAttributeEffect extends ResponseEffect {

	/** Attribute key. */
	final public static String ATTRIBUTE_KEY = "ATTRIBUTE";

	/** Amount key. */
	final public static String AMOUNT_KEY = "AMOUNT";
	
	
	@Override
	public String key()
	 { return "PASSIVE_MODIFY_ATTRIBUTE_EFFECT"; }
	
	@Override
	public boolean passiveTrigger(Response response, DerivedStats dsstats)
	 {
		String attribute = response.getString(ATTRIBUTE_KEY);
		Integer amount = response.getInt(AMOUNT_KEY);
		
		dsstats.modAttribScore(attribute, amount);

		return true;
	 }
	
}
