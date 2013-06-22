package org.andfRa.mythr.listeners;


import org.andfRa.mythr.Mythr;
import org.andfRa.mythr.config.VanillaConfiguration;
import org.andfRa.mythr.items.ItemType;
import org.andfRa.mythr.player.MythrPlayer;
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
	public void onPlayerJoin(EntityDamageByEntityEvent event)
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
		
		// Attack type:
		if(!(attacker instanceof LivingEntity)) return;
		ItemStack item = ((LivingEntity) attacker).getEquipment().getItemInHand();
		ItemType type;
		
		// Ranged:
		if(projectile != null){
			// Magic:
			if(projectile.getType() == EntityType.FIREBALL){
				type = ItemType.MAGIC_WEAPON;
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
		
		// PvP:
		if(mattacker != null && mdefender != null){
		}
		// PvC:
		else if(mattacker != null && cdefender != null){
		}
		// CvP:
		else if(cattacker != null && mdefender != null){
		}
		// CvC:
		// TODO: CvC
		
		
		
	 }

	
}
