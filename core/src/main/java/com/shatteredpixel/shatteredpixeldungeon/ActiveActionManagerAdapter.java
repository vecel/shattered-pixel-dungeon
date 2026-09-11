package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;

public class ActiveActionManagerAdapter implements ActiveActionManager {
    @Override
    public void set(ActionIndicator.Action action) {
        ActionIndicator.setAction(action);
    }

    @Override
    public void clear() {
        ActionIndicator.clearAction();
    }

    @Override
    public void clear(ActionIndicator.Action action) {
        ActionIndicator.clearAction(action);
    }

    @Override
    public void refresh() {
        ActionIndicator.refresh();
    }
}
