package com.alx.groundclimber.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Platform extends Image {

  Body body;
  World world;

  public Platform(World world, float x, float y, float height, float width) {
    super(new Texture(Gdx.files.internal("normal_platform.png")));
    this.setPosition(x, y);
    this.setSize(width, height);

    this.world = world;

    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    bodyDef.position.set(x, y);
    body = world.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(this.getWidth() / 2, this.getHeight() / 2);
    // Move body to fit texture
    body.setTransform(this.getX() + (this.getWidth() / 2), this.getY() + (this.getHeight() / 2), 0);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    body.createFixture(fixtureDef);

    shape.dispose();
  }

}
