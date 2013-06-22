package org.andfRa.mythr.player;

import java.util.HashMap;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.util.LinearFunction;

public class Skill {

	/** Skill name. */
	private String name;

	/** Specifiers. */
	private HashMap<Specifier, LinearFunction> specifiers;
	

	// CONSTRUCTION:
	/** Fixes all missing fields. */
	public void complete()
	 {
		if(name == null){
			MythrLogger.nullField(getClass(), "name");
			name = "????";
		}
		
		if(specifiers == null){
			specifiers = new HashMap<Specifier, LinearFunction>();
			MythrLogger.nullField(getClass(), "specifiers");
		}
		for (LinearFunction f : specifiers.values()) { f.complete(); }
	 }
	
	
	// VALUES:
	/**
	 * Gets the skill name.
	 * 
	 * @return skill name
	 */
	public String getName()
	 { return name; }
	
	/**
	 * Gets the specifier value function.
	 * 
	 * @param specifier specifier key
	 * @return specifier value
	 */
	public LinearFunction getSpecifier(Specifier specifier)
	 {
		return specifiers.get(specifier);
	 }

	/**
	 * Gets the specifier value function.
	 * 
	 * @param specifier specifier key
	 * @param specifier skill score
	 * @return specifier value
	 */
	public int getSpecifier(Specifier specifier, int score)
	 {
		LinearFunction f = specifiers.get(specifier);
		if(f == null) return 0;
		return f.yIntFloor(score);
	 }
	
}
