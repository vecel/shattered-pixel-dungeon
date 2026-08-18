package com.shatteredpixel.shatteredpixeldungeon.audio;

public interface Audio {

    // This should contain Sound object, but I use Object for backward compatibility
    void play(Object id);
}
