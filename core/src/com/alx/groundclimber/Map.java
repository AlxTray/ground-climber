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

    OrthographicCamera camera;
    SpriteBatch batch;
    Texture playerImage;
    Texture backgroundImage;

    World world;
    PlatformGenerator platGenerator;

    // Map objects
    Player player;
    Array<Platform> platforms = new Array<Platform>();

    // Attributes for use in endless mode so that only batches of platforms are active at once
    Array<Platform> initialPlatformBatch = new Array<Platform>();
    Platform lastPlatformInBatch;

    // Debug rendering
    Box2DDebugRenderer debugRenderer;
    int debugMode;

    public Map(int debugMode) {
        this.debugMode = debugMode;

        world = new World(new Vector2(0, -75), true);
        spawnNewPlayer(
                PLAYER_INITIAL_X,
                PLAYER_INITIAL_Y,
                PLAYER_INITIAL_RADIUS
        );

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        playerImage = new Texture(Gdx.files.internal("ball.png"));
        backgroundImage = new Texture(Gdx.files.internal("background.png"));

        platGenerator = new PlatformGenerator(world);
        initialPlatformBatch = platGenerator.generateInitialBatch();
        lastPlatformInBatch = new Platform(world,520f, 0, 20f, 60f);

        debugRenderer = new Box2DDebugRenderer();
    }

    public void spawnNewPlayer(int x, int y, int radius) {
        player = new Player(world, x, y, radius);
    }

    public void update(float delta) {
        if (player.body.getPosition().x > 100) camera.translate(0.6f, 0);
        camera.update();
        player.update(delta);

        if (player.body.getPosition().y < 0 || player.body.getPosition().x < (camera.position.x - (camera.viewportWidth / 2))) {
            Gdx.app.exit();
        }

        if (lastPlatformInBatch.getX() < player.body.getPosition().x) {
            platforms = platGenerator.generatePlatformBatch(world, camera);
            lastPlatformInBatch = platforms.get(platforms.size - 1);
        }

        if (debugMode != 2) {
            batch.begin();
            batch.disableBlending();
            batch.draw(
                    backgroundImage,
                    camera.position.x - (camera.viewportWidth / 2),
                    camera.position.y - (camera.viewportHeight / 2),
                    camera.viewportWidth,
                    camera.viewportHeight
            );
            batch.enableBlending();
            batch.setProjectionMatrix(camera.combined);
            batch.draw(
                    playerImage,
                    player.body.getPosition().x - (playerImage.getWidth() / 2f),
                    player.body.getPosition().y - (playerImage.getHeight() / 2f)
            );

            for (Platform platform : initialPlatformBatch) {
                platform.draw(batch, 1);
            }
            for (Platform platform : platforms) {
                platform.draw(batch, 1);
            }

            batch.end();
        }
        if (debugMode != 0) {
            debugRenderer.render(world, camera.combined);
        }

        world.step(1/60f, 6, 2);
    }

    public void dispose() {
        playerImage.dispose();
        batch.dispose();
    }
}
