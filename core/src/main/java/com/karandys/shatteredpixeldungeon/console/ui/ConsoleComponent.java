package com.karandys.shatteredpixeldungeon.console.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.karandys.shatteredpixeldungeon.console.ConsoleAware;
import com.karandys.shatteredpixeldungeon.console.GameConsole;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.gltextures.TextureCache;
import com.watabou.input.KeyBindings;
import com.watabou.input.KeyEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.TextInput;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Signal;

public class ConsoleComponent extends Component implements ConsoleAware {
    private GameConsole console;
    private Signal.Listener<KeyEvent> listener;
    private TextInput input;

    public ConsoleComponent(Camera camera) {
        this.camera = camera;
        this.x = 0;
        this.y = 0;
        this.width = camera.width / 2f;
        this.height = 100f;
    }

    @Override
    protected void createChildren() {
        this.listener = event -> {
            if (event.pressed && KeyBindings.getActionForKey(event) == SPDAction.CONSOLE) {
                toggle();
                return true;
            }
            return false;
        };

        KeyEvent.addKeyListener(listener);
    }

    @Override
    public synchronized void destroy() {
        super.destroy();
        KeyEvent.removeKeyListener(listener);
    }

    @Override
    protected void layout() {
        if (input != null) {
            input.setRect(0, 0, width, 12);
        }
    }

    private void toggle() {
        if (input == null) {
            show();
        } else {
            hide();
        }
    }

    private void show() {
        NinePatch inputBackground = new NinePatch(TextureCache.createSolid(0x66444444), 1);
        input = new TextInput(inputBackground, false, 12) {
            @Override
            public void enterPressed() {
                if (console != null) {
                    String command = getText();
                    console.process(command);
                }
                hide();
            }
        }
            .withFontColor(Color.GREEN)
            .withTextAlignement(Align.left);

        add(input);
        layout();
    }

    private void hide() {
        if (input != null) {
            erase(input);
            input.destroy();
            input = null;
        }
    }

    @Override
    public void setConsole(GameConsole console) {
        this.console = console;
    }
}
