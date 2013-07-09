package org.andfRa.mythr.responses;

import org.andfRa.mythr.dependencies.EffectDependancy;
import org.andfRa.mythr.player.DamageType;
import org.andfRa.mythr.player.DerivedStats;
import org.bukkit.entity.LivingEntity;

public class HealEffect extends ResponseEffect {

	/** Attribute used for checks key. */
	final public static String ATTRIBUTE_KEY = ResponseEffect.ATTRIBUTE_KEY;

	/** Attack score modifier key. */
	final public static String ATTACK_SCORE_MODIFIER_KEY = ResponseEffect.ATTACK_SCORE_MODIFIER_KEY;


	/** Damage type key. */
	final public static String DAMAGE_TYPE_KEY = "DAMAGE_TYPE";

	/** Bonus damage multiplier key. */
	final public static String BONUS_DAMAGE_MULTIPLIER_KEY = "BONUS_DAMAGE_MULTIPLIER";

	
	@Override
	public String key()
	 { return "HEAL_EFFECT"; }
	
	@Override
	public boolean attackTrigger(Response response, LivingEntity lcaster, LivingEntity ltarget, DerivedStats dsattacker, DerivedStats dsdefender)
	 {
		// Check for bonus:
		boolean bonus = findAttribScoreSuccess(response, dsattacker, dsdefender);
		
		// Type:
		DamageType type = DamageType.match(response.getString(DAMAGE_TYPE_KEY));
		if(type == null) return false;
		
		// Calculate health:
		double heal = dsdefender.defend(type, dsattacker);
		if(bonus) heal*= response.getDouble(BONUS_DAMAGE_MULTIPLIER_KEY);
		
		double health = ltarget.getHealth() + heal;
		if(health > ltarget.getMaxHealth()) health = ltarget.getHealth();
		
		// Apply heal:
		ltarget.setHealth(health);
		
		// Effect:
		EffectDependancy.playHeal(ltarget.getLocation());
		
		return true;
	 }

	
}
