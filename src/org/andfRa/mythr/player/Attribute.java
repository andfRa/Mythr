package org.andfRa.mythr.player;

import java.util.HashMap;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.util.LinearFunction;

public class Attribute {

	/** Attribute name. */
	private String name;

	/** Attribute abbreviation. */
	private String abbrev;
	
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
		
		if(abbrev == null){
			MythrLogger.nullField(getClass(), "abbrev");
			abbrev = "???";
		}
		
		if(specifiers == null){
			specifiers = new HashMap<Specifier, LinearFunction>();
			MythrLogger.nullField(getClass(), "specifiers");
		}
		for (LinearFunction f : specifiers.values()) { f.complete(); }
	 }
	
	
	// VALUES:
	/**
	 * Gets the attribute name.
	 * 
	 * @return attribute name
	 */
	public String getName()
	 { return name; }
	
	/**
	 * Gets the abbreviation of the attribute name.
	 * 
	 * @return abbreviation
	 */
	public String getAbbrev()
	 { return abbrev; }

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
	 * @param specifier attribute score
	 * @return specifier value
	 */
	public int getSpecifier(Specifier specifier, int score)
	 {
		LinearFunction f = specifiers.get(specifier);
		if(f == null) return 0;
		return f.yIntFloor(score);
	 }
	
}
