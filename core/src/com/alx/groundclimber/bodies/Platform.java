package com.alx.groundclimber.bodies;

import com.badlogic.gdx.physics.box2d.*;

public class Platform {

  public Body body;
  public float height;
  public float width;
  World world;

  public Platform(World world, float x, float y, float height, float width) {
    this.world = world;
    this.height = height;
    this.width = width;

    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    // Box2D places based on the centre so needs to be adjusted for LibGDX which places based on bottom-left corner
    bodyDef.position.set(x + (width / 2), y + (height / 2));
    body = world.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(width / 2, height / 2);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    body.createFixture(fixtureDef);

    shape.dispose();
  }

}
