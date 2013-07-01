package org.andfRa.mythr.util;

import java.util.List;

import org.andfRa.mythr.Mythr;
import org.andfRa.mythr.config.ResponseConfiguration;
import org.andfRa.mythr.responses.Response;
import org.bukkit.entity.Projectile;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class MetadataUtil {

	/** Response reaction key. */
	public static String RESPONSE_METADATA_KEY = "RESPONSE_REACTION";

	
	/**
	 * Sets the projectile response reaction.
	 * 
	 * @param projectile projectile
	 * @param response response
	 */
	public static void setResponseReaction(Projectile projectile, Response response)
	 {
		projectile.setMetadata(RESPONSE_METADATA_KEY, new FixedMetadataValue(Mythr.plugin(), response.getName()));
	 }
	
	/**
	 * Gets projectiles the response reaction.
	 * 
	 * @param projectile projectile
	 * @return response reaction, null if none
	 */
	public static Response getResponseReaction(Projectile projectile)
	 {
		List<MetadataValue> values = projectile.getMetadata(RESPONSE_METADATA_KEY);
		for(MetadataValue value : values){
			if(value.getOwningPlugin().getDescription().getName().equals(Mythr.plugin().getDescription().getName())){
				return ResponseConfiguration.getResponse(value.asString());
			}
		}
		return null;
	 }
	
}
