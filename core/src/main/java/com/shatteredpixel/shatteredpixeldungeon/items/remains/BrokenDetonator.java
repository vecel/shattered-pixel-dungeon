package com.shatteredpixel.shatteredpixeldungeon.items.remains;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.audio.Audio;
import com.shatteredpixel.shatteredpixeldungeon.audio.AudioAdapter;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class BrokenDetonator extends RemainsItem {

    private final Audio audio;

    public BrokenDetonator() {
        image = ItemSpriteSheet.BROKEN_DETONATOR;

        this.audio = new AudioAdapter();
    }

    @Override
    protected void doEffect(Hero hero) {
        hero.belongings.charge(1f);
        hero.applyShielding(1);
        ScrollOfRecharging.charge(hero);
        audio.play(Assets.Sounds.CHARGEUP);
    }
}
