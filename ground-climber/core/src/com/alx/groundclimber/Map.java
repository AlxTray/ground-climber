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

    Player player;
    OrthographicCamera camera;
    SpriteBatch batch;
    Texture playerImage;
    Texture backgroundImage;

    World world;
    PlatformGenerator platGenerator;

    Array<Platform> platformBatch = new Array<Platform>();
    Array<Platform> initialPlatformBatch = new Array<Platform>();

    Box2DDebugRenderer debugRenderer;
    boolean debugMode;

    public Map(boolean debugMode) {
        this.debugMode = debugMode;

        world = new World(new Vector2(0, -450), true);
        spawnNewPlayer(
                PLAYER_INITIAL_X,
                PLAYER_INITIAL_Y,
                PLAYER_INITIAL_RADIUS
        );

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        playerImage = new Texture(Gdx.files.internal("bucket.png"));
        backgroundImage = new Texture(Gdx.files.internal("background.png"));

        platGenerator = new PlatformGenerator(world);
        initialPlatformBatch = platGenerator.generateInitialBatch();

        debugRenderer = new Box2DDebugRenderer();
    }

    public void spawnNewPlayer(int x, int y, int radius) {
        player = new Player(world, x, y, radius);
    }

    public void update(float delta) {
        if (player.body.getPosition().x > 100) camera.translate(0.35f, 0);
        camera.update();
        player.update(delta);

        if (debugMode) {
            debugRenderer.render(world, camera.combined);
        } else {
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
            batch.draw(playerImage, player.body.getPosition().x, player.body.getPosition().y);
            for (Platform platform : initialPlatformBatch) {
                platform.draw(batch, 1);
            }
            batch.end();
        }

        world.step(delta, 6, 2);
    }

    public void dispose() {
        playerImage.dispose();
        batch.dispose();
    }
}
