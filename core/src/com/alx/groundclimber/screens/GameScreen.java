package com.alx.groundclimber.screens;

import com.alx.groundclimber.*;
import com.alx.groundclimber.enums.DebugRenderMode;
import com.alx.groundclimber.enums.GameMode;
import com.alx.groundclimber.map.Map;
import com.alx.groundclimber.map.MapRenderer;
import com.alx.groundclimber.utilities.AssetLibrary;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GameScreen implements Screen {

  final GroundClimber game;

  final Array<FileHandle> selectedLevelFiles = new Array<>();
  Map map;
  final MapRenderer mapRenderer;

  public GameScreen(GroundClimber game, GameMode gameMode, DebugRenderMode debugMode, String... selectedLevelNames) {
    this.game = game;
    
    // Loads all assets that are required for all levels
    AssetLibrary.getInstance().loadGeneralLevelAssets();
    Gdx.app.log(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString() + " INFO GameScreen", "Begun loading general level assets");
    AssetManager assetManager = AssetLibrary.getInstance().getAssetManager();
    while (!assetManager.isFinished()) {
      assetManager.update();
    }
    Gdx.app.log(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString() + " INFO GameScreen", "Finished loading general level assets");
    
    Gdx.app.log(
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString() + " INFO GameScreen",
        String.format("The current game mode is: %s", gameMode.name()));
    if (gameMode == GameMode.NORMAL) {
      for (String levelName : selectedLevelNames) {
        selectedLevelFiles.add(Gdx.files.internal("levels/" + levelName));
      }
      Json json = new Json();
      map = json.fromJson(Map.class, selectedLevelFiles.first().readString());
    } else if (gameMode == GameMode.ENDLESS) {
      map = new Map();
      Gdx.app.log(
          LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString()
              + " INFO GameScreen",
          "Empty map successfully created for ENDLESS mode");
    }
    map.setGameMode(gameMode);

    mapRenderer = new MapRenderer(map, gameMode, debugMode);
    map.attachRenderer(mapRenderer);
  }

  @Override
  public void render(float delta) {
    ScreenUtils.clear(0.3f, 0.3f, 0.46f, 1);
    map.update(delta);
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
    map.dispose();
    mapRenderer.dispose();
    Gdx.app.debug(
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString() + " DEBUG GameScreen",
        "Disposed objects");
  }

}
