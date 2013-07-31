package org.andfRa.mythr.config;

import java.io.IOException;
import java.util.HashMap;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.inout.Directory;
import org.andfRa.mythr.inout.FileIO;
import org.andfRa.mythr.responses.BeamShapeEffect;
import org.andfRa.mythr.responses.DamageEffect;
import org.andfRa.mythr.responses.DisplayJournalEffect;
import org.andfRa.mythr.responses.HealEffect;
import org.andfRa.mythr.responses.ModAttributeEffect;
import org.andfRa.mythr.responses.ModLevelEffect;
import org.andfRa.mythr.responses.PassiveModAttributeEffect;
import org.andfRa.mythr.responses.Response;
import org.andfRa.mythr.responses.ResponseEffect;
import org.andfRa.mythr.responses.ShootFireballEffect;
import org.andfRa.mythr.responses.SprintBonusEffect;
import org.andfRa.mythr.responses.UpdateDerivedEffect;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParseException;

public class ResponseConfiguration {
	
	/** Instance of the configuration. */
	private static ResponseConfiguration config;
	
	
	/** Spell cast radius. */
	private Double spellCastRadius;
	
	/** Responses. */
	private Response[] responses;

	/** Response effects. */
	transient private HashMap<String, ResponseEffect> effects;

	/** Response map. */
	transient private HashMap<String, Response> responseMap;
	

	// CONSTRUCTION:
	/** Fixes all missing fields. */
	public void complete()
	 {
		initEffects();
		
		// Fields:
		if(responses == null){
			MythrLogger.nullField(getClass(), "responses");
			responses = new Response[0];
		}
		for (int i = 0; i < responses.length; i++){
			responses[i].complete();
			responses[i].assign(effects);
		}
		
		if(spellCastRadius == null){
			MythrLogger.nullField(getClass(), "spellCastRadius");
			spellCastRadius = 1.0;
		}
		
		// Response map:
		responseMap = new HashMap<String, Response>();
		for (int i = 0; i < responses.length; i++) {
			if(responseMap.put(responses[i].getName(), responses[i]) != null){
				MythrLogger.warning(getClass(), "Found duplicate responce: " + responses[i].getName() + ".");
			}
		}

	 }
	
	/** Initiates effects. */
	private void initEffects()
	 {
		effects = new HashMap<String, ResponseEffect>();
		ResponseEffect effect;

		// Shoot fireball:
		effect = new ShootFireballEffect();
		effects.put(effect.key(), effect);

		// Modify attribute:
		effect = new ModAttributeEffect();
		effects.put(effect.key(), effect);

		// Passive modify attribute:
		effect = new PassiveModAttributeEffect();
		effects.put(effect.key(), effect);

		// Beam shape:
		effect = new BeamShapeEffect();
		effects.put(effect.key(), effect);

		// Damage effect:
		effect = new DamageEffect();
		effects.put(effect.key(), effect);

		// Heal effect:
		effect = new HealEffect();
		effects.put(effect.key(), effect);

		// Display journal effect:
		effect = new DisplayJournalEffect();
		effects.put(effect.key(), effect);

		// Update derived stats effect:
		effect = new UpdateDerivedEffect();
		effects.put(effect.key(), effect);

		// Mofify level effect:
		effect = new ModLevelEffect();
		effects.put(effect.key(), effect);

		// Sprint bonus effect:
		effect = new SprintBonusEffect();
		effects.put(effect.key(), effect);
		
	 }
	
	
	// VALUES:
	/**
	 * Gets the radius after witch the spell is cancelled.
	 * 
	 * @return spell cancel radius
	 */
	public static Double getCastRadius()
	 { return config.spellCastRadius; }
	
	/**
	 * Gets a response with the given name.
	 * 
	 * @param name response name
	 * @return response, null if none
	 */
	public static Response getResponse(String name)
	 {
		return config.responseMap.get(name);
	 }
	
	
	// LOAD UNLOAD:
	/** Loads the configuration. */
	public static void load(){


		// Create config:
		if(!FileIO.exists(Directory.RESPONSE_CONFIG)){

			try {
				FileIO.unpackConfig(Directory.RESPONSE_CONFIG);
			}
			catch (IOException e) {
				MythrLogger.severe(ResponseConfiguration.class, "Failed to unpack default configuration.");
				MythrLogger.severe(" " + e.getClass().getSimpleName() + ":" + e.getMessage());
			}
			
		}
		
		// Read config:
		ResponseConfiguration config;
		try {
			
			config = FileIO.readConfig(Directory.RESPONSE_CONFIG, ResponseConfiguration.class);
			
		} catch (IOException e) {
			
			MythrLogger.severe(ResponseConfiguration.class, "Failed to read configuration.");
			MythrLogger.severe(" " + e.getClass().getSimpleName() + ":" + e.getMessage());
			config = new ResponseConfiguration();
			
		} catch (JsonParseException e) {

			MythrLogger.severe(ResponseConfiguration.class, "Failed to parse configuration.");
			MythrLogger.severe(" " + e.getClass().getSimpleName() + ":" + e.getMessage());
			config = new ResponseConfiguration();
			
		}
		
		// Set instance:
		ResponseConfiguration.config = config;
		
		// Complete:
		config.complete();
		
	}
	
	/** Unloads the configuration. */
	public static void unload()
	 {
		ResponseConfiguration.config = null;
	 }
	
}
