package com.alx.groundclimber.screens;

import com.alx.groundclimber.*;
import com.alx.groundclimber.enums.DebugRenderMode;
import com.alx.groundclimber.enums.GameMode;
import com.alx.groundclimber.map.Map;
import com.alx.groundclimber.map.MapRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

    final GroundClimber game;
    Map map;
    MapRenderer mapRenderer;

    public GameScreen(GroundClimber game, GameMode gameMode, DebugRenderMode debugMode) {
        this.game = game;

        switch (gameMode) {
            case NORMAL:
                FileHandle fileHandle = Gdx.files.internal("levels/level1.json");
                Json json = new Json();
                map = json.fromJson(Map.class, fileHandle.readString());
                break;
            case ENDLESS:
                map = new Map();
                break;
        }
        map.setGameMode(gameMode);

        mapRenderer = new MapRenderer(map, gameMode, debugMode);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        map.update(delta);
        ScreenUtils.clear(0.3f, 0.3f, 0.46f, 1);
        mapRenderer.render();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
    }
}
