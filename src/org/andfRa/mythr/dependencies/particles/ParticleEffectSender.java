package org.andfRa.mythr.dependencies.particles;

import net.minecraft.server.v1_6_R2.Packet;
import net.minecraft.server.v1_6_R2.Packet63WorldParticles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R2.CraftServer;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Used in particle effect sending. Thanks to microgeek.
 * http://forums.bukkit.org/threads/particle-packet-library.138493/
 * 
 * @author microgeek
 *
 */
public class ParticleEffectSender {

	/**
	 * Sends a particle effect to a player.
	 * 
	 * @param effect
	 * @param player
	 * @param location
	 * @param offsetX
	 * @param offsetY
	 * @param offsetZ
	 * @param speed
	 * @param count
	 * @throws Exception
	 */
	public static void sendToPlayer(ParticleEffect effect, Player player, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count) throws Exception
	 {
        Packet63WorldParticles packet = new Packet63WorldParticles();
        ReflectionUtilities.setValue(packet, "a", effect.getParticleName());
        ReflectionUtilities.setValue(packet, "b", (float) location.getX());
        ReflectionUtilities.setValue(packet, "c", (float) location.getY());
        ReflectionUtilities.setValue(packet, "d", (float) location.getZ());
        ReflectionUtilities.setValue(packet, "e", offsetX);
        ReflectionUtilities.setValue(packet, "f", offsetY);
        ReflectionUtilities.setValue(packet, "g", offsetZ);
        ReflectionUtilities.setValue(packet, "h", speed);
        ReflectionUtilities.setValue(packet, "i", count);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	  }
    
    /**
     * Sends a particle effect to all players.
     * 
     * @param effect
     * @param location
     * @param offsetX
     * @param offsetY
     * @param offsetZ
     * @param speed
     * @param count
     * @throws Exception
     */
    public static void send(ParticleEffect effect, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count) throws Exception
     {
        Packet63WorldParticles packet = new Packet63WorldParticles();
        ReflectionUtilities.setValue(packet, "a", effect.getParticleName());
        ReflectionUtilities.setValue(packet, "b", (float) location.getX());
        ReflectionUtilities.setValue(packet, "c", (float) location.getY());
        ReflectionUtilities.setValue(packet, "d", (float) location.getZ());
        ReflectionUtilities.setValue(packet, "e", offsetX);
        ReflectionUtilities.setValue(packet, "f", offsetY);
        ReflectionUtilities.setValue(packet, "g", offsetZ);
        ReflectionUtilities.setValue(packet, "h", speed);
        ReflectionUtilities.setValue(packet, "i", count);
        sendPacket(packet);
	 }

	/**
	 * Sends a packet.
	 * 
	 * @param packet
	 * @param loc
	 */
	public static void sendPacket(Packet packet)
	 {
		((CraftServer)Bukkit.getServer()).getServer().getPlayerList().sendAll((packet));
	 }

}
