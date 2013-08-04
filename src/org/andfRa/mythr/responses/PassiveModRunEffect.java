package org.andfRa.mythr.responses;

import org.andfRa.mythr.player.DerivedStats;

public class PassiveModRunEffect extends ResponseEffect {

	/** Modification amount key. */
	final public static String MOD_AMOUNT_KEY = "MOD_AMOUNT";

	
	@Override
	public String key()
	 { return "PASSIVE_MODIFY_RUN_SPEED_EFFECT"; }
	
	
	@Override
	public boolean passiveTrigger(Response response, DerivedStats dsstats)
	 {
		float mod = response.getFloat(MOD_AMOUNT_KEY);
		dsstats.modRunMult(mod);
		return true;
	 }
	
}
