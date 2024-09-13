package com.alx.groundclimber;

import com.alx.groundclimber.enums.LogLevel;
import com.alx.groundclimber.screens.MainMenuScreen;
import com.alx.groundclimber.utilities.AssetLibrary;
import com.alx.groundclimber.utilities.Logger;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

public class GroundClimber extends Game {

    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        // Initialise AssetLibrary singleton class
        AssetLibrary.getInstance().init();
        Logger.log(
                "Game",
                "Begun loading initial assets",
                LogLevel.INFO);
        AssetManager assetManager = AssetLibrary.getInstance().getAssetManager();
        while (!assetManager.isFinished()) {
            assetManager.update();
        }
        Logger.log(
                "Game",
                "Finished loading initial assets",
                LogLevel.INFO);

        this.setScreen(new MainMenuScreen(this));
    }

}
