package io.github.alxtray.groundclimber.screens;

import com.badlogic.gdx.utils.ObjectIntMap;
import io.github.alxtray.groundclimber.bodies.Platform;
import io.github.alxtray.groundclimber.bodies.Player;
import io.github.alxtray.groundclimber.controllers.CameraController;
import io.github.alxtray.groundclimber.controllers.PhysicsController;
import io.github.alxtray.groundclimber.controllers.PlayerController;
import io.github.alxtray.groundclimber.enums.DebugRenderMode;
import io.github.alxtray.groundclimber.enums.GameMode;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.level.LevelData;
import io.github.alxtray.groundclimber.controllers.GameRenderController;
import io.github.alxtray.groundclimber.utilities.AssetLibrary;
import io.github.alxtray.groundclimber.utilities.EndlessPlatformGenerator;
import io.github.alxtray.groundclimber.utilities.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input.Keys;
import io.github.alxtray.groundclimber.utilities.PlatformFactory;
import text.formic.Stringf;

public class GameScreen implements Screen {
    private final GameMode gameMode;
    private final EndlessPlatformGenerator platformGenerator;
    private final CameraController cameraController;
    private final GameRenderController gameRenderController;
    private final PhysicsController physicsController;
    private final PlayerController playerController;

    public GameScreen(GameMode gameMode, DebugRenderMode debugMode, String... selectedLevelNames) {
        this.gameMode = gameMode;
        // Loads all assets that are required for all levels
        AssetLibrary.getInstance().loadGeneralLevelAssets();
        Logger.log(
                "GameScreen",
                "Begun loading general level assets",
                LogLevel.INFO);
        final AssetManager assetManager = AssetLibrary.getInstance().getAssetManager();
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
        Json json = new Json();
        LevelData levelData = null;
        if (gameMode == GameMode.NORMAL) {
            Array<FileHandle> selectedLevelFiles = new Array<>();
            for (String levelName : selectedLevelNames) {
                selectedLevelFiles.add(Gdx.files.internal("levels/" + levelName));
            }
            levelData = json.fromJson(LevelData.class, selectedLevelFiles.first().readString());
        } else if (gameMode == GameMode.ENDLESS) {
            levelData = json.fromJson(LevelData.class, Gdx.files.internal("endless.json"));
            Logger.log(
                    "GameScreen",
                    "Empty levelData successfully created for ENDLESS mode",
                    LogLevel.INFO);
        }
        cameraController = new CameraController(levelData.getCameraStartPosition(), levelData.getBounds());
        gameRenderController = new GameRenderController(debugMode);
        physicsController = new PhysicsController(levelData.getPlatformsData());
        if (gameMode.equals(GameMode.ENDLESS)) {
            platformGenerator = new EndlessPlatformGenerator(physicsController.getWorld());
        } else {
            platformGenerator = null;
        }
        playerController = new PlayerController(physicsController.getWorld(), levelData.getPlayerSpawn());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.3f, 0.3f, 0.46f, 1);

        if (Gdx.input.isKeyJustPressed(Keys.F3)) {
            gameRenderController.toggleDebugInfo();
        }

        Array<Platform> platforms = physicsController.getPlatforms();
        // Try catch hack to see if the player has just started and the only platform is the initial one
        try {
            if (gameMode.equals(GameMode.ENDLESS) && platforms.get(physicsController.getPlatforms().size - 5).getPosition().x < playerController.getPlayer().getPosition().x) {
                platforms.addAll(platformGenerator.generatePlatformBatch());
                // Check against 31 as this is three batches including the extra platform
                if (platforms.size == 31) {
                    for (int i = 0; i < 10; i++) {
                        physicsController.addObjectToDestroy(platforms.get(i).getBody());
                    }
                }
                Logger.log(
                    "Map",
                    "Successfully generated new endless platform batch",
                    LogLevel.INFO);
            }
        } catch (IndexOutOfBoundsException e) {
            platforms.addAll(platformGenerator.generatePlatformBatch());
        }

        playerController.update(delta);
        Player player = playerController.getPlayer();
        ObjectIntMap<String> bounds = cameraController.getBounds();
        // Kill player if they leave map bounds
        if (player.getBody().getPosition().x < bounds.get("left", 0)
            || player.getPosition().x > bounds.get("right", 0)
            || player.getPosition().y < bounds.get("bottom", 0)
            || player.getPosition().y > bounds.get("top", 0)) {
            Logger.log(
                "GameScreen",
                "Player has fell out of bounds",
                LogLevel.INFO);
            Gdx.app.exit();
        }

        cameraController.update(delta, playerController.getPlayer(), gameMode);
        physicsController.step(delta);
        gameRenderController.render(
                cameraController.getCamera(),
                playerController.getPlayer(),
                physicsController.getWorld(),
                physicsController.getPlatforms());
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
        Logger.log(
                "GameScreen",
                "Disposed objects",
                LogLevel.DEBUG);
    }

}
