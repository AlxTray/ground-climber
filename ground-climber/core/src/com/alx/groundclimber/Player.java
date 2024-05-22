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
        final int BASE_X_MOVE_AMOUNT = 450;
        final int BASE_Y_JUMP_VELOCITY = 600;
        final int BASE_Y_DROP_VELOCITY = -610;
        final int BASE_NO_JUMP_FRAMES = 33;
        final double X_MOVEMENT_JUMP_MULTIPLIER = 1.6;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) bounds.x -= (BASE_X_MOVE_AMOUNT * jumpXMultiplier) * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) bounds.x += (BASE_X_MOVE_AMOUNT * jumpXMultiplier) * delta;

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && bounds.y == 20) {
            velocityY = BASE_Y_JUMP_VELOCITY * delta;
            jumpFrames = BASE_NO_JUMP_FRAMES * delta;
        }
        if (jumpFrames > 0) {
            jumpFrames -= delta;
            jumpXMultiplier = X_MOVEMENT_JUMP_MULTIPLIER;
        } else {
            velocityY = BASE_Y_DROP_VELOCITY * delta;
            jumpXMultiplier = 1;
        }
        bounds.y += velocityY;

        if (bounds.x < 0) bounds.x = 0;
        if (bounds.y < 20) bounds.y = 20;
        if (bounds.x > 800 - 64) bounds.x = 800 - 64;
    }

}
