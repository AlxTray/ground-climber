package io.github.alxtray.groundclimber.map;

import io.github.alxtray.groundclimber.bodies.Platform;
import io.github.alxtray.groundclimber.bodies.Player;
import io.github.alxtray.groundclimber.enums.GameMode;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.listeners.ContactListenerImpl;
import io.github.alxtray.groundclimber.listeners.CrackedPlatformContactListener;
import io.github.alxtray.groundclimber.listeners.DebugContactListener;
import io.github.alxtray.groundclimber.utilities.EndlessPlatformGenerator;
import io.github.alxtray.groundclimber.utilities.Logger;
import io.github.alxtray.groundclimber.utilities.PlatformFactory;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class Map implements Json.Serializable {

    private static final int PLAYER_INITIAL_RADIUS = 16;
    private static final int CAMERA_MOVEMENT_THRESHOLD = 300;
    private static final float TIME_STEP = 1f / 120f;
    private static final float CAMERA_TRANSLATION_STEP = 170f;
    private static final float AUTOSCROLL_CAMERA_TRANSLATION_STEP = 100f;
    private static final int BOUNDS_LEFT_INDEX = 0;
    private static final int BOUNDS_RIGHT_INDEX = 3;
    private static final int BOUNDS_BOTTOM_INDEX = 1;
    private static final int BOUNDS_TOP_INDEX = 2;
    public final World world;
    final OrthographicCamera camera;
    final Array<Body> objectsToDestroy = new Array<>();
    final Array<Integer> bounds = new Array<>();
    final Array<Integer> playerSpawn = new Array<>();
    private final ContactListenerImpl contactListener;
    protected MapRenderer mapRenderer;
    float deltaAccumulator;
    private EndlessPlatformGenerator platGenerator;
    private Player player;
    private GameMode gameMode;
    private Array<Platform> platforms = new Array<>();
    private Platform lastPlatformInBatch;

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


    public Array<Platform> getPlatforms() {
        return platforms;
    }

    public Body getPlayerBody() {
        return player.getBody();
    }


    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;

        // Generate initial endless platform batch here as when Map is instantiated the
        // game mode is not known
        if (gameMode.equals(GameMode.ENDLESS)) {
            platGenerator = new EndlessPlatformGenerator(world);
            platforms = platGenerator.generateInitialBatch();
            lastPlatformInBatch = new Platform(world, 520f, 0, 20f, 60f);
            Logger.log(
                    "Map",
                    "Successfully generated initial endless platforms",
                    LogLevel.INFO);
        }
    }

    public void attachRenderer(MapRenderer mapRenderer) {
        this.mapRenderer = mapRenderer;
    }

    public void spawnNewPlayer(int radius) {
        player = new Player(world, playerSpawn.get(0), playerSpawn.get(1), radius);
        // Make sure that player starts bouncing at spawn
        player.getBody().setLinearVelocity(0, -150);
        Logger.log(
                "Map",
                "New player spawned successfully",
                LogLevel.INFO);
    }


    public void update(float delta) {
        doPhysicsStep(delta);

        player.update(delta);

        if (gameMode.equals(GameMode.ENDLESS) && player.getBody().getPosition().x > 100) {
            camera.translate(AUTOSCROLL_CAMERA_TRANSLATION_STEP * delta, 0);
        } else {
            repositionCamera(delta);
        }
        camera.update();

        if (Gdx.input.isKeyJustPressed(Keys.F3)) {
            mapRenderer.toggleDebugInfo();
        }

        // Kill player if they leave map bounds
        if (player.getBody().getPosition().x < bounds.get(BOUNDS_LEFT_INDEX)
                || player.getPosition().x > bounds.get(BOUNDS_RIGHT_INDEX)
                || player.getPosition().y < bounds.get(BOUNDS_BOTTOM_INDEX)
                || player.getPosition().y > bounds.get(BOUNDS_TOP_INDEX)) {
            Logger.log(
                    "Map",
                    "Player has fell out of bounds",
                    LogLevel.INFO);
            Gdx.app.exit();
        }

        if (gameMode.equals(GameMode.ENDLESS) && lastPlatformInBatch.getPosition().x < player.getPosition().x) {
            platforms = platGenerator.generatePlatformBatch();
            lastPlatformInBatch = platforms.get(platforms.size - 1);
            Logger.log(
                    "Map",
                    "Successfully generated new endless platform batch",
                    LogLevel.INFO);
        }


        destroyQueuedObjects();

        mapRenderer.render(camera);
    }

    private void destroyQueuedObjects() {
        // Grab all queued objects to destroy from listeners
        // So that all objects can be destroyed at once and will not be locked
        objectsToDestroy.addAll(contactListener.getBodiesToDestroy());

        for (Body objectToDestroy : objectsToDestroy) {
            Object objectData = objectToDestroy.getUserData();
            platforms.removeValue((Platform) objectData, false);

            world.destroyBody(objectToDestroy);
            Logger.log(
                    "Map",
                    String.format("The object %s has been destroyed from world", objectData.getClass().getSimpleName()),
                    LogLevel.DEBUG);
        }
        objectsToDestroy.clear();
    }

    private void doPhysicsStep(float delta) {
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
        Vector2 playerPos = player.getPosition();

        // Have to add/subtract threshold back so the camera stop bound is absolute to
        // the position defined
        if (playerPos.x < (cameraLeft + CAMERA_MOVEMENT_THRESHOLD) && cameraLeft > bounds.get(BOUNDS_LEFT_INDEX)) {
            camera.translate(-CAMERA_TRANSLATION_STEP * delta, 0);
        }
        if (playerPos.x > (cameraRight - CAMERA_MOVEMENT_THRESHOLD) && cameraRight < bounds.get(BOUNDS_RIGHT_INDEX)) {
            camera.translate(CAMERA_TRANSLATION_STEP * delta, 0);
        }
        if (playerPos.y < (cameraBottom + CAMERA_MOVEMENT_THRESHOLD) && cameraBottom > bounds.get(BOUNDS_BOTTOM_INDEX)) {
            camera.translate(0, -CAMERA_TRANSLATION_STEP * delta);
        }
        if (playerPos.y > (cameraTop - CAMERA_MOVEMENT_THRESHOLD) && cameraTop < bounds.get(BOUNDS_TOP_INDEX)) {
            camera.translate(0, CAMERA_TRANSLATION_STEP * delta);
        }

        // Move camera back within bounds if it has left
        if (cameraLeft < bounds.get(BOUNDS_LEFT_INDEX)) {
            camera.position.set(bounds.get(BOUNDS_LEFT_INDEX) + camera.viewportWidth / 2, camera.position.y, camera.position.z);
        }
        if (cameraRight > bounds.get(BOUNDS_RIGHT_INDEX)) {
            camera.position.set(bounds.get(BOUNDS_RIGHT_INDEX) - camera.viewportWidth / 2, camera.position.y, camera.position.z);
        }
        if (cameraBottom < bounds.get(BOUNDS_BOTTOM_INDEX)) {
            camera.position.set(camera.position.x, bounds.get(BOUNDS_BOTTOM_INDEX) + camera.viewportHeight / 2, camera.position.z);
        }
        if (cameraTop > bounds.get(BOUNDS_TOP_INDEX)) {
            camera.position.set(camera.position.x, bounds.get(BOUNDS_TOP_INDEX) - camera.viewportHeight / 2, camera.position.z);
        }
    }

    public void dispose() { // Nothing to dispose currently
    }


    @Override
    public void write(Json json) { // No need to be used at the moment. (Useful if level editor is made)
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        Logger.log(
                "Map",
                "Loading level data...",
                LogLevel.INFO);
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
        Logger.log(
                "Map",
                String.format("Data for level %s loaded successfully", jsonData.get("data").get("name").asString()),
                LogLevel.INFO);
    }

}
