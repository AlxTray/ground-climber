package com.alx.groundclimber.screens;

import com.alx.groundclimber.GroundClimber;
import com.alx.groundclimber.enums.DebugRenderMode;
import com.alx.groundclimber.enums.GameMode;
import com.alx.groundclimber.utilities.AssetLibrary;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

import java.util.Arrays;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LevelSelectScreen implements Screen {

  final GroundClimber game;
  final DebugRenderMode debugMode;

  SpriteBatch batch;
  BitmapFont font;
  OrthographicCamera camera;
  Stage stage;
  Skin skin;
  FileHandle[] levelFiles;

  public LevelSelectScreen(final GroundClimber game, final DebugRenderMode debugMode) {
    this.game = game;
    this.debugMode = debugMode;

    batch = new SpriteBatch();
    font = new BitmapFont();
    camera = new OrthographicCamera();
    camera.setToOrtho(false, 800, 480);

    stage = new Stage();
    Gdx.input.setInputProcessor(stage);
    
    skin = AssetLibrary.getInstance().getAsset("skin", Skin.class);

    levelFiles = Gdx.files.internal("levels").list();
    Gdx.app.debug(
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString()
            + " DEBUG LevelScreen",
        String.format("Found the following level files: %s", Arrays.toString(levelFiles)));
    float buttonXIncrement = 0;
    for (FileHandle levelFile : levelFiles) {
      JsonReader jsonReader = new JsonReader();
      JsonValue jsonData = jsonReader.parse(levelFile);

      TextButton levelButton = new TextButton(jsonData.get("data").get("name").asString(), skin);
      levelButton.setPosition(100 + buttonXIncrement, 110);
      levelButton.setName(levelFile.name());
      Gdx.app.debug(
          LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString()
              + " DEBUG LevelScreen",
          String.format("Successfully created button for level: %s", levelFile.name()));

      levelButton.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
          Actor target = event.getTarget();
          // If the text in the button is clicked the event is for the Label not
          // TextButton
          // So, if the event is Label the TextButton is the parent Actor
          String levelName = (target instanceof Label) ? target.getParent().getName() : target.getName();
          Gdx.app.log(
              LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString()
                  + " INFO LevelScreenButton",
              String.format("Selected level: %s", levelName));
          game.setScreen(new GameScreen(
              game,
              GameMode.NORMAL,
              debugMode,
              levelName));
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
    batch.setProjectionMatrix(camera.combined);

    stage.act(delta);
    stage.draw();

    batch.begin();
    font.draw(batch, "Select from the following levels.", 100, 150);
    batch.end();
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
    Gdx.app.debug(
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString()
            + " DEBUG LevelScreen",
        "Disposed objects");
  }

}
