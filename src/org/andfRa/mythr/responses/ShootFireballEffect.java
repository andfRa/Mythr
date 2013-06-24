package org.andfRa.mythr.responses;

import org.andfRa.mythr.player.DerivedStats;
import org.andfRa.mythr.player.MythrPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ShootFireballEffect extends ResponseEffect {

	@Override
	public String key()
	 { return "SHOOT_FIREBALL_EFFECT"; }
	
	@Override
	public void interactTrigger(Response response, MythrPlayer mplayer, DerivedStats dsstats) {
		
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
	
	}
	
}
