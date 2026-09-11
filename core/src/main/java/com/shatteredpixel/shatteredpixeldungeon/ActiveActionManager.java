package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;

/**
 * Provides interface for interactions with active action (a button typically
 * displayed above inventory like cleric's quick spell or monk energy).
 *
 * @see ActionIndicator
 */
public interface ActiveActionManager {
    void set(ActionIndicator.Action action);
    void clear();
    void clear(ActionIndicator.Action action);
    void refresh();
}
