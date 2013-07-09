package org.andfRa.mythr.listeners;


import org.andfRa.mythr.Mythr;
import org.andfRa.mythr.config.VanillaConfiguration;
import org.andfRa.mythr.items.ItemType;
import org.andfRa.mythr.player.DerivedStats;
import org.andfRa.mythr.responses.Response;
import org.andfRa.mythr.util.MetadataUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
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

		// Check living:
		if(!(attacker instanceof LivingEntity)) return;
		if(!(defender instanceof LivingEntity)) return;
		LivingEntity lattacker = (LivingEntity) attacker;
		final LivingEntity ldefender = (LivingEntity) defender;
		
		// Check ticks:
		if(VanillaConfiguration.checkNoDamageTicks(ldefender)){
			event.setCancelled(true); // Just in case!
			return;
		}
		
		// Attack type:
		ItemStack item = lattacker.getEquipment().getItemInHand();
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
		DerivedStats dsdefender = null;
		if(projectile != null) dsattacker = MetadataUtil.retrieveDerivedStats(projectile);
		if(dsattacker == null) dsattacker = DerivedStats.findDerived(lattacker);
		dsdefender = DerivedStats.findDerived(ldefender);
		
		// Reaction:
		if(reaction != null) reaction.attackTrigger(lattacker, ldefender, dsattacker, dsdefender);

		// Prepare:
		damage = dsdefender.defend(type, dsattacker);
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
