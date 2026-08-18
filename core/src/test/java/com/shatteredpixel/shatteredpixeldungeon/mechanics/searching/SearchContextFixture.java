package com.shatteredpixel.shatteredpixeldungeon.mechanics.searching;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.shatteredpixel.shatteredpixeldungeon.DungeonInterface;
import com.shatteredpixel.shatteredpixeldungeon.audio.Audio;
import com.shatteredpixel.shatteredpixeldungeon.fakes.logger.GameLoggerFake;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameSceneInterface;
import com.watabou.noosa.Visual;

import java.util.List;

public class SearchContextFixture {
    public final DungeonInterface mockDungeon = mock(DungeonInterface.class);
    public final GameSceneInterface mockScene = mock(GameSceneInterface.class);
    public final Audio mockAudio = mock(Audio.class);
    public final GameLoggerFake fakeLogger = new GameLoggerFake();

    public SearchContextFixture() {
        when(mockDungeon.hasSecretAt(any(Integer.class))).thenReturn(true);

        doNothing().when(mockScene).effectOverFog(any(Visual.class));
        doNothing().when(mockScene).discover(any(Integer.class), any(Integer.class));
        doNothing().when(mockScene).updateFog(any(Integer.class), any(Integer.class));
        doNothing().when(mockDungeon).discoverCell(any(Integer.class));
    }
}
