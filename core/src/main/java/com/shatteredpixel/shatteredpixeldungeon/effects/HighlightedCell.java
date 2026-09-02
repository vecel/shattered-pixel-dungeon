package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.scenes.GameSceneInterface;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;

public class HighlightedCell extends Image {

    private float time = 0f;
    public HighlightedCell(int position) {
        super(TextureCache.createSolid(0x22FFFFFF));

        origin.set(0.5f);
        scale.set(DungeonTilemap.SIZE);

        point(DungeonTilemap.tileToWorld(position).offset(
            (float) DungeonTilemap.SIZE / 2,
            (float) DungeonTilemap.SIZE / 2)
        );

        alpha(0.9f);
    }

    @Override
    public void update() {
        time += Game.elapsed;

        float alpha = 0.9f + (float) Math.sin(time * 4) * 0.1f;
        alpha(alpha);
    }
}
