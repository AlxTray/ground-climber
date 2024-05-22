package com.alx.groundclimber.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Platform extends Image {

    Body body;
    World world;

    public Platform(World world, int x, int y, int height, int width) {
        super(new Texture(Gdx.files.internal("bucket.png")));
        this.setPosition(x, y);
        this.setSize(width, height);
        this.setOrigin(width / 2,height / 2);

        this.world = world;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        body.createFixture(shape, 0.0f);

        shape.dispose();
    }

}
