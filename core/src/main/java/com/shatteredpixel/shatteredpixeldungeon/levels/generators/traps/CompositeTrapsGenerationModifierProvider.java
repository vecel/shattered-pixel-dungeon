package com.shatteredpixel.shatteredpixeldungeon.levels.generators.traps;

import com.shatteredpixel.shatteredpixeldungeon.modifiers.CompositeModifier;
import com.shatteredpixel.shatteredpixeldungeon.modifiers.Modifier;

import java.util.Arrays;
import java.util.List;

public class CompositeTrapsGenerationModifierProvider implements TrapsGenerationModifierProvider {

    private final List<TrapsGenerationModifierProvider> providers;

    public CompositeTrapsGenerationModifierProvider(TrapsGenerationModifierProvider... providers) {
        this.providers = Arrays.asList(providers);
    }

    @Override
    public Modifier getTrapsGenerationModifier() {
        return new CompositeModifier(providers.stream().map(TrapsGenerationModifierProvider::getTrapsGenerationModifier).toArray(Modifier[]::new));
    }
}
