package com.alx.groundclimber.screens;

import com.alx.groundclimber.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

    final GroundClimber game;
    Map map;
    MapRenderer mapRenderer;

    public GameScreen(GroundClimber game, int debugMode) {
        this.game = game;

        FileHandle fileHandle = Gdx.files.internal("levels/level1.json");
        Json json = new Json();
        map = json.fromJson(Map.class, fileHandle.readString());

        mapRenderer = new MapRenderer(map, debugMode);
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
