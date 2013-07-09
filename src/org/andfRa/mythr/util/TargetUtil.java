package org.andfRa.mythr.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class TargetUtil {

	/** All transparent blocks. */
	@SuppressWarnings("serial")
	final private static HashSet<Byte> TRANSPERENT_BLOCKS = new HashSet<Byte>(){
		{
			Material[] materials = Material.values();
			for (int i = 0; i < materials.length; i++) {
				if(materials[i].isTransparent()) add((byte) materials[i].getId());
			}
		}
	};

	
	/**
	 * Calculates the angle between the x-axis and the vector.
	 * 
	 * @param vector vector
	 * @return angle between x-axis and vector in radians
	 */
	public static double calcPhi(Vector vector)
	 {
		if(vector.getZ() > 0) return Math.atan2(vector.getZ(), vector.getX());
		else return 2*Math.PI + Math.atan2(vector.getZ(), vector.getX());
	 }

	/**
	 * Calculates the angle between the y-axis and the vector.
	 * 
	 * @param vector vector
	 * @return angle between y-axis and vector in radians
	 */
	public static double calcTheta(Vector vector)
	 {
		return Math.acos(vector.getY() / vector.length());
	 }

	/**
	 * Calculates the angle between two vectors.
	 * 
	 * @param v_1 first vector
	 * @param v_2 second vector
	 * @return angle between vectors in radians, 0 if undefined
	 */
	public static double calcAngle(Vector v_1, Vector v_2)
	 {
		double l_1 = v_1.length();
		double l_2 = v_2.length();
		if(l_1 == 0 || l_2 == 0) return 0.0;
		return Math.acos(v_1.dot(v_2) / (l_1 * l_2));
	 }
	
	
	/**
	 * Gets the target block.
	 * 
	 * @param lentity living entity
	 * @param maxDinstance maximum distance
	 * @return target block, null if none
	 */
	public static Block getTargetBlock(LivingEntity lentity, int maxDinstance)
	 {
		return lentity.getTargetBlock(TRANSPERENT_BLOCKS, maxDinstance); // TODO: Add transperent.
	 }
	
	/**
	 * Checks if the point is located in the beam.
	 * 
	 * @param direction beam direction, normalisation not required
	 * @param R beam radius
	 * @param p point to check
	 * @return true if the point is located in the beam
	 */
	public static boolean checkBeam(Vector direction, double R, Vector p)
	 {
		double angle = calcAngle(direction, p);
		if(angle > Math.PI / 2.0) return false;
		
		double phi = calcPhi(direction);
		double theta = calcTheta(direction);
		
		double alpha = Math.PI / 2.0 - theta;
		double beta = phi;
		
		double x = p.getX();
		double y = p.getY();
		double z = p.getZ();
		
		double A = (-x*Math.cos(-beta) +z*Math.sin(-beta))*Math.sin(alpha) + y*Math.cos(alpha);
		double B = x*Math.sin(-beta) + z*Math.cos(-beta);

		return A*A + B*B <= R*R;
	 }
	
	/**
	 * Checks if the point is located in the beam.
	 * Convenience method.
	 * 
	 * @param lshooter shooter location with direction
	 * @param R beam radius
	 * @param ltarget target location
	 * @return
	 */
	public static boolean checkBeam(Location lshooter, double R, Location ltarget)
	 {
		Vector p = ltarget.toVector().subtract(lshooter.toVector());
		return checkBeam(lshooter.getDirection(), R, p);
	 }
	
	/**
	 * Finds all entities caught in the beam.
	 * 
	 * @param lshooter living entity
	 * @param R beam radius
	 * @param l beam length
	 * @return all entities caught in the beam
	 */
	public static ArrayList<Entity> findBeamCollisions(LivingEntity lshooter, double R, double l)
	 {
		ArrayList<Entity> caught = new ArrayList<Entity>();

		List<Entity> nearby = lshooter.getNearbyEntities(l, l, l);
		for (Entity entity : nearby) {
			if(entity.getLocation().distanceSquared(lshooter.getLocation()) > l*l) continue;
			
			if(checkBeam(lshooter.getEyeLocation(), R, entity.getLocation())) caught.add(entity);
			else if(entity instanceof LivingEntity && checkBeam(lshooter.getEyeLocation(), R, ((LivingEntity) entity).getEyeLocation())) caught.add(entity);
		}
		
		return caught;
	 }
	
	
}
