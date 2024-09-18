package io.github.alxtray.groundclimber.screens;

import com.badlogic.gdx.graphics.Texture;
import io.github.alxtray.groundclimber.Core;
import io.github.alxtray.groundclimber.enums.DebugRenderMode;
import io.github.alxtray.groundclimber.enums.GameMode;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.utilities.AssetLibrary;
import io.github.alxtray.groundclimber.utilities.ButtonBuilder;
import io.github.alxtray.groundclimber.utilities.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ScreenUtils;
import text.formic.Stringf;

import java.util.Arrays;

public class LevelSelectScreen implements Screen {

    private static final float TITLE_MOVE_AMOUNT = 1.4f;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final Stage stage;
    private final Texture title;
    private final float titleWidth;
    private final float titleHeight;
    private final float titleX;
    private float currentTitleY;
    private final float finalTitleY;
    private final Texture backgroundImage;

    public LevelSelectScreen(final Core game, final DebugRenderMode debugMode) {

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        title = AssetLibrary.getInstance().getAsset("level_title", Texture.class);
        titleWidth = camera.viewportWidth * 0.4f;
        titleHeight = camera.viewportHeight * 0.2f;
        titleX = (camera.viewportWidth - titleWidth) / 2;
        currentTitleY = camera.viewportHeight;
        finalTitleY = (camera.viewportHeight - titleHeight) / 1.2f;
        backgroundImage = AssetLibrary.getInstance().getAsset("title_background", Texture.class);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        FileHandle[] levelFiles = Gdx.files.internal("levels").list();
        Logger.log(
                "LevelScreen",
                Stringf.format("Found the following level files: %s", Arrays.toString(levelFiles)),
                LogLevel.DEBUG);
        Skin skin = AssetLibrary.getInstance().getAsset("skin", Skin.class);
        float viewportTop = camera.position.y + camera.viewportHeight / 2;
        float buttonHeight = (float) Gdx.graphics.getHeight() / 10;
        float buttonWidth = (float) Gdx.graphics.getWidth() / 5;
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
        if (currentTitleY > finalTitleY) {
            currentTitleY -= TITLE_MOVE_AMOUNT;
        }
        batch.draw(
            title,
            titleX,
            currentTitleY,
            titleWidth,
            titleHeight);
        batch.end();

        stage.act(delta);
        stage.draw();
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
                "LevelScreen",
                "Disposed objects",
                LogLevel.DEBUG);
    }

}
