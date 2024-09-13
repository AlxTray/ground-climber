package com.alx.groundclimber.map;

import com.alx.groundclimber.enums.DebugRenderMode;
import com.alx.groundclimber.enums.GameMode;
import com.alx.groundclimber.bodies.Platform;
import com.alx.groundclimber.utilities.AssetLibrary;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MapRenderer {
  
  static final int TILE_SIZE = 18;

  final Map map;
  final GameMode gameMode;
  final SpriteBatch batch;
  final BitmapFont font;
  final Texture playerImage;
  final Texture playerFaceImage;
  final Texture backgroundImage;
  final Texture platformTileImage;

  // Debug rendering
  final Box2DDebugRenderer debugRenderer;
  final DebugRenderMode debugMode;
  boolean debugInfo;

  public MapRenderer(Map map, GameMode gameMode, DebugRenderMode debugMode) {
    this.map = map;
    this.gameMode = gameMode;
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
              map.player.body.getPosition().x - (playerFaceImage.getWidth() / 2f),
              map.player.body.getPosition().y - (playerFaceImage.getHeight() / 2f));
      batch.draw(
              playerFaceImage,
              map.player.body.getPosition().x - (playerFaceImage.getWidth() / 2f),
              map.player.body.getPosition().y - (playerFaceImage.getHeight() / 2f),
              playerFaceImage.getWidth() / 2f,
              playerFaceImage.getWidth() / 2f,
              playerFaceImage.getWidth(),
              playerFaceImage.getHeight(),
              1f,
              1f,
              (float) Math.toDegrees(map.player.body.getAngle()),
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
            String.format("Player Pos: (%.2f, %.2f)", map.player.body.getPosition().x, map.player.body.getPosition().y),
            cornerX, cornerY - 30);
        font.draw(batch, String.format("Player Lin Vec: (%.2f, %.2f)", map.player.body.getLinearVelocity().x,
            map.player.body.getLinearVelocity().y), cornerX, cornerY - 50);
        font.draw(batch, String.format("Player Ang Vec: %.2f", map.player.body.getAngularVelocity()), cornerX,
            cornerY - 70);
      }
      
      for (Platform platform : map.platforms) {
        Vector2 platformPos = platform.body.getPosition();
        for (float placementX = platformPos.x; placementX < platform.width + platformPos.x; placementX += TILE_SIZE) {
          for (float placementY = platformPos.y; placementY < platform.height + platformPos.y; placementY += TILE_SIZE) {
            // Minus half width and height as x and y used here is the adjusted version for Box2D
            batch.draw(platformTileImage, placementX - (platform.width / 2), placementY - (platform.height / 2));
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
    Gdx.app.debug(
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString()
            + " DEBUG MapRenderer",
        "Disposed objects");
  }

}
