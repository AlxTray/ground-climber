package com.alx.groundclimber.screens;

import com.alx.groundclimber.GroundClimber;
import com.alx.groundclimber.enums.DebugRenderMode;
import com.alx.groundclimber.enums.GameMode;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Optional;

public class LevelSelectScreen implements Screen {

    final GroundClimber game;
    final DebugRenderMode debugMode;

    OrthographicCamera camera;
    Stage stage;
    Skin skin;
    FileHandle[] levelFiles;

    public LevelSelectScreen(final GroundClimber game, final DebugRenderMode debugMode) {
        this.game = game;
        this.debugMode = debugMode;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        levelFiles = Gdx.files.internal("levels").list();
        float buttonXIncrement = 0;
        for (FileHandle levelFile : levelFiles) {
            JsonReader jsonReader = new JsonReader();
            JsonValue jsonData = jsonReader.parse(levelFile);

            TextButton levelButton = new TextButton(jsonData.get("data").get("name").asString(), skin);
            levelButton.setPosition(100 + buttonXIncrement, 110);
            levelButton.setName(levelFile.name());

            levelButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Actor target = event.getTarget();
                    game.setScreen(new GameScreen(
                            game,
                            GameMode.NORMAL,
                            debugMode,
                            (target instanceof Label) ? target.getParent().getName() : target.getName()
                    ));
                }
            });

            stage.addActor(levelButton);
            buttonXIncrement += levelButton.getWidth() + 10;
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.3f, 0.3f, 0.46f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        stage.act(delta);
        stage.draw();

        game.batch.begin();
        game.font.draw(game.batch, "Select from the following levels.", 100, 150);
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
