package org.andfRa.mythr.dependencies.particles;


/**
 * Used in particle effect sending. Thanks to microgeek.
 * http://forums.bukkit.org/threads/particle-packet-library.138493/
 * 
 * @author microgeek
 *
 */
public enum ParticleEffect {
 
    HUGE_EXPLOSION("hugeexplosion"),
    LARGE_EXPLODE("largeexplode"),
    FIREWORKS_SPARK("fireworksSpark"),
    BUBBLE("bubble"),
    SUSPEND("suspend"),
    DEPTH_SUSPEND("depthSuspend"),
    TOWN_AURA("townaura"),
    CRIT("crit"),
    MAGIC_CRIT("magicCrit"),
    MOB_SPELL("mobSpell"),
    MOB_SPELL_AMBIENT("mobSpellAmbient"),
    SPELL("spell"),
    INSTANT_SPELL("instantSpell"),
    WITCH_MAGIC("witchMagic"),
    NOTE("note"),
    PORTAL("portal"),
    ENCHANTMENT_TABLE("enchantmenttable"),
    EXPLODE("explode"),
    FLAME("flame"),
    LAVA("lava"),
    FOOTSTEP("footstep"),
    SPLASH("splash"),
    LARGE_SMOKE("largesmoke"),
    CLOUD("cloud"),
    RED_DUST("reddust"),
    SNOWBALL_POOF("snowballpoof"),
    DRIP_WATER("dripWater"),
    DRIP_LAVA("dripLava"),
    SNOW_SHOVEL("snowshovel"),
    SLIME("slime"),
    HEART("heart"),
    ANGRY_VILLAGER("angryVillager"),
    HAPPY_VILLAGER("happerVillager"),
    ICONCRACK("iconcrack_"),
    TILECRACK("tilecrack_");
   
    private String particleName;
   
    ParticleEffect(String particleName) {
        this.particleName = particleName;
    }
 
    /**
     * Gets the particle name.
     * 
     * @return particle name
     */
    public String getParticleName()
     { return particleName; }
   
}


