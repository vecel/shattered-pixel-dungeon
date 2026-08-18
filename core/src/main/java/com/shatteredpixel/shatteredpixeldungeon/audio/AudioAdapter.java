package com.shatteredpixel.shatteredpixeldungeon.audio;

import com.watabou.noosa.audio.Sample;

public class AudioAdapter implements Audio {

    @Override
    public void play(Object id) {
        Sample.INSTANCE.play(id);
    }
}
