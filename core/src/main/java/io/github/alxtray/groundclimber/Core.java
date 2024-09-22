package io.github.alxtray.groundclimber;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.screens.MainMenuScreen;
import io.github.alxtray.groundclimber.utilities.AssetLibrary;
import io.github.alxtray.groundclimber.utilities.Logger;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Core extends Game {
    @Override
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
