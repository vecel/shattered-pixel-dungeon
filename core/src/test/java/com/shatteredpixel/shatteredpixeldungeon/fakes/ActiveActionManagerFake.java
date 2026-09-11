package com.shatteredpixel.shatteredpixeldungeon.fakes;

import com.shatteredpixel.shatteredpixeldungeon.ActiveActionManager;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;

public class ActiveActionManagerFake implements ActiveActionManager {

    private ActionIndicator.Action action;

    @Override
    public void set(ActionIndicator.Action action) {
        this.action = action;
    }

    @Override
    public void clear() {
        this.action = null;
    }

    @Override
    public void clear(ActionIndicator.Action action) {
        if (this.action == null) return;
        if (this.action.equals(action)) this.action = null;
    }

    @Override
    public void refresh() {}

    public ActionIndicator.Action getAction() {
        return action;
    }
}
