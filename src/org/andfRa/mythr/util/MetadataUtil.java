package org.andfRa.mythr.util;

import java.util.List;

import org.andfRa.mythr.Mythr;
import org.andfRa.mythr.config.ResponseConfiguration;
import org.andfRa.mythr.player.DerivedStats;
import org.andfRa.mythr.responses.Response;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class MetadataUtil {

	/** Response reaction key. */
	public static String RESPONSE_METADATA_KEY = "RESPONSE_REACTION";

	/** Derived stats attached to the entity. */
	public static String DERIVED_STATS_METADATA_KEY = "DERIVED_STATS";

	
	/**
	 * Attaches a response reaction to the projectile.
	 * 
	 * @param projectile projectile
	 * @param response response
	 */
	public static void attachResponseReaction(Projectile projectile, Response response)
	 {
		projectile.setMetadata(RESPONSE_METADATA_KEY, new FixedMetadataValue(Mythr.plugin(), response.getName()));
	 }
	
	/**
	 * Gets response reaction attached to the projectile.
	 * 
	 * @param projectile projectile
	 * @return response reaction, null if none
	 */
	public static Response retrieveResponseReaction(Projectile projectile)
	 {
		List<MetadataValue> values = projectile.getMetadata(RESPONSE_METADATA_KEY);
		for(MetadataValue value : values){
			if(value.getOwningPlugin().getDescription().getName().equals(Mythr.plugin().getDescription().getName())){
				return ResponseConfiguration.getResponse(value.asString());
			}
		}
		return null;
	 }
	

	/**
	 * Attaches derived stats to the projectile.
	 * 
	 * @param projectile projectile
	 * @param dstats derived stats
	 */
	public static void attachDerivedStats(Projectile projectile, DerivedStats dstats)
	 {
		projectile.setMetadata(DERIVED_STATS_METADATA_KEY, new FixedMetadataValue(Mythr.plugin(), dstats.clone()));
	 }
	
	/**
	 * Gets derived stats attached to the projectile.
	 * 
	 * @param projectile projectile
	 * @return derived stats, null if none
	 */
	public static DerivedStats retrieveDerivedStats(Projectile projectile)
	 {
		List<MetadataValue> values = projectile.getMetadata(DERIVED_STATS_METADATA_KEY);
		for(MetadataValue value : values){
			if(value.getOwningPlugin().getDescription().getName().equals(Mythr.plugin().getDescription().getName())){
				if(value.value() instanceof DerivedStats) return (DerivedStats) value.value();
			}
		}
		return null;
	 }

	/**
	 * Attaches derived stats to the living entity.
	 * 
	 * @param lentity living entity
	 * @param dstats derived stats
	 */
	public static void attachDerivedStats(LivingEntity lentity, DerivedStats dstats)
	 {
		lentity.setMetadata(DERIVED_STATS_METADATA_KEY, new FixedMetadataValue(Mythr.plugin(), dstats));
	 }
	
	/**
	 * Gets derived stats attached to the living entity.
	 * 
	 * @param lentity living entity
	 * @return derived stats, null if none
	 */
	public static DerivedStats retrieveDerivedStats(LivingEntity lentity)
	 {
		List<MetadataValue> values = lentity.getMetadata(DERIVED_STATS_METADATA_KEY);
		for(MetadataValue value : values){
			if(value.getOwningPlugin().getDescription().getName().equals(Mythr.plugin().getDescription().getName())){
				if(value.value() instanceof DerivedStats) return (DerivedStats) value.value();
			}
		}
		return null;
	 }
	
	
}
