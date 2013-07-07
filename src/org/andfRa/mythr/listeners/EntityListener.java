package org.andfRa.mythr.listeners;


import org.andfRa.mythr.Mythr;
import org.andfRa.mythr.config.VanillaConfiguration;
import org.andfRa.mythr.items.ItemType;
import org.andfRa.mythr.player.DerivedStats;
import org.andfRa.mythr.player.MythrPlayer;
import org.andfRa.mythr.responses.Response;
import org.andfRa.mythr.util.MetadataUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class EntityListener implements Listener {


	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	 {
		
		Entity attacker = event.getDamager();
		Entity defender = event.getEntity();
		Projectile projectile = null;

		// Projectile:
		if(attacker instanceof Projectile){
			projectile = (Projectile) attacker;
			attacker = projectile.getShooter();
		}else{
			projectile = null;
		}

		if(!(attacker instanceof LivingEntity)) return;
		if(!(defender instanceof LivingEntity)) return;
		
		// Check ticks:
		final LivingEntity ldefender = (LivingEntity) defender;
		if(VanillaConfiguration.checkNoDamageTicks(ldefender)){
			event.setCancelled(true); // Just in case!
			return;
		}
		
		MythrPlayer mattacker = null;
		MythrPlayer mdefender = null;
		Creature cattacker = null;
		Creature cdefender = null;
		
		if(attacker instanceof Creature) cattacker = (Creature) attacker;
		else if(attacker instanceof Player) mattacker = Mythr.plugin().getLoadedPlayer(((Player) attacker).getName());
		
		if(defender instanceof Creature) cdefender = (Creature) defender;
		else if(defender instanceof Player) mdefender = Mythr.plugin().getLoadedPlayer(((Player) defender).getName());
		
		// Attack type:
		ItemStack item = ((LivingEntity) attacker).getEquipment().getItemInHand();
		ItemType type;
		
		// Ranged:
		if(projectile != null){
			// Magic:
			if(projectile.getType() == EntityType.FIREBALL){
				type = ItemType.ARCANE_SPELL;
			}
			// Ranged:
			else {
				type = ItemType.RANGED_WEAPON;
			}
		}
		// Melee:
		else if(VanillaConfiguration.isSword(item)){
			type = ItemType.MELEE_WEAPON;
		}
		// Other:
		else{
			return;
		}
		
		double damage = event.getDamage();
		
		// Response reaction:
		Response reaction = null;
		if(projectile != null) reaction = MetadataUtil.retrieveResponseReaction(projectile);
		
		// Derived stats:
		DerivedStats dsattacker = null;
		if(projectile != null) dsattacker = MetadataUtil.retrieveDerivedStats(projectile);
		if(dsattacker == null){
			if(mattacker != null) dsattacker = mattacker.getDerived();
			else dsattacker = DerivedStats.DEFAULT_CREATURE_STATS;
		}
		
		// PvP:
		if(mattacker != null && mdefender != null){
			damage = mdefender.getDerived().defend(type, dsattacker);
			if(reaction != null) reaction.attackTrigger(mattacker, mdefender, dsattacker, mdefender.getDerived());
		}
		// PvC:
		else if(mattacker != null && cdefender != null){
			damage = DerivedStats.DEFAULT_CREATURE_STATS.defend(type, dsattacker);
			if(reaction != null) reaction.attackTrigger(mattacker, cdefender, dsattacker, DerivedStats.DEFAULT_CREATURE_STATS);
		}
		// CvP:
		else if(cattacker != null && mdefender != null){
			damage = mdefender.getDerived().defend(type, dsattacker);
			if(reaction != null) reaction.attackTrigger(cattacker, mdefender, dsattacker, mdefender.getDerived());
		}
		// CvC:
		else if(cattacker != null && cdefender !=  null){
			damage = DerivedStats.DEFAULT_CREATURE_STATS.defend(type, dsattacker);
			reaction.attackTrigger(cattacker, cdefender, dsattacker, DerivedStats.DEFAULT_CREATURE_STATS);
		}

		// Prepare:
		final double harm = damage;
		
		// Not on my watch:
		event.setDamage(0);
		
		// Queue damage:
		Bukkit.getServer().getScheduler().runTask(Mythr.plugin(), new Runnable() {
			@Override
			public void run() {
				ldefender.damage(harm);
			}
		});
		
	 }

	
}
