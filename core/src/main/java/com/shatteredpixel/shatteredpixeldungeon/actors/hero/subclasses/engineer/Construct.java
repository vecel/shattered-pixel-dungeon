package com.shatteredpixel.shatteredpixeldungeon.actors.hero.subclasses.engineer;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;

public abstract class Construct extends NPC {

    public Construct() {
        alignment = Alignment.ALLY;
        properties.add(Property.IMMOVABLE);
    }
}
