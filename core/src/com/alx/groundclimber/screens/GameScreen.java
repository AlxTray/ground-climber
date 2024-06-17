package com.alx.groundclimber.screens;

import com.alx.groundclimber.*;
import com.alx.groundclimber.enums.DebugRenderMode;
import com.alx.groundclimber.enums.GameMode;
import com.alx.groundclimber.map.Map;
import com.alx.groundclimber.map.MapRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Arrays;

public class GameScreen implements Screen {

    final GroundClimber game;

    Array<FileHandle> selectedLevelFiles = new Array<>();
    Map map;
    MapRenderer mapRenderer;

    public GameScreen(GroundClimber game, GameMode gameMode, DebugRenderMode debugMode, String... selectedLevelNames) {
        this.game = game;

        Gdx.app.log(
                "Game Screen INFO",
                String.format("The current game mode is: %s", gameMode.name())
        );
        switch (gameMode) {
            case NORMAL:
                for (String levelName : selectedLevelNames) {
                    selectedLevelFiles.add(Gdx.files.internal("levels/" + levelName));
                }

                Json json = new Json();
                map = json.fromJson(Map.class, selectedLevelFiles.first().readString());
                Gdx.app.log("Game Screen INFO", "Created new map for selected level data");
                break;
            case ENDLESS:
                map = new Map();
                Gdx.app.log("Game Screen INFO", "Created new empty map for ENDLESS mode");
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
        mapRenderer.dispose();
        Gdx.app.debug("Game Screen DEBUG", "Disposed objects");
    }

}
