package com.shatteredpixel.shatteredpixeldungeon.fixtures;

import static org.mockito.Mockito.mockStatic;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.watabou.noosa.Game;

import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.List;

public class EngineFixture {

    private MockedStatic<Game> mockedGame;

    public void setUp() {
        Actor.clear();

        mockedGame = mockStatic(Game.class);
        mockedGame.when(Game::switchingScene).thenReturn(false);
    }

    public void tearDown() {
        Actor.clear();

        if (mockedGame != null) mockedGame.close();
    }

    public void add(Actor actor) {
        Actor.add(actor);
    }

    public void processNext() {
        Actor.resolveCurrent();
        Actor.processCurrent();
    }

    public List<Actor> getActors() {
        return new ArrayList<>(Actor.all());
    }

    public Actor getActorById(int id) {
        return Actor.findById(id);
    }

    public int getActorCount() {
        return Actor.all().size();
    }

    public boolean hasActor(Actor actor) {
        return Actor.all().contains(actor);
    }
}
