package org.andfRa.mythr.dependencies;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.dependencies.particles.ParticleEffect;
import org.andfRa.mythr.dependencies.particles.ParticleEffectSender;
import org.bukkit.Location;

// TODO: Use reflection or a separate plugin to stop bukkit from breaking the plugin every update.

public class EffectDependancy {
	
	/**
	 * Plays particle effect.
	 * 
	 * @param mat particle material
	 * @param loc effect location
	 */
	public static void playParticle(ParticleEffect effect, Location loc, float offset, float speed, int count)
	 {
		try {
			ParticleEffectSender.send(effect, loc, offset, offset, offset, speed, count);
		}
		catch (Exception e) {
			MythrLogger.severe(EffectDependancy.class, "Failed to send effect.");
			MythrLogger.severe(" " + e.getClass().getSimpleName() + ":" + e.getMessage());
		}
	 }
	
	/** Enables the dependency manager. */
	public static void enable()
	 {
		
	 }

	/** Disables the dependency manager. */
	public static void disable()
	 {
		
	 }
	
}
