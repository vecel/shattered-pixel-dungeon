package com.karandys.shatteredpixeldungeon.hero.subclasses.engineer.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;

public class CrossbowConstructSprite extends MobSprite {
    public CrossbowConstructSprite() {
        super();
        texture(Assets.Sprites.CONSTRUCTS);
        TextureFilm frames = new TextureFilm(texture, 13, 16);

        idle = new Animation(10, true);
        idle.frames(frames, 0);
    }
}
