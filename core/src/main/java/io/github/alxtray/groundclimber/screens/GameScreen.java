package io.github.alxtray.groundclimber.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.ObjectIntMap;
import io.github.alxtray.groundclimber.bodies.EnvironmentObject;
import io.github.alxtray.groundclimber.bodies.Player;
import io.github.alxtray.groundclimber.controllers.CameraController;
import io.github.alxtray.groundclimber.controllers.PhysicsController;
import io.github.alxtray.groundclimber.controllers.PlayerController;
import io.github.alxtray.groundclimber.enums.DebugRenderMode;
import io.github.alxtray.groundclimber.enums.GameMode;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.level.LevelData;
import io.github.alxtray.groundclimber.renderers.BackgroundObjectRenderer;
import io.github.alxtray.groundclimber.renderers.EnvironmentObjectRenderer;
import io.github.alxtray.groundclimber.renderers.EnvironmentObjectVisitor;
import io.github.alxtray.groundclimber.renderers.PlayerRenderer;
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
import text.formic.Stringf;

public class GameScreen implements Screen {
    private final GameMode gameMode;
    private final DebugRenderMode renderMode;
    private boolean displayDebugInfo;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Box2DDebugRenderer debugRenderer;
    private final EndlessPlatformGenerator platformGenerator;
    private final CameraController cameraController;
    private final PhysicsController physicsController;
    private final PlayerController playerController;
    private final PlayerRenderer playerRenderer;
    private final BackgroundObjectRenderer backgroundObjectRenderer;
    private final EnvironmentObjectVisitor environmentObjectRenderer;

    public GameScreen(GameMode gameMode, DebugRenderMode renderMode, String... selectedLevelNames) {
        this.gameMode = gameMode;
        this.renderMode = renderMode;
        displayDebugInfo = false;
        batch = new SpriteBatch();
        font = new BitmapFont();
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
        physicsController = new PhysicsController(levelData.getPlatformsData());
        if (gameMode.equals(GameMode.ENDLESS)) {
            platformGenerator = new EndlessPlatformGenerator();
        } else {
            platformGenerator = null;
        }
        playerController = new PlayerController(physicsController.getWorld(), levelData.getPlayerSpawn());
        if (renderMode.equals(DebugRenderMode.ONLY) || renderMode.equals(DebugRenderMode.OVERLAY)) {
            debugRenderer = new Box2DDebugRenderer();
        } else {
            debugRenderer = null;
        }
        playerRenderer = new PlayerRenderer();
        backgroundObjectRenderer = new BackgroundObjectRenderer();
        environmentObjectRenderer = new EnvironmentObjectRenderer();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        handleInput();

        if (gameMode.equals(GameMode.ENDLESS)) {
            platformGenerator.checkAndGenerateBatch(
                physicsController.getWorld(),
                playerController.getPlayer(),
                physicsController.getEnvironmentObjects());
        }

        playerController.update(delta);
        checkPlayerInBounds();

        Player player = playerController.getPlayer();
        cameraController.update(delta, player, gameMode);
        physicsController.step(delta);

        OrthographicCamera camera = cameraController.getCamera();
        if (!renderMode.equals(DebugRenderMode.ONLY)) {
            batch.begin();
            batch.setProjectionMatrix(camera.combined);
            backgroundObjectRenderer.render(batch, camera);
            playerRenderer.render(batch, player);
            for (EnvironmentObject environmentObject : physicsController.getEnvironmentObjects()) {
                environmentObject.acceptRender(environmentObjectRenderer, batch);
            }
            if (displayDebugInfo) {
                drawDebugInfo(camera, player);
            }
            batch.end();
        }
        if (renderMode.equals(DebugRenderMode.ONLY) || renderMode.equals(DebugRenderMode.OVERLAY)) {
            debugRenderer.render(physicsController.getWorld(), camera.combined);
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            displayDebugInfo = !displayDebugInfo;
        }
    }

    private void drawDebugInfo(OrthographicCamera camera, Player player) {
        // 10 added as each line will be 10 pixels away from left anyway
        float cornerX = (camera.position.x - camera.viewportWidth / 2) + 10;
        float cornerY = camera.position.y + camera.viewportHeight / 2;
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), cornerX, cornerY - 10);
        font.draw(batch,
            Stringf.format(
                "Player Pos: (%.2f, %.2f)",
                player.getBody().getPosition().x,
                player.getBody().getPosition().y),
            cornerX, cornerY - 30);
        font.draw(batch, Stringf.format("Player Lin Vec: (%.2f, %.2f)", player.getBody().getLinearVelocity().x,
            player.getBody().getLinearVelocity().y), cornerX, cornerY - 50);
        font.draw(batch, Stringf.format("Player Ang Vec: %.2f", player.getBody().getAngularVelocity()), cornerX,
            cornerY - 70);
    }

    private void checkPlayerInBounds() {
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
        font.dispose();
        Logger.log(
                "GameScreen",
                "Disposed objects",
                LogLevel.DEBUG);
    }

}
