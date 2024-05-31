package com.alx.groundclimber;

import com.alx.groundclimber.bodies.Platform;
import com.alx.groundclimber.bodies.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Map {

    int PLAYER_INITIAL_RADIUS = 16;
    int PLAYER_INITIAL_Y = 500;
    int PLAYER_INITIAL_X = 50;

    PlatformGenerator platGenerator;

    // Map objects
    public World world;
    public Player player;
    public Array<Platform> platforms = new Array<Platform>();

    // Attributes for use in endless mode so that only batches of platforms are active at once
    Array<Platform> initialPlatformBatch = new Array<Platform>();
    Platform lastPlatformInBatch;

    public Map() {
        world = new World(new Vector2(0, -75), true);
        spawnNewPlayer(
                PLAYER_INITIAL_X,
                PLAYER_INITIAL_Y,
                PLAYER_INITIAL_RADIUS
        );

        platGenerator = new PlatformGenerator(world);
        initialPlatformBatch = platGenerator.generateInitialBatch();
        lastPlatformInBatch = new Platform(world,520f, 0, 20f, 60f);
    }

    public void spawnNewPlayer(int x, int y, int radius) {
        player = new Player(world, x, y, radius);
    }

    public void update(float delta) {
        player.update(delta);

        if (player.body.getPosition().y < 0) {
            Gdx.app.exit();
        }

        if (lastPlatformInBatch.getX() < player.body.getPosition().x) {
            platforms = platGenerator.generatePlatformBatch(world);
            lastPlatformInBatch = platforms.get(platforms.size - 1);
        }

        world.step(1/60f, 6, 2);
    }

    public void dispose() {
    }
}
