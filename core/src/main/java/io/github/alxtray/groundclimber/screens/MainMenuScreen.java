package io.github.alxtray.groundclimber.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.alxtray.groundclimber.Core;
import io.github.alxtray.groundclimber.enums.DebugRenderMode;
import io.github.alxtray.groundclimber.enums.GameMode;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.utilities.ButtonBuilder;
import io.github.alxtray.groundclimber.utilities.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class MainMenuScreen extends MenuScreen {
    private static final String TITLE_IMAGE_NAME = "title_text";
    private static final String BACKGROUND_IMAGE_NAME = "menu_background";
    private static final int BUTTON_HEIGHT_FACTOR = 5;
    private static final int BUTTON_WIDTH_FACTOR = 5;
    private DebugRenderMode debugMode = DebugRenderMode.NORMAL;

    public MainMenuScreen(final Core game) {
        super(
            game,
            TITLE_IMAGE_NAME,
            BACKGROUND_IMAGE_NAME,
            BUTTON_HEIGHT_FACTOR,
            BUTTON_WIDTH_FACTOR);

        float levelsButtonTopMargin = Gdx.graphics.getHeight() / 1.5f;
        float endlessButtonTopMargin = (levelsButtonTopMargin + buttonHeight) + (float) Gdx.graphics.getHeight() / 40;
        new ButtonBuilder("Levels", skin, stage)
            .setSize(buttonWidth, buttonHeight)
            .setPosition(camera.position.x - (buttonWidth / 2), viewportTop - levelsButtonTopMargin)
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
            .setSize(buttonWidth, buttonHeight)
            .setPosition(camera.position.x - (buttonWidth / 2), viewportTop - endlessButtonTopMargin)
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
        super.render(delta);

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

}
