package org.andfRa.mythr.responses;

import java.util.ArrayList;

import org.andfRa.mythr.MythrLogger;
import org.andfRa.mythr.config.ResponseConfiguration;
import org.andfRa.mythr.dependencies.EffectDependancy;
import org.andfRa.mythr.dependencies.particles.ParticleEffect;
import org.andfRa.mythr.player.DerivedStats;
import org.andfRa.mythr.player.MythrPlayer;
import org.andfRa.mythr.player.SpellManager;
import org.andfRa.mythr.util.TargetUtil;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class BeamShapeEffect extends ResponseEffect {

	/** Key for beam radius. */
	public static String BEAM_RADIUS_KEY = "BEAM_RADIUS";

	/** Key for beam length. */
	public static String BEAM_LENGTH_KEY = "BEAM_LENGTH";

	/** Key for applied response. */
	public static String APPLIED_RESPONSE_KEY = "APPLIED_RESPONSE";

	/** Key for penetrate. */
	public static String PENETRATES_KEY = "PENETRATES";
	
	/** Key for sound name. */
	public static String SOUND_KEY = "SOUND";

	/** Key for particles name. (Optional) */
	public static String PARTICLES_KEY = "PARTICLES";
	
	
	
	@Override
	public String key()
	 { return "BEAM_SHAPE_EFFECT"; }

	@Override
	public boolean interactTrigger(Response response, MythrPlayer mplayer, DerivedStats dsstats)
	 {
		Location loc = mplayer.getPlayer().getLocation();
		Sound sound = matchSound(response.getString(SOUND_KEY));
		if(sound != null) loc.getWorld().playSound(loc, sound, 0.5f, 0.5f);
		
		SpellManager.startCast(mplayer, response.getName(), response.getDouble(CAST_TICKS_KEY));
		
		return true;
	 }
	
	@Override
	public boolean castTrigger(Response response, MythrPlayer mplayer, DerivedStats dsattacker)
	 {
		Player player = mplayer.getPlayer();
		
		// Applied response:
		String respName = response.getString(APPLIED_RESPONSE_KEY);
		Response applied = ResponseConfiguration.getResponse(respName);
		if(applied == null){
			MythrLogger.warning(getClass(), "Failed to retrieve applied response " + respName + ".");
			return false;
		}
		
		// Determine beam paramaters:
		double R = response.getDouble(BEAM_RADIUS_KEY);
		double l = response.getDouble(BEAM_LENGTH_KEY);

		Block target = TargetUtil.getTargetBlock(player, (int)l);
		if(target != null){
			double d2 = target.getLocation().distanceSquared(player.getLocation());
			if(d2 < l*l) l = Math.sqrt(d2) + 0.75;
		}
		
		// Find all caught in the beam:
		ArrayList<Entity> caught = TargetUtil.findBeamCollisions(player, R, l);
		
		// Apply to all:
		if(response.getBoolean(PENETRATES_KEY)){

			DerivedStats dsdefender = null;
			
			for (Entity entity : caught) {
				if(!(entity instanceof LivingEntity)) continue;
				dsdefender = DerivedStats.findDerived((LivingEntity)entity);
				applied.attackTrigger(player, (LivingEntity)entity, dsattacker, dsdefender);
			}
		}
		
		// Apply to closest:
		else{
			double distance2 = -1;
			
			DerivedStats dsclosest = null;
			LivingEntity closest = null;
			double distclosest2 = Double.MAX_VALUE;
			
			for (Entity entity : caught) {
				if(!(entity instanceof LivingEntity)) continue;
				distance2 = entity.getLocation().distanceSquared(player.getLocation());
				if(distance2 < distclosest2){
					closest = (LivingEntity) entity;
					distclosest2 = distance2;
					dsclosest = DerivedStats.findDerived((LivingEntity)entity);
				}
			}
			
			if(closest != null) applied.attackTrigger(player, closest, dsattacker, dsclosest);
		}
		
		// Sound effect:
		Location loc = mplayer.getPlayer().getLocation();
		Sound sound = matchSound(response.getString(SOUND_KEY));
		if(sound != null) loc.getWorld().playSound(loc, sound, 1.0f, 1.0f);
		
		// Particle effect:
		if(response.hasParameter(PARTICLES_KEY)){
			ParticleEffect particles = matchParticles(response.getString(PARTICLES_KEY));
			if(particles != null) EffectDependancy.playSpiralBeam(player.getEyeLocation(), R, l, particles);
		}
		
		return true;
	 }
	
	
	// UTILITY:
	/**
	 * Matches a sound.
	 * 
	 * @param name sound name
	 * @return matched sound, null if none
	 */
	public static Sound matchSound(String name)
	 {
		try {
			return Sound.valueOf(name.toUpperCase().replace(' ', '_'));
		}
		catch (IllegalArgumentException e) {
			MythrLogger.warning(BeamShapeEffect.class, "Failed to find sound effect for " + name + ".");
			return null;
		}
	 }
	
	/**
	 * Matches a particle effect.
	 * 
	 * @param name particle effect name
	 * @return matched particle effect
	 */
	public static ParticleEffect matchParticles(String name)
	 {
		try {
			return ParticleEffect.valueOf(name.toUpperCase().replace(' ', '_'));
		}
		catch (IllegalArgumentException e) {
			MythrLogger.warning(BeamShapeEffect.class, "Failed to find particle effect for " + name + ".");
			return null;
		}
	 }
	
}
