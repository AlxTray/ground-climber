package com.alx.groundclimber.bodies;

import com.badlogic.gdx.physics.box2d.*;

public class Platform {

  public Body body;
  World world;

  public Platform(World world, float x, float y, float height, float width) {
    this.world = world;

    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    bodyDef.position.set(x, y);
    body = world.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(width / 2, height / 2);
    // Move body to fit texture
    body.setTransform(x + (width / 2), y + (height / 2), 0);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    body.createFixture(fixtureDef);

    shape.dispose();
  }

}
