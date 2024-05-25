package com.alx.groundclimber.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Player {

    final int MAX_VELOCITY = 240;

    public Body body;
    World world;

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
        fixtureDef.density = 0.25f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0.98f;
        body.createFixture(fixtureDef);

        playerShape.dispose();
    }

    public void update(float delta) {
        handleKeyPresses();
    }

    public void handleKeyPresses() {
        Vector2 velocity = this.body.getLinearVelocity();
        Vector2 position = this.body.getPosition();

        if (Gdx.input.isKeyPressed(Input.Keys.A) && velocity.x > -MAX_VELOCITY) {
            this.body.applyLinearImpulse((velocity.x < 0) ? -115f : -150f, 0, position.x, position.y, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && velocity.x < MAX_VELOCITY) {
            this.body.applyLinearImpulse((velocity.x > 0) ? 115f : 150f, 0, position.x, position.y, true);
        }
    }

}
