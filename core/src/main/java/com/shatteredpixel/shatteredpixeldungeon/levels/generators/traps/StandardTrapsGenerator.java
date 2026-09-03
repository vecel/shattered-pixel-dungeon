package com.shatteredpixel.shatteredpixeldungeon.levels.generators.traps;

import com.karandys.todo.Todo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.CityLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.HallsLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.VaultLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TrapType;
import com.shatteredpixel.shatteredpixeldungeon.modifiers.Modifier;
import com.watabou.utils.Random;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class StandardTrapsGenerator implements TrapsGenerator {

    private static final Map<Class<? extends Level>, TrapsPool> pools = Map.of(
        SewerLevel.class, new TrapsPool(
            List.of(TrapType.WORN_DART, TrapType.CHILLING, TrapType.SHOCKING, TrapType.TOXIC, TrapType.ALARM, TrapType.OOZE, TrapType.CONFUSION, TrapType.FLOCK, TrapType.SUMMONING, TrapType.TELEPORTATION, TrapType.GATEWAY),
            List.of(4f, 4f, 4f, 4f, 2f, 2f, 1f, 1f, 1f, 1f, 1f)
        ),
        PrisonLevel.class, new TrapsPool(
                List.of(TrapType.CHILLING, TrapType.SHOCKING, TrapType.TOXIC, TrapType.BURNING, TrapType.POISON_DART, TrapType.ALARM, TrapType.OOZE, TrapType.GRIPPING, TrapType.CONFUSION, TrapType.FLOCK, TrapType.SUMMONING, TrapType.TELEPORTATION, TrapType.GATEWAY, TrapType.GEYSER),
                List.of(4f, 4f, 4f, 4f, 4f, 2f, 2f, 2f, 1f, 1f, 1f, 1f, 1f, 1f)
        ),
        CavesLevel.class, new TrapsPool(
                List.of(TrapType.BURNING, TrapType.POISON_DART, TrapType.FROST, TrapType.STORM, TrapType.CORROSION, TrapType.GRIPPING, TrapType.ROCKFALL, TrapType.GUARDIAN, TrapType.CONFUSION, TrapType.SUMMONING, TrapType.WARPING, TrapType.PITFALL, TrapType.GATEWAY, TrapType.GEYSER),
                List.of(4f, 4f, 4f, 4f, 4f, 2f, 2f, 2f, 1f, 1f, 1f, 1f, 1f, 1f)
        ),
        CityLevel.class, new TrapsPool(
                List.of(TrapType.FROST, TrapType.STORM, TrapType.CORROSION, TrapType.BLAZING, TrapType.DISINTEGRATION, TrapType.ROCKFALL, TrapType.FLASHING, TrapType.GUARDIAN, TrapType.WEAKENING, TrapType.DISARMING, TrapType.SUMMONING, TrapType.WARPING, TrapType.CURSING, TrapType.PITFALL, TrapType.DISTORTION, TrapType.GATEWAY, TrapType.GEYSER),
                List.of(4f, 4f, 4f, 4f, 4f, 2f, 2f, 2f, 2f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f)
        ),
        HallsLevel.class, new TrapsPool(
                List.of(TrapType.FROST, TrapType.STORM, TrapType.CORROSION, TrapType.BLAZING, TrapType.DISINTEGRATION, TrapType.ROCKFALL, TrapType.FLASHING, TrapType.GUARDIAN, TrapType.WEAKENING, TrapType.DISARMING, TrapType.SUMMONING, TrapType.WARPING, TrapType.CURSING, TrapType.GRIM, TrapType.PITFALL, TrapType.DISTORTION, TrapType.GATEWAY, TrapType.GEYSER),
                List.of(4f, 4f, 4f, 4f, 4f, 2f, 2f, 2f, 2f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f)
        )
    );

    @Override
    public int getCount(@NotNull Level level, @NotNull TrapsGenerationModifierProvider provider) {
        if (level instanceof SewerBossLevel) return 0;
        if (level instanceof VaultLevel) return 0;

        int count = Random.NormalIntRange(2, 3 + level.getDepth()/5);

        Modifier modifier = provider.getTrapsGenerationModifier();

        return (int) modifier.modify(count);
    }

    @Override
    public @NotNull TrapsPool getPool(@NotNull Level level) {
        return pools.getOrDefault(level.getClass(), TrapsPool.Empty);
    }
}
