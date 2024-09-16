package io.github.alxtray.groundclimber.screens;

import io.github.alxtray.groundclimber.enums.DebugRenderMode;
import io.github.alxtray.groundclimber.enums.GameMode;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.map.Map;
import io.github.alxtray.groundclimber.map.MapRenderer;
import io.github.alxtray.groundclimber.utilities.AssetLibrary;
import io.github.alxtray.groundclimber.utilities.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;
import text.formic.Stringf;

public class GameScreen implements Screen {

    private final MapRenderer mapRenderer;
    private Map map;

    public GameScreen(GameMode gameMode, DebugRenderMode debugMode, String... selectedLevelNames) {

        // Loads all assets that are required for all levels
        AssetLibrary.getInstance().loadGeneralLevelAssets();
        Logger.log(
                "GameScreen",
                "Begun loading general level assets",
                LogLevel.INFO);
        AssetManager assetManager = AssetLibrary.getInstance().getAssetManager();
        while (!assetManager.isFinished()) {
            assetManager.update();
        }
        Logger.log(
                "GameScreen",
                "Finished loading general level assets",
                LogLevel.INFO);

        Logger.log(
                "GameScreen",
                Stringf.format("The current game mode is: %s", gameMode.name()),
                LogLevel.INFO);
        if (gameMode == GameMode.NORMAL) {
            Array<FileHandle> selectedLevelFiles = new Array<>();
            for (String levelName : selectedLevelNames) {
                selectedLevelFiles.add(Gdx.files.internal("levels/" + levelName));
            }
            Json json = new Json();
            map = json.fromJson(Map.class, selectedLevelFiles.first().readString());
        } else if (gameMode == GameMode.ENDLESS) {
            Json json = new Json();
            map = json.fromJson(Map.class, Gdx.files.internal("endless.json"));
            Logger.log(
                    "GameScreen",
                    "Empty map successfully created for ENDLESS mode",
                    LogLevel.INFO);
        }
        mapRenderer = new MapRenderer(map, debugMode);
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
        Logger.log(
                "GameScreen",
                "Disposed objects",
                LogLevel.DEBUG);
    }

}
