package io.github.alxtray.groundclimber.controllers;

import com.badlogic.gdx.physics.box2d.World;
import io.github.alxtray.groundclimber.bodies.Platform;
import io.github.alxtray.groundclimber.bodies.Player;
import io.github.alxtray.groundclimber.enums.DebugRenderMode;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.level.LevelData;
import io.github.alxtray.groundclimber.utilities.AssetLibrary;
import io.github.alxtray.groundclimber.utilities.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import text.formic.Stringf;

public class GameRenderController {

    private static final int TILE_SIZE = 18;

    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Texture playerImage;
    private final Texture playerFaceImage;
    private final Texture backgroundImage;
    private final Texture grassSingleLeftImage;
    private final Texture grassSingleMiddleImage;
    private final Texture grassSingleRightImage;
    private final Texture grassTopLeftImage;
    private final Texture grassTopMiddleImage;
    private final Texture grassTopRightImage;
    private final Texture groundLeftBottomImage;
    private final Texture groundLeftMiddleImage;
    private final Texture groundMiddleBottomImage;
    private final Texture groundNoBorderImage;
    private final Texture groundRightBottomImage;
    private final Texture groundRightMiddleImage;

    // Debug rendering
    private final Box2DDebugRenderer debugRenderer;
    private final DebugRenderMode debugMode;
    private boolean debugInfo;

    public GameRenderController(DebugRenderMode debugMode) {
        this.debugMode = debugMode;
        batch = new SpriteBatch();
        font = new BitmapFont();

        AssetLibrary assetLibrary = AssetLibrary.getInstance();
        playerImage = assetLibrary.getAsset("player", Texture.class);
        playerFaceImage = assetLibrary.getAsset("playerFace", Texture.class);
        backgroundImage = assetLibrary.getAsset("background", Texture.class);
        grassSingleLeftImage = assetLibrary.getAsset("grass_single_left", Texture.class);
        grassSingleMiddleImage = assetLibrary.getAsset("grass_single_middle", Texture.class);
        grassSingleRightImage = assetLibrary.getAsset("grass_single_right", Texture.class);
        grassTopLeftImage = assetLibrary.getAsset("grass_top_left", Texture.class);
        grassTopMiddleImage = assetLibrary.getAsset("grass_top_middle", Texture.class);
        grassTopRightImage = assetLibrary.getAsset("grass_top_right", Texture.class);
        groundLeftBottomImage = assetLibrary.getAsset("ground_left_bottom", Texture.class);
        groundLeftMiddleImage = assetLibrary.getAsset("ground_left_middle", Texture.class);
        groundMiddleBottomImage = assetLibrary.getAsset("ground_middle_bottom", Texture.class);
        groundNoBorderImage = assetLibrary.getAsset("ground_no_border", Texture.class);
        groundRightBottomImage = assetLibrary.getAsset("ground_right_bottom", Texture.class);
        groundRightMiddleImage = assetLibrary.getAsset("ground_right_middle", Texture.class);

        debugRenderer = new Box2DDebugRenderer();
        debugInfo = false;
    }

    public void render(OrthographicCamera camera, Player player, World world, Array<Platform> platforms) {
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
                    player.getPosition().x - (playerFaceImage.getWidth() / 2f),
                    player.getPosition().y - (playerFaceImage.getHeight() / 2f));
            batch.draw(
                    playerFaceImage,
                    player.getPosition().x - (playerFaceImage.getWidth() / 2f),
                    player.getPosition().y - (playerFaceImage.getHeight() / 2f),
                    playerFaceImage.getWidth() / 2f,
                    playerFaceImage.getWidth() / 2f,
                    playerFaceImage.getWidth(),
                    playerFaceImage.getHeight(),
                    1f,
                    1f,
                    (float) Math.toDegrees(player.getBody().getAngle()),
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
                        Stringf.format("Player Pos: (%.2f, %.2f)", player.getPosition().x, player.getPosition().y),
                        cornerX, cornerY - 30);
                font.draw(batch, Stringf.format("Player Lin Vec: (%.2f, %.2f)", player.getBody().getLinearVelocity().x,
                        player.getBody().getLinearVelocity().y), cornerX, cornerY - 50);
                font.draw(batch, Stringf.format("Player Ang Vec: %.2f", player.getBody().getAngularVelocity()), cornerX,
                        cornerY - 70);
            }

            Texture platformTileTexture = groundNoBorderImage;
            int rowTileNumber = 0;
            int columnTileNumber = 0;
            for (Platform platform : platforms) {
                Vector2 platformPos = platform.getPosition();
                int numberOfRows = (int) platform.getWidth() / TILE_SIZE;
                int numberOfColumns = (int) platform.getHeight() / TILE_SIZE;
                for (float placementX = platformPos.x; placementX < platform.getWidth() + platformPos.x; placementX += TILE_SIZE) {
                    rowTileNumber++;
                    for (float placementY = platformPos.y; placementY < platform.getHeight() + platformPos.y; placementY += TILE_SIZE) {
                        columnTileNumber++;
                        // Minus half width and height as x and y used here is the adjusted version for Box2D
                        if (columnTileNumber == numberOfColumns && rowTileNumber == 1) {
                            platformTileTexture = grassTopLeftImage;
                            if (numberOfColumns == 1) {
                                platformTileTexture = grassSingleLeftImage;
                            }
                        } else if (columnTileNumber == numberOfColumns && rowTileNumber == numberOfRows) {
                            platformTileTexture = grassTopRightImage;
                            if (numberOfColumns == 1) {
                                platformTileTexture = grassSingleRightImage;
                            }
                        } else if (columnTileNumber == numberOfColumns) {
                            platformTileTexture = grassTopMiddleImage;
                            if (numberOfColumns == 1) {
                                platformTileTexture = grassSingleMiddleImage;
                            }
                        } else if (columnTileNumber == 1 && rowTileNumber == 1) {
                            platformTileTexture = groundLeftBottomImage;
                        } else if (rowTileNumber == 1) {
                            platformTileTexture = groundLeftMiddleImage;
                        } else if (columnTileNumber == 1 && rowTileNumber == numberOfRows) {
                            platformTileTexture = groundRightBottomImage;
                        } else if (rowTileNumber == numberOfRows) {
                            platformTileTexture = groundRightMiddleImage;
                        } else if (columnTileNumber == 1) {
                            platformTileTexture = groundMiddleBottomImage;
                        }
                        batch.draw(platformTileTexture, placementX - (platform.getWidth() / 2), placementY - (platform.getHeight() / 2));
                        platformTileTexture = groundNoBorderImage;
                    }
                    columnTileNumber = 0;
                }
                rowTileNumber = 0;
            }
            batch.end();
        }
        if (!debugMode.equals(DebugRenderMode.NORMAL)) {
            debugRenderer.render(world, camera.combined);
        }
    }

    public void toggleDebugInfo() {
        debugInfo = !debugInfo;
    }

    public void dispose() {
        playerImage.dispose();
        batch.dispose();
        Logger.log(
                "GameRenderController",
                "Disposed objects",
                LogLevel.DEBUG);
    }

}
