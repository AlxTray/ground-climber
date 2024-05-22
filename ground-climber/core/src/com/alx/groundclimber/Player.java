package com.alx.groundclimber;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

public class Player {

    public Rectangle bounds = new Rectangle();
    float velocityY;
    float jumpFrames;
    double jumpXMultiplier;

    public Player(int x, int y, int height, int width) {
        bounds.x = x;
        bounds.y = y;
        bounds.height = height;
        bounds.width = width;
    }

    public void update(float delta) {
        handleKeyPresses(delta);
    }

    public void handleKeyPresses(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) bounds.x -= (450 * jumpXMultiplier) * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.D)) bounds.x += (450 * jumpXMultiplier) * Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && bounds.y == 20) {
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
        bounds.y += velocityY;

        if (bounds.x < 0) bounds.x = 0;
        if (bounds.y < 20) bounds.y = 20;
        if (bounds.x > 800 - 64) bounds.x = 800 - 64;
    }

}
