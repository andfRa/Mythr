package org.andfRa.mythr.items;

import java.util.ArrayList;
import java.util.HashMap;

import org.andfRa.mythr.util.RandomSelectionUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MythrDrop extends MythrItem {

	
	/** All possible additional responses. */
	private ArrayList<HashMap<String, Double>> randoms = null;
	

	// CONSTRUCTION:
	/**
	 * Creates a Mythr drop.
	 * 
	 * @param mat item material
	 */
	public MythrDrop(Material mat)
	 {
		super(mat);
	 }
	
	
	// VALUES:
	/**
	 * Generates a Mythr item from the drop.
	 * 
	 * @return Mythr item, {@link Material#AIR} item if none, possibility eliminated by complete
	 */
	public MythrItem generate()
	 {
		// Get base item:
		MythrItem mitem = super.clone();
		if(mitem == null) return MythrItem.fromBukkitItem(new ItemStack(Material.AIR)); // Should not do this.
		
		// Add responses:
		if(randoms != null)
		 for (HashMap<String, Double> responses : randoms) {
			String response = RandomSelectionUtil.selectRandom(responses);
			if(response == null || response.length() == 0) continue;
			mitem.addResponse(response);
		 }
		
		return mitem;
	 }
	
}
