package com.alx.groundclimber;

import com.alx.groundclimber.screens.MainMenuScreen;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class GroundClimber extends Game {

  public void create() {
    Gdx.app.setLogLevel(Application.LOG_DEBUG);
    this.setScreen(new MainMenuScreen(this));
  }

  public void render() {
    super.render();
  }

  public void dispose() {
  }

}
