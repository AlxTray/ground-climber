package com.alx.groundclimber.map;

import com.alx.groundclimber.enums.GameMode;
import com.alx.groundclimber.bodies.Platform;
import com.alx.groundclimber.bodies.Player;
import com.alx.groundclimber.listeners.ContactListenerImpl;
import com.alx.groundclimber.listeners.CrackedPlatformContactListener;
import com.alx.groundclimber.listeners.DebugContactListener;
import com.alx.groundclimber.utilities.EndlessPlatformGenerator;
import com.alx.groundclimber.utilities.PlatformFactory;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Map implements Json.Serializable {

  int PLAYER_INITIAL_RADIUS = 16;
  int PLAYER_INITIAL_Y = 500;
  int PLAYER_INITIAL_X = 50;
  int CAMERA_MOVEMENT_THRESHOLD = 300;

  EndlessPlatformGenerator platGenerator;
  ContactListenerImpl contactListener;
  MapRenderer mapRenderer;

  public World world;
  public Player player;
  OrthographicCamera camera;
  GameMode gameMode;
  Array<Body> objectsToDestroy = new Array<>();

  public Array<Platform> platforms = new Array<>();
  Array<Integer> bounds = new Array<>();

  Platform lastPlatformInBatch;

  public Map() {
    world = new World(new Vector2(0, -75), true);
    spawnNewPlayer(
        PLAYER_INITIAL_X,
        PLAYER_INITIAL_Y,
        PLAYER_INITIAL_RADIUS);

    camera = new OrthographicCamera();
    camera.setToOrtho(false, 800, 480);

    contactListener = new ContactListenerImpl();
    contactListener.addContactListener(new CrackedPlatformContactListener());
    if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
      contactListener.addContactListener(new DebugContactListener());
    }
    world.setContactListener(contactListener);
  }

  public void setGameMode(GameMode gameMode) {
    this.gameMode = gameMode;

    // Generate initial endless platform batch here as when Map is instantiated the
    // game mode is not known
    if (gameMode.equals(GameMode.ENDLESS)) {
      platGenerator = new EndlessPlatformGenerator(world);
      platforms = platGenerator.generateInitialBatch();
      lastPlatformInBatch = new Platform(world, 520f, 0, 20f, 60f);
      Gdx.app.log(
          LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString() + " INFO Map",
          "Successfully generated initial endless platforms");
    }
  }

  public void attachRenderer(MapRenderer mapRenderer) {
    this.mapRenderer = mapRenderer;
  }

  public void spawnNewPlayer(int x, int y, int radius) {
    player = new Player(world, x, y, radius);
    Gdx.app.log(
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString() + " INFO Map",
        "New player spawned successfully");
  }

  public void update(float delta) {
    player.update(delta);

    if (gameMode.equals(GameMode.ENDLESS) && player.body.getPosition().x > 100) {
      camera.translate(0.6f, 0);
    }
    if (gameMode.equals(GameMode.NORMAL)) {
      repositionCamera();
    }
    camera.update();

    if (Gdx.input.isKeyJustPressed(Keys.F3)) {
      mapRenderer.toggleDebugInfo();
    }

    // Kill player if they leave map bounds
    if (player.body.getPosition().y < 0) {
      Gdx.app.log(
          LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString() + " INFO Map",
          "Player has fell out of bounds");
      Gdx.app.exit();
    }

    if (gameMode.equals(GameMode.ENDLESS)) {
      if (lastPlatformInBatch.getX() < player.body.getPosition().x) {
        platforms = platGenerator.generatePlatformBatch();
        lastPlatformInBatch = platforms.get(platforms.size - 1);
        Gdx.app.log(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString() + " INFO Map",
            "Successfully generated new endless platform batch");
      }
    }

    destroyQueuedObjects();
    world.step(1 / 60f, 6, 2);

    mapRenderer.render(camera);
  }

  public void destroyQueuedObjects() {
    // Grab all queued objects to destroy from listeners
    // So that all objects can be destroyed at once and will not be locked
    objectsToDestroy.addAll(contactListener.getBodiesToDestroy());

    for (Body objectToDestroy : objectsToDestroy) {
      Object objectData = objectToDestroy.getUserData();
      platforms.removeValue((Platform) objectData, false);

      world.destroyBody(objectToDestroy);
      Gdx.app.debug(
          LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString() + " DEBUG Map",
          String.format("The object %s has been destroyed from world", objectData.getClass().getSimpleName()));
    }
    objectsToDestroy.clear();
  }

  private void repositionCamera() {
    float cameraLeft = camera.position.x - camera.viewportWidth / 2;
    float cameraRight = camera.position.x + camera.viewportWidth / 2;
    float cameraBottom = camera.position.y - camera.viewportHeight / 2;
    float cameraTop = camera.position.y + camera.viewportHeight / 2;
    Vector2 playerPos = player.body.getPosition();

    // Have to add/subtract threshold back so the camera stop bound is absolute to
    // the position defined
    if (playerPos.x < (cameraLeft + CAMERA_MOVEMENT_THRESHOLD) && cameraLeft > bounds.get(0)) {
      camera.translate(-1.3f, 0);
    }
    if (playerPos.x > (cameraRight - CAMERA_MOVEMENT_THRESHOLD) && cameraRight < bounds.get(3)) {
      camera.translate(1.3f, 0);
    }
    if (playerPos.y < (cameraBottom + CAMERA_MOVEMENT_THRESHOLD) && cameraBottom > bounds.get(1)) {
      camera.translate(0, -1.3f);
    }
    if (playerPos.y > (cameraTop - CAMERA_MOVEMENT_THRESHOLD) && cameraTop < bounds.get(2)) {
      camera.translate(0, 1.3f);
    }

    // Move camera back within bounds if it has left
    if (cameraLeft < bounds.get(0)) {
      camera.position.set(bounds.get(0) + camera.viewportWidth / 2, camera.position.y, camera.position.z);
    }
    if (cameraRight > bounds.get(3)) {
      camera.position.set(bounds.get(3) - camera.viewportWidth / 2, camera.position.y, camera.position.z);
    }
    if (cameraBottom < bounds.get(1)) {
      camera.position.set(camera.position.x, bounds.get(1) + camera.viewportHeight / 2, camera.position.z);
    }
    if (cameraLeft > bounds.get(2)) {
      camera.position.set(camera.position.x, bounds.get(2) - camera.viewportHeight / 2, camera.position.z);
    }
  }

  public void dispose() {
  }

  @Override
  public void write(Json json) {
    // No need to be used at the moment. (Useful if level editor is made)
  }

  @Override
  public void read(Json json, JsonValue jsonData) {
    Gdx.app.log(
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString() + " INFO Map",
        "Loading level data...");
    JsonValue bounds = jsonData.get("data").get("bounds");
    for (JsonValue boundsData = bounds.child; boundsData != null; boundsData = boundsData.next) {
      this.bounds.add(boundsData.asInt());
    }

    JsonValue cameraPos = jsonData.get("data").get("camera_start_pos");
    float[] _cameraStartPos = cameraPos.asFloatArray();
    // This is getting a float[] of length two [x, y] but camera requires a float[]
    // of [x, y, z]
    // this is 2D so did not want to have needless 0 in every level JSON camera
    // position attribute
    float[] cameraStartPos = new float[3];
    System.arraycopy(_cameraStartPos, 0, cameraStartPos, 0, 2);
    cameraStartPos[2] = 0;
    camera.position.set(cameraStartPos);

    PlatformFactory platformFactory = new PlatformFactory(world);
    JsonValue platforms = jsonData.get("objects").get("platforms");
    for (JsonValue platformData = platforms.child; platformData != null; platformData = platformData.next) {
      this.platforms.add(platformFactory.createPlatform(
          platformData.get("type").asString(),
          platformData.get("x").asFloat(),
          platformData.get("y").asFloat(),
          platformData.get("height").asFloat(),
          platformData.get("width").asFloat()));
    }
    Gdx.app.log(
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString() + " INFO Map",
        String.format("Data for level %s loaded successfully", jsonData.get("data").get("name").asString()));
  }

}
