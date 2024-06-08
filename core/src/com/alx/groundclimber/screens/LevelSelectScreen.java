package com.alx.groundclimber.screens;

import com.alx.groundclimber.GroundClimber;
import com.alx.groundclimber.enums.DebugRenderMode;
import com.alx.groundclimber.enums.GameMode;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class LevelSelectScreen implements Screen {

    final GroundClimber game;
    final DebugRenderMode debugMode;

    OrthographicCamera camera;
    FileHandle[] levelFiles;

    public LevelSelectScreen(final GroundClimber game, final DebugRenderMode debugMode) {
        this.game = game;
        this.debugMode = debugMode;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        levelFiles = Gdx.files.internal("levels").list();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.3f, 0.3f, 0.46f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Select from the following levels, using number keys.", 100, 150);
        for (FileHandle levelFile : levelFiles) {
            game.font.draw(game.batch, levelFile.name(), 100, 110);
        }
        game.batch.end();
    }

    @Override
    public void show() {
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
    }

}
