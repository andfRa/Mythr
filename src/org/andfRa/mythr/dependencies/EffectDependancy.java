package org.andfRa.mythr.dependencies;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.dependencies.particles.ParticleEffect;
import org.andfRa.mythr.dependencies.particles.ParticleEffectSender;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
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
	
	/**
	 * Plays arcane spell effect.
	 * 
	 * @param loc location
	 * @param progress spell progress
	 */
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

	/**
	 * Plays action fail.
	 * 
	 * @param player player
	 */
	public static void playFail(Player player)
	 {
		player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 0.80f);
	 }
	

	/**
	 * Plays beam.
	 * 
	 * @param lshooter shooter location, includes direction
	 * @param radius beam radius
	 * @param length beam length
	 * @param particles beam particles
	 */
	public static void playBeam(Location lshooter, double radius, double length, ParticleEffect particles)
	 {
		Vector direction = lshooter.getDirection().clone();
		
		Location plocation = lshooter.clone();
		for (double i = 0; i <= length; i+=0.25) {
			plocation.add(direction);
			playParticle(particles, plocation, 0.1f, 1.0f, 5);
		}
	 }
	
	
	
	// ENABLE DISABLE:
	/** Enables the dependency manager. */
	public static void enable()
	 {
		
	 }

	/** Disables the dependency manager. */
	public static void disable()
	 {
		
	 }
	
}
