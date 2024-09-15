package io.github.alxtray.groundclimber.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.alxtray.groundclimber.Core;
import io.github.alxtray.groundclimber.enums.DebugRenderMode;
import io.github.alxtray.groundclimber.enums.GameMode;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.utilities.AssetLibrary;
import io.github.alxtray.groundclimber.utilities.ButtonBuilder;
import io.github.alxtray.groundclimber.utilities.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    private final Core game;

    private final SpriteBatch batch;
    private final Stage stage;
    private final OrthographicCamera camera;
    private final Texture backgroundImage;
    private DebugRenderMode debugMode = DebugRenderMode.NORMAL;

    public MainMenuScreen(final Core game) {
        this.game = game;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        backgroundImage = AssetLibrary.getInstance().getAsset("title_background", Texture.class);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Skin skin = AssetLibrary.getInstance().getAsset("skin", Skin.class);
        float viewportTop = camera.position.y + camera.viewportHeight / 2;
        float mainButtonHeight = (float) Gdx.graphics.getHeight() / 5;
        float mainButtonWidth = (float) Gdx.graphics.getWidth() / 5;
        float levelsButtonTopMargin = Gdx.graphics.getHeight() / 1.5f;
        float endlessButtonTopMargin = (levelsButtonTopMargin + mainButtonHeight) + (float) Gdx.graphics.getHeight() / 40;
        new ButtonBuilder("Levels", skin, stage)
            .setSize(mainButtonWidth, mainButtonHeight)
            .setPosition(camera.position.x - (mainButtonWidth / 2), viewportTop - levelsButtonTopMargin)
            .setClickListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new LevelSelectScreen(
                        game,
                        debugMode));
                }
            })
            .build();
        new ButtonBuilder("Endless", skin, stage)
            .setSize(mainButtonWidth, mainButtonHeight)
            .setPosition(camera.position.x - (mainButtonWidth / 2), viewportTop - endlessButtonTopMargin)
            .setClickListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new GameScreen(
                        GameMode.ENDLESS,
                        debugMode));
                }
            })
            .build();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.3f, 0.3f, 0.46f, 1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(
            backgroundImage,
            camera.position.x - (camera.viewportWidth / 2),
            camera.position.y - (camera.viewportHeight / 2),
            camera.viewportWidth,
            camera.viewportHeight);
        batch.end();

        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            Logger.log(
                    "MainScreen",
                    "Changing to GameScreen",
                    LogLevel.DEBUG);
            game.setScreen(new GameScreen(GameMode.ENDLESS, debugMode));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            debugMode = DebugRenderMode.NORMAL;
            Logger.log(
                    "MainScreen",
                    "Updated render debug mode to: NORMAL",
                    LogLevel.INFO);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            debugMode = DebugRenderMode.OVERLAY;
            Logger.log(
                    "MainScreen",
                    "Updated render debug mode to: OVERLAY",
                    LogLevel.INFO);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            debugMode = DebugRenderMode.ONLY;
            Logger.log(
                    "MainScreen",
                    "Updated render debug mode to: ONLY",
                    LogLevel.INFO);
        }
    }

    @Override
    public void show() { // No logic needed
    }

    @Override
    public void resize(int i, int i1) { // No logic needed for resize currently
    }

    @Override
    public void pause() { // No logic needed
    }

    @Override
    public void resume() { // No logic needed
    }

    @Override
    public void hide() { // No logic needed
    }

    @Override
    public void dispose() {
        batch.dispose();
        Logger.log(
                "MainScreen",
                "Disposed objects",
                LogLevel.DEBUG);
    }

}
