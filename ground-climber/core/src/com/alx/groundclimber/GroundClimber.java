package com.alx.groundclimber;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class GroundClimber extends ApplicationAdapter {
	private Texture playerImage;
	private Sound jumpSound;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Rectangle player;

	@Override
	public void create() {
		playerImage = new Texture(Gdx.files.internal("bucket.png"));

		// load the drop sound effect and the rain background "music"
		jumpSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		batch = new SpriteBatch();

		// create a Rectangle to logically represent the bucket
		player = new Rectangle();
		player.x = 800 / 2 - 64 / 2;
		player.y = 20;
		player.width = 64;
		player.height = 64;
	}

	@Override
	public void render() {
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
		batch.draw(playerImage, player.x, player.y);
		batch.end();

		// process user input
		if(Gdx.input.isKeyPressed(Input.Keys.A)) player.x -= 450 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.D)) player.x += 450 * Gdx.graphics.getDeltaTime();

		// make sure the bucket stays within the screen bounds
		if(player.x < 0) player.x = 0;
		if(player.x > 800 - 64) player.x = 800 - 64;
	}

	@Override
	public void dispose() {
		playerImage.dispose();
		jumpSound.dispose();
		batch.dispose();
	}
}
