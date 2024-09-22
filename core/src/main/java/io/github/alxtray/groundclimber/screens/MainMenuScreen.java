package io.github.alxtray.groundclimber.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.alxtray.groundclimber.Core;
import io.github.alxtray.groundclimber.enums.DebugRenderMode;
import io.github.alxtray.groundclimber.enums.GameMode;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.utilities.ButtonBuilder;
import io.github.alxtray.groundclimber.utilities.Logger;

public class MainMenuScreen extends MenuScreen {
    private static final String TITLE_IMAGE_NAME = "title_text";
    private static final String BACKGROUND_IMAGE_NAME = "menu_background";
    private static final int BUTTON_HEIGHT_FACTOR = 5;
    private static final int BUTTON_WIDTH_FACTOR = 5;
    private static final float LEVELS_MARGIN_FACTOR = 1.5f;
    private static final float ENDLESS_MARGIN_FACTOR = 40f;
    private DebugRenderMode debugMode = DebugRenderMode.NORMAL;

    public MainMenuScreen(final Core game) {
        super(
            game,
            TITLE_IMAGE_NAME,
            BACKGROUND_IMAGE_NAME,
            BUTTON_HEIGHT_FACTOR,
            BUTTON_WIDTH_FACTOR);

        final float levelsButtonTopMargin = Gdx.graphics.getHeight() / LEVELS_MARGIN_FACTOR;
        final float endlessButtonTopMargin =
            (levelsButtonTopMargin + buttonHeight) + Gdx.graphics.getHeight() / ENDLESS_MARGIN_FACTOR;
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
    public void render(final float delta) {
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
