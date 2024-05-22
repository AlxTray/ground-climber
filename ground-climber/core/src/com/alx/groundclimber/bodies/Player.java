package com.alx.groundclimber.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Player {

    final int BASE_X_MOVE_AMOUNT = 450;
    final int BASE_Y_JUMP_VELOCITY = 600;
    final int BASE_Y_DROP_VELOCITY = -610;
    final int BASE_NO_JUMP_FRAMES = 33;
    final double X_MOVEMENT_JUMP_MULTIPLIER = 1.6;
    final int MAX_VELOCITY = 10;

    public Body body;
    World world;

    float velocityY;
    float jumpFrames;
    double jumpXMultiplier;

    public Player(World world, int x, int y, int radius) {
        this.world = world;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);

        CircleShape playerShape = new CircleShape();
        playerShape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = playerShape;
        fixtureDef.density = 5f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 1f;
        body.createFixture(fixtureDef);

        playerShape.dispose();
    }

    public void update(float delta) {
        handleKeyPresses(delta);
    }

    public void handleKeyPresses(float delta) {
        Vector2 velocity = this.body.getLinearVelocity();
        Vector2 position = this.body.getPosition();

        System.out.println(velocity.x);

        if (Gdx.input.isKeyPressed(Input.Keys.A) && velocity.x > -MAX_VELOCITY) {
            this.body.applyLinearImpulse(-0.80f, 0, position.x, position.y, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && velocity.x < MAX_VELOCITY) {
            this.body.applyLinearImpulse(0.80f, 0, position.x, position.y, true);
        }

//        if (Gdx.input.isKeyPressed(Input.Keys.A)) position.x -= (BASE_X_MOVE_AMOUNT * jumpXMultiplier) * delta;
//        if (Gdx.input.isKeyPressed(Input.Keys.D)) position.x += (BASE_X_MOVE_AMOUNT * jumpXMultiplier) * delta;
//
//        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && position.y == 20) {
//            velocityY = BASE_Y_JUMP_VELOCITY * delta;
//            jumpFrames = BASE_NO_JUMP_FRAMES * delta;
//        }
//        if (jumpFrames > 0) {
//            jumpFrames -= delta;
//            jumpXMultiplier = X_MOVEMENT_JUMP_MULTIPLIER;
//        } else {
//            velocityY = BASE_Y_DROP_VELOCITY * delta;
//            jumpXMultiplier = 1;
//        }
//        position.y += velocityY;
//
//        if (position.x < 0) position.x = 0;
//        if (position.y < 20) position.y = 20;
//        if (position.x > 800 - 64) position.x = 800 - 64;
    }

}
