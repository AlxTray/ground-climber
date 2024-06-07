package com.alx.groundclimber;

import com.alx.groundclimber.bodies.Platform;
import com.alx.groundclimber.bodies.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class Map implements Json.Serializable {

    int PLAYER_INITIAL_RADIUS = 16;
    int PLAYER_INITIAL_Y = 500;
    int PLAYER_INITIAL_X = 50;

    PlatformGenerator platGenerator;

    public World world;
    public Player player;
    GameMode gameMode;

    public Array<Platform> platforms = new Array<>();

    Platform lastPlatformInBatch;

    public Map() {
        world = new World(new Vector2(0, -75), true);
        spawnNewPlayer(
                PLAYER_INITIAL_X,
                PLAYER_INITIAL_Y,
                PLAYER_INITIAL_RADIUS
        );
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;

        if (gameMode.equals(GameMode.ENDLESS)) {
            platGenerator = new PlatformGenerator(world);
            platforms = platGenerator.generateInitialBatch();
            lastPlatformInBatch = new Platform(world, 520f, 0, 20f, 60f);
        }
    }

    public void spawnNewPlayer(int x, int y, int radius) {
        player = new Player(world, x, y, radius);
    }

    public void update(float delta) {
        player.update(delta);

        if (player.body.getPosition().y < 0) {
            Gdx.app.exit();
        }

        if (gameMode.equals(GameMode.ENDLESS)) {
            if (lastPlatformInBatch.getX() < player.body.getPosition().x) {
                platforms = platGenerator.generatePlatformBatch(world);
                lastPlatformInBatch = platforms.get(platforms.size - 1);
            }
        }

        world.step(1/60f, 6, 2);
    }

    public void dispose() {
    }

    @Override
    public void write(Json json) {
        // No need to be used at the moment. (Useful if level editor is made)
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        PlatformFactory platformFactory = new PlatformFactory(world);
        JsonValue normalPlatforms = jsonData.get("objects").get("platforms");
        for (JsonValue platformData = normalPlatforms.child; platformData != null; platformData = platformData.next) {
            platforms.add(platformFactory.createPlatform(
                    platformData.get("type").asString(),
                    platformData.get("x").asFloat(),
                    platformData.get("y").asFloat(),
                    platformData.get("height").asFloat(),
                    platformData.get("width").asFloat()
            ));
        }
    }

}
