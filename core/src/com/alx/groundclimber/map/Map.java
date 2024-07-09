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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class Map implements Json.Serializable {

    int PLAYER_INITIAL_RADIUS = 16;
    int PLAYER_INITIAL_Y = 500;
    int PLAYER_INITIAL_X = 50;

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
    Array<Float> cameraStartPos = new Array<>();

    Platform lastPlatformInBatch;

    public Map() {
        world = new World(new Vector2(0, -75), true);
        spawnNewPlayer(
                PLAYER_INITIAL_X,
                PLAYER_INITIAL_Y,
                PLAYER_INITIAL_RADIUS
        );

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

        // Generate initial endless platform batch here as when Map is instantiated the game mode is not known
        if (gameMode.equals(GameMode.ENDLESS)) {
            platGenerator = new EndlessPlatformGenerator(world);
            platforms = platGenerator.generateInitialBatch();
            lastPlatformInBatch = new Platform(world, 520f, 0, 20f, 60f);
            Gdx.app.log("Map - INFO", "Successfully generated initial endless platforms");
        }
    }

    public void attachRenderer(MapRenderer mapRenderer) {
        this.mapRenderer = mapRenderer;
    }

    public void spawnNewPlayer(int x, int y, int radius) {
        player = new Player(world, x, y, radius);
        Gdx.app.log("Map - INFO", "New player spawned successfully");
    }

    public void update(float delta) {
        player.update(delta);

        if (gameMode.equals(GameMode.ENDLESS) && player.body.getPosition().x > 100) {
            camera.translate(0.6f, 0);
        }
        if (gameMode.equals(GameMode.NORMAL)) {
            camera.position.set(player.body.getPosition().x, player.body.getPosition().y, 0);
        }
        camera.update();

        // Kill player if they leave map bounds
        if (player.body.getPosition().y < 0) {
            Gdx.app.debug("Map - DEBUG", "Player has fell out of bounds");
            Gdx.app.exit();
        }

        if (gameMode.equals(GameMode.ENDLESS)) {
            if (lastPlatformInBatch.getX() < player.body.getPosition().x) {
                platforms = platGenerator.generatePlatformBatch();
                lastPlatformInBatch = platforms.get(platforms.size - 1);
                Gdx.app.log("Map - INFO", "Successfully generated new endless platform batch");
            }
        }

        destroyQueuedObjects();
        world.step(1/60f, 6, 2);

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
                    "Map - DEBUG",
                    String.format("The object %s has been destroyed from world", objectData.getClass().getSimpleName())
            );
        }
        objectsToDestroy.clear();
    }

    public void dispose() {
    }

    @Override
    public void write(Json json) {
        // No need to be used at the moment. (Useful if level editor is made)
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        Gdx.app.log("Map - INFO", "Loading level data...");
        JsonValue bounds = jsonData.get("data").get("bounds");
        for (JsonValue boundsData = bounds.child; boundsData != null; boundsData = boundsData.next) {
            this.bounds.add(boundsData.asInt());
        }

        JsonValue cameraPos = jsonData.get("data").get("bounds");
        for (JsonValue cameraPosData = cameraPos.child; cameraPosData != null; cameraPosData = cameraPosData.next) {
            this.cameraStartPos.add(cameraPosData.asFloat());
        }

        PlatformFactory platformFactory = new PlatformFactory(world);
        JsonValue platforms = jsonData.get("objects").get("platforms");
        for (JsonValue platformData = platforms.child; platformData != null; platformData = platformData.next) {
            this.platforms.add(platformFactory.createPlatform(
                    platformData.get("type").asString(),
                    platformData.get("x").asFloat(),
                    platformData.get("y").asFloat(),
                    platformData.get("height").asFloat(),
                    platformData.get("width").asFloat()
            ));
        }
        Gdx.app.log(
                "Map - INFO",
                String.format("Data for level %s loaded successfully", jsonData.get("data").get("name").asString())
        );
    }

}
