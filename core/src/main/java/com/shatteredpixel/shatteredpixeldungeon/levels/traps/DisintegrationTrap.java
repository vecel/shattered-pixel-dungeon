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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.modifiers.Modifier;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class DisintegrationTrap extends TargetTrap implements PhysicalDamageTrap {

	{
		color = VIOLET;
		shape = CROSSHAIR;
		
		canBeHidden = false;
		avoidsHallways = true;
	}

	@Override
	public void activate() {
		activateWithModifier(Modifier.None);
	}

	@Override
	public int getDamage() {
		return Random.NormalIntRange(30, 50) + scalingDepth();
	}

	@Override
	public void activateWithModifier(Modifier modifier) {
		Char target = findClosestCharacter();

		Heap heap = Dungeon.level.heaps.get(pos);
		if (heap != null) heap.explode();

		if (target != null) {
			if (target instanceof Mob){
				Buff.prolong(target, Trap.HazardAssistTracker.class, HazardAssistTracker.DURATION);
			}
			if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[target.pos]) {
				Sample.INSTANCE.play(Assets.Sounds.RAY);
				ShatteredPixelDungeon.scene().add(new Beam.DeathRay(DungeonTilemap.tileCenterToWorld(pos), target.sprite.center()));
				Sample.INSTANCE.play( Assets.Sounds.RAY );
			}
			int dmg = getModifiedDamage(modifier);
			target.damage(dmg, this );
			if (target == Dungeon.hero){
				Hero hero = (Hero)target;
				if (!hero.isAlive()){
					Badges.validateDeathFromGrimOrDisintTrap();
					Dungeon.fail( this );
					GLog.n( Messages.get(this, "ondeath") );
					if (reclaimed) Badges.validateDeathFromFriendlyMagic();
				}
			}
		}
	}
}
