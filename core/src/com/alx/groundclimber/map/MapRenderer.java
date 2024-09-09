package com.alx.groundclimber.map;

import com.alx.groundclimber.enums.DebugRenderMode;
import com.alx.groundclimber.enums.GameMode;
import com.alx.groundclimber.bodies.Platform;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MapRenderer {

  Map map;
  GameMode gameMode;
  SpriteBatch batch;
  BitmapFont font;
  Texture playerImage;
  Texture playerFaceImage;
  Texture backgroundImage;

  // Debug rendering
  Box2DDebugRenderer debugRenderer;
  DebugRenderMode debugMode;
  boolean debugInfo;

  public MapRenderer(Map map, GameMode gameMode, DebugRenderMode debugMode) {
    this.map = map;
    this.gameMode = gameMode;
    this.debugMode = debugMode;

    batch = new SpriteBatch();
    font = new BitmapFont();
    playerImage = new Texture(Gdx.files.internal("player.png"));
    playerFaceImage = new Texture(Gdx.files.internal("player_face.png"));
    backgroundImage = new Texture(Gdx.files.internal("background.png"));

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
              playerFaceImage.getWidth() / 2,
              playerFaceImage.getWidth() / 2,
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
        platform.draw(batch, 1);
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
