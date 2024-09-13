package com.alx.groundclimber.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Player {

    private static final int MAX_VELOCITY = 240;
    private static final float DEFAULT_FORCE = 1500000;

    private final Body body;

    public Player(World world, int x, int y, int radius) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);
        body.setUserData(this);

        CircleShape playerShape = new CircleShape();
        playerShape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = playerShape;
        fixtureDef.density = 0.1f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 1f;
        body.createFixture(fixtureDef);

        playerShape.dispose();
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }


    public void update(float delta) {
        handleKeyPresses(delta);
    }

    public void handleKeyPresses(float delta) {
        Vector2 velocity = this.body.getLinearVelocity();
        if (Gdx.input.isKeyPressed(Input.Keys.A) && velocity.x > -MAX_VELOCITY) {
            body.applyForceToCenter(-DEFAULT_FORCE * delta, 0, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && velocity.x < MAX_VELOCITY) {
            body.applyForceToCenter(DEFAULT_FORCE * delta, 0, true);
        }
    }

}
