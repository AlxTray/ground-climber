package io.github.alxtray.groundclimber.screens;

import io.github.alxtray.groundclimber.Core;
import io.github.alxtray.groundclimber.enums.DebugRenderMode;
import io.github.alxtray.groundclimber.enums.GameMode;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.utilities.ButtonBuilder;
import io.github.alxtray.groundclimber.utilities.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import text.formic.Stringf;

import java.util.Arrays;

public class LevelSelectScreen extends MenuScreen {
    private static final String TITLE_IMAGE_NAME = "level_title";
    private static final String BACKGROUND_IMAGE_NAME = "title_background";
    private static final int BUTTON_HEIGHT_FACTOR = 10;
    private static final int BUTTON_WIDTH_FACTOR = 5;

    public LevelSelectScreen(final Core game, final DebugRenderMode debugMode) {
        super(
            game,
            TITLE_IMAGE_NAME,
            BACKGROUND_IMAGE_NAME,
            BUTTON_HEIGHT_FACTOR,
            BUTTON_WIDTH_FACTOR);

        FileHandle[] levelFiles = Gdx.files.internal("levels").list();
        Logger.log(
                "LevelScreen",
                Stringf.format("Found the following level files: %s", Arrays.toString(levelFiles)),
                LogLevel.DEBUG);

        float buttonY = viewportTop - Gdx.graphics.getHeight() / 1.75f;
        for (FileHandle levelFile : levelFiles) {
            JsonReader jsonReader = new JsonReader();
            JsonValue jsonData = jsonReader.parse(levelFile);

            new ButtonBuilder(jsonData.get("data").get("name").asString(), skin, stage)
                .setActorName(levelFile.name())
                .setPosition(camera.position.x - (buttonWidth / 2), buttonY)
                .setSize(buttonWidth, buttonHeight)
                .setClickListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Actor target = event.getTarget();
                        // If the text in the button is clicked the event is for the Label not
                        // TextButton
                        // So, if the event is Label the TextButton is the parent Actor
                        String levelName = (target instanceof Label) ? target.getParent().getName() : target.getName();
                        Logger.log(
                            "LevelSelectButton",
                            Stringf.format("Selected level: %s", levelName),
                            LogLevel.INFO);
                        game.setScreen(new GameScreen(
                            GameMode.NORMAL,
                            debugMode,
                            levelName));
                    }
                })
                .build();

            buttonY -= buttonHeight + (float) Gdx.graphics.getHeight() / 50;
        }
    }

}
