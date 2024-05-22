package com.alx.groundclimber;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Map {

    Player player;
    OrthographicCamera camera;
    SpriteBatch batch;
    Texture playerImage;

    Array<Platform> platformBatch = new Array<Platform>();
    Array<Platform> initialPlatformBatch = new Array<Platform>();

    public Map() {
        int PLAYER_INITIAL_WIDTH = 64;
        int PLAYER_INITIAL_HEIGHT = 64;
        int PLAYER_INITIAL_Y = 20;
        int PLAYER_INITIAL_X = 50;

        spawnNewPlayer(
                PLAYER_INITIAL_X,
                PLAYER_INITIAL_Y,
                PLAYER_INITIAL_HEIGHT,
                PLAYER_INITIAL_WIDTH
        );

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        playerImage = new Texture(Gdx.files.internal("bucket.png"));
        initialPlatformBatch = PlatformGenerator.generateInitialBatch();
    }

    public void spawnNewPlayer(int x, int y, int height, int width) {
        player = new Player(x, y, height, width);
    }

    public void update(float delta) {
        camera.update();
        player.update(delta);

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        batch.draw(playerImage, player.bounds.x, player.bounds.y);
        for (Platform platform : initialPlatformBatch) {
            batch.draw(playerImage, platform.position.x, platform.position.y);
        }
        batch.end();
    }

    public void dispose() {
        playerImage.dispose();
        batch.dispose();
    }
}
