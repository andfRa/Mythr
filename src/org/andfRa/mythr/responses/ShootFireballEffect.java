package org.andfRa.mythr.responses;

import org.andfRa.mythr.player.DerivedStats;
import org.andfRa.mythr.player.MythrPlayer;
import org.andfRa.mythr.player.SpellManager;
import org.andfRa.mythr.util.MetadataUtil;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ShootFireballEffect extends ResponseEffect {

	/** Key for burn duration ticks key. */
	public static String BURN_DURATION_TICKS_KEY = "BURN_TICKS";
	
	@Override
	public String key()
	 { return "SHOOT_FIREBALL_EFFECT"; }

	@Override
	public boolean interactTrigger(Response response, MythrPlayer mplayer, DerivedStats dsstats)
	 {
		Location loc = mplayer.getPlayer().getLocation();
		loc.getWorld().playSound(loc, Sound.GHAST_FIREBALL, 0.5f, 0.5f);
		
		SpellManager.startCast(mplayer, response.getName(), response.getDouble(CAST_TICKS_KEY));
		
		return true;
	 }
	
	@Override
	public boolean castTrigger(Response response, MythrPlayer mplayer, DerivedStats dsstats)
	 {
		Player player = mplayer.getPlayer();
		double speed = 2.0;
		
		// Offset:
		Vector offset = player.getEyeLocation().getDirection().normalize();
		Location shootLocation = player.getEyeLocation().add(offset);
		
		// Direction vector:
		Vector velocity = offset.clone().multiply(speed);
		
		// Create the fireball:
		Fireball fireball = shootLocation.getWorld().spawn(shootLocation, Fireball.class);
		fireball.setVelocity(velocity);
		
		// Set shooter:
		fireball.setShooter(player);
		
		// Remove fire:
		fireball.setIsIncendiary(false);
		
		// Metadata:
		MetadataUtil.attachResponseReaction(fireball, response);
		MetadataUtil.attachDerivedStats(fireball, mplayer.getDerived());
		
		// Effect:
		Location loc = mplayer.getPlayer().getLocation();
		loc.getWorld().playSound(loc, Sound.GHAST_FIREBALL, 1.0f, 1.0f);
		
		return true;
	 }
	
	@Override
	public boolean attackTrigger(Response response, MythrPlayer mattacker, MythrPlayer mdefender, DerivedStats dsattacker, DerivedStats dsdefender) {
		return handleIgnite(response, mattacker.getPlayer(), mdefender.getPlayer(), dsattacker, dsdefender);
	}
	
	@Override
	public boolean attackTrigger(Response response, MythrPlayer mattacker, Creature cdefender, DerivedStats dsattacker, DerivedStats dsdefender) {
		return handleIgnite(response, mattacker.getPlayer(), cdefender, dsattacker, dsdefender);
	}
	
	@Override
	public boolean attackTrigger(Response response, Creature cattacker, MythrPlayer mdefender, DerivedStats dsattacker, DerivedStats dsdefender) {
		return handleIgnite(response, cattacker, mdefender.getPlayer(), dsattacker, dsdefender);
	}
	
	@Override
	public boolean attackTrigger(Response response, Creature cattacker, Creature cdefender, DerivedStats dsattacker, DerivedStats dsdefender) {
		return handleIgnite(response, cdefender, cdefender, dsattacker, dsdefender);
	}
	
	/**
	 * Handles ignition from the fireball.
	 * 
	 * @param response response
	 * @param lattacker attacker living entity
	 * @param ldefender defender living entity
	 * @param dsattacker attackers derived stats
	 * @param dsdefender defenders derived stats
	 * @return true if successful
	 */
	private boolean handleIgnite(Response response, LivingEntity lattacker, LivingEntity ldefender, DerivedStats dsattacker, DerivedStats dsdefender)
	 {
		// Check if successful:
		boolean ignite = findAttribScoreSuccess(response, dsattacker, dsdefender);
		if(!ignite) return false;
		
		// Ignite:
		int ticks = response.getInt(BURN_DURATION_TICKS_KEY);
		if(ticks == 0) return false;
		ldefender.setFireTicks(ldefender.getFireTicks() + ticks);
		
		// Effect:
		Location loc = ldefender.getLocation();
		loc.getWorld().playSound(loc, Sound.FIRE_IGNITE, 1.0f, 1.0f);

		return true;
	 }
	
}
