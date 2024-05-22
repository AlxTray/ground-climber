package com.alx.groundclimber.screens;

import com.alx.groundclimber.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

    final GroundClimber game;

    Texture playerImage;
    Sound jumpSound;
    SpriteBatch batch;
    OrthographicCamera camera;
    Map map;
    float velocityY;
    float jumpFrames;
    double jumpXMultiplier;

    Array<Platform> initialPlatforms = PlatformGenerator.generateInitialBatch();

    public GameScreen(GroundClimber game) {
        this.game = game;

        playerImage = new Texture(Gdx.files.internal("bucket.png"));

        // load the drop sound effect and the rain background "music"
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        map = new Map();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to clear are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the bucket and
        // all drops
        batch.begin();
        batch.draw(playerImage, map.player.bounds.x, map.player.bounds.y);
        for (Platform platform : initialPlatforms) {
            batch.draw(playerImage, platform.position.x, platform.position.y);
        }
        batch.end();

        // process user input
        if (Gdx.input.isKeyPressed(Input.Keys.A)) map.player.bounds.x -= (450 * jumpXMultiplier) * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.D)) map.player.bounds.x += (450 * jumpXMultiplier) * Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && map.player.bounds.y == 20) {
            velocityY = 600 * Gdx.graphics.getDeltaTime();
            jumpFrames = 33 * Gdx.graphics.getDeltaTime();
        }
        if (jumpFrames > 0) {
            jumpFrames -= Gdx.graphics.getDeltaTime();
            jumpXMultiplier = 1.6;
        } else {
            velocityY = -610 * Gdx.graphics.getDeltaTime();
            jumpXMultiplier = 1;
        }
        map.player.bounds.y += velocityY;

        // make sure the bucket stays within the screen bounds
        if (map.player.bounds.x < 0) map.player.bounds.x = 0;
        if (map.player.bounds.y < 20) map.player.bounds.y = 20;
        if (map.player.bounds.x > 800 - 64) map.player.bounds.x = 800 - 64;
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        playerImage.dispose();
        jumpSound.dispose();
        batch.dispose();
    }
}
