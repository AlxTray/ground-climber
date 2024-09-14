package io.github.alxtray.groundclimber.map;

import io.github.alxtray.groundclimber.bodies.Platform;
import io.github.alxtray.groundclimber.enums.DebugRenderMode;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.utilities.AssetLibrary;
import io.github.alxtray.groundclimber.utilities.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class MapRenderer {

    private static final int TILE_SIZE = 18;

    private final Map map;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Texture playerImage;
    private final Texture playerFaceImage;
    private final Texture backgroundImage;
    private final Texture platformTileImage;

    // Debug rendering
    private final Box2DDebugRenderer debugRenderer;
    private final DebugRenderMode debugMode;
    private boolean debugInfo;

    public MapRenderer(Map map, DebugRenderMode debugMode) {
        this.map = map;
        this.debugMode = debugMode;

        batch = new SpriteBatch();
        font = new BitmapFont();

        AssetLibrary assetLibrary = AssetLibrary.getInstance();
        playerImage = assetLibrary.getAsset("player", Texture.class);
        playerFaceImage = assetLibrary.getAsset("playerFace", Texture.class);
        backgroundImage = assetLibrary.getAsset("background", Texture.class);
        platformTileImage = assetLibrary.getAsset("platformTile", Texture.class);

        debugRenderer = new Box2DDebugRenderer();
        debugInfo = false;
    }

    public void render(OrthographicCamera camera) {
        if (!debugMode.equals(DebugRenderMode.ONLY)) {
            batch.begin();
            batch.disableBlending();
            batch.draw(
                    backgroundImage,
                    camera.position.x - (camera.viewportWidth / 2),
                    camera.position.y - (camera.viewportHeight / 2),
                    camera.viewportWidth,
                    camera.viewportHeight);
            batch.enableBlending();
            batch.setProjectionMatrix(camera.combined);
            batch.draw(
                    playerImage,
                    map.getPlayerBody().getPosition().x - (playerFaceImage.getWidth() / 2f),
                    map.getPlayerBody().getPosition().y - (playerFaceImage.getHeight() / 2f));
            batch.draw(
                    playerFaceImage,
                    map.getPlayerBody().getPosition().x - (playerFaceImage.getWidth() / 2f),
                    map.getPlayerBody().getPosition().y - (playerFaceImage.getHeight() / 2f),
                    playerFaceImage.getWidth() / 2f,
                    playerFaceImage.getWidth() / 2f,
                    playerFaceImage.getWidth(),
                    playerFaceImage.getHeight(),
                    1f,
                    1f,
                    (float) Math.toDegrees(map.getPlayerBody().getAngle()),
                    0,
                    0,
                    playerFaceImage.getWidth(),
                    playerFaceImage.getHeight(),
                    false,
                    false);

            if (debugInfo) {
                // 10 added as each line will be 10 pixels away from left anyway
                float cornerX = (camera.position.x - camera.viewportWidth / 2) + 10;
                float cornerY = camera.position.y + camera.viewportHeight / 2;
                font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), cornerX, cornerY - 10);
                font.draw(batch,
                        String.format("Player Pos: (%.2f, %.2f)", map.getPlayerBody().getPosition().x, map.getPlayerBody().getPosition().y),
                        cornerX, cornerY - 30);
                font.draw(batch, String.format("Player Lin Vec: (%.2f, %.2f)", map.getPlayerBody().getLinearVelocity().x,
                        map.getPlayerBody().getLinearVelocity().y), cornerX, cornerY - 50);
                font.draw(batch, String.format("Player Ang Vec: %.2f", map.getPlayerBody().getAngularVelocity()), cornerX,
                        cornerY - 70);
            }

            for (Platform platform : map.getPlatforms()) {
                Vector2 platformPos = platform.getPosition();
                for (float placementX = platformPos.x; placementX < platform.getWidth() + platformPos.x; placementX += TILE_SIZE) {
                    for (float placementY = platformPos.y; placementY < platform.getHeight() + platformPos.y; placementY += TILE_SIZE) {
                        // Minus half width and height as x and y used here is the adjusted version for Box2D
                        batch.draw(platformTileImage, placementX - (platform.getWidth() / 2), placementY - (platform.getHeight() / 2));
                    }
                }
            }
            batch.end();
        }
        if (!debugMode.equals(DebugRenderMode.NORMAL)) {
            debugRenderer.render(map.world, camera.combined);
        }
    }

    public void toggleDebugInfo() {
        debugInfo = !debugInfo;
    }

    public void dispose() {
        playerImage.dispose();
        batch.dispose();
        Logger.log(
                "MapRenderer",
                "Disposed objects",
                LogLevel.DEBUG);
    }

}
