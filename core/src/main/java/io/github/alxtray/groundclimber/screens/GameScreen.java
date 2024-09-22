package io.github.alxtray.groundclimber.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.ObjectIntMap;
import io.github.alxtray.groundclimber.bodies.Player;
import io.github.alxtray.groundclimber.controllers.ControllerManager;
import io.github.alxtray.groundclimber.enums.DebugRenderMode;
import io.github.alxtray.groundclimber.enums.GameMode;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.level.LevelData;
import io.github.alxtray.groundclimber.renderers.RenderManager;
import io.github.alxtray.groundclimber.utilities.AssetLibrary;
import io.github.alxtray.groundclimber.utilities.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;
import text.formic.Stringf;

public class GameScreen implements Screen {
    private final DebugRenderMode renderMode;
    private final SpriteBatch batch;
    private final Box2DDebugRenderer debugRenderer;
    private final ControllerManager controllerManager;
    private final RenderManager renderManager;

    public GameScreen(GameMode gameMode, DebugRenderMode renderMode, String... selectedLevelNames) {
        this.renderMode = renderMode;
        batch = new SpriteBatch();
        debugRenderer =
            (renderMode.equals(DebugRenderMode.ONLY) || renderMode.equals(DebugRenderMode.OVERLAY))
                ? new Box2DDebugRenderer() : null;
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

        LevelData levelData = loadLevelData(gameMode, selectedLevelNames);
        controllerManager = new ControllerManager(gameMode, levelData);
        renderManager = new RenderManager();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        handleInput();

        controllerManager.update(delta);
        checkPlayerInBounds();

        OrthographicCamera camera = controllerManager.getCamera();
        Player player = controllerManager.getPlayer();
        batch.setProjectionMatrix(camera.combined);
        if (!renderMode.equals(DebugRenderMode.ONLY)) {
            batch.begin();
            renderManager.render(camera, player, controllerManager.getEnvironmentObjects(), batch);
            batch.end();
        }
        if (renderMode.equals(DebugRenderMode.ONLY) || renderMode.equals(DebugRenderMode.OVERLAY)) {
            debugRenderer.render(controllerManager.getWorld(), camera.combined);
        }
    }

    private LevelData loadLevelData(GameMode gameMode, String... selectedLevelNames) {
        final Json json = new Json();
        if (gameMode.equals(GameMode.NORMAL)) {
            return json.fromJson(LevelData.class, Gdx.files.internal("levels/" + selectedLevelNames[0]).readString());
        } else if (gameMode.equals(GameMode.ENDLESS)) {
            Logger.log("GameScreen", "Empty levelData successfully created for ENDLESS mode", LogLevel.INFO);
            return json.fromJson(LevelData.class, Gdx.files.internal("endless.json").readString());
        }
        return null;
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            renderManager.toggleDebugInfo();
        }
    }

    private void checkPlayerInBounds() {
        Player player = controllerManager.getPlayer();
        ObjectIntMap<String> bounds = controllerManager.getBounds();
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
        renderManager.dispose();
        Logger.log(
                "GameScreen",
                "Disposed objects",
                LogLevel.DEBUG);
    }

}
