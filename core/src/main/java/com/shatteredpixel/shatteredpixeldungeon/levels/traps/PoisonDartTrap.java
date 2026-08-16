/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.PoisonDart;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.modifiers.DamageModifier;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class PoisonDartTrap extends TargetTrap implements PhysicalDamageTrap {

	{
		color = GREEN;
		shape = CROSSHAIR;
		
		canBeHidden = false;
		avoidsHallways = true;
	}
	
	protected int poisonAmount(){
		return 8 + Math.round(2*scalingDepth() / 3f);
	}
	
	protected boolean canTarget( Char ch ){
		return true;
	}
	
	@Override
	public void activate() {
		activateWithModifier(new DamageModifier(0, 1));
	}

	@Override
	public int getDamage() {
		return Random.NormalIntRange(4, 8);
	}

	@Override
	public void activateWithModifier(DamageModifier modifier) {
		//we handle this inside of a separate actor as the trap may produce a visual effect we need to pause for
		Actor.add(new Actor() {

			{
				actPriority = VFX_PRIO;
			}

			@Override
			protected boolean act() {
				Actor.remove(this);
				Char target = findClosestCharacter();

				if (target != null) {
					if (target instanceof Mob){
						Buff.prolong(target, Trap.HazardAssistTracker.class, HazardAssistTracker.DURATION);
					}
					final Char finalTarget = target;
					if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[target.pos]) {
						((MissileSprite) ShatteredPixelDungeon.scene().recycle(MissileSprite.class)).
								reset(pos, finalTarget.sprite, new PoisonDart(), new Callback() {
									@Override
									public void call() {
										int dmg = getDamage() - finalTarget.drRoll();
										dmg = modifier.apply(dmg);
										finalTarget.damage(dmg, PoisonDartTrap.this);
										if (finalTarget == Dungeon.hero){
											//for the poison dart traps in the Tengu fight
											if (Dungeon.depth == 10) {
												Statistics.qualifiedForBossChallengeBadge = false;
												Statistics.bossScores[1] -= 100;
											}
											if (!finalTarget.isAlive()) {
												Dungeon.fail(PoisonDartTrap.this);
												GLog.n(Messages.get(PoisonDartTrap.class, "ondeath"));
												if (reclaimed) Badges.validateDeathFromFriendlyMagic();
											}
										}
										Buff.affect( finalTarget, Poison.class ).set( poisonAmount() );
										Sample.INSTANCE.play(Assets.Sounds.HIT, 1, 1, Random.Float(0.8f, 1.25f));
										finalTarget.sprite.bloodBurstA(finalTarget.sprite.center(), dmg);
										finalTarget.sprite.flash();
										next();
									}
								});
						return false;
					} else {
						int dmg = getDamage() - finalTarget.drRoll();
						dmg = modifier.apply(dmg);
						finalTarget.damage(dmg, PoisonDartTrap.this);
						Buff.affect( finalTarget, Poison.class ).set( poisonAmount() );
						return true;
					}
				} else {
					return true;
				}
			}

		});
	}
}
