package com.alx.groundclimber;

import com.alx.groundclimber.screens.MainMenuScreen;
import com.alx.groundclimber.utilities.AssetLibrary;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GroundClimber extends Game {

  public void create() {
    Gdx.app.setLogLevel(Application.LOG_DEBUG);
    
    // Initialise AssetLibrary singleton class
    AssetLibrary.getInstance().init();
    Gdx.app.log(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString() + " INFO Game", "Begun loading initial assets");
    AssetManager assetManager = AssetLibrary.getInstance().getAssetManager();
    while (!assetManager.isFinished()) {
      assetManager.update();
    }
    Gdx.app.log(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString() + " INFO Game", "Finished loading initial assets");
    
    this.setScreen(new MainMenuScreen(this));
  }

}
