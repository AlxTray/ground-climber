package com.alx.groundclimber.screens;

import com.alx.groundclimber.GroundClimber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final GroundClimber game;

    OrthographicCamera camera;

    public MainMenuScreen(final GroundClimber game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.3f, 0.3f, 0.46f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Welcome to Ground Climber! ", 100, 150);
        game.font.draw(game.batch, "Press enter to begin! Or F1 to enable debug rendering, or F2 for only debug rendering", 100, 100);
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game, 0));
            dispose();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F1)) {
            game.setScreen(new GameScreen(game, 1));
            dispose();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F2)) {
            game.setScreen(new GameScreen(game, 2));
            dispose();
        }
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
