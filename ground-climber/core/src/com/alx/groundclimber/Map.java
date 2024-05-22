package com.alx.groundclimber;

import com.alx.groundclimber.bodies.Platform;
import com.alx.groundclimber.bodies.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Map {

    int PLAYER_INITIAL_RADIUS = 5;
    int PLAYER_INITIAL_Y = 60;
    int PLAYER_INITIAL_X = 50;

    Player player;
    OrthographicCamera camera;
    SpriteBatch batch;
    Texture playerImage;

    World world;
    PlatformGenerator platGenerator;

    Array<Platform> platformBatch = new Array<Platform>();
    Array<Platform> initialPlatformBatch = new Array<Platform>();

    public Map() {
        world = new World(new Vector2(0, 0), true);
        spawnNewPlayer(
                PLAYER_INITIAL_X,
                PLAYER_INITIAL_Y,
                PLAYER_INITIAL_RADIUS
        );

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        playerImage = new Texture(Gdx.files.internal("bucket.png"));

        platGenerator = new PlatformGenerator(world);
        initialPlatformBatch = platGenerator.generateInitialBatch();
    }

    public void spawnNewPlayer(int x, int y, int radius) {
        player = new Player(world, x, y, radius);
    }

    public void update(float delta) {
        camera.update();
        player.update(delta);

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        batch.draw(playerImage, player.body.getPosition().x, player.body.getPosition().y);
        for (Platform platform : initialPlatformBatch) {
            platform.draw(batch, 1);
        }
        batch.end();

        world.step(delta, 6, 2);
    }

    public void dispose() {
        playerImage.dispose();
        batch.dispose();
    }
}
