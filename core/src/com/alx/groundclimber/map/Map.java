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

  static final int PLAYER_INITIAL_RADIUS = 16;
  static final int CAMERA_MOVEMENT_THRESHOLD = 300;
  static final float TIME_STEP = 1f / 120f;
  static final float CAMERA_TRANSLATION_STEP = 170f;
  static final float AUTOSCROLL_CAMERA_TRANSLATION_STEP = 100f;

  EndlessPlatformGenerator platGenerator;
  final ContactListenerImpl contactListener;
  MapRenderer mapRenderer;

  public final World world;
  public Player player;
  final OrthographicCamera camera;
  GameMode gameMode;
  float deltaAccumulator;
  final Array<Body> objectsToDestroy = new Array<>();

  public Array<Platform> platforms = new Array<>();
  final Array<Integer> bounds = new Array<>();
  final Array<Integer> playerSpawn = new Array<>();

  Platform lastPlatformInBatch;

  public Map() {
    world = new World(new Vector2(0, -425), true);

    camera = new OrthographicCamera();
    camera.setToOrtho(false, 800, 480);

    contactListener = new ContactListenerImpl();
    contactListener.addContactListener(new CrackedPlatformContactListener());
    if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
      contactListener.addContactListener(new DebugContactListener());
    }
    world.setContactListener(contactListener);
    deltaAccumulator = 0;
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

  public void spawnNewPlayer(int radius) {
    player = new Player(world, playerSpawn.get(0), playerSpawn.get(1), radius);
    // Make sure that player starts bouncing at spawn
    player.body.setLinearVelocity(0, -150);
    Gdx.app.log(
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString() + " INFO Map",
        "New player spawned successfully");
  }

  public void update(float delta) {
    doPhysicsStep(delta);

    player.update(delta);

    if (gameMode.equals(GameMode.ENDLESS) && player.body.getPosition().x > 100) {
      camera.translate(AUTOSCROLL_CAMERA_TRANSLATION_STEP * delta, 0);
    } else {
      repositionCamera(delta);
    }
    camera.update();

    if (Gdx.input.isKeyJustPressed(Keys.F3)) {
      mapRenderer.toggleDebugInfo();
    }

    // Kill player if they leave map bounds
    if (player.body.getPosition().x < bounds.get(0)
        || player.body.getPosition().x > bounds.get(3)
        || player.body.getPosition().y < bounds.get(1)
        || player.body.getPosition().y > bounds.get(2)) {
      Gdx.app.log(
          LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString() + " INFO Map",
          "Player has fell out of bounds");
      Gdx.app.exit();
    }

    if (gameMode.equals(GameMode.ENDLESS) && lastPlatformInBatch.body.getPosition().x < player.body.getPosition().x) {
        platforms = platGenerator.generatePlatformBatch();
        lastPlatformInBatch = platforms.get(platforms.size - 1);
        Gdx.app.log(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString() + " INFO Map",
            "Successfully generated new endless platform batch");
      }


    destroyQueuedObjects();

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

  public void doPhysicsStep(float delta) {
    deltaAccumulator += delta;
    while (deltaAccumulator >= TIME_STEP) {
      world.step(TIME_STEP, 6, 2);
      deltaAccumulator -= TIME_STEP;
    }
  }

  private void repositionCamera(float delta) {
    float cameraLeft = camera.position.x - camera.viewportWidth / 2;
    float cameraRight = camera.position.x + camera.viewportWidth / 2;
    float cameraBottom = camera.position.y - camera.viewportHeight / 2;
    float cameraTop = camera.position.y + camera.viewportHeight / 2;
    Vector2 playerPos = player.body.getPosition();

    // Have to add/subtract threshold back so the camera stop bound is absolute to
    // the position defined
    if (playerPos.x < (cameraLeft + CAMERA_MOVEMENT_THRESHOLD) && cameraLeft > bounds.get(0)) {
      camera.translate(-CAMERA_TRANSLATION_STEP * delta, 0);
    }
    if (playerPos.x > (cameraRight - CAMERA_MOVEMENT_THRESHOLD) && cameraRight < bounds.get(3)) {
      camera.translate(CAMERA_TRANSLATION_STEP * delta, 0);
    }
    if (playerPos.y < (cameraBottom + CAMERA_MOVEMENT_THRESHOLD) && cameraBottom > bounds.get(1)) {
      camera.translate(0, -CAMERA_TRANSLATION_STEP * delta);
    }
    if (playerPos.y > (cameraTop - CAMERA_MOVEMENT_THRESHOLD) && cameraTop < bounds.get(2)) {
      camera.translate(0, CAMERA_TRANSLATION_STEP * delta);
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

  public void dispose() { // Nothing to dispose currently
  }
  

  @Override
  public void write(Json json) { // No need to be used at the moment. (Useful if level editor is made)
  }

  @Override
  public void read(Json json, JsonValue jsonData) {
    Gdx.app.log(
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString() + " INFO Map",
        "Loading level data...");
    JsonValue boundsValue = jsonData.get("data").get("bounds");
    for (JsonValue boundsData = boundsValue.child; boundsData != null; boundsData = boundsData.next) {
      this.bounds.add(boundsData.asInt());
    }

    JsonValue playerSpawnValue = jsonData.get("data").get("player_start_pos");
    for (JsonValue playerSpawnData = playerSpawnValue.child; playerSpawnData != null; playerSpawnData = playerSpawnData.next) {
      this.playerSpawn.add(playerSpawnData.asInt());
    }
    spawnNewPlayer(PLAYER_INITIAL_RADIUS);

    JsonValue cameraPos = jsonData.get("data").get("camera_start_pos");
    float[] oldCameraStartPos = cameraPos.asFloatArray();
    // This is getting a float[] of length two [x, y] but camera requires a float[]
    // of [x, y, z]
    // this is 2D so did not want to have needless 0 in every level JSON camera
    // position attribute
    float[] cameraStartPos = new float[3];
    System.arraycopy(oldCameraStartPos, 0, cameraStartPos, 0, 2);
    cameraStartPos[2] = 0;
    camera.position.set(cameraStartPos);

    PlatformFactory platformFactory = new PlatformFactory(world);
    JsonValue platformsJson = jsonData.get("objects").get("platforms");
    for (JsonValue platformData = platformsJson.child; platformData != null; platformData = platformData.next) {
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
