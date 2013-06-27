package org.andfRa.mythr.responses;

import org.andfRa.mythr.player.DerivedStats;
import org.andfRa.mythr.player.MythrPlayer;
import org.andfRa.mythr.player.SpellManager;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ShootFireballEffect extends ResponseEffect {

	@Override
	public String key()
	 { return "SHOOT_FIREBALL_EFFECT"; }
	
	@Override
	public void castTrigger(Response response, MythrPlayer mplayer, DerivedStats dsstats)
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
		
		// Effect:
		Location loc = mplayer.getPlayer().getLocation();
		loc.getWorld().playSound(loc, Sound.GHAST_FIREBALL, 1.0f, 1.0f);
	 }
	
	@Override
	public void interactTrigger(Response response, MythrPlayer mplayer, DerivedStats dsstats)
	 {
		Location loc = mplayer.getPlayer().getLocation();
		loc.getWorld().playSound(loc, Sound.GHAST_FIREBALL, 0.5f, 0.5f);
		
		SpellManager.startCast(mplayer, response.getName(), response.getDouble(CAST_TICKS_KEY));
	 }
	
}
