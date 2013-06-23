package org.andfRa.mythr.listeners;


import org.andfRa.mythr.Mythr;
import org.andfRa.mythr.config.VanillaConfiguration;
import org.andfRa.mythr.items.ItemType;
import org.andfRa.mythr.player.DerivedStats;
import org.andfRa.mythr.player.MythrPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Damageable;
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
		
		MythrPlayer mattacker = null;
		MythrPlayer mdefender = null;
		Creature cattacker = null;
		Creature cdefender = null;
		
		if(attacker instanceof Creature) cattacker = (Creature) attacker;
		else if(attacker instanceof Player) mattacker = Mythr.plugin().getLoadedPlayer(((Player) attacker).getName());
		
		if(defender instanceof Creature) cdefender = (Creature) defender;
		else if(defender instanceof Player) mdefender = Mythr.plugin().getLoadedPlayer(((Player) defender).getName());
		
		if(!(attacker instanceof LivingEntity)) return;
		if(!(defender instanceof LivingEntity)) return;
		
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
		
		int damage = event.getDamage();
		
		// PvP:
		if(mattacker != null && mdefender != null){
			damage = mdefender.getDerived().defend(type, mattacker.getDerived());
		}
		// PvC:
		else if(mattacker != null && cdefender != null){
			damage = DerivedStats.DEFAULT_CREATURE_STATS.defend(type, mattacker.getDerived());
		}
		// CvP:
		else if(cattacker != null && mdefender != null){
			damage = mdefender.getDerived().defend(type, DerivedStats.DEFAULT_CREATURE_STATS);
		}
		// CvC:
		else if(cattacker != null && cdefender !=  null){
			damage = DerivedStats.DEFAULT_CREATURE_STATS.defend(type, DerivedStats.DEFAULT_CREATURE_STATS);
		}

		// Prepare:
		final LivingEntity ldefender = (LivingEntity) defender;
		final LivingEntity lattacker = (LivingEntity) attacker;
		final int harm = damage;
		
		// Not on my watch:
		if(event.getDamage() > ldefender.getHealth()) event.setDamage(ldefender.getHealth() - 1);
		
		// Queue damage:
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Mythr.plugin(), new Runnable() {
			@Override
			public void run() {
				ldefender.damage(harm);
			}
		}, 1);
		
	 }

	
}
