package com.shatteredpixel.shatteredpixeldungeon.actors.hero.subclasses;

import com.shatteredpixel.shatteredpixeldungeon.ActiveActionManager;
import com.shatteredpixel.shatteredpixeldungeon.ActiveActionManagerAdapter;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.subclasses.engineer.EngineerSubclassInitializer;

import java.util.EnumMap;
import java.util.Map;

public class SubclassesRegistry {
    private final Map<HeroSubClass, SubclassInitializer> registry = new EnumMap<>(HeroSubClass.class);
    private final ActiveActionManager activeActionManager;

    SubclassesRegistry(ActiveActionManager activeActionManager) {
        this.activeActionManager = activeActionManager;

        registry.put(HeroSubClass.ENGINEER, new EngineerSubclassInitializer(activeActionManager));
    }

    public SubclassesRegistry() {
        activeActionManager = new ActiveActionManagerAdapter();

        registry.put(HeroSubClass.ENGINEER, new EngineerSubclassInitializer(activeActionManager));
    }

    public SubclassInitializer get(HeroSubClass id) {
        return registry.getOrDefault(id, hero -> {});
    }
}
