package org.andfRa.mythr.dependencies;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.dependencies.particles.ParticleEffect;
import org.andfRa.mythr.dependencies.particles.ParticleEffectSender;
import org.bukkit.Location;
import org.bukkit.util.Vector;

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
	
	public static void playArcane(Location loc, double progress)
	 {
		final double r = 0.5;
		final double totPhi = 6.2831853;
		final double totY = 2.0;
		final double dPhi = totPhi / 6.0;
		double y = totY * progress;
		
		double phi = 0;
		while(phi < totPhi){
			Vector displ = new Vector(r * Math.cos(phi), y, r * Math.sin(phi));
			playParticle(ParticleEffect.INSTANT_SPELL, loc.clone().add(displ), 0, 0.5f, 1);
			phi+= dPhi;
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
