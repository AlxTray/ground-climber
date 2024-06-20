package com.alx.groundclimber.screens;

import com.alx.groundclimber.enums.DebugRenderMode;
import com.alx.groundclimber.enums.GameMode;
import com.alx.groundclimber.GroundClimber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final GroundClimber game;

    SpriteBatch batch;
    BitmapFont font;
    DebugRenderMode debugMode = DebugRenderMode.NORMAL;
    OrthographicCamera camera;

    public MainMenuScreen(final GroundClimber game) {
        this.game = game;

        batch = new SpriteBatch();
        font = new BitmapFont();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.3f, 0.3f, 0.46f, 1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        font.draw(batch, "Welcome to Ground Climber! ", 100, 150);
        font.draw(batch, "Press Enter to begin, or Backspace for Endless mode!\n" +
                "Press F2 to enable debug rendering, or F3 for only debug rendering\n" +
                "F1 will reset the above options", 100, 100);
        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            Gdx.app.debug("MainMenu - DEBUG", "Changing to Level Select screen");
            game.setScreen(new LevelSelectScreen(game, debugMode));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) {
            Gdx.app.debug("MainMenu - DEBUG", "Changing to Game screen");
            game.setScreen(new GameScreen(game, GameMode.ENDLESS, debugMode));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F1)) {
            debugMode = DebugRenderMode.NORMAL;
            Gdx.app.log("MainMenu - INFO", "Updated render debug mode to: NORMAL");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F2)) {
            debugMode = DebugRenderMode.OVERLAY;
            Gdx.app.log("MainMenu - INFO", "Updated render debug mode to: OVERLAY");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F3)) {
            debugMode = DebugRenderMode.ONLY;
            Gdx.app.log("MainMenu - INFO", "Updated render debug mode to: ONLY");
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
        batch.dispose();
        font.dispose();
        Gdx.app.debug("MainMenu - DEBUG", "Disposed objects");
    }

}
