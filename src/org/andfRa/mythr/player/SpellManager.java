package org.andfRa.mythr.player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.andfRa.mythr.Mythr;
import org.andfRa.mythr.config.ResponseConfiguration;
import org.andfRa.mythr.dependencies.EffectDependancy;
import org.andfRa.mythr.responses.Response;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SpellManager implements Runnable {

	/** Manager instance. */
	private static SpellManager manager;
	
	/** Ticks between cycles. */
	private static long CYCLE_STEP = 3;
	
	
	/** Response name. */
	private HashMap<String, String> responses;

	/** Spell progress. */
	private HashMap<String, Double> progresses;

	/** Spell progress steps. */
	private HashMap<String, Double> steps;

	/** Cast locations. */
	private HashMap<String, Location> locations;
	
	
	/** True if the clock is running. */
	transient private boolean tick;
	
	
	// CONSTRUCTION:
	/**
	 * Creates all fields.
	 * 
	 */
	private SpellManager()
	 {
		responses = new HashMap<String, String>();
		progresses = new HashMap<String, Double>();
		steps = new HashMap<String, Double>();
		locations = new HashMap<String, Location>();
		tick = false;
	 }
	
	
	// OPERATION:
	/* 
	 * Progresses.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	 {
		Iterator<Entry<String, String>> respit = responses.entrySet().iterator();
		 while (respit.hasNext()) {
			Entry<String, String> pairs = respit.next();
			
			String plyrName = pairs.getKey();
			double progress = progresses.get(plyrName);
			playSpell(plyrName, progress);
			progress+= steps.get(plyrName);
			
			// Trigger:
			if(progress >= 1.0){
				String respName = responses.get(plyrName);
				triggerSpell(plyrName, respName);
				respit.remove();
				progresses.remove(plyrName);
				steps.remove(plyrName);
				locations.remove(plyrName);
			}
			// Progress:
			else{
				progresses.put(plyrName, progress);
			}
			
		}
		
		// Start next tick:
		if(!responses.isEmpty()){
			Bukkit.getScheduler().scheduleSyncDelayedTask(Mythr.plugin(), this, CYCLE_STEP);
			tick = true;
		}else{
			tick = false;
		}
		
	 }
	
	/**
	 * Starts the next cycle, if not started.
	 * 
	 */
	private static void start()
	 {
		if(manager.tick) return;
		Bukkit.getScheduler().scheduleSyncDelayedTask(Mythr.plugin(), manager, CYCLE_STEP);
		manager.tick = true;
	 }
	
	/**
	 * Stars a spell casting.
	 * 
	 * @param mplayer Mythr player
	 * @param respName response name
	 * @param ticks cast duration in ticks
	 */
	public static void startCast(MythrPlayer mplayer, String respName, double ticks)
	 {
		if(manager == null) manager = new SpellManager();
		
		String plyrName = mplayer.getName();
		manager.responses.put(plyrName, respName);
		manager.progresses.put(plyrName, 0.0);
		manager.steps.put(plyrName, CYCLE_STEP / ticks);
		manager.locations.put(plyrName, mplayer.getPlayer().getLocation().clone());
		
		start();
	 }
	
	/**
	 * Plays spell cast effect.
	 * 
	 * @param name player name
	 * @param progress progress
	 */
	private void playSpell(String name, double progress)
	 {
		Player player = Bukkit.getServer().getPlayer(name);
		if(player == null) return;
		
		EffectDependancy.playArcane(player.getLocation(), progress);
	 }
	
	/**
	 * Triggers the spell.
	 * 
	 * @param plyrName player name
	 * @param respName response name
	 */
	private void triggerSpell(String plyrName, String respName)
	 {
		MythrPlayer mplayer = Mythr.plugin().getLoadedPlayer(plyrName);
		Response response = ResponseConfiguration.getResponse(respName);
		
		if(mplayer == null || response == null) return;
		
		response.castTrigger(mplayer, mplayer.getDerived());
	 }
	
	
	// INTERRUPTION:
	/**
	 * Handles player moving.
	 * 
	 * @param player player
	 * @param newLoc new location
	 */
	public static void handleMoving(Player player, Location newLoc)
	 {
		if(manager.responses.isEmpty()) return;
		
		String plyrName = player.getName();
		Location oldLoc = manager.locations.get(plyrName);
		if(oldLoc == null) return;
		
		double radius2 = ResponseConfiguration.getCastRadius()*ResponseConfiguration.getCastRadius();
		
		// Stop casting:
		if(newLoc.distanceSquared(oldLoc) > radius2){
			interrupt(plyrName);
			EffectDependancy.playFail(player);
		}
	 }
	
	/**
	 * Interrupts casting.
	 * 
	 * @param plyrName player name
	 */
	public static void interrupt(String plyrName)
	 {
		manager.responses.remove(plyrName);
		manager.progresses.remove(plyrName);
		manager.steps.remove(plyrName);
		manager.locations.remove(plyrName);
	 }
	
	
	// LOAD UNLOAD:
	/** Loads the configuration. */
	public static void load()
	 {
		manager = new SpellManager();
	 }
	
	/** Unloads the configuration. */
	public static void unload()
	 {
		manager = null;
	 }
	
}
